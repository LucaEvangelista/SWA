var paginaCorrente = 1;
var dimensionePagina = 10;
var statoCorrente = "";
var totaleElementi = 0;
var totalePagine = 0;
var caricamentoInCorso = false;


/*
 * Quando il DOM è pronto, collega gli eventi e carica la prima pagina.
 */
document.addEventListener("DOMContentLoaded", function () {
    collegaEventi();
    caricaMezzi();
});


/*
 * Collega gli eventi al filtro, alla dimensione della pagina
 * e ai pulsanti della paginazione.
 */
function collegaEventi() {
    var filtroStato = document.getElementById("filtro-stato-mezzi");
    var selezioneDimensione = document.getElementById("dimensione-pagina");
    var pulsantePrecedente = document.getElementById("pagina-precedente");
    var pulsanteSuccessiva = document.getElementById("pagina-successiva");

    if (filtroStato !== null) {
        filtroStato.addEventListener("change", function () {
            statoCorrente = filtroStato.value;
            paginaCorrente = 1;
            caricaMezzi();
        });
    }

    if (selezioneDimensione !== null) {
        selezioneDimensione.addEventListener("change", function () {
            var nuovaDimensione = parseInt(
                selezioneDimensione.value,
                10
            );

            if (!isNaN(nuovaDimensione) && nuovaDimensione > 0) {
                dimensionePagina = nuovaDimensione;
                paginaCorrente = 1;
                caricaMezzi();
            }
        });
    }

    if (pulsantePrecedente !== null) {
        pulsantePrecedente.addEventListener("click", function () {
            if (!caricamentoInCorso && paginaCorrente > 1) {
                paginaCorrente--;
                caricaMezzi();
            }
        });
    }

    if (pulsanteSuccessiva !== null) {
        pulsanteSuccessiva.addEventListener("click", function () {
            if (
                !caricamentoInCorso &&
                paginaCorrente < totalePagine
            ) {
                paginaCorrente++;
                caricaMezzi();
            }
        });
    }
}


/*
 * Costruisce la URL della REST.
 *
 * Quando statoCorrente è vuoto, il parametro "stato"
 * non viene aggiunto e la REST restituisce tutti i mezzi.
 */
function costruisciUrlMezzi() {
    var url = "/soccorso_Web_SWA/rest/mezzi/list";

    url += "?page=" + encodeURIComponent(paginaCorrente);
    url += "&size=" + encodeURIComponent(dimensionePagina);

    if (statoCorrente !== null && statoCorrente !== "") {
        url += "&stato=" + encodeURIComponent(statoCorrente);
    }

    return url;
}


/*
 * Carica dalla REST la pagina corrente dei mezzi.
 */
function caricaMezzi() {
    var url = costruisciUrlMezzi();

    nascondiMessaggioErrore();
    mostraCaricamento();
    impostaCaricamento(true);

    fetch(url, {
        method: "GET",

        headers: {
            "Accept": "application/json"
        },

        /*
         * Invia il cookie contenente il token JWT.
         */
        credentials: "same-origin"
    })
        .then(function (response) {
            return leggiRispostaRest(response);
        })
        .then(function (risultato) {
            validaRispostaPaginata(risultato);

            paginaCorrente = convertiIntero(
                risultato.page,
                paginaCorrente
            );

            dimensionePagina = convertiIntero(
                risultato.size,
                dimensionePagina
            );

            totaleElementi = convertiIntero(
                risultato.totalElements,
                0
            );

            totalePagine = convertiIntero(
                risultato.totalPages,
                0
            );

            mostraMezzi(risultato.content);
            aggiornaPaginazione();
        })
        .catch(function (errore) {
            console.error(
                "Errore durante il caricamento dei mezzi:",
                errore
            );

            totaleElementi = 0;
            totalePagine = 0;

            mostraTabellaVuota(
                "Impossibile caricare i mezzi."
            );

            aggiornaPaginazione();

            mostraMessaggioErrore(
                errore.message ||
                "Errore durante il caricamento dei mezzi."
            );
        })
        .then(function () {
            /*
             * Equivale a finally e mantiene una sintassi
             * compatibile senza async/await.
             */
            impostaCaricamento(false);
        });
}


/*
 * Controlla che il JSON ricevuto abbia la struttura
 * della PaginatedResponse restituita dalla REST.
 */
function validaRispostaPaginata(risultato) {
    if (
        risultato === null ||
        typeof risultato !== "object"
    ) {
        throw new Error(
            "La REST non ha restituito una risposta paginata valida."
        );
    }

    if (!Array.isArray(risultato.content)) {
        throw new Error(
            "Il campo content della risposta non contiene una lista valida."
        );
    }
}


/*
 * Legge il corpo della risposta anche in caso di errore HTTP,
 * così è possibile mostrare il messaggio dell'ErrorResponse.
 */
function leggiRispostaRest(response) {
    return response.text().then(function (testoRisposta) {
        var contenuto = null;

        if (
            testoRisposta !== null &&
            testoRisposta.trim() !== ""
        ) {
            try {
                contenuto = JSON.parse(testoRisposta);
            } catch (erroreParsing) {
                contenuto = testoRisposta;
            }
        }

        if (!response.ok) {
            var messaggio = estraiMessaggioErrore(
                contenuto,
                response.status,
                response.statusText
            );

            var erroreHttp = new Error(messaggio);

            erroreHttp.status = response.status;
            erroreHttp.contenuto = contenuto;

            throw erroreHttp;
        }

        return contenuto;
    });
}


/*
 * Estrae il messaggio dall'ErrorResponse restituito dal server.
 */
function estraiMessaggioErrore(contenuto, status, statusText) {
    if (
        contenuto !== null &&
        typeof contenuto === "object"
    ) {
        if (contenuto.message) {
            return contenuto.message;
        }

        if (contenuto.messaggio) {
            return contenuto.messaggio;
        }

        if (contenuto.error) {
            return contenuto.error;
        }

        if (contenuto.errore) {
            return contenuto.errore;
        }

        if (contenuto.detail) {
            return contenuto.detail;
        }
    }

    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== "" &&
        contenuto.trim().charAt(0) !== "<"
    ) {
        return contenuto;
    }

    if (status === 400) {
        return "I parametri inviati alla REST non sono validi.";
    }

    if (status === 401) {
        return "Devi effettuare il login per visualizzare i mezzi.";
    }

    if (status === 403) {
        return "Non hai i permessi per visualizzare la lista dei mezzi.";
    }

    if (status === 404) {
        return "L'endpoint della lista mezzi non è stato trovato.";
    }

    if (status >= 500) {
        return "Si è verificato un errore interno del server.";
    }

    if (statusText !== null && statusText !== "") {
        return "Errore HTTP " + status + ": " + statusText;
    }

    return "Errore HTTP " + status + ".";
}


/*
 * Inserisce i mezzi restituiti dalla REST nella tabella.
 */
function mostraMezzi(mezzi) {
    var corpoTabella = document.getElementById(
        "corpo-tabella-mezzi"
    );

    if (corpoTabella === null) {
        return;
    }

    corpoTabella.innerHTML = "";

    if (!Array.isArray(mezzi) || mezzi.length === 0) {
        mostraTabellaVuota(
            "Nessun mezzo trovato con i filtri selezionati."
        );

        return;
    }

    mezzi.forEach(function (mezzo) {
        var riga = document.createElement("tr");

        riga.appendChild(
            creaCella(valoreTestuale(mezzo.tipologia))
        );

        riga.appendChild(
            creaCella(valoreTestuale(mezzo.seriale))
        );

        riga.appendChild(
            creaCellaStato(mezzo.status)
        );

        corpoTabella.appendChild(riga);
    });
}


/*
 * Crea una normale cella della tabella.
 */
function creaCella(valore) {
    var cella = document.createElement("td");

    cella.textContent = valore;

    return cella;
}


/*
 * Crea la cella dello stato usando le classi definite nel CSS.
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
 * Mostra una singola riga quando non ci sono dati da visualizzare.
 */
function mostraTabellaVuota(messaggio) {
    var corpoTabella = document.getElementById(
        "corpo-tabella-mezzi"
    );

    if (corpoTabella === null) {
        return;
    }

    corpoTabella.innerHTML = "";

    var riga = document.createElement("tr");
    var cella = document.createElement("td");

    cella.colSpan = 4;
    cella.className = "text-center text-muted py-5";
    cella.textContent = messaggio;

    riga.appendChild(cella);
    corpoTabella.appendChild(riga);
}


/*
 * Mostra il messaggio di caricamento nella tabella.
 */
function mostraCaricamento() {
    mostraTabellaVuota("Caricamento mezzi...");
}


/*
 * Aggiorna il totale, il testo della pagina e i pulsanti.
 */
function aggiornaPaginazione() {
    var totale = document.getElementById("totale-mezzi");

    var informazioni = document.getElementById(
        "informazioni-paginazione"
    );

    var pulsantePrecedente = document.getElementById(
        "pagina-precedente"
    );

    var pulsanteSuccessiva = document.getElementById(
        "pagina-successiva"
    );

    if (totale !== null) {
        totale.textContent =
            "Mezzi trovati: " + totaleElementi;
    }

    if (informazioni !== null) {
        if (totalePagine === 0) {
            informazioni.textContent = "Pagina 0 di 0";
        } else {
            informazioni.textContent =
                "Pagina " +
                paginaCorrente +
                " di " +
                totalePagine;
        }
    }

    if (pulsantePrecedente !== null) {
        pulsantePrecedente.disabled =
            caricamentoInCorso ||
            totalePagine === 0 ||
            paginaCorrente <= 1;
    }

    if (pulsanteSuccessiva !== null) {
        pulsanteSuccessiva.disabled =
            caricamentoInCorso ||
            totalePagine === 0 ||
            paginaCorrente >= totalePagine;
    }
}


/*
 * Attiva o disattiva lo stato di caricamento.
 */
function impostaCaricamento(valore) {
    caricamentoInCorso = valore === true;

    aggiornaPaginazione();
}


/*
 * Mostra un messaggio di errore sopra le card.
 */
function mostraMessaggioErrore(messaggio) {
    var contenitore = document.getElementById(
        "messaggio-errore"
    );

    if (contenitore === null) {
        return;
    }

    contenitore.textContent = messaggio;
    contenitore.classList.remove("d-none");
}


/*
 * Nasconde e pulisce il messaggio di errore.
 */
function nascondiMessaggioErrore() {
    var contenitore = document.getElementById(
        "messaggio-errore"
    );

    if (contenitore === null) {
        return;
    }

    contenitore.textContent = "";
    contenitore.classList.add("d-none");
}


/*
 * Converte il valore ricevuto in un numero intero.
 */
function convertiIntero(valore, valorePredefinito) {
    var numero = parseInt(valore, 10);

    if (isNaN(numero)) {
        return valorePredefinito;
    }

    return numero;
}


/*
 * Restituisce un testo sicuro per la visualizzazione.
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