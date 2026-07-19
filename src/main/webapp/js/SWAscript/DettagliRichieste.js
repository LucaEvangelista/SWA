document.addEventListener("DOMContentLoaded", function () {
    caricaDettaglioRichiesta();
});

function caricaDettaglioRichiesta(){
	
	//legge l'id dalla URL
	var parametri = new URLSearchParams(window.location.search);
	var id = parametri.get("id");
	
	//controllo validità id
	if (id === null || id === "") {
	    mostraErrore("ID richiesta mancante nella URL.");
	    return;
	}
	
	//crea la chiamata REST
	var url = "/soccorso_Web_SWA/rest/richieste/" + id;

	fetch(url)
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error("Errore HTTP: " + response.status);
	        }

	        return response.json();
	    })
		.then(function (data) {
		    console.log("JSON ricevuto:", data);
			
			mostraRichiesta(data);
			nascondiCaricamento();
			
			})
			.catch(function (errore) {
			    console.error("Errore durante il caricamento del dettaglio:", errore);
			    mostraErrore("Errore durante il caricamento del dettaglio della richietsa.");
			});
}

function mostraRichiesta(richiesta) {

    document.getElementById("card-richiesta").classList.remove("d-none");

    document.getElementById("richiesta-id").textContent =
        valore(richiesta.id);

    document.getElementById("richiesta-nome").textContent =
        valore(richiesta.nomePersona);

    document.getElementById("richiesta-email").textContent =
        valore(richiesta.mailPersona);

    document.getElementById("richiesta-indirizzo").textContent =
        valore(richiesta.indirizzo);

    document.getElementById("richiesta-fase").textContent =
        valore(richiesta.fase);

    document.getElementById("richiesta-descrizione").textContent =
        valore(richiesta.descrizione);

    document.getElementById("richiesta-created-at").textContent =
        formattaDataOra(richiesta.createdAt);

    document.getElementById("richiesta-working-at").textContent =
        formattaDataOra(richiesta.workingAt);

    document.getElementById("richiesta-closed-at").textContent =
        formattaDataOra(richiesta.closedAt);

    document.getElementById("richiesta-uuid").textContent =
        valore(richiesta.uuid);
		
	// controllo per pulsante rifiuta missione
	var contenitoreRifiuta = document.getElementById("contenitore-rifiuta");

	if (richiesta.fase === "attiva") {
	    contenitoreRifiuta.classList.remove("d-none");
	} else {
	    contenitoreRifiuta.classList.add("d-none");
	}
	
	// controllo per pulsante crea missione
	var contenitoreCreaMissione = document.getElementById("contenitore-crea-missione");

	if (richiesta.fase === "attiva") {
	    contenitoreCreaMissione.classList.remove("d-none");
	} else {
	    contenitoreCreaMissione.classList.add("d-none");
	}
	
	// creazione missione
	var btnCreaMissione = document.getElementById("btn-crea-missione");

	if (btnCreaMissione !== null) {
	    btnCreaMissione.onclick = function () {
	        window.location.href = "NuovaMissione.html?id=" + richiesta.id;
	    };
	}
	
	// controllo per pulsante vai a missione
	var contenitoreVaiMissione =
	    document.getElementById("contenitore-vai-missione");

	var btnVaiMissione =
	    document.getElementById("btn-vai-missione");

	// La missione esiste quando la richiesta è in esecuzione o terminata
	if (
	    richiesta.fase === "in esecuzione" ||
	    richiesta.fase === "terminata"
	) {
	    contenitoreVaiMissione.classList.remove("d-none");

	    btnVaiMissione.onclick = function () {
	        vaiAlDettaglioMissione(richiesta.id);
	    };

	} else {
	    contenitoreVaiMissione.classList.add("d-none");
	}
}

function vaiAlDettaglioMissione(idRichiesta) {

    var url = "/soccorso_Web_SWA/rest/missioni/list";

    fetch(url, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        },
        credentials: "same-origin"
    })
    .then(function (response) {

        return response.json().then(function (data) {

            if (!response.ok) {
                throw new Error(
                    data.message ||
                    "Errore durante il caricamento delle missioni."
                );
            }

            return data;
        });
    })
    .then(function (missioni) {

        if (!Array.isArray(missioni)) {
            throw new Error("La risposta del server non contiene una lista di missioni.");
        }

        // Cerca la missione collegata alla richiesta visualizzata
        var missioneTrovata = missioni.find(function (missione) {
            return Number(missione.richiestaRif) === Number(idRichiesta);
        });

        if (missioneTrovata === undefined) {
            throw new Error(
                "Non è stata trovata una missione associata a questa richiesta."
            );
        }

        // Apre la pagina del dettaglio missione
        window.location.href =
            "DettagliMissione.html?id=" +
            encodeURIComponent(missioneTrovata.missioneId);
    })
    .catch(function (errore) {
        console.error(
            "Errore durante la ricerca della missione:",
            errore
        );

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

function formattaDataOra(data) {
    if (data === null || data === undefined || data === "") {
        return "-";
    }

    var d = new Date(data);

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

function rifiutaRichiesta() {

    var parametri = new URLSearchParams(window.location.search);
    var id = parametri.get("id");

    if (id === null || id === "") {
        mostraErrore("ID richiesta mancante nella URL.");
        return;
    }

    var conferma = confirm("Sei sicuro di voler rifiutare questa richiesta?");

    if (!conferma) {
        return;
    }

    var url = "/soccorso_Web_SWA/rest/richieste/" + id + "/rifiutata";

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
    .then(function (data) {
        console.log("Richiesta rifiutata:", data);

        mostraRichiesta(data);

        alert("Richiesta rifiutata correttamente.");
    })
    .catch(function (errore) {
        console.error("Errore durante il rifiuto della richiesta:", errore);
        mostraErrore(errore.message);
    });
}