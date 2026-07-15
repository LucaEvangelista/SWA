document.addEventListener("DOMContentLoaded", function () {

    var form = document.getElementById("form-nuovo-mezzo");

    if (form !== null) {
        form.addEventListener("submit", gestisciInvioForm);
    }
});


/*
 * Gestisce l'invio del modulo.
 */
function gestisciInvioForm(event) {

    event.preventDefault();

    var form = document.getElementById("form-nuovo-mezzo");

    if (form === null) {
        return;
    }

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

    var tipologiaInput = document.getElementById("tipologia");
    var serialeInput = document.getElementById("seriale");

    var tipologia = tipologiaInput.value.trim();
    var seriale = serialeInput.value.trim();

    /*
     * Controllo aggiuntivo perché required non blocca sempre
     * una stringa composta soltanto da spazi.
     */
    if (tipologia === "" || seriale === "") {

        form.classList.add("was-validated");

        mostraMessaggio(
            "Tipologia e numero seriale non possono essere vuoti.",
            "danger"
        );

        return;
    }

    /*
     * Oggetto che verrà trasformato in JSON.
     */
    var nuovoMezzo = {
        tipologia: tipologia,
        seriale: seriale
    };

    creaMezzo(nuovoMezzo);
}


/*
 * Invia il nuovo mezzo alla REST.
 */
function creaMezzo(nuovoMezzo) {

    var pulsanteCrea = document.getElementById("pulsante-crea");

    impostaPulsanteCaricamento(pulsanteCrea, true);

    fetch("/soccorso_Web_SWA/rest/mezzi", {

        method: "POST",

        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },

        /*
         * Permette al browser di inviare il cookie JWT
         * insieme alla richiesta.
         */
        credentials: "same-origin",

        body: JSON.stringify(nuovoMezzo)
    })
        .then(function (response) {

            return leggiRispostaRest(response);
        })
        .then(function (mezzoCreato) {

            var messaggio = "Mezzo aggiunto correttamente.";

            /*
             * La REST restituisce il mezzo appena creato.
             * Se è presente il seriale, viene mostrato nel messaggio.
             */
            if (
                mezzoCreato !== null &&
                typeof mezzoCreato === "object" &&
                mezzoCreato.seriale
            ) {

                messaggio =
                    "Mezzo con seriale " +
                    mezzoCreato.seriale +
                    " aggiunto correttamente.";
            }

            mostraMessaggio(messaggio, "success");

            pulisciForm();

            /*
             * Dopo il salvataggio torna alla lista dei mezzi.
             */
            window.setTimeout(function () {

                window.location.href = "ListaMezzi.html";

            }, 1200);
        })
        .catch(function (errore) {

            console.error(
                "Errore durante la creazione del mezzo:",
                errore
            );

            mostraMessaggio(
                errore.message ||
                "Si è verificato un errore durante il salvataggio del mezzo.",
                "danger"
            );
        })
        .then(function () {

            /*
             * Equivalente di finally, senza usare async/await.
             */
            impostaPulsanteCaricamento(pulsanteCrea, false);
        });
}


/*
 * Legge il corpo della risposta.
 *
 * Viene usato response.text() perché la REST potrebbe restituire:
 * - un oggetto JSON in caso di successo;
 * - un ErrorResponse JSON;
 * - una risposta vuota;
 * - una pagina HTML in caso di errore del server.
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

                contenuto = testoRisposta;
            }
        }

        /*
         * response.ok è true per gli status HTTP da 200 a 299.
         */
        if (!response.ok) {

            var messaggioErrore = estraiMessaggioErrore(
                contenuto,
                response.status,
                response.statusText
            );

            var erroreHttp = new Error(messaggioErrore);

            erroreHttp.status = response.status;
            erroreHttp.contenuto = contenuto;

            throw erroreHttp;
        }

        return contenuto;
    });
}


/*
 * Estrae il messaggio dall'ErrorResponse della REST.
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
     * Mostra una risposta testuale soltanto quando non sembra HTML.
     */
    if (
        typeof contenuto === "string" &&
        contenuto.trim() !== "" &&
        contenuto.trim().charAt(0) !== "<"
    ) {

        return contenuto;
    }

    if (status === 400) {
        return "I dati inseriti non sono validi.";
    }

    if (status === 401) {
        return "Devi effettuare il login per aggiungere un mezzo.";
    }

    if (status === 403) {
        return "Non hai i permessi per aggiungere un mezzo.";
    }

    if (status === 404) {
        return "L'endpoint per la creazione del mezzo non è stato trovato.";
    }

    if (status === 409) {
        return "Esiste già un mezzo con il numero seriale indicato.";
    }

    if (status >= 500) {
        return "Si è verificato un errore interno durante il salvataggio.";
    }

    if (statusText !== null && statusText !== "") {
        return "Errore HTTP " + status + ": " + statusText;
    }

    return "Errore HTTP " + status + ".";
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

    if (contenitore === null) {
        return;
    }

    contenitore.textContent = testo;

    contenitore.className =
        "alert alert-" + tipo + " mb-4";

    contenitore.classList.remove("d-none");
}


/*
 * Nasconde e pulisce il messaggio precedente.
 */
function nascondiMessaggio() {

    var contenitore = document.getElementById("messaggio");

    if (contenitore === null) {
        return;
    }

    contenitore.textContent = "";
    contenitore.className = "alert d-none mb-4";
}


/*
 * Pulisce il form dopo la creazione.
 */
function pulisciForm() {

    var form = document.getElementById("form-nuovo-mezzo");

    if (form === null) {
        return;
    }

    form.reset();
    form.classList.remove("was-validated");
}


/*
 * Disabilita il pulsante durante la richiesta HTTP,
 * evitando l'invio dello stesso mezzo più volte.
 */
function impostaPulsanteCaricamento(pulsante, caricamento) {

    if (pulsante === null) {
        return;
    }

    if (caricamento) {

        pulsante.disabled = true;
        pulsante.textContent = "Salvataggio in corso...";

    } else {

        pulsante.disabled = false;
        pulsante.textContent = "Aggiungi mezzo";
    }
}