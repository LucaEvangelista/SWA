"use strict";

/*
 * Configurazione generale.
 */
var CONTEXT_PATH = "/soccorso_Web_SWA";
var URL_LISTA_OPERATORI =
    CONTEXT_PATH + "/rest/operatori/list";

/*
 * Stato corrente della paginazione.
 */
var paginaCorrente = 1;
var dimensionePagina = 10;
var totalePagine = 0;
var totaleOperatori = 0;
var statoCorrente = "";

/*
 * Avvio della pagina.
 */
document.addEventListener(
    "DOMContentLoaded",
    function () {

        collegaEventiPagina();
        caricaOperatori();
    }
);

/*
 * Collega gli eventi ai controlli HTML.
 */
function collegaEventiPagina() {

    var filtroStato =
        document.getElementById(
            "filtro-stato-operatori"
        );

    var selezioneDimensione =
        document.getElementById(
            "dimensione-pagina"
        );

    var pulsantePrecedente =
        document.getElementById(
            "pagina-precedente"
        );

    var pulsanteSuccessiva =
        document.getElementById(
            "pagina-successiva"
        );

    /*
     * Quando cambia il filtro, si torna
     * automaticamente alla prima pagina.
     */
    if (filtroStato !== null) {

        filtroStato.addEventListener(
            "change",
            function () {

                var valore =
                    filtroStato.value;

                if (
                    valore === "tutti" ||
                    valore === ""
                ) {
                    statoCorrente = "";
                } else {
                    statoCorrente = valore;
                }

                paginaCorrente = 1;

                caricaOperatori();
            }
        );
    }

    /*
     * Cambiamento del numero di elementi
     * mostrati per pagina.
     */
    if (selezioneDimensione !== null) {

        selezioneDimensione.addEventListener(
            "change",
            function () {

                var nuovaDimensione =
                    parseInt(
                        selezioneDimensione.value,
                        10
                    );

                if (
                    !isNaN(nuovaDimensione) &&
                    nuovaDimensione >= 1 &&
                    nuovaDimensione <= 100
                ) {
                    dimensionePagina =
                        nuovaDimensione;

                    paginaCorrente = 1;

                    caricaOperatori();
                }
            }
        );
    }

    /*
     * Pagina precedente.
     */
    if (pulsantePrecedente !== null) {

        pulsantePrecedente.addEventListener(
            "click",
            function () {

                if (paginaCorrente > 1) {
                    paginaCorrente--;

                    caricaOperatori();
                }
            }
        );
    }

    /*
     * Pagina successiva.
     */
    if (pulsanteSuccessiva !== null) {

        pulsanteSuccessiva.addEventListener(
            "click",
            function () {

                if (
                    paginaCorrente <
                    totalePagine
                ) {
                    paginaCorrente++;

                    caricaOperatori();
                }
            }
        );
    }
}

/*
 * Costruisce la URL della REST.
 *
 * Esempio senza filtro:
 *
 * /rest/operatori/list?page=1&size=10
 *
 * Esempio con filtro:
 *
 * /rest/operatori/list?page=1&size=10&stato=libero
 */
function costruisciUrlOperatori() {

    var url =
        URL_LISTA_OPERATORI;

    url +=
        "?page=" +
        encodeURIComponent(
            paginaCorrente
        );

    url +=
        "&size=" +
        encodeURIComponent(
            dimensionePagina
        );

    if (
        statoCorrente !== null &&
        statoCorrente !== ""
    ) {
        url +=
            "&stato=" +
            encodeURIComponent(
                statoCorrente
            );
    }

    return url;
}

/*
 * Costruisce gli header della richiesta.
 */
function creaHeaderAutenticazione() {

    var headers = {
        "Accept": "application/json"
    };

    var token =
        sessionStorage.getItem(
            "authToken"
        );

    /*
     * Se il token è presente viene inviato
     * anche nell'header Authorization.
     */
    if (
        token !== null &&
        token.trim() !== ""
    ) {
        headers.Authorization =
            "Bearer " + token.trim();
    }

    return headers;
}

/*
 * Carica una pagina di operatori.
 */
function caricaOperatori() {

    var url =
        costruisciUrlOperatori();

    nascondiMessaggioErrore();
    impostaCaricamento(true);

    fetch(
        url,
        {
            method: "GET",

            headers:
                creaHeaderAutenticazione(),

            /*
             * Permette di inviare il cookie token
             * creato durante il login.
             */
            credentials: "include"
        }
    )
        .then(function (response) {

            return leggiRispostaRest(
                response
            );
        })
        .then(function (risultato) {

            validaRispostaPaginata(
                risultato
            );

            paginaCorrente =
                convertiIntero(
                    risultato.page,
                    paginaCorrente
                );

            dimensionePagina =
                convertiIntero(
                    risultato.size,
                    dimensionePagina
                );

            totaleOperatori =
                convertiIntero(
                    risultato.totalElements,
                    0
                );

            totalePagine =
                convertiIntero(
                    risultato.totalPages,
                    0
                );

            mostraOperatori(
                risultato.content
            );

            aggiornaPaginazione();
        })
        .catch(function (errore) {

            console.error(
                "Errore durante il caricamento degli operatori:",
                errore
            );

            totaleOperatori = 0;
            totalePagine = 0;

            mostraTabellaErrore();
            aggiornaPaginazione();

            mostraMessaggioErrore(
                errore.message ||
                "Errore durante il caricamento degli operatori."
            );
        })
        .then(function () {

            /*
             * Equivalente a finally,
             * senza utilizzare async/await.
             */
            impostaCaricamento(false);
        });
}

/*
 * Legge il corpo della risposta anche quando
 * lo status HTTP indica un errore.
 */
function leggiRispostaRest(response) {

    return response
        .text()
        .then(function (testo) {

            var contenuto = null;

            if (
                testo !== null &&
                testo.trim() !== ""
            ) {
                try {
                    contenuto =
                        JSON.parse(testo);
                } catch (erroreParsing) {
                    contenuto = testo;
                }
            }

            if (!response.ok) {

                var messaggio =
                    estraiMessaggioErrore(
                        contenuto,
                        response.status,
                        response.statusText
                    );

                var erroreHttp =
                    new Error(messaggio);

                erroreHttp.status =
                    response.status;

                throw erroreHttp;
            }

            return contenuto;
        });
}

/*
 * Controlla che la risposta abbia la struttura
 * del PaginatedResponse.
 */
function validaRispostaPaginata(
    risultato
) {

    if (
        risultato === null ||
        typeof risultato !== "object"
    ) {
        throw new Error(
            "La REST non ha restituito una risposta paginata valida."
        );
    }

    if (
        !Array.isArray(
            risultato.content
        )
    ) {
        throw new Error(
            "Il campo content non contiene una lista di operatori valida."
        );
    }
}

/*
 * Mostra gli operatori nella tabella.
 */
function mostraOperatori(
    operatori
) {

    var tbody =
        document.getElementById(
            "corpo-tabella-operatori"
        );

    if (tbody === null) {

        console.error(
            "Elemento corpo-tabella-operatori non trovato."
        );

        return;
    }

    if (
        operatori === null ||
        operatori.length === 0
    ) {
        tbody.innerHTML =
            "<tr>" +
                "<td colspan='6' " +
                    "class='text-center text-muted'>" +
                    "Nessun operatore trovato" +
                "</td>" +
            "</tr>";

        return;
    }

    var righe = "";

    for (
        var i = 0;
        i < operatori.length;
        i++
    ) {
        var op = operatori[i];

        righe += "<tr>";

        righe +=
            "<td>" +
                valoreSicuro(op.name) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(op.surname) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(op.email) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(
                    formattaData(op.age)
                ) +
            "</td>";

        righe +=
            "<td>" +
                creaBadgeStato(
                    op.status
                ) +
            "</td>";

        righe +=
            "<td class='text-center'>" +
                "<button " +
                    "type='button' " +
                    "class='btn btn-primary btn-sm' " +
                    "onclick='vediDettaglio(" +
                        convertiIntero(
                            op.id,
                            0
                        ) +
                    ")'>" +
                    "Dettaglio" +
                "</button>" +
            "</td>";

        righe += "</tr>";
    }

    tbody.innerHTML = righe;
}

/*
 * Badge Bootstrap per lo stato.
 */
function creaBadgeStato(stato) {

    if (
        stato === null ||
        stato === undefined ||
        stato === ""
    ) {
        return "-";
    }

    var statoNormalizzato =
        String(stato).toLowerCase();

    if (statoNormalizzato === "libero") {

        return (
            "<span class='badge text-bg-success'>" +
                "Libero" +
            "</span>"
        );
    }

    if (statoNormalizzato === "occupato") {

        return (
            "<span class='badge text-bg-warning'>" +
                "Occupato" +
            "</span>"
        );
    }

    return valoreSicuro(stato);
}

/*
 * Aggiorna i pulsanti e le informazioni
 * della paginazione.
 */
function aggiornaPaginazione() {

    var informazioni =
        document.getElementById(
            "informazioni-paginazione"
        );

    var totale =
        document.getElementById(
            "totale-operatori"
        );

    var pulsantePrecedente =
        document.getElementById(
            "pagina-precedente"
        );

    var pulsanteSuccessiva =
        document.getElementById(
            "pagina-successiva"
        );

    var selezioneDimensione =
        document.getElementById(
            "dimensione-pagina"
        );

    var paginaVisualizzata =
        paginaCorrente;

    if (totalePagine === 0) {
        paginaVisualizzata = 0;
    }

    if (informazioni !== null) {

        informazioni.textContent =
            "Pagina " +
            paginaVisualizzata +
            " di " +
            totalePagine;
    }

    if (totale !== null) {

        totale.textContent =
            "Operatori trovati: " +
            totaleOperatori;
    }

    if (pulsantePrecedente !== null) {

        pulsantePrecedente.disabled =
            totalePagine === 0 ||
            paginaCorrente <= 1;
    }

    if (pulsanteSuccessiva !== null) {

        pulsanteSuccessiva.disabled =
            totalePagine === 0 ||
            paginaCorrente >=
                totalePagine;
    }

    if (selezioneDimensione !== null) {

        selezioneDimensione.value =
            String(
                dimensionePagina
            );
    }
}

/*
 * Mostra lo stato di caricamento.
 */
function impostaCaricamento(
    caricamento
) {

    var tbody =
        document.getElementById(
            "corpo-tabella-operatori"
        );

    var filtro =
        document.getElementById(
            "filtro-stato-operatori"
        );

    var selezioneDimensione =
        document.getElementById(
            "dimensione-pagina"
        );

    var pulsantePrecedente =
        document.getElementById(
            "pagina-precedente"
        );

    var pulsanteSuccessiva =
        document.getElementById(
            "pagina-successiva"
        );

    if (filtro !== null) {
        filtro.disabled =
            caricamento;
    }

    if (selezioneDimensione !== null) {
        selezioneDimensione.disabled =
            caricamento;
    }

    if (caricamento) {

        if (pulsantePrecedente !== null) {
            pulsantePrecedente.disabled =
                true;
        }

        if (pulsanteSuccessiva !== null) {
            pulsanteSuccessiva.disabled =
                true;
        }

        if (tbody !== null) {

            tbody.innerHTML =
                "<tr>" +
                    "<td colspan='6' " +
                        "class='text-center text-muted'>" +
                        "Caricamento operatori..." +
                    "</td>" +
                "</tr>";
        }

    } else {

        aggiornaPaginazione();
    }
}

/*
 * Mostra una riga in caso di errore.
 */
function mostraTabellaErrore() {

    var tbody =
        document.getElementById(
            "corpo-tabella-operatori"
        );

    if (tbody !== null) {

        tbody.innerHTML =
            "<tr>" +
                "<td colspan='6' " +
                    "class='text-center text-danger'>" +
                    "Impossibile caricare gli operatori" +
                "</td>" +
            "</tr>";
    }
}

/*
 * Estrae il messaggio restituito
 * dall'ErrorResponse Java.
 */
function estraiMessaggioErrore(
    contenuto,
    status,
    statusText
) {

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
    }

    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== "" &&
        contenuto.trim().charAt(0) !== "<"
    ) {
        return contenuto;
    }

    if (status === 400) {
        return "I parametri della richiesta non sono validi.";
    }

    if (status === 401) {
        return "Non sei autenticato. Effettua nuovamente il login.";
    }

    if (status === 403) {
        return "Non hai i permessi per visualizzare la lista degli operatori.";
    }

    if (status === 404) {
        return "La risorsa richiesta non è stata trovata.";
    }

    if (status === 500) {
        return "Si è verificato un errore interno del server.";
    }

    if (
        statusText !== null &&
        statusText !== undefined &&
        statusText !== ""
    ) {
        return (
            "Errore HTTP " +
            status +
            ": " +
            statusText
        );
    }

    return "Errore HTTP " + status + ".";
}

/*
 * Mostra il messaggio di errore.
 */
function mostraMessaggioErrore(
    messaggio
) {

    var elemento =
        document.getElementById(
            "messaggio-errore"
        );

    if (elemento === null) {
        return;
    }

    elemento.textContent =
        messaggio;

    elemento.classList.remove(
        "d-none"
    );
}

/*
 * Nasconde il messaggio precedente.
 */
function nascondiMessaggioErrore() {

    var elemento =
        document.getElementById(
            "messaggio-errore"
        );

    if (elemento !== null) {

        elemento.textContent = "";

        elemento.classList.add(
            "d-none"
        );
    }
}

/*
 * Apertura della pagina di dettaglio.
 */
function vediDettaglio(id) {

    window.location.href =
        CONTEXT_PATH +
        "/viste/DettaglioOperatore.html?id=" +
        encodeURIComponent(id);
}

/*
 * Formatta LocalDate:
 *
 * 1995-01-01 -> 01/01/1995
 */
function formattaData(data) {

    if (
        data === null ||
        data === undefined ||
        data === ""
    ) {
        return "-";
    }

    /*
     * Jackson normalmente restituisce LocalDate
     * come stringa yyyy-MM-dd.
     */
    if (typeof data === "string") {

        var parti =
            data.split("-");

        if (parti.length === 3) {

            return (
                parti[2] +
                "/" +
                parti[1] +
                "/" +
                parti[0]
            );
        }

        return data;
    }

    /*
     * Gestisce anche l'eventuale formato array:
     * [1995, 1, 1]
     */
    if (
        Array.isArray(data) &&
        data.length >= 3
    ) {
        return (
            aggiungiZero(data[2]) +
            "/" +
            aggiungiZero(data[1]) +
            "/" +
            data[0]
        );
    }

    return String(data);
}

function aggiungiZero(numero) {

    if (numero < 10) {
        return "0" + numero;
    }

    return String(numero);
}

/*
 * Conversione sicura in intero.
 */
function convertiIntero(
    valore,
    valorePredefinito
) {

    var numero =
        parseInt(valore, 10);

    if (isNaN(numero)) {
        return valorePredefinito;
    }

    return numero;
}

/*
 * Gestione sicura di null e undefined.
 */
function valoreSicuro(valore) {

    if (
        valore === null ||
        valore === undefined ||
        valore === ""
    ) {
        return "-";
    }

    return escapeHtml(
        String(valore)
    );
}

/*
 * Protezione dei valori inseriti nella tabella.
 */
function escapeHtml(testo) {

    return testo
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}