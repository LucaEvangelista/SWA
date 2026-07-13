"use strict";

var CONTEXT_PATH = "/soccorso_Web_SWA";
var PAGINA_LOGIN = CONTEXT_PATH + "/viste/Login.html";

document.addEventListener("DOMContentLoaded", function () {
    caricaDettaglioMissione();
});

function caricaDettaglioMissione() {

    // Legge l'id della missione dalla URL.
    var parametri = new URLSearchParams(window.location.search);
    var id = parametri.get("id");

    if (id === null || id === "") {
        mostraErrore("ID missione mancante nella URL.");
        nascondiCaricamento();
        return;
    }

    var urlMissione =
        CONTEXT_PATH + "/rest/missioni/" + id;

    var urlSquadra =
        CONTEXT_PATH + "/rest/missioni/" + id + "/squadra";

    fetchAutenticata(urlMissione, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(function (response) {
            return leggiRispostaJson(
                response,
                "Errore durante il caricamento della missione."
            );
        })
        .then(function (missione) {
            console.log("Missione ricevuta:", missione);

            mostraMissione(missione);

            /*
             * Dopo aver caricato la missione,
             * carica anche la squadra associata.
             */
            return fetchAutenticata(urlSquadra, {
                method: "GET",
                headers: {
                    "Accept": "application/json"
                }
            });
        })
        .then(function (response) {
            return leggiRispostaJson(
                response,
                "Errore durante il caricamento della squadra."
            );
        })
        .then(function (squadra) {
            console.log("Squadra ricevuta:", squadra);

            mostraSquadra(squadra);
            nascondiCaricamento();
        })
        .catch(function (errore) {

            /*
             * Quando il server restituisce 401,
             * fetchAutenticata esegue già il redirect.
             */
            if (errore.reindirizzamentoLogin === true) {
                return;
            }

            console.error(
                "Errore durante il caricamento del dettaglio missione:",
                errore
            );

            mostraErrore(
                errore.message ||
                "Errore durante il caricamento del dettaglio della missione."
            );

            nascondiCaricamento();
        });
}

function mostraMissione(missione) {

    var cardMissione =
        document.getElementById("card-missione");

    if (cardMissione !== null) {
        cardMissione.classList.remove("d-none");
    }

    impostaTestoElemento(
        "missione-id",
        missione.missioneId
    );

    impostaTestoElemento(
        "obiettivo-missione",
        missione.obiettivo
    );

    impostaTestoElemento(
        "posizione-missione",
        missione.posizione
    );

    impostaTestoElemento(
        "stato-missione",
        missione.status
    );

    /*
     * Controlla la visibilità del pulsante
     * "Termina missione".
     */
    var contenitoreTerminazione =
        document.getElementById(
            "contenitore-terminazione"
        );

    if (contenitoreTerminazione === null) {
        return;
    }

    if (missione.status === "attiva") {
        contenitoreTerminazione.classList.remove("d-none");
    } else {
        contenitoreTerminazione.classList.add("d-none");
    }
}

function mostraSquadra(squadra) {
    impostaTestoElemento(
        "squadra-missione",
        squadra.nome
    );
}

function terminaMissione() {

    var parametri =
        new URLSearchParams(window.location.search);

    var id = parametri.get("id");

    if (id === null || id === "") {
        mostraErrore("ID missione mancante nella URL.");
        return;
    }

    var conferma = confirm(
        "Sei sicuro di voler terminare questa missione?"
    );

    if (!conferma) {
        return;
    }

    var url =
        CONTEXT_PATH +
        "/rest/missioni/" +
        id +
        "/terminazione";

    fetchAutenticata(url, {
        method: "PUT",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(function (response) {
            return leggiRispostaJson(
                response,
                "Errore durante la terminazione della missione."
            );
        })
        .then(function (missioneAggiornata) {
            console.log(
                "Missione terminata:",
                missioneAggiornata
            );

            mostraMissione(missioneAggiornata);

            alert(
                "Missione terminata correttamente."
            );
        })
        .catch(function (errore) {

            /*
             * In caso di 401 il redirect è già stato
             * eseguito da fetchAutenticata.
             */
            if (errore.reindirizzamentoLogin === true) {
                return;
            }

            console.error(
                "Errore durante la terminazione della missione:",
                errore
            );

            mostraErrore(
                errore.message ||
                "Errore durante la terminazione della missione."
            );
        });
}

/*
 * Esegue una chiamata fetch autenticata.
 *
 * Il token viene recuperato da sessionStorage
 * e inserito nell'header:
 *
 * Authorization: Bearer token
 *
 * credentials: "include" permette di inviare
 * anche il cookie di autenticazione.
 */
function fetchAutenticata(url, opzioni) {

    var configurazione = opzioni || {};
    var headers = configurazione.headers || {};

    var token =
        sessionStorage.getItem("authToken");

    /*
     * Se il token è presente, viene aggiunto
     * all'header Authorization.
     */
    if (token !== null && token !== "") {
        headers["Authorization"] =
            "Bearer " + token;
    }

    configurazione.headers = headers;

    /*
     * Invia anche gli eventuali cookie
     * associati alla richiesta.
     */
    configurazione.credentials = "include";

    return fetch(url, configurazione)
        .then(function (response) {

            /*
             * 401 significa che l'utente:
             *
             * - non è autenticato;
             * - non possiede un token;
             * - possiede un token scaduto;
             * - possiede un token non valido.
             */
            if (response.status === 401) {

                pulisciDatiAutenticazione();
                reindirizzaAlLogin();

                var erroreLogin = new Error(
                    "Sessione assente o scaduta."
                );

                /*
                 * Questa proprietà serve al catch
                 * per capire che il redirect è già
                 * stato eseguito.
                 */
                erroreLogin.reindirizzamentoLogin = true;

                throw erroreLogin;
            }

            return response;
        });
}

/*
 * Legge la risposta del server.
 *
 * Non usa direttamente response.json(), perché
 * il server potrebbe restituire:
 *
 * - un JSON;
 * - una risposta vuota;
 * - una pagina HTML di errore;
 * - un testo semplice.
 */
function leggiRispostaJson(
    response,
    messaggioFallback
) {

    return response.text()
        .then(function (testo) {

            var dati = null;

            if (testo !== "") {
                try {
                    dati = JSON.parse(testo);
                } catch (erroreParsing) {

                    /*
                     * Se la risposta HTTP è positiva,
                     * ma il contenuto non è JSON,
                     * viene segnalato un errore.
                     */
                    if (response.ok) {
                        throw new Error(
                            "Il server ha restituito " +
                            "una risposta non valida."
                        );
                    }
                }
            }

            /*
             * Gestione degli errori HTTP.
             */
            if (!response.ok) {

                var messaggio = messaggioFallback;

                /*
                 * Cerca il messaggio restituito
                 * dall'ErrorResponse Java.
                 */
                if (dati !== null) {
                    messaggio =
                        dati.message ||
                        dati.messaggio ||
                        messaggioFallback;
                }

                /*
                 * 403 significa che l'utente è
                 * autenticato, ma non possiede
                 * il ruolo necessario.
                 */
                if (response.status === 403) {
                    messaggio =
                        "Sei autenticato, ma non hai " +
                        "i permessi per eseguire " +
                        "questa operazione.";
                }

                /*
                 * 404 significa che la missione
                 * o la squadra non è stata trovata.
                 */
                if (
                    response.status === 404 &&
                    dati === null
                ) {
                    messaggio =
                        "La risorsa richiesta " +
                        "non è stata trovata.";
                }

                var erroreHttp = new Error(
                    messaggio +
                    " Codice HTTP: " +
                    response.status
                );

                erroreHttp.status = response.status;

                throw erroreHttp;
            }

            return dati;
        });
}

/*
 * Reindirizza l'utente alla pagina di login.
 *
 * replace impedisce di tornare alla pagina
 * protetta utilizzando il pulsante Indietro.
 */
function reindirizzaAlLogin() {
    window.location.replace(PAGINA_LOGIN);
}

/*
 * Elimina dal browser i dati relativi
 * all'utente autenticato.
 */
function pulisciDatiAutenticazione() {
    sessionStorage.removeItem("authToken");
    sessionStorage.removeItem("emailUtente");
    sessionStorage.removeItem("ruoloUtente");
}

/*
 * Inserisce un valore in un elemento HTML,
 * verificando prima che l'elemento esista.
 */
function impostaTestoElemento(
    idElemento,
    contenuto
) {

    var elemento =
        document.getElementById(idElemento);

    if (elemento !== null) {
        elemento.textContent = valore(contenuto);
    }
}

function mostraErrore(messaggio) {

    var boxErrore =
        document.getElementById("messaggio-errore");

    if (boxErrore === null) {
        return;
    }

    boxErrore.textContent = messaggio;
    boxErrore.classList.remove("d-none");
}

function nascondiCaricamento() {

    var boxCaricamento =
        document.getElementById(
            "messaggio-caricamento"
        );

    if (boxCaricamento !== null) {
        boxCaricamento.classList.add("d-none");
    }
}

function valore(val) {

    if (
        val === null ||
        val === undefined ||
        val === ""
    ) {
        return "-";
    }

    return val;
}