"use strict";

var CONTEXT_PATH = "/soccorso_Web_SWA";
var LOGIN_URL = CONTEXT_PATH + "/rest/auth/login";
var PAGINA_DOPO_LOGIN =
    CONTEXT_PATH + "/viste/ListaOperatori.html";

document.addEventListener("DOMContentLoaded", function () {

    var loginForm = document.getElementById("login-form");
    var emailInput = document.getElementById("email");
    var passwordInput = document.getElementById("password");
    var loginButton = document.getElementById("login-button");
    var loginMessage = document.getElementById("login-message");
    var mostraPasswordButton =
        document.getElementById("mostra-password");

    if (loginForm === null) {
        console.error(
            "Impossibile trovare il form con id 'login-form'."
        );
        return;
    }

    if (emailInput === null || passwordInput === null) {
        console.error(
            "I campi email o password non sono presenti nella pagina."
        );
        return;
    }

    /*
     * Mostra o nasconde la password.
     */
    if (mostraPasswordButton !== null) {

        mostraPasswordButton.addEventListener(
            "click",
            function () {

                if (passwordInput.type === "password") {

                    passwordInput.type = "text";
                    mostraPasswordButton.textContent = "Nascondi";

                } else {

                    passwordInput.type = "password";
                    mostraPasswordButton.textContent = "Mostra";
                }
            }
        );
    }

    /*
     * Gestione dell'invio del form di login.
     */
    loginForm.addEventListener(
        "submit",
        function (event) {

            event.preventDefault();

            pulisciMessaggio();

            var email = emailInput.value.trim();
            var password = passwordInput.value;

            /*
             * Controllo del campo email.
             */
            if (email === "") {

                mostraErrore(
                    "Inserisci l'indirizzo email."
                );

                emailInput.focus();

                return;
            }

            /*
             * Controllo del campo password.
             */
            if (password === "") {

                mostraErrore(
                    "Inserisci la password."
                );

                passwordInput.focus();

                return;
            }

            /*
             * AuthRes riceve i dati tramite:
             *
             * @FormParam("email")
             * @FormParam("password")
             *
             * Per questo utilizziamo
             * application/x-www-form-urlencoded.
             */
            var datiLogin = new URLSearchParams();

            datiLogin.append("email", email);
            datiLogin.append("password", password);

            disabilitaLogin();

            fetch(
                LOGIN_URL,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded",

                        "Accept":
                            "text/plain, application/json"
                    },

                    /*
                     * Permette al browser di salvare e inviare
                     * il cookie contenente il token.
                     */
                    credentials: "include",

                    body: datiLogin.toString()
                }
            )
            .then(function (response) {

                /*
                 * Se il server restituisce 401, 403, 404,
                 * 500 oppure un altro errore HTTP.
                 */
                if (!response.ok) {

                    return leggiMessaggioErrore(response)
                        .then(function (messaggio) {

                            throw new Error(messaggio);
                        });
                }

                /*
                 * AuthRes restituisce il token direttamente
                 * nel corpo della risposta.
                 */
                return response.text();
            })
            .then(function (testoToken) {

                var token = testoToken.trim();

                if (token === "") {

                    throw new Error(
                        "Il server non ha restituito " +
                        "il token di autenticazione."
                    );
                }

                /*
                 * Salviamo il token anche nel sessionStorage.
                 *
                 * Il cookie viene già impostato dal server
                 * tramite NewCookie.
                 */
                sessionStorage.setItem(
                    "authToken",
                    token
                );

                mostraSuccesso(
                    "Accesso effettuato correttamente."
                );

                /*
                 * Reindirizzamento dopo il login.
                 */
                window.location.href =
                    PAGINA_DOPO_LOGIN;
            })
            .catch(function (error) {

                console.error(
                    "Errore durante il login:",
                    error
                );

                if (
                    error !== null &&
                    error !== undefined &&
                    error.message
                ) {

                    mostraErrore(error.message);

                } else {

                    mostraErrore(
                        "Impossibile contattare il server. " +
                        "Riprova più tardi."
                    );
                }
            })
            .then(function () {

                /*
                 * Questo blocco viene eseguito sia dopo
                 * il successo sia dopo un errore.
                 */
                abilitaLogin();
            });
        }
    );

    /*
     * Legge il messaggio di errore restituito dal server.
     *
     * La risposta potrebbe essere:
     *
     * - JSON con ErrorResponse;
     * - testo semplice;
     * - risposta senza corpo.
     */
    function leggiMessaggioErrore(response) {

        var contentType =
            response.headers.get("content-type") || "";

        /*
         * Risposta JSON.
         */
        if (
            contentType.indexOf(
                "application/json"
            ) !== -1
        ) {

            return response.json()
                .then(function (errore) {

                    if (
                        errore !== null &&
                        errore !== undefined
                    ) {

                        if (errore.message) {
                            return errore.message;
                        }

                        if (errore.messaggio) {
                            return errore.messaggio;
                        }

                        if (errore.error) {
                            return errore.error;
                        }
                    }

                    return "Email o password non corrette.";
                })
                .catch(function (error) {

                    console.error(
                        "Errore durante la lettura " +
                        "della risposta JSON:",
                        error
                    );

                    return messaggioDaStatus(
                        response.status
                    );
                });
        }


        return response.text()
            .then(function (testo) {

                if (testo.trim() !== "") {
                    return testo;
                }

                return messaggioDaStatus(
                    response.status
                );
            })
            .catch(function (error) {

                console.error(
                    "Errore durante la lettura " +
                    "della risposta:",
                    error
                );

                return messaggioDaStatus(
                    response.status
                );
            });
    }

    /*
     * Genera un messaggio in base allo status HTTP.
     */
    function messaggioDaStatus(status) {

        if (status === 400) {

            return "I dati inseriti non sono validi.";
        }

        if (status === 401) {

            return "Email o password non corrette.";
        }

        if (status === 403) {

            return "Non hai i permessi per accedere.";
        }

        if (status === 404) {

            return "Servizio di login non trovato.";
        }

        if (status === 405) {

            return "Metodo HTTP non consentito.";
        }

        if (status >= 500) {

            return "Errore interno del server.";
        }

        return "Accesso non riuscito.";
    }


    function mostraErrore(messaggio) {

        if (loginMessage === null) {

            alert(messaggio);

            return;
        }

        loginMessage.textContent = messaggio;
        loginMessage.className =
            "alert alert-danger";
        loginMessage.hidden = false;
    }


    function mostraSuccesso(messaggio) {

        if (loginMessage === null) {
            return;
        }

        loginMessage.textContent = messaggio;
        loginMessage.className =
            "alert alert-success";
        loginMessage.hidden = false;
    }


    function pulisciMessaggio() {

        if (loginMessage === null) {
            return;
        }

        loginMessage.textContent = "";
        loginMessage.className = "";
        loginMessage.hidden = true;
    }


    function disabilitaLogin() {

        if (loginButton === null) {
            return;
        }

        loginButton.disabled = true;
        loginButton.textContent =
            "Accesso in corso...";
    }


    function abilitaLogin() {

        if (loginButton === null) {
            return;
        }

        loginButton.disabled = false;
        loginButton.textContent = "Accedi";
    }
});