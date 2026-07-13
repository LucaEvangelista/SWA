"use strict";

var CONTEXT_PATH = "/soccorso_Web_SWA";
var PAGINA_LOGIN = CONTEXT_PATH + "/viste/Login.html";

// Aspetta che la pagina HTML sia completamente caricata.
document.addEventListener("DOMContentLoaded", function () {
    caricaMissioni();
});

/*
 * Carica la lista delle missioni dall'endpoint REST.
 */
function caricaMissioni() {

    var url = CONTEXT_PATH + "/rest/missioni/list";

    fetchAutenticata(url, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(function (response) {
            return leggiRispostaJson(
                response,
                "Errore durante il caricamento delle missioni."
            );
        })
        .then(function (missioni) {

            if (!Array.isArray(missioni)) {
                throw new Error(
                    "Il server non ha restituito una lista valida di missioni."
                );
            }

            mostraMissioni(missioni);
        })
        .catch(function (errore) {

            /*
             * In caso di 401 il redirect al login
             * è già stato effettuato.
             */
            if (errore.reindirizzamentoLogin === true) {
                return;
            }

            console.error(
                "Errore durante il caricamento delle missioni:",
                errore
            );

            mostraErrore(
                errore.message ||
                "Errore durante il caricamento delle missioni."
            );
        });
}

/*
 * Esegue una richiesta fetch autenticata.
 *
 * Il token viene recuperato da sessionStorage
 * e aggiunto all'header Authorization.
 *
 * credentials: "include" permette di inviare
 * anche il cookie contenente il token.
 */
function fetchAutenticata(url, opzioni) {

    var configurazione = opzioni || {};
    var headers = configurazione.headers || {};
    var token = sessionStorage.getItem("authToken");

    /*
     * Se il token è presente nel sessionStorage,
     * viene inserito nell'header Authorization.
     */
    if (token !== null && token !== "") {
        headers["Authorization"] = "Bearer " + token;
    }

    configurazione.headers = headers;
    configurazione.credentials = "include";

    return fetch(url, configurazione)
        .then(function (response) {

            /*
             * 401 Unauthorized:
             *
             * - utente non autenticato;
             * - token assente;
             * - token scaduto;
             * - token non valido.
             */
            if (response.status === 401) {

                pulisciDatiAutenticazione();
                reindirizzaAlLogin();

                var erroreLogin = new Error(
                    "Sessione assente o scaduta."
                );

                /*
                 * Questa proprietà permette al catch
                 * di capire che il redirect è già avvenuto.
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
 * Viene usato response.text() prima del parsing,
 * perché il server potrebbe restituire anche una
 * risposta vuota oppure HTML in caso di errore.
 */
function leggiRispostaJson(response, messaggioFallback) {

    return response.text()
        .then(function (testo) {

            var dati = null;

            if (testo !== "") {
                try {
                    dati = JSON.parse(testo);
                } catch (erroreParsing) {

                    /*
                     * La risposta ha codice positivo,
                     * ma non contiene JSON valido.
                     */
                    if (response.ok) {
                        throw new Error(
                            "Il server ha restituito una risposta non valida."
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
                 * Recupera il messaggio eventualmente
                 * restituito dall'ErrorResponse Java.
                 */
                if (dati !== null) {
                    messaggio =
                        dati.message ||
                        dati.messaggio ||
                        dati.error ||
                        messaggioFallback;
                }

                /*
                 * 403 Forbidden:
                 * l'utente è autenticato, ma non ha
                 * il ruolo necessario.
                 */
                if (response.status === 403) {
                    messaggio =
                        "Sei autenticato, ma non hai i permessi " +
                        "per visualizzare la lista delle missioni.";
                }

                if (response.status === 404) {
                    messaggio =
                        "L'indirizzo REST richiesto non è stato trovato.";
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
 * Prende l'array JSON ricevuto e crea
 * le righe della tabella.
 */
function mostraMissioni(missioni) {

    var tbody = document.getElementById(
        "corpo-tabella-missioni"
    );

    if (tbody === null) {
        console.error(
            "Elemento corpo-tabella-missioni non trovato."
        );

        return;
    }

    /*
     * Nessuna missione presente.
     */
    if (missioni.length === 0) {
        tbody.innerHTML =
            "<tr>" +
                '<td colspan="4" class="text-center">' +
                    "Nessuna missione disponibile." +
                "</td>" +
            "</tr>";

        return;
    }

    var righe = "";

    for (var i = 0; i < missioni.length; i++) {

        var missione = missioni[i];
        var missioneId = parseInt(
            missione.missioneId,
            10
        );

        righe += "<tr>";

        righe +=
            "<td>" +
            testoSicuro(missione.obiettivo) +
            "</td>";

        righe +=
            "<td>" +
            testoSicuro(missione.posizione) +
            "</td>";

        righe +=
            "<td>" +
            testoSicuro(missione.status) +
            "</td>";

        righe += "<td>";

        if (!isNaN(missioneId)) {
            righe +=
                '<button type="button" ' +
                'class="btn btn-primary" ' +
                'onclick="vediDettaglio(' +
                missioneId +
                ')">' +
                "Dettaglio" +
                "</button>";
        }

        righe += "</td>";
        righe += "</tr>";
    }

    tbody.innerHTML = righe;
}

/*
 * Apre la pagina del dettaglio della missione.
 */
function vediDettaglio(id) {

    var missioneId = parseInt(id, 10);

    if (isNaN(missioneId)) {
        mostraErrore("ID missione non valido.");
        return;
    }

    window.location.href =
        CONTEXT_PATH +
        "/views/DettagliMissione.html?id=" +
        encodeURIComponent(missioneId);
}

/*
 * Reindirizza alla pagina di login.
 *
 * replace impedisce di tornare alla pagina protetta
 * premendo il pulsante Indietro del browser.
 */
function reindirizzaAlLogin() {
    window.location.replace(PAGINA_LOGIN);
}

/*
 * Rimuove i dati del login dal sessionStorage.
 */
function pulisciDatiAutenticazione() {
    sessionStorage.removeItem("authToken");
    sessionStorage.removeItem("emailUtente");
    sessionStorage.removeItem("ruoloUtente");
}

/*
 * Mostra un messaggio di errore nella pagina.
 */
function mostraErrore(messaggio) {

    var messaggioErrore = document.getElementById(
        "messaggio-errore"
    );

    if (messaggioErrore !== null) {
        messaggioErrore.textContent = messaggio;
        messaggioErrore.classList.remove("d-none");
    }
}

/*
 * Restituisce un trattino quando il valore
 * è nullo, undefined oppure vuoto.
 */
function valore(val) {

    if (
        val === null ||
        val === undefined ||
        val === ""
    ) {
        return "-";
    }

    return String(val);
}

/*
 * Protegge la costruzione della tabella da eventuali
 * caratteri HTML presenti nei dati ricevuti.
 */
function testoSicuro(val) {

    var testo = valore(val);

    return testo
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}