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



    filtroStato.addEventListener("change", function () {

        statoCorrente = filtroStato.value;
        paginaCorrente = 1;

        caricaMateriali();
    });



    selezioneDimensione.addEventListener("change", function () {

        dimensionePagina = parseInt(
            selezioneDimensione.value,
            10
        );

        paginaCorrente = 1;

        caricaMateriali();
    });


    pulsantePrecedente.addEventListener("click", function () {

        if (
            !caricamentoInCorso &&
            paginaCorrente > 1
        ) {

            paginaCorrente--;

            caricaMateriali();
        }
    });



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


function costruisciUrl() {

    var url = "/soccorso_Web_SWA/rest/materiali/list";

    url += "?page=" + encodeURIComponent(paginaCorrente);
    url += "&size=" + encodeURIComponent(dimensionePagina);



    if (statoCorrente !== "") {

        url += "&stato=" + encodeURIComponent(statoCorrente);
    }


    return url;
}


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

        credentials: "same-origin"
    })
        .then(function (response) {

            return leggiRisposta(response);
        })
        .then(function (risultato) {

            if (
                !risultato ||
                !Array.isArray(risultato.content)
            ) {

                throw new Error(
                    "La risposta della REST non è valida."
                );
            }

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

            impostaCaricamento(false);
        });
}

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

function estraiMessaggioErrore(
    contenuto,
    status,
    statusText
) {

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

function mostraMateriali(materiali) {

    var corpoTabella = document.getElementById(
        "corpo-tabella-materiali"
    );

    corpoTabella.innerHTML = "";


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

function creaCella(valore) {

    var cella = document.createElement("td");

    cella.textContent = valoreTestuale(valore);

    return cella;
}

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

function mostraRigaInformativa(messaggio) {

    var corpoTabella = document.getElementById(
        "corpo-tabella-materiali"
    );


    corpoTabella.innerHTML = "";


    var riga = document.createElement("tr");
    var cella = document.createElement("td");

    cella.colSpan = 4;

    cella.className =
        "text-center text-muted py-5";

    cella.textContent = messaggio;


    riga.appendChild(cella);

    corpoTabella.appendChild(riga);
}

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

    precedente.disabled =
        caricamentoInCorso ||
        paginaCorrente <= 1 ||
        totalePagine === 0;

    successiva.disabled =
        caricamentoInCorso ||
        paginaCorrente >= totalePagine ||
        totalePagine === 0;
}

function impostaCaricamento(valore) {

    caricamentoInCorso = valore;

    aggiornaPaginazione();
}

function mostraErrore(messaggio) {

    var contenitore = document.getElementById(
        "messaggio-errore"
    );


    contenitore.textContent =
        messaggio ||
        "Si è verificato un errore.";


    contenitore.classList.remove("d-none");
}

function nascondiErrore() {

    var contenitore = document.getElementById(
        "messaggio-errore"
    );


    contenitore.textContent = "";

    contenitore.classList.add("d-none");
}

function numeroIntero(
    valore,
    valorePredefinito
) {

    var numero = parseInt(valore, 10);


    return isNaN(numero)
        ? valorePredefinito
        : numero;
}

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