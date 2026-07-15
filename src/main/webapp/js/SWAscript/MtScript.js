var paginaCorrente = 1;
var dimensionePagina = 10;
var statoCorrente = "";
var totalePagine = 0;
var totaleMateriali = 0;
var caricamentoInCorso = false;

document.addEventListener("DOMContentLoaded", function () {
    collegaEventi();
    caricaMateriali();
});


/*
 * Collega gli eventi agli elementi presenti nell'HTML.
 */
function collegaEventi() {

    var filtroStato = document.getElementById(
        "filtro-stato-materiali"
    );

    var selezioneDimensione = document.getElementById(
        "dimensione-pagina"
    );

    var pulsantePrecedente = document.getElementById(
        "pagina-precedente"
    );

    var pulsanteSuccessiva = document.getElementById(
        "pagina-successiva"
    );


    /*
     * Quando cambia il filtro:
     * - aggiorna lo stato;
     * - torna alla prima pagina;
     * - ricarica i materiali.
     */
    filtroStato.addEventListener("change", function () {

        statoCorrente = filtroStato.value;
        paginaCorrente = 1;

        caricaMateriali();
    });


    /*
     * Quando cambia il numero di elementi per pagina.
     */
    selezioneDimensione.addEventListener("change", function () {

        dimensionePagina = parseInt(
            selezioneDimensione.value,
            10
        );

        paginaCorrente = 1;

        caricaMateriali();
    });


    /*
     * Passa alla pagina precedente.
     */
    pulsantePrecedente.addEventListener("click", function () {

        if (
            !caricamentoInCorso &&
            paginaCorrente > 1
        ) {

            paginaCorrente--;

            caricaMateriali();
        }
    });


    /*
     * Passa alla pagina successiva.
     */
    pulsanteSuccessiva.addEventListener("click", function () {

        if (
            !caricamentoInCorso &&
            paginaCorrente < totalePagine
        ) {

            paginaCorrente++;

            caricaMateriali();
        }
    });
}


/*
 * Costruisce la URL completa della REST.
 *
 * Esempio senza filtro:
 * /rest/materiali/list?page=1&size=10
 *
 * Esempio con filtro:
 * /rest/materiali/list?page=1&size=10&stato=libero
 */
function costruisciUrl() {

    var url = "/soccorso_Web_SWA/rest/materiali/list";

    url += "?page=" + encodeURIComponent(paginaCorrente);
    url += "&size=" + encodeURIComponent(dimensionePagina);


    /*
     * Se il filtro è vuoto non aggiunge il parametro stato.
     * La REST restituirà tutti i materiali.
     */
    if (statoCorrente !== "") {

        url += "&stato=" + encodeURIComponent(statoCorrente);
    }


    return url;
}


/*
 * Carica i materiali dalla REST.
 */
function caricaMateriali() {

    nascondiErrore();

    mostraRigaInformativa(
        "Caricamento materiali..."
    );

    impostaCaricamento(true);


    fetch(costruisciUrl(), {

        method: "GET",

        headers: {
            "Accept": "application/json"
        },

        /*
         * Permette al browser di inviare il cookie JWT.
         */
        credentials: "same-origin"
    })
        .then(function (response) {

            return leggiRisposta(response);
        })
        .then(function (risultato) {

            /*
             * Controlla che la REST abbia restituito
             * una PaginatedResponse valida.
             */
            if (
                !risultato ||
                !Array.isArray(risultato.content)
            ) {

                throw new Error(
                    "La risposta della REST non è valida."
                );
            }


            /*
             * Aggiorna le informazioni della paginazione
             * usando i dati restituiti dal server.
             */
            paginaCorrente = numeroIntero(
                risultato.page,
                paginaCorrente
            );

            dimensionePagina = numeroIntero(
                risultato.size,
                dimensionePagina
            );

            totaleMateriali = numeroIntero(
                risultato.totalElements,
                0
            );

            totalePagine = numeroIntero(
                risultato.totalPages,
                0
            );


            mostraMateriali(risultato.content);

            aggiornaPaginazione();
        })
        .catch(function (errore) {

            console.error(
                "Errore caricamento materiali:",
                errore
            );


            totaleMateriali = 0;
            totalePagine = 0;


            mostraRigaInformativa(
                "Impossibile caricare i materiali."
            );

            mostraErrore(
                errore.message
            );

            aggiornaPaginazione();
        })
        .then(function () {

            /*
             * Equivalente di finally senza async/await.
             */
            impostaCaricamento(false);
        });
}


/*
 * Legge il corpo della risposta HTTP.
 *
 * Usa response.text() perché il server potrebbe restituire:
 * - una risposta JSON corretta;
 * - un ErrorResponse JSON;
 * - una risposta testuale;
 * - una pagina HTML di errore.
 */
function leggiRisposta(response) {

    return response.text().then(function (testo) {

        var contenuto = null;


        if (testo.trim() !== "") {

            try {

                contenuto = JSON.parse(testo);

            } catch (erroreParsing) {

                contenuto = testo;
            }
        }


        /*
         * response.ok è false per gli status HTTP
         * come 400, 401, 403, 404 e 500.
         */
        if (!response.ok) {

            throw new Error(
                estraiMessaggioErrore(
                    contenuto,
                    response.status,
                    response.statusText
                )
            );
        }


        return contenuto;
    });
}


/*
 * Estrae il messaggio dall'ErrorResponse restituito dalla REST.
 */
function estraiMessaggioErrore(
    contenuto,
    status,
    statusText
) {

    /*
     * Prova a leggere i possibili nomi
     * del campo del messaggio JSON.
     */
    if (
        contenuto &&
        typeof contenuto === "object"
    ) {

        return contenuto.message ||
            contenuto.messaggio ||
            contenuto.error ||
            contenuto.errore ||
            "Errore HTTP " + status + ".";
    }


    /*
     * Se il server ha restituito testo semplice,
     * lo mostra soltanto quando non sembra HTML.
     */
    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== "" &&
        contenuto.trim().charAt(0) !== "<"
    ) {

        return contenuto;
    }


    if (status === 400) {

        return "I parametri inviati non sono validi.";
    }


    if (status === 401) {

        return "Devi effettuare il login.";
    }


    if (status === 403) {

        return "Non hai i permessi per visualizzare i materiali.";
    }


    if (status === 404) {

        return "L'endpoint della lista materiali non è stato trovato.";
    }


    if (status >= 500) {

        return "Errore interno del server.";
    }


    return statusText ||
        "Errore HTTP " + status + ".";
}


/*
 * Mostra nella tabella i materiali ricevuti dalla REST.
 */
function mostraMateriali(materiali) {

    var corpoTabella = document.getElementById(
        "corpo-tabella-materiali"
    );


    /*
     * Elimina il contenuto precedente della tabella.
     */
    corpoTabella.innerHTML = "";


    /*
     * Gestisce il caso in cui la lista sia vuota.
     */
    if (materiali.length === 0) {

        mostraRigaInformativa(
            "Nessun materiale trovato."
        );

        return;
    }


    materiali.forEach(function (materiale) {

        var riga = document.createElement("tr");

        riga.appendChild(
            creaCella(materiale.tipologia)
        );

        riga.appendChild(
            creaCella(materiale.seriale)
        );

        riga.appendChild(
            creaCellaStato(materiale.status)
        );


        corpoTabella.appendChild(riga);
    });
}


/*
 * Crea una normale cella della tabella.
 */
function creaCella(valore) {

    var cella = document.createElement("td");

    cella.textContent = valoreTestuale(valore);

    return cella;
}


/*
 * Crea la cella dello stato con un badge colorato.
 */
function creaCellaStato(stato) {

    var cella = document.createElement("td");
    var badge = document.createElement("span");

    var statoNormalizzato = valoreTestuale(stato)
        .toLowerCase();


    badge.className = "status-badge";


    if (statoNormalizzato === "libero") {

        badge.classList.add("status-libero");

    } else if (statoNormalizzato === "occupato") {

        badge.classList.add("status-occupato");

    } else {

        badge.classList.add("status-sconosciuto");
    }


    badge.textContent = valoreTestuale(stato);

    cella.appendChild(badge);

    return cella;
}


/*
 * Mostra una riga singola all'interno della tabella.
 *
 * Viene usata per:
 * - caricamento;
 * - lista vuota;
 * - errore.
 */
function mostraRigaInformativa(messaggio) {

    var corpoTabella = document.getElementById(
        "corpo-tabella-materiali"
    );


    corpoTabella.innerHTML = "";


    var riga = document.createElement("tr");
    var cella = document.createElement("td");


    /*
     * La tabella contiene quattro colonne:
     * ID, tipologia, seriale e stato.
     */
    cella.colSpan = 4;

    cella.className =
        "text-center text-muted py-5";

    cella.textContent = messaggio;


    riga.appendChild(cella);

    corpoTabella.appendChild(riga);
}


/*
 * Aggiorna:
 * - numero totale dei materiali;
 * - indicazione della pagina;
 * - stato dei pulsanti.
 */
function aggiornaPaginazione() {

    var totale = document.getElementById(
        "totale-materiali"
    );

    var informazioni = document.getElementById(
        "informazioni-paginazione"
    );

    var precedente = document.getElementById(
        "pagina-precedente"
    );

    var successiva = document.getElementById(
        "pagina-successiva"
    );


    totale.textContent =
        "Materiali trovati: " + totaleMateriali;


    if (totalePagine === 0) {

        informazioni.textContent =
            "Pagina 0 di 0";

    } else {

        informazioni.textContent =
            "Pagina " +
            paginaCorrente +
            " di " +
            totalePagine;
    }


    /*
     * Il pulsante precedente viene disabilitato:
     * - durante il caricamento;
     * - nella prima pagina;
     * - quando non ci sono pagine.
     */
    precedente.disabled =
        caricamentoInCorso ||
        paginaCorrente <= 1 ||
        totalePagine === 0;


    /*
     * Il pulsante successivo viene disabilitato:
     * - durante il caricamento;
     * - nell'ultima pagina;
     * - quando non ci sono pagine.
     */
    successiva.disabled =
        caricamentoInCorso ||
        paginaCorrente >= totalePagine ||
        totalePagine === 0;
}


/*
 * Aggiorna lo stato di caricamento.
 */
function impostaCaricamento(valore) {

    caricamentoInCorso = valore;

    aggiornaPaginazione();
}


/*
 * Mostra il messaggio di errore sopra la tabella.
 */
function mostraErrore(messaggio) {

    var contenitore = document.getElementById(
        "messaggio-errore"
    );


    contenitore.textContent =
        messaggio ||
        "Si è verificato un errore.";


    contenitore.classList.remove("d-none");
}


/*
 * Nasconde e pulisce il messaggio di errore precedente.
 */
function nascondiErrore() {

    var contenitore = document.getElementById(
        "messaggio-errore"
    );


    contenitore.textContent = "";

    contenitore.classList.add("d-none");
}


/*
 * Converte un valore in numero intero.
 *
 * Se non è possibile, restituisce il valore predefinito.
 */
function numeroIntero(
    valore,
    valorePredefinito
) {

    var numero = parseInt(valore, 10);


    return isNaN(numero)
        ? valorePredefinito
        : numero;
}


/*
 * Converte un valore in testo sicuro per la tabella.
 */
function valoreTestuale(valore) {

    if (
        valore === null ||
        typeof valore === "undefined" ||
        String(valore).trim() === ""
    ) {

        return "-";
    }


    return String(valore);
}