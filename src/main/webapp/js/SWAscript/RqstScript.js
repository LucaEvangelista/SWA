
var paginaCorrente = 1;
var dimensionePagina = 10;
var totalePagine = 0;
var totaleElementi = 0;
var statoCorrente = "";
var richiesteCaricate = [];


 //Aspetta che la pagina HTML sia completamente caricata.
 
document.addEventListener("DOMContentLoaded", function () {
    collegaEventiPagina();
    caricaRichieste();
});

//Collega gli eventi ai controlli presenti nell'HTML
function collegaEventiPagina() {
    var filtroStato =
        document.getElementById("filtro-stato-richieste");

    var selezioneDimensione =
        document.getElementById("dimensione-pagina");

    var pulsantePrecedente =
        document.getElementById("pagina-precedente");

    var pulsanteSuccessiva =
        document.getElementById("pagina-successiva");

    /*
     * Quando cambia il filtro, viene eseguita una nuova chiamata
     * alla REST partendo dalla prima pagina.
     */
	
    if (filtroStato !== null) {
        filtroStato.addEventListener("change", function () {
            var valoreFiltro = filtroStato.value;

            if (
                valoreFiltro === "tutte" ||
                valoreFiltro === ""
            ) {
                statoCorrente = "";
            } else {
                statoCorrente = valoreFiltro;
            }

            paginaCorrente = 1;
            caricaRichieste();
        });
    }

    /*
     * Modifica il numero di richieste mostrate per pagina.
     */
    if (selezioneDimensione !== null) {
        selezioneDimensione.addEventListener("change", function () {
            var nuovaDimensione =
                parseInt(selezioneDimensione.value, 10);

            if (
                !isNaN(nuovaDimensione) &&
                nuovaDimensione >= 1 &&
                nuovaDimensione <= 100
            ) {
                dimensionePagina = nuovaDimensione;
                paginaCorrente = 1;

                caricaRichieste();
            }
        });
    }

    /*
     * Torna alla pagina precedente.
     */
    if (pulsantePrecedente !== null) {
        pulsantePrecedente.addEventListener("click", function () {
            if (paginaCorrente > 1) {
                paginaCorrente--;
                caricaRichieste();
            }
        });
    }

    /*
     * Passa alla pagina successiva.
     */
    if (pulsanteSuccessiva !== null) {
        pulsanteSuccessiva.addEventListener("click", function () {
            if (paginaCorrente < totalePagine) {
                paginaCorrente++;
                caricaRichieste();
            }
        });
    }
}

/*
 * Costruisce la URL della REST.
 *
 * Esempi:
 *
 * /rest/richieste/list?page=1&size=10
 *
 * /rest/richieste/list?page=1&size=10&stato=attiva
 */
function costruisciUrlRichieste() {
    var url =
        "/soccorso_Web_SWA/rest/richieste/list";

    url +=
        "?page=" +
        encodeURIComponent(paginaCorrente);

    url +=
        "&size=" +
        encodeURIComponent(dimensionePagina);

    /*
     * Il parametro stato viene aggiunto solo quando
     * è stato selezionato uno stato specifico.
     */
    if (
        statoCorrente !== null &&
        statoCorrente !== ""
    ) {
        url +=
            "&stato=" +
            encodeURIComponent(statoCorrente);
    }

    return url;
}

/*
 * Carica una pagina di richieste dalla REST.
 */
function caricaRichieste() {
    var url = costruisciUrlRichieste();

    nascondiMessaggioErrore();
    impostaCaricamento(true);

    fetch(url, {
        method: "GET",

        headers: {
            "Accept": "application/json"
        },

        /*
         * Invia il cookie contenente il token
         * di autenticazione.
         */
        credentials: "same-origin"
    })
        .then(function (response) {
            return leggiRispostaRest(response);
        })
        .then(function (risultato) {
            /*
             * La REST non restituisce più direttamente un array.
             *
             * Restituisce un oggetto PaginatedResponse simile a:
             *
             * {
             *     "content": [],
             *     "page": 1,
             *     "size": 10,
             *     "totalElements": 25,
             *     "totalPages": 3,
             *     "first": true,
             *     "last": false
             * }
             */

            validaRispostaPaginata(risultato);

            richiesteCaricate = risultato.content;

            paginaCorrente = numeroIntero(
                risultato.page,
                paginaCorrente
            );

            dimensionePagina = numeroIntero(
                risultato.size,
                dimensionePagina
            );

            totaleElementi = numeroIntero(
                risultato.totalElements,
                0
            );

            totalePagine = numeroIntero(
                risultato.totalPages,
                0
            );

            mostraRichieste(richiesteCaricate);
            aggiornaControlliPaginazione();
        })
        .catch(function (errore) {
            console.error(
                "Errore durante il caricamento delle richieste:",
                errore
            );

            richiesteCaricate = [];
            totaleElementi = 0;
            totalePagine = 0;

            mostraTabellaVuota();
            aggiornaControlliPaginazione();

            mostraMessaggioErrore(
                errore.message ||
                "Errore durante il caricamento delle richieste."
            );
        })
        .then(function () {
            /*
             * Viene eseguito sia dopo il successo
             * sia dopo la gestione dell'errore.
             */
            impostaCaricamento(false);
        });
}

/*
 * Controlla che il JSON restituito dalla REST
 * abbia la struttura di PaginatedResponse.
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
 * Legge sempre il corpo della risposta REST,
 * anche quando lo status HTTP indica un errore.
 *
 * In questo modo viene recuperato il messaggio
 * contenuto nell'ErrorResponse.
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
                /*
                 * La risposta non è JSON.
                 */
                contenuto = testoRisposta;
            }
        }

        if (!response.ok) {
            var messaggio =
                estraiMessaggioErrore(
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
 * Estrae il messaggio contenuto nell'ErrorResponse.
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

        if (contenuto.detail) {
            return contenuto.detail;
        }
    }

    /*
     * Se la REST restituisce direttamente una stringa,
     * viene mostrata solo se non è una pagina HTML.
     */
    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== ""
    ) {
        if (contenuto.trim().charAt(0) !== "<") {
            return contenuto;
        }
    }

    if (status === 400) {
        return "I parametri inviati alla REST non sono validi.";
    }

    if (status === 401) {
        return "Non sei autenticato. Devi effettuare il login per visualizzare le richieste.";
    }

    if (status === 403) {
        return "Non hai i permessi necessari per visualizzare le richieste.";
    }

    if (status === 404) {
        return "La risorsa richiesta non è stata trovata.";
    }

    if (status === 409) {
        return "L'operazione richiesta non può essere eseguita nello stato attuale.";
    }

    if (status === 500) {
        return "Si è verificato un errore interno del server.";
    }

    if (
        statusText !== null &&
        statusText !== undefined &&
        statusText !== ""
    ) {
        return "Errore HTTP " +
            status +
            ": " +
            statusText;
    }

    return "Errore HTTP " + status + ".";
}

/*
 * Mostra le richieste della pagina corrente nella tabella.
 */
function mostraRichieste(richieste) {
    var tbody =
        document.getElementById(
            "corpo-tabella-richieste"
        );

    if (tbody === null) {
        console.error(
            "Elemento con id corpo-tabella-richieste non trovato."
        );

        return;
    }

    var righe = "";

    if (richieste.length === 0) {
        tbody.innerHTML =
            "<tr>" +
                "<td colspan='9' " +
                    "class='text-center text-muted'>" +
                    "Nessuna richiesta trovata" +
                "</td>" +
            "</tr>";

        return;
    }

    for (
        var i = 0;
        i < richieste.length;
        i++
    ) {
        var rq = richieste[i];

        var idRichiesta =
            numeroIntero(rq.id, 0);

        righe += "<tr>";

        righe +=
            "<td>" +
                valoreSicuro(rq.nomePersona) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(rq.mailPersona) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(rq.indirizzo) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(rq.descrizione) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(rq.fase) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(
                    formattaDataOra(rq.createdAt)
                ) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(
                    formattaDataOra(rq.workingAt)
                ) +
            "</td>";

        righe +=
            "<td>" +
                valoreSicuro(
                    formattaDataOra(rq.closedAt)
                ) +
            "</td>";

        righe +=
            "<td class='text-center'>";

        righe +=
            "<button " +
                "type='button' " +
                "class='btn btn-primary btn-sm' " +
                "onclick='vediDettaglioRichiesta(" +
                    idRichiesta +
                ")'>" +
                "Dettaglio" +
            "</button>";

        righe += "</td>";
        righe += "</tr>";
    }

    tbody.innerHTML = righe;
}

/*
 * Aggiorna il testo e lo stato dei pulsanti
 * della paginazione.
 */
function aggiornaControlliPaginazione() {
    var informazioni =
        document.getElementById(
            "informazioni-paginazione"
        );

    var totale =
        document.getElementById(
            "totale-richieste"
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

    /*
     * Se non esistono risultati mostriamo
     * "Pagina 0 di 0".
     */
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
            "Richieste trovate: " +
            totaleElementi;
    }

    if (pulsantePrecedente !== null) {
        pulsantePrecedente.disabled =
            totalePagine === 0 ||
            paginaCorrente <= 1;
    }

    if (pulsanteSuccessiva !== null) {
        pulsanteSuccessiva.disabled =
            totalePagine === 0 ||
            paginaCorrente >= totalePagine;
    }

    if (selezioneDimensione !== null) {
        selezioneDimensione.value =
            String(dimensionePagina);
    }
}

/*
 * Mostra una riga temporanea durante il caricamento
 * e disabilita i controlli.
 */
function impostaCaricamento(inCaricamento) {
    var tbody =
        document.getElementById(
            "corpo-tabella-richieste"
        );

    var filtroStato =
        document.getElementById(
            "filtro-stato-richieste"
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

    if (filtroStato !== null) {
        filtroStato.disabled =
            inCaricamento;
    }

    if (selezioneDimensione !== null) {
        selezioneDimensione.disabled =
            inCaricamento;
    }

    if (inCaricamento) {
        if (pulsantePrecedente !== null) {
            pulsantePrecedente.disabled = true;
        }

        if (pulsanteSuccessiva !== null) {
            pulsanteSuccessiva.disabled = true;
        }

        if (tbody !== null) {
            tbody.innerHTML =
                "<tr>" +
                    "<td colspan='9' " +
                        "class='text-center text-muted'>" +
                        "Caricamento richieste..." +
                    "</td>" +
                "</tr>";
        }
    } else {
        aggiornaControlliPaginazione();
    }
}

/*
 * Mostra il messaggio di errore nella pagina HTML.
 */
function mostraMessaggioErrore(messaggio) {
    var messaggioErrore =
        document.getElementById(
            "messaggio-errore"
        );

    if (messaggioErrore === null) {
        console.error(
            "Elemento con id messaggio-errore non trovato nell'HTML."
        );

        return;
    }

    messaggioErrore.textContent =
        messaggio ||
        "Errore durante il caricamento delle richieste.";

    messaggioErrore.classList.remove(
        "d-none"
    );
}

/*
 * Nasconde un eventuale messaggio precedente.
 */
function nascondiMessaggioErrore() {
    var messaggioErrore =
        document.getElementById(
            "messaggio-errore"
        );

    if (messaggioErrore !== null) {
        messaggioErrore.textContent = "";

        messaggioErrore.classList.add(
            "d-none"
        );
    }
}

/*
 * Pulisce la tabella quando la REST restituisce
 * un errore.
 */
function mostraTabellaVuota() {
    var tbody =
        document.getElementById(
            "corpo-tabella-richieste"
        );

    if (tbody !== null) {
        tbody.innerHTML =
            "<tr>" +
                "<td colspan='9' " +
                    "class='text-center text-muted'>" +
                    "Impossibile caricare le richieste" +
                "</td>" +
            "</tr>";
    }
}

/*
 * Apre la pagina del dettaglio della richiesta.
 */
function vediDettaglioRichiesta(id) {
    window.location.href =
        "/soccorso_Web_SWA/viste/" +
        "DettagliRichieste.html?id=" +
        encodeURIComponent(id);
}

/*
 * Converte un valore in numero intero.
 */
function numeroIntero(
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
 * Evita di stampare null o undefined
 * e protegge il contenuto HTML.
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
 * Impedisce che i dati ricevuti dalla REST
 * siano interpretati come codice HTML.
 */
function escapeHtml(testo) {
    return testo
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

/*
 * Formatta le date LocalDateTime ricevute
 * dalla REST.
 */
function formattaDataOra(data) {
    if (
        data === null ||
        data === undefined ||
        data === ""
    ) {
        return "-";
    }

    var d;

    /*
     * Jackson può restituire una stringa:
     *
     * "2026-07-12T16:30:00"
     */
    if (typeof data === "string") {
        d = new Date(data);
    }

    /*
     * Oppure può restituire un array:
     *
     * [2026, 7, 12, 16, 30, 0]
     */
    else if (Array.isArray(data)) {
        d = new Date(
            data[0],
            data[1] - 1,
            data[2],
            data[3] || 0,
            data[4] || 0,
            data[5] || 0
        );
    } else {
        return data;
    }

    if (isNaN(d.getTime())) {
        return data;
    }

    return d.toLocaleString(
        "it-IT",
        {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}