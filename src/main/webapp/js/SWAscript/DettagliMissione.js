document.addEventListener("DOMContentLoaded", function () {
    caricaDettaglioMissione();
});

function caricaDettaglioMissione() {

    // legge l'id dalla URL
    var parametri = new URLSearchParams(window.location.search);
    var id = parametri.get("id");

    // controllo validità id
    if (id === null || id === "") {
        mostraErrore("ID missione mancante nella URL.");
        nascondiCaricamento();
        return;
    }

    // chiamata REST dettaglio missione
    var urlMissione = "/soccorso_Web_SWA/rest/missioni/" + id;
    var urlSquadra = "/soccorso_Web_SWA/rest/missioni/" + id + "/squadra";

    fetch(urlMissione)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP: " + response.status);
            }

            return response.json();
        })
        .then(function (missione) {
            console.log("Missione ricevuta:", missione);

            mostraMissione(missione);
            nascondiCaricamento();

            // dopo aver caricato la missione, carico anche la squadra
            return fetch(urlSquadra);
        })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP squadra: " + response.status);
            }

            return response.json();
        })
        .then(function (squadra) {
            console.log("Squadra ricevuta:", squadra);

            mostraSquadra(squadra);
        })
        .catch(function (errore) {
            console.error("Errore durante il caricamento del dettaglio missione:", errore);
            mostraErrore("Errore durante il caricamento del dettaglio della missione.");
            nascondiCaricamento();
        });
}

function mostraMissione(missione) {

    document.getElementById("card-missione").classList.remove("d-none");

    document.getElementById("missione-id").textContent =
        valore(missione.missioneId);

    document.getElementById("obiettivo-missione").textContent =
        valore(missione.obiettivo);

    document.getElementById("posizione-missione").textContent =
        valore(missione.posizione);

    document.getElementById("stato-missione").textContent =
        valore(missione.status);

    // controllo visibilità pulsante termina missione
    var contenitoreTerminazione = document.getElementById("contenitore-terminazione");

    if (missione.status === "attiva") {
        contenitoreTerminazione.classList.remove("d-none");
    } else {
        contenitoreTerminazione.classList.add("d-none");
    }
}

function mostraSquadra(squadra) {

    var campoSquadra = document.getElementById("squadra-missione");

    if (campoSquadra === null) {
        return;
    }

    campoSquadra.textContent = valore(squadra.nome);
}

function terminaMissione() {

    var parametri = new URLSearchParams(window.location.search);
    var id = parametri.get("id");

    if (id === null || id === "") {
        mostraErrore("ID missione mancante nella URL.");
        return;
    }

    var conferma = confirm("Sei sicuro di voler terminare questa missione?");

    if (!conferma) {
        return;
    }

    var url = "/soccorso_Web_SWA/rest/missioni/" + id + "/terminazione";

    fetch(url, {
        method: "PUT",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(function (response) {
            return response.json().then(function (data) {
                if (!response.ok) {
                    throw new Error(data.message || "Errore HTTP: " + response.status);
                }

                return data;
            });
        })
        .then(function (missioneAggiornata) {
            console.log("Missione terminata:", missioneAggiornata);

            mostraMissione(missioneAggiornata);

            alert("Missione terminata correttamente.");
        })
        .catch(function (errore) {
            console.error("Errore durante la terminazione della missione:", errore);
            mostraErrore(errore.message);
        });
}

function mostraErrore(messaggio) {
    var boxErrore = document.getElementById("messaggio-errore");

    boxErrore.textContent = messaggio;
    boxErrore.classList.remove("d-none");
}

function nascondiCaricamento() {
    var boxCaricamento = document.getElementById("messaggio-caricamento");

    if (boxCaricamento !== null) {
        boxCaricamento.classList.add("d-none");
    }
}

function valore(val) {
    if (val === null || val === undefined || val === "") {
        return "-";
    }

    return val;
}