document.addEventListener("DOMContentLoaded", function () {

	var azioniConvalida = document.getElementById("azioni-convalida");
    var form = document.getElementById("form-richiesta");
    var messaggio = document.getElementById("messaggio");
    var btnInvia = document.getElementById("btn-invia");
    var titoloPagina = document.getElementById("titolo-pagina");

    var uuid = getParametroUrl("uuid");

    if (uuid !== null && uuid !== "") {
        convalidaRichiesta(uuid);
        return;
    }

    if (!form) {
        console.error("Form richiesta non trovato");
        return;
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        nascondiMessaggio();

        var richiesta = {
            nomePersona: document.getElementById("nomePersona").value.trim(),
            mailPersona: document.getElementById("mailPersona").value.trim(),
            indirizzo: document.getElementById("indirizzo").value.trim(),
            descrizione: document.getElementById("descrizione").value.trim()
        };

        if (!validaRichiesta(richiesta)) {
            mostraMessaggio("Compila correttamente tutti i campi.", "danger");
            return;
        }

        btnInvia.disabled = true;
        btnInvia.textContent = "Invio in corso...";

        fetch(getContextPath() + "/rest/richieste", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(richiesta)
        })
        .then(function (response) {
            return leggiRisposta(response).then(function (data) {
                return {
                    response: response,
                    data: data
                };
            });
        })
        .then(function (result) {
            var response = result.response;
            var data = result.data;

            if (!response.ok) {
                mostraMessaggio(estraiMessaggioErrore(data, "Errore durante la creazione della richiesta."), "danger");
                return;
            }

            form.reset();

            mostraMessaggio("Richiesta inviata correttamente. Controlla il link di conferma per convalidarla.", "success");
        })
        .catch(function (error) {
            console.error("Errore invio richiesta:", error);
            mostraMessaggio("Errore di connessione con il server.", "danger");
        })
        .then(function () {
            btnInvia.disabled = false;
            btnInvia.textContent = "Invia richiesta";
        });
    });

    function convalidaRichiesta(uuid) {

        if (titoloPagina) {
            titoloPagina.textContent = "Convalida richiesta di soccorso";
        }
		
		if (azioniConvalida) {
		    azioniConvalida.classList.remove("d-none");
		}

        if (form) {
            form.classList.add("d-none");
        }

        mostraMessaggio("Convalida della richiesta in corso...", "info");

        fetch(getContextPath() + "/rest/convalida?uuid=" + encodeURIComponent(uuid), {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        })
        .then(function (response) {
            return leggiRisposta(response).then(function (data) {
                return {
                    response: response,
                    data: data
                };
            });
        })
        .then(function (result) {
            var response = result.response;
            var data = result.data;

            if (!response.ok) {
                mostraMessaggio(estraiMessaggioErrore(data, "Errore durante la convalida della richiesta."), "danger");
                return;
            }

            var testo = "Richiesta convalidata correttamente.";

            if (data && data.message) {
                testo = data.message;
            }

            mostraMessaggio(testo, "success");
        })
        .catch(function (error) {
            console.error("Errore convalida richiesta:", error);
            mostraMessaggio("Errore di connessione con il server.", "danger");
        });
    }

    function validaRichiesta(richiesta) {
        if (richiesta.nomePersona === "") {
            return false;
        }

        if (richiesta.mailPersona === "") {
            return false;
        }

        if (richiesta.indirizzo === "") {
            return false;
        }

        if (richiesta.descrizione === "") {
            return false;
        }

        return true;
    }

    function leggiRisposta(response) {
        var contentType = response.headers.get("content-type");

        if (contentType && contentType.indexOf("application/json") !== -1) {
            return response.json();
        }

        return response.text().then(function (testo) {
            return {
                message: testo || "Risposta non valida dal server."
            };
        });
    }

    function estraiMessaggioErrore(data, messaggioDefault) {
        if (data && data.message) {
            return data.message;
        }

        if (data && data.error) {
            return data.error;
        }

        return messaggioDefault;
    }

    function mostraMessaggio(testo, tipo) {
        messaggio.textContent = testo;
        messaggio.className = "alert alert-" + tipo;
        messaggio.classList.remove("d-none");
    }

    function nascondiMessaggio() {
        messaggio.textContent = "";
        messaggio.className = "alert d-none";
    }

    function getParametroUrl(nomeParametro) {
        var queryString = window.location.search.substring(1);

        if (queryString === "") {
            return null;
        }

        var parametri = queryString.split("&");

        for (var i = 0; i < parametri.length; i++) {
            var coppia = parametri[i].split("=");

            if (decodeURIComponent(coppia[0]) === nomeParametro) {
                return decodeURIComponent(coppia[1] || "");
            }
        }

        return null;
    }

    function getContextPath() {
        var path = window.location.pathname;
        var parti = path.split("/");

        if (parti.length > 1) {
            return "/" + parti[1];
        }

        return "";
    }

});