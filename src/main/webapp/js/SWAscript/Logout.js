document.addEventListener("DOMContentLoaded", function () {

    const pulsanteLogout =
        document.getElementById("pulsante-logout");

    const pulsanteIndietro =
        document.getElementById("pulsante-indietro");

    pulsanteLogout.addEventListener(
        "click",
        eseguiLogout
    );

    pulsanteIndietro.addEventListener(
        "click",
        tornaIndietro
    );

});


/**
 * Invia la richiesta DELETE alla REST del logout.
 */
function eseguiLogout() {

    impostaCaricamento(true);
    nascondiMessaggio();

    fetch("../rest/auth/logout", {

        method: "DELETE",

        /*
         * Permette al browser di inviare il cookie "token"
         * insieme alla richiesta.
         */
        credentials: "same-origin",

        headers: {
            "Accept": "application/json"
        }

    })
        .then(function (response) {

            /*
             * Il metodo logout della REST restituisce
             * Response.noContent(), quindi lo status previsto
             * è 204.
             */
            if (response.ok) {

                pulisciTokenClient();

                /*
                 * replace impedisce di tornare alla pagina di logout
                 * premendo il pulsante indietro del browser.
                 */
                window.location.replace("Login.html");

                return null;
            }

            /*
             * Se il token è già scaduto o non è più valido,
             * consideriamo comunque terminata la sessione locale.
             */
            if (response.status === 401) {

                pulisciTokenClient();

                window.location.replace("Login.html");

                return null;
            }

            /*
             * Leggiamo il messaggio restituito dalla REST.
             */
            return leggiMessaggioErrore(response)
                .then(function (messaggio) {

                    throw new Error(messaggio);

                });

        })
        .catch(function (errore) {

            mostraMessaggio(
                errore.message ||
                "Non è stato possibile effettuare il logout.",
                "danger"
            );

        })
        .finally(function () {

            impostaCaricamento(false);

        });

}


/**
 * Legge l'eventuale ErrorResponse restituito dal server.
 */
function leggiMessaggioErrore(response) {

    return response.text()
        .then(function (testo) {

            const messaggioPredefinito =
                "Errore durante il logout. Codice HTTP: "
                + response.status;

            if (!testo || testo.trim() === "") {
                return messaggioPredefinito;
            }

            try {

                const dati = JSON.parse(testo);

                return dati.message ||
                    dati.messaggio ||
                    dati.error ||
                    messaggioPredefinito;

            } catch (errore) {

                /*
                 * Se il server non restituisce JSON,
                 * mostriamo direttamente il testo ricevuto.
                 */
                return testo;

            }

        });

}


/**
 * Torna alla pagina dalla quale l'utente
 * ha aperto la pagina di logout.
 */
function tornaIndietro() {

    if (document.referrer) {

        try {

            const paginaPrecedente =
                new URL(document.referrer);

            /*
             * Torniamo indietro solo se la pagina precedente
             * appartiene alla stessa applicazione.
             */
            if (
                paginaPrecedente.origin ===
                window.location.origin
            ) {

                window.history.back();

                return;
            }

        } catch (errore) {

            console.warn(
                "Impossibile leggere la pagina precedente.",
                errore
            );

        }

    }

    /*
     * Pagina usata come alternativa nel caso in cui
     * Logout.html venga aperta direttamente.
     */
    window.location.href = "ListaRichieste.html";

}


/**
 * Attiva o disattiva lo stato di caricamento.
 */
function impostaCaricamento(inCaricamento) {

    const pulsanteLogout =
        document.getElementById("pulsante-logout");

    const pulsanteIndietro =
        document.getElementById("pulsante-indietro");

    const testoLogout =
        document.getElementById("testo-logout");

    const spinnerLogout =
        document.getElementById("spinner-logout");

    pulsanteLogout.disabled = inCaricamento;
    pulsanteIndietro.disabled = inCaricamento;

    if (inCaricamento) {

        testoLogout.textContent =
            "Logout in corso...";

    } else {

        testoLogout.textContent =
            "Esci dall'account";

    }

    spinnerLogout.classList.toggle(
        "d-none",
        !inCaricamento
    );

}


/**
 * Mostra un messaggio Bootstrap.
 */
function mostraMessaggio(testo, tipo) {

    const contenitore =
        document.getElementById("messaggio-logout");

    contenitore.textContent = testo;

    contenitore.className =
        "alert alert-" + tipo;

}


/**
 * Nasconde il messaggio precedente.
 */
function nascondiMessaggio() {

    const contenitore =
        document.getElementById("messaggio-logout");

    contenitore.textContent = "";
    contenitore.className = "alert d-none";

}


/**
 * Elimina eventuali copie del token presenti
 * nel browser.
 */
function pulisciTokenClient() {

    localStorage.removeItem("token");
    sessionStorage.removeItem("token");

    document.cookie =
        "token=; Path=/; Max-Age=0; SameSite=Lax";

}