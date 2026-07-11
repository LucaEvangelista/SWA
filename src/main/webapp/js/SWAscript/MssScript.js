//aspetta che la pagina html sia caricata
document.addEventListener("DOMContentLoaded", function () {
    caricaMissioni();
});

//chiama l'endpoint REST
function caricaMissioni() {
    var url = "/soccorso_Web_SWA/rest/missioni/list";

	//in pratica è come se il browser aprisse questo indirizzo
    fetch(url)
	//controllo se la response è andata a buon fine (es. 200 ok)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP: " + response.status);
            }

            return response.json();
        })
		//prende l'array delle missioni e lo passa alla tabella
        .then(function (missioni) {
            mostraMissioni(missioni);
        })
		//gestione delgi errori
        .catch(function (errore) {
            console.error("Errore durante il caricamento delle missioni:", errore);

            var messaggioErrore = document.getElementById("messaggio-errore");

            if (messaggioErrore !== null) {
                messaggioErrore.innerHTML = "Errore durante il caricamento delle missioni.";
            }
        });
}

//prende il JSON ricevuto e lo inserisce dentro la tabella
function mostraMissioni(missioni) {
	//ricerca corpo della tabella
    var tbody = document.getElementById("corpo-tabella-missioni");

	//controllo esistenza del corpo della tabella
    if (tbody === null) {
        console.error("Elemento corpo-tabella-missioni non trovato.");
        return;
    }

	//creazione delle righe della tabella per ogni missione 
    var righe = "";

    for (var i = 0; i < missioni.length; i++) {
        var ms = missioni[i];

        righe += "<tr>";
        righe += "<td>" + ms.obiettivo + "</td>";
        righe += "<td>" + ms.posizione + "</td>";
        righe += "<td>" + ms.status + "</td>";
        righe += '<td><button class="btn btn-primary" onclick="vediDettaglio(' + ms.missioneId + ')">Dettaglio</button></td>';
        righe += "</tr>";
    }

	//inserimento di tutte le righe create nel body della tabella
    tbody.innerHTML = righe;
	}
	
	//dettaglio della missione 
	function vediDettaglio(id) {
	    window.location.href = "DettagliMissione.html?id=" + id;
	}