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

    fetch(url)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP: " + response.status);
            }

            return response.json();
        })
        .then(function (richieste) {
            richiesteCaricate = richieste;
            mostraRichieste(richiesteCaricate);
        })
        .catch(function (errore) {
            console.error("Errore durante il caricamento delle richieste:", errore);

            var messaggioErrore = document.getElementById("messaggio-errore");

            if (messaggioErrore !== null) {
                messaggioErrore.innerHTML = "Errore durante il caricamento delle richieste.";
                messaggioErrore.classList.remove("d-none");
            }
        });
}

// Mostra le richieste nella tabella
function mostraRichieste(richieste) {
    var tbody = document.getElementById("corpo-tabella-richieste");

    if (tbody === null) {
        console.error("Elemento corpo-tabella-richieste non trovato.");
        return;
    }

    var righe = "";

    if (richieste.length === 0) {
        righe += "<tr>";
        righe += "<td colspan='9' class='text-center text-muted'>Nessuna richiesta trovata</td>";
        righe += "</tr>";

        tbody.innerHTML = righe;
        return;
    }

    for (var i = 0; i < richieste.length; i++) {
        var rq = richieste[i];

        righe += "<tr>";
        righe += "<td>" + valore(rq.nomePersona) + "</td>";
        righe += "<td>" + valore(rq.mailPersona) + "</td>";
        righe += "<td>" + valore(rq.indirizzo) + "</td>";
        righe += "<td>" + valore(rq.descrizione) + "</td>";
        righe += "<td>" + valore(rq.fase) + "</td>";

        // Qui si usa la funzione di formattazione
        righe += "<td>" + formattaDataOra(rq.createdAt) + "</td>";
        righe += "<td>" + formattaDataOra(rq.workingAt) + "</td>";
        righe += "<td>" + formattaDataOra(rq.closedAt) + "</td>";

        righe += "<td>";
        righe += "<button class='btn btn-primary btn-sm' onclick='vediDettaglioRichiesta(" + rq.id + ")'>";
        righe += "Dettaglio";
        righe += "</button>";
        righe += "</td>";

        righe += "</tr>";
    }

    tbody.innerHTML = righe;
}

// Filtro per stato/fase della richiesta
function filtraRichiestePerStato() {
    var filtro = document.getElementById("filtro-stato-richieste");

    if (filtro === null) {
        mostraRichieste(richiesteCaricate);
        return;
    }

    var statoSelezionato = filtro.value;

    if (statoSelezionato === "tutte") {
        mostraRichieste(richiesteCaricate);
        return;
    }

    var richiesteFiltrate = [];

    for (var i = 0; i < richiesteCaricate.length; i++) {
        var rq = richiesteCaricate[i];

        if (
            rq.fase !== null &&
            rq.fase !== undefined &&
            rq.fase.toLowerCase() === statoSelezionato.toLowerCase()
        ) {
            richiesteFiltrate.push(rq);
        }
    }

    mostraRichieste(richiesteFiltrate);
}

// Vai al dettaglio della richiesta
function vediDettaglioRichiesta(id) {
    window.location.href = "/soccorso_Web_SWA/viste/DettagliRichieste.html?id=" + id;
}

// Evita di stampare null o undefined nella tabella
function valore(val) {
    if (val === null || val === undefined || val === "") {
        return "-";
    }

    return val;
}

// Formatta le date LocalDateTime ricevute da Jackson
function formattaDataOra(data) {
    if (data === null || data === undefined || data === "") {
        return "-";
    }

    var d;

    // Caso 1: Jackson restituisce una stringa tipo "2026-06-30T18:54:43"
    if (typeof data === "string") {
        d = new Date(data);
    }

    // Caso 2: Jackson restituisce un array tipo [2026, 6, 30, 18, 54, 43]
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

    // Caso 3: formato non previsto
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