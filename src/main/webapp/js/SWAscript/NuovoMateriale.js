document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("form-nuovo-materiale");

    form.addEventListener("submit", gestisciInvio);
});


function gestisciInvio(event) {
    event.preventDefault();

    var form = document.getElementById("form-nuovo-materiale");
    var tipologiaInput = document.getElementById("tipologia");
    var serialeInput = document.getElementById("seriale");

    nascondiMessaggio();

    /*
     * Controlla i campi required presenti nell'HTML.
     */
    if (!form.checkValidity()) {
        form.classList.add("was-validated");

        mostraMessaggio(
            "Compila correttamente tutti i campi obbligatori.",
            "danger"
        );

        return;
    }

    /*
     * trim elimina gli spazi all'inizio e alla fine.
     */
    var tipologia = tipologiaInput.value.trim();
    var seriale = serialeInput.value.trim();

    /*
     * Controllo aggiuntivo per impedire l'invio
     * di stringhe contenenti soltanto spazi.
     */
    if (tipologia === "" || seriale === "") {
        mostraMessaggio(
            "Tipologia e numero seriale non possono essere vuoti.",
            "danger"
        );

        return;
    }

    /*
     * Oggetto che verrà trasformato in JSON.
     */
    var nuovoMateriale = {
        tipologia: tipologia,
        seriale: seriale
    };

    creaMateriale(nuovoMateriale);
}


/*
 * Effettua la richiesta POST alla REST.
 */
function creaMateriale(nuovoMateriale) {
    var pulsante = document.getElementById("pulsante-crea");

    impostaCaricamentoPulsante(pulsante, true);

    fetch("/soccorso_Web_SWA/rest/materiali", {
        method: "POST",

        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },

        /*
         * Invia il cookie contenente il token JWT.
         */
        credentials: "same-origin",

        /*
         * Trasforma l'oggetto JavaScript in JSON.
         */
        body: JSON.stringify(nuovoMateriale)
    })
        .then(function (response) {
            return leggiRisposta(response);
        })
        .then(function (materialeCreato) {
            var messaggio = "Materiale aggiunto correttamente.";

            /*
             * La REST restituisce il materiale appena creato.
             * Se è presente il seriale, viene mostrato nel messaggio.
             */
            if (materialeCreato && materialeCreato.seriale) {
                messaggio =
                    "Materiale con seriale " +
                    materialeCreato.seriale +
                    " aggiunto correttamente.";
            }

            mostraMessaggio(messaggio, "success");

            pulisciForm();

            /*
             * Dopo 1,2 secondi torna alla lista.
             */
            window.setTimeout(function () {
                window.location.href = "ListaMateriali.html";
            }, 1200);
        })
        .catch(function (errore) {
            console.error(
                "Errore creazione materiale:",
                errore
            );

            mostraMessaggio(
                errore.message ||
                "Si è verificato un errore durante il salvataggio.",
                "danger"
            );
        })
        .then(function () {
            /*
             * Equivalente di finally senza utilizzare async/await.
             */
            impostaCaricamentoPulsante(pulsante, false);
        });
}


/*
 * Legge il corpo della risposta.
 *
 * Si utilizza response.text() perché il server potrebbe restituire:
 * - il materiale JSON;
 * - un ErrorResponse JSON;
 * - una risposta vuota;
 * - una pagina HTML in caso di errore.
 */
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

        /*
         * response.ok è true per gli status da 200 a 299.
         */
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


/*
 * Estrae il messaggio dall'ErrorResponse restituito dalla REST.
 */
function estraiMessaggioErrore(contenuto, status, statusText) {
    if (contenuto && typeof contenuto === "object") {
        return contenuto.message ||
            contenuto.messaggio ||
            contenuto.error ||
            contenuto.errore ||
            "Errore HTTP " + status + ".";
    }

    /*
     * Mostra il contenuto testuale soltanto se non sembra HTML.
     */
    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== "" &&
        contenuto.trim().charAt(0) !== "<"
    ) {
        return contenuto;
    }

    if (status === 400) {
        return "Tipologia e numero seriale sono obbligatori.";
    }

    if (status === 401) {
        return "Devi effettuare il login.";
    }

    if (status === 403) {
        return "Non hai i permessi per aggiungere un materiale.";
    }

    if (status === 404) {
        return "L'endpoint per la creazione del materiale non è stato trovato.";
    }

    if (status >= 500) {
        return "Errore interno durante il salvataggio del materiale.";
    }

    return statusText || "Errore HTTP " + status + ".";
}


/*
 * Mostra un messaggio Bootstrap.
 *
 * tipo può essere:
 * - success
 * - danger
 * - warning
 * - info
 */
function mostraMessaggio(testo, tipo) {
    var contenitore = document.getElementById("messaggio");

    contenitore.textContent = testo;

    contenitore.className =
        "alert alert-" + tipo + " mb-4";
}


/*
 * Nasconde e pulisce il messaggio precedente.
 */
function nascondiMessaggio() {
    var contenitore = document.getElementById("messaggio");

    contenitore.textContent = "";
    contenitore.className = "alert d-none mb-4";
}


/*
 * Pulisce il form dopo il salvataggio.
 */
function pulisciForm() {
    var form = document.getElementById("form-nuovo-materiale");

    form.reset();
    form.classList.remove("was-validated");
}


/*
 * Disabilita il pulsante mentre la richiesta è in corso,
 * impedendo invii multipli.
 */
function impostaCaricamentoPulsante(pulsante, caricamento) {
    pulsante.disabled = caricamento;

    if (caricamento) {
        pulsante.textContent = "Salvataggio in corso...";
    } else {
        pulsante.textContent = "Aggiungi materiale";
    }
}