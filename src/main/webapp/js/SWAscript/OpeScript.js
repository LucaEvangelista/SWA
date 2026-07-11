//aspetta che la pagina html sia caricata
document.addEventListener("DOMContentLoaded", function () {
    caricaOperatori();
});

//chiama l'endpoint REST
function caricaOperatori() {
    var url = "/soccorso_Web_SWA/rest/operatori/list";

	//in pratica è come se il browser aprisse questo indirizzo
    fetch(url)
	//controllo se la response è andata a buon fine (es. 200 ok)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP: " + response.status);
            }

            return response.json();
        })
		//prende l'array degli operatori e lo passa alla tabella
        .then(function (operatori) {
            mostraOperatori(operatori);
        })
		//gestione delgi errori
        .catch(function (errore) {
            console.error("Errore durante il caricamento degli operatori:", errore);

            var messaggioErrore = document.getElementById("messaggio-errore");

            if (messaggioErrore !== null) {
                messaggioErrore.innerHTML = "Errore durante il caricamento degli operatori.";
            }
        });
}

//prende il JSON ricevuto e lo inserisce dentro la tabella
function mostraOperatori(operatori) {
	//ricerca corpo della tabella
    var tbody = document.getElementById("corpo-tabella-operatori");

	//controllo esistenza del corpo della tabella
    if (tbody === null) {
        console.error("Elemento corpo-tabella-operatori non trovato.");
        return;
    }

	//creazione delle righe della tabella per ogni operatore 
    var righe = "";

    for (var i = 0; i < operatori.length; i++) {
        var op = operatori[i];

        righe += "<tr>";
        righe += "<td>" + op.name + "</td>";
        righe += "<td>" + op.surname + "</td>";
        righe += "<td>" + op.email + "</td>";
        righe += "<td>" + op.age + "</td>";
        righe += "<td>" + op.status + "</td>";
        righe += '<td><button class="btn btn-primary" onclick="vediDettaglio(' + op.id + ')">Dettaglio</button></td>';
        righe += "</tr>";
    }

	//inserimento di tutte le righe create nel body della tabella
    tbody.innerHTML = righe;
}

//dettaglio dell'operatore 
function vediDettaglio(id) {
    window.location.href = "DettaglioOperatore.html?id=" + id;
}