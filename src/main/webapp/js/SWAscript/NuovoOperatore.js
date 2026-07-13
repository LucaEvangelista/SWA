"use strict";

var API_OPERATORI = "/soccorso_Web_SWA/rest/operatori";

document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("form-nuovo-operatore");
    var dataNascita = document.getElementById("data-nascita");
    var mostraPassword = document.getElementById("mostra-password");

    if (dataNascita !== null) {
        dataNascita.max = ottieniDataOdierna();
    }

    if (form !== null) {
        form.addEventListener("submit", creaOperatore);
    }

    if (mostraPassword !== null) {
        mostraPassword.addEventListener(
            "change",
            cambiaVisibilitaPassword
        );
    }
});


/**
 * Gestisce l'invio del form per creare un nuovo operatore.
 */
function creaOperatore(event) {
    event.preventDefault();

    var form = document.getElementById("form-nuovo-operatore");
    var pulsanteCrea = document.getElementById("pulsante-crea");

    nascondiMessaggio();

    if (form === null) {
        mostraMessaggio(
            "Errore: il form non è stato trovato nella pagina.",
            "danger"
        );
        return;
    }

    /*
     * Controlla i campi required, email e data.
     */
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return;
    }

    var nome = document.getElementById("nome").value.trim();
    var cognome = document.getElementById("cognome").value.trim();
    var dataNascita = document.getElementById("data-nascita").value;
    var email = document.getElementById("email").value.trim();
    var passkey = document.getElementById("passkey").value;
    var confermaPasskey =
        document.getElementById("conferma-passkey").value;

    if (passkey !== confermaPasskey) {
        mostraMessaggio(
            "Le password inserite non coincidono.",
            "danger"
        );

        document.getElementById("conferma-passkey").focus();
        return;
    }


    var operatore = {
        name: nome,
        surname: cognome,
        age: dataNascita,
        email: email,
        passkey: passkey
    };

    impostaCaricamento(pulsanteCrea, true);

    fetch(API_OPERATORI, {
        method: "POST",

        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },

        /*
         * Invia il cookie contenente il token JWT.
         */
        credentials: "same-origin",

        body: JSON.stringify(operatore)
    })
    .then(function (response) {

        /*
         * Legge inizialmente la risposta come testo.
         * In questo modo non si verifica un errore quando
         * Tomcat restituisce una pagina HTML invece di JSON.
         */
        return response.text().then(function (testoRisposta) {
            var dati = null;

            if (testoRisposta !== null && testoRisposta.trim() !== "") {
                try {
                    dati = JSON.parse(testoRisposta);
                } catch (erroreJson) {
                    dati = {
                        message: testoRisposta
                    };
                }
            }

            return {
                response: response,
                dati: dati
            };
        });
    })
    .then(function (risultato) {
        var response = risultato.response;
        var dati = risultato.dati;

        if (!response.ok) {
            gestisciErroreHttp(response.status, dati);
            return;
        }

        /*
         * La REST restituisce HTTP 201 Created
         * con l'operatore appena creato.
         */
        var messaggio = "Operatore creato correttamente.";

        if (dati !== null) {
            var idOperatore = recuperaIdOperatore(dati);

            if (idOperatore !== null) {
                messaggio +=
                    " Identificativo operatore: " +
                    idOperatore +
                    ".";
            }
        }

        mostraMessaggio(messaggio, "success");

        form.reset();
        form.classList.remove("was-validated");

        var inputData = document.getElementById("data-nascita");

        if (inputData !== null) {
            inputData.max = ottieniDataOdierna();
        }

        window.scrollTo(0, 0);
    })
    .catch(function (errore) {
        console.error(
            "Errore durante la creazione dell'operatore:",
            errore
        );

        mostraMessaggio(
            "Non è stato possibile contattare il server. " +
            "Controlla che Tomcat sia avviato e che la REST sia raggiungibile.",
            "danger"
        );
    })
    .finally(function () {
        impostaCaricamento(pulsanteCrea, false);
    });
}


/**
 * Cerca l'identificativo dell'operatore restituito dalla REST.
 * La proprietà può cambiare in base ai getter del model Java.
 */
function recuperaIdOperatore(dati) {
    if (dati === null || typeof dati !== "object") {
        return null;
    }

    if (dati.id !== undefined && dati.id !== null) {
        return dati.id;
    }

    if (
        dati.operatorID !== undefined &&
        dati.operatorID !== null
    ) {
        return dati.operatorID;
    }

    if (
        dati.operatoreID !== undefined &&
        dati.operatoreID !== null
    ) {
        return dati.operatoreID;
    }

    return null;
}



function gestisciErroreHttp(status, dati) {
    var messaggio = estraiMessaggioErrore(dati);

    if (status === 400) {
        if (messaggio === "") {
            messaggio = "I dati inseriti non sono validi.";
        }
    } else if (status === 401) {
        messaggio =
            "Non sei autenticato oppure la sessione è scaduta.";
    } else if (status === 403) {
        messaggio =
            "Non hai i permessi necessari per creare un operatore. " +
            "Questa operazione è riservata agli amministratori.";
    } else if (status === 404) {
        messaggio =
            "La risorsa REST non è stata trovata. " +
            "Controlla l'indirizzo /rest/operatori.";
    } else if (status === 409) {
        if (messaggio === "") {
            messaggio =
                "Esiste già un operatore con questi dati.";
        }
    } else if (status === 500) {
        if (messaggio === "") {
            messaggio =
                "Si è verificato un errore interno durante il salvataggio.";
        }
    } else {
        if (messaggio === "") {
            messaggio =
                "Operazione non riuscita. Codice HTTP: " +
                status +
                ".";
        }
    }

    mostraMessaggio(messaggio, "danger");
}


//Recupera il messaggio dalla classe ErrorResponse.
 
function estraiMessaggioErrore(dati) {
    if (dati === null || dati === undefined) {
        return "";
    }

    if (typeof dati === "string") {
        return dati;
    }

    if (
        dati.message !== undefined &&
        dati.message !== null
    ) {
        return dati.message;
    }

    if (
        dati.messaggio !== undefined &&
        dati.messaggio !== null
    ) {
        return dati.messaggio;
    }

    if (
        dati.error !== undefined &&
        dati.error !== null
    ) {
        return dati.error;
    }

    if (
        dati.description !== undefined &&
        dati.description !== null
    ) {
        return dati.description;
    }

    return "";
}



function mostraMessaggio(testo, tipo) {
    var contenitore = document.getElementById("messaggio");

    if (contenitore === null) {
        console.error(
            "Elemento con id 'messaggio' non trovato."
        );
        return;
    }

    contenitore.textContent = testo;
    contenitore.className = "alert alert-" + tipo;
    contenitore.classList.remove("d-none");
}



function nascondiMessaggio() {
    var contenitore = document.getElementById("messaggio");

    if (contenitore === null) {
        return;
    }

    contenitore.textContent = "";
    contenitore.className = "alert d-none";
}



function impostaCaricamento(pulsante, caricamento) {
    if (pulsante === null) {
        return;
    }

    pulsante.disabled = caricamento;

    if (caricamento) {
        pulsante.textContent = "Creazione in corso...";
    } else {
        pulsante.textContent = "Crea operatore";
    }
}



function cambiaVisibilitaPassword(event) {
    var passkey = document.getElementById("passkey");
    var confermaPasskey =
        document.getElementById("conferma-passkey");

    var tipoCampo;

    if (event.currentTarget.checked) {
        tipoCampo = "text";
    } else {
        tipoCampo = "password";
    }

    if (passkey !== null) {
        passkey.type = tipoCampo;
    }

    if (confermaPasskey !== null) {
        confermaPasskey.type = tipoCampo;
    }
}



function ottieniDataOdierna() {
    var oggi = new Date();

    var anno = oggi.getFullYear();
    var mese = String(oggi.getMonth() + 1);
    var giorno = String(oggi.getDate());

    if (mese.length < 2) {
        mese = "0" + mese;
    }

    if (giorno.length < 2) {
        giorno = "0" + giorno;
    }

    return anno + "-" + mese + "-" + giorno;
}