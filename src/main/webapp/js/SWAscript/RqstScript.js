// Array globale: contiene tutte le richieste ricevute dalla REST
var richiesteCaricate = [];

// Aspetta che la pagina HTML sia caricata
document.addEventListener("DOMContentLoaded", function () {
    caricaRichieste();

    var filtroStato = document.getElementById("filtro-stato-richieste");

    if (filtroStato !== null) {
        filtroStato.addEventListener("change", function () {
            filtraRichiestePerStato();
        });
    }
});

// Chiama l'endpoint REST
function caricaRichieste() {
    var url = "/soccorso_Web_SWA/rest/richieste/list";

    // Nasconde un eventuale vecchio messaggio di errore
    nascondiMessaggioErrore();

    fetch(url, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },

        // Invia il cookie contenente il token di autenticazione
        credentials: "same-origin"
    })
        .then(function (response) {
            return leggiRispostaRest(response);
        })
        .then(function (richieste) {
            if (!Array.isArray(richieste)) {
                throw new Error(
                    "La REST non ha restituito una lista valida di richieste."
                );
            }

            richiesteCaricate = richieste;
            mostraRichieste(richiesteCaricate);
        })
        .catch(function (errore) {
            console.error(
                "Errore durante il caricamento delle richieste:",
                errore
            );

            richiesteCaricate = [];

            mostraTabellaVuota();
            mostraMessaggioErrore(errore.message);
        });
}

/*
 * Legge sempre il corpo della risposta REST, anche quando lo status
 * HTTP è 401, 403, 404, 409 oppure 500.
 *
 * In questo modo viene recuperato il messaggio contenuto
 * nell'oggetto ErrorResponse restituito dalla REST.
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
                // La risposta non è JSON
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
 * Estrae il messaggio restituito dalla REST.
 *
 * Sono gestiti diversi nomi possibili per il campo
 * presente nella classe ErrorResponse.
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

    /*
     * Se la REST ha restituito direttamente una stringa,
     * viene mostrata a condizione che non sia una pagina HTML.
     */
    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== ""
    ) {
        if (contenuto.trim().charAt(0) !== "<") {
            return contenuto;
        }
    }

    /*
     * Messaggi predefiniti usati solamente quando la REST
     * non ha restituito un ErrorResponse leggibile.
     */
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
        return "Errore HTTP " + status + ": " + statusText;
    }

    return "Errore HTTP " + status + ".";
}

// Mostra il messaggio di errore nella pagina HTML
function mostraMessaggioErrore(messaggio) {
    var messaggioErrore =
        document.getElementById("messaggio-errore");

    if (messaggioErrore === null) {
        console.error(
            "Elemento con id messaggio-errore non trovato nell'HTML."
        );

        return;
    }

    /*
     * Usiamo textContent per evitare che il contenuto ricevuto
     * dal server venga interpretato come codice HTML.
     */
    messaggioErrore.textContent =
        messaggio ||
        "Errore durante il caricamento delle richieste.";

    messaggioErrore.classList.remove("d-none");
}

// Nasconde un eventuale messaggio precedente
function nascondiMessaggioErrore() {
    var messaggioErrore =
        document.getElementById("messaggio-errore");

    if (messaggioErrore !== null) {
        messaggioErrore.textContent = "";
        messaggioErrore.classList.add("d-none");
    }
}

// Pulisce la tabella quando la REST restituisce un errore
function mostraTabellaVuota() {
    var tbody =
        document.getElementById("corpo-tabella-richieste");

    if (tbody !== null) {
        tbody.innerHTML =
            "<tr>" +
                "<td colspan='9' class='text-center text-muted'>" +
                    "Impossibile caricare le richieste" +
                "</td>" +
            "</tr>";
    }
}

// Mostra le richieste nella tabella
function mostraRichieste(richieste) {
    var tbody =
        document.getElementById("corpo-tabella-richieste");

    if (tbody === null) {
        console.error(
            "Elemento con id corpo-tabella-richieste non trovato."
        );

        return;
    }

    var righe = "";

    if (richieste.length === 0) {
        righe += "<tr>";

        righe +=
            "<td colspan='9' class='text-center text-muted'>" +
                "Nessuna richiesta trovata" +
            "</td>";

        righe += "</tr>";

        tbody.innerHTML = righe;

        return;
    }

    for (var i = 0; i < richieste.length; i++) {
        var rq = richieste[i];

        righe += "<tr>";

        righe +=
            "<td>" +
                valore(rq.nomePersona) +
            "</td>";

        righe +=
            "<td>" +
                valore(rq.mailPersona) +
            "</td>";

        righe +=
            "<td>" +
                valore(rq.indirizzo) +
            "</td>";

        righe +=
            "<td>" +
                valore(rq.descrizione) +
            "</td>";

        righe +=
            "<td>" +
                valore(rq.fase) +
            "</td>";

        righe +=
            "<td>" +
                formattaDataOra(rq.createdAt) +
            "</td>";

        righe +=
            "<td>" +
                formattaDataOra(rq.workingAt) +
            "</td>";

        righe +=
            "<td>" +
                formattaDataOra(rq.closedAt) +
            "</td>";

        righe += "<td>";

        righe +=
            "<button " +
                "type='button' " +
                "class='btn btn-primary btn-sm' " +
                "onclick='vediDettaglioRichiesta(" + rq.id + ")'>" +
                "Dettaglio" +
            "</button>";

        righe += "</td>";

        righe += "</tr>";
    }

    tbody.innerHTML = righe;
}

// Filtra le richieste in base allo stato selezionato
function filtraRichiestePerStato() {
    var filtro =
        document.getElementById("filtro-stato-richieste");

    if (filtro === null) {
        mostraRichieste(richiesteCaricate);
        return;
    }

    var statoSelezionato = filtro.value;

    if (
        statoSelezionato === "" ||
        statoSelezionato === "tutte"
    ) {
        mostraRichieste(richiesteCaricate);
        return;
    }

    var richiesteFiltrate = [];

    for (var i = 0; i < richiesteCaricate.length; i++) {
        var rq = richiesteCaricate[i];

        if (
            rq.fase !== null &&
            rq.fase !== undefined &&
            rq.fase.toLowerCase() ===
                statoSelezionato.toLowerCase()
        ) {
            richiesteFiltrate.push(rq);
        }
    }

    mostraRichieste(richiesteFiltrate);
}

// Apre la pagina del dettaglio della richiesta
function vediDettaglioRichiesta(id) {
    window.location.href =
        "/soccorso_Web_SWA/viste/DettagliRichieste.html?id=" +
        id;
}

// Evita di stampare null oppure undefined nella tabella
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

// Formatta le date LocalDateTime ricevute dalla REST
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
     * Caso 1:
     * Jackson restituisce una stringa:
     * "2026-07-12T16:30:00"
     */
    if (typeof data === "string") {
        d = new Date(data);
    }

    /*
     * Caso 2:
     * Jackson restituisce un array:
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
    }

    // Formato non previsto
    else {
        return data;
    }

    if (isNaN(d.getTime())) {
        return data;
    }

    return d.toLocaleString("it-IT", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
    });
}