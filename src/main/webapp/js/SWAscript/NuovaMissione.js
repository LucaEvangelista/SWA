var CONTEXT_PATH = "/soccorso_Web_SWA";
var REST_BASE_URL = CONTEXT_PATH + "/rest";

var URL_RICHIESTE = REST_BASE_URL + "/richieste";
var URL_MISSIONI = REST_BASE_URL + "/missioni";


var URL_SQUADRE = REST_BASE_URL + "/squadre/list";
var URL_MEZZI = REST_BASE_URL + "/mezzi/list";
var URL_MATERIALI = REST_BASE_URL + "/materiali/list";


var FASE_RICHIESTA_CREABILE = "attiva";

document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("form-crea-missione");

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        creaMissione();
    });

    caricaPagina();
});

function caricaPagina() {
    var richiestaId = leggiRichiestaIdDaUrl();

    if (richiestaId === null) {
        mostraErrore("ID richiesta mancante nella URL.");
        nascondiCaricamento();
        return;
    }

    document.getElementById("richiestaId").value = richiestaId;
    document.getElementById("btn-annulla").href = "DettagliRichieste.html?id=" + richiestaId;

    Promise.all([
        caricaRichiesta(richiestaId),
        caricaSquadre(),
        caricaMezzi(),
        caricaMateriali()
    ])
    .then(function () {
        nascondiCaricamento();
        document.getElementById("form-crea-missione").classList.remove("d-none");
    })
    .catch(function (errore) {
        console.error("Errore caricamento pagina:", errore);
        nascondiCaricamento();
        mostraErrore(errore.message);
    });
}

function leggiRichiestaIdDaUrl() {
    var parametri = new URLSearchParams(window.location.search);

    var id = parametri.get("id");

    if (id === null || id === "") {
        id = parametri.get("richiestaId");
    }

    if (id === null || id === "" || isNaN(parseInt(id))) {
        return null;
    }

    return parseInt(id);
}

function caricaRichiesta(id) {
    return fetchJson(URL_RICHIESTE + "/" + id)
        .then(function (richiesta) {
            mostraDatiRichiesta(richiesta);

            if (richiesta.fase !== FASE_RICHIESTA_CREABILE) {
                bloccaCreazione(
                    "Questa richiesta non può generare una missione perché la fase attuale è: " +
                    valore(richiesta.fase)
                );
            }
        });
}

function mostraDatiRichiesta(richiesta) {
    document.getElementById("richiesta-id").textContent =
        valore(richiesta.id);

    document.getElementById("richiesta-nome").textContent =
        valore(richiesta.nomePersona);

    document.getElementById("richiesta-email").textContent =
        valore(richiesta.mailPersona);

    document.getElementById("richiesta-fase").textContent =
        valore(richiesta.fase);

    document.getElementById("obiettivo").value =
        valoreInput(richiesta.descrizione);

    document.getElementById("posizione").value =
        valoreInput(richiesta.indirizzo);
}

function caricaSquadre() {
    return fetchJson(URL_SQUADRE)
        .then(function (squadre) {
            popolaSelectSquadre(squadre);
        });
}

function popolaSelectSquadre(squadre) {
    var select = document.getElementById("squadraId");

    select.innerHTML = "";

    var optionDefault = document.createElement("option");
    optionDefault.value = "";
    optionDefault.textContent = "-- Seleziona una squadra --";
    select.appendChild(optionDefault);

    if (!Array.isArray(squadre) || squadre.length === 0) {
        var optionVuota = document.createElement("option");
        optionVuota.value = "";
        optionVuota.textContent = "Nessuna squadra disponibile";
        optionVuota.disabled = true;
        select.appendChild(optionVuota);
        return;
    }

    for (var i = 0; i < squadre.length; i++) {
        var sq = squadre[i];


        if (!elementoDisponibile(sq)) {
            continue;
        }

        var id = primoValoreValido([
            sq.squadraId,
            sq.id,
            sq.squadraID
        ]);

        var nome = primoValoreValido([
            sq.nome,
            sq.name,
            "Squadra #" + id
        ]);

        if (id === null || id === undefined || id === "") {
            continue;
        }

        var option = document.createElement("option");
        option.value = id;
        option.textContent = nome;

        select.appendChild(option);
    }
}

function caricaMezzi() {
    return fetchJson(URL_MEZZI)
        .then(function (mezzi) {
            popolaCheckboxMezzi(mezzi);
        });
}

function popolaCheckboxMezzi(mezzi) {
    var contenitore = document.getElementById("lista-mezzi");

    contenitore.innerHTML = "";

    if (!Array.isArray(mezzi) || mezzi.length === 0) {
        contenitore.innerHTML = "<p class='text-muted mb-0'>Nessun mezzo disponibile.</p>";
        return;
    }

    var almenoUno = false;

    for (var i = 0; i < mezzi.length; i++) {
        var mz = mezzi[i];

        if (!elementoDisponibile(mz)) {
            continue;
        }

        var id = primoValoreValido([
            mz.id,
            mz.mezzoId,
            mz.mezzoID
        ]);

        var tipologia = primoValoreValido([
            mz.tipologia,
            mz.tipo,
            mz.nome,
            "Mezzo #" + id
        ]);

        var seriale = primoValoreValido([
            mz.seriale,
            mz.targa,
            ""
        ]);

        if (id === null || id === undefined || id === "") {
            continue;
        }

        almenoUno = true;

        var div = document.createElement("div");
        div.className = "form-check mb-2";

        var input = document.createElement("input");
        input.className = "form-check-input";
        input.type = "checkbox";
        input.name = "mezziIds";
        input.value = id;
        input.id = "mezzo_" + id;

        var label = document.createElement("label");
        label.className = "form-check-label";
        label.setAttribute("for", "mezzo_" + id);

        if (seriale !== "") {
            label.textContent = tipologia + " (" + seriale + ")";
        } else {
            label.textContent = tipologia;
        }

        div.appendChild(input);
        div.appendChild(label);

        contenitore.appendChild(div);
    }

    if (!almenoUno) {
        contenitore.innerHTML = "<p class='text-muted mb-0'>Nessun mezzo libero disponibile.</p>";
    }
}

function caricaMateriali() {
    return fetchJson(URL_MATERIALI)
        .then(function (materiali) {
            popolaCheckboxMateriali(materiali);
        });
}

function popolaCheckboxMateriali(materiali) {
    var contenitore = document.getElementById("lista-materiali");

    contenitore.innerHTML = "";

    if (!Array.isArray(materiali) || materiali.length === 0) {
        contenitore.innerHTML = "<p class='text-muted mb-0'>Nessun materiale disponibile.</p>";
        return;
    }

    var almenoUno = false;

    for (var i = 0; i < materiali.length; i++) {
        var mt = materiali[i];

        if (!elementoDisponibile(mt)) {
            continue;
        }

        var id = primoValoreValido([
            mt.id,
            mt.materialeId,
            mt.materialeID
        ]);

        var tipologia = primoValoreValido([
            mt.tipologia,
            mt.tipo,
            mt.nome,
            "Materiale #" + id
        ]);

        var seriale = primoValoreValido([
            mt.seriale,
            ""
        ]);

        if (id === null || id === undefined || id === "") {
            continue;
        }

        almenoUno = true;

        var div = document.createElement("div");
        div.className = "form-check mb-2";

        var input = document.createElement("input");
        input.className = "form-check-input";
        input.type = "checkbox";
        input.name = "materialiIds";
        input.value = id;
        input.id = "materiale_" + id;

        var label = document.createElement("label");
        label.className = "form-check-label";
        label.setAttribute("for", "materiale_" + id);

        if (seriale !== "") {
            label.textContent = tipologia + " (" + seriale + ")";
        } else {
            label.textContent = tipologia;
        }

        div.appendChild(input);
        div.appendChild(label);

        contenitore.appendChild(div);
    }

    if (!almenoUno) {
        contenitore.innerHTML = "<p class='text-muted mb-0'>Nessun materiale libero disponibile.</p>";
    }
}

function creaMissione() {
    pulisciMessaggi();

    var richiestaId = parseInt(document.getElementById("richiestaId").value);
    var squadraId = parseInt(document.getElementById("squadraId").value);
    var obiettivo = document.getElementById("obiettivo").value.trim();
    var posizione = document.getElementById("posizione").value.trim();

    if (isNaN(richiestaId)) {
        mostraErrore("ID richiesta non valido.");
        return;
    }

    if (isNaN(squadraId)) {
        mostraErrore("Devi selezionare una squadra.");
        return;
    }

    if (obiettivo === "") {
        mostraErrore("L'obiettivo della missione è obbligatorio.");
        return;
    }

    if (posizione === "") {
        mostraErrore("La posizione della missione è obbligatoria.");
        return;
    }

    var payload = {
        richiestaId: richiestaId,
        obiettivo: obiettivo,
        posizione: posizione,
        squadraId: squadraId,
        mezziIds: valoriCheckboxSelezionati("mezziIds"),
        materialiIds: valoriCheckboxSelezionati("materialiIds")
    };

    var btn = document.getElementById("btn-crea-missione");
    btn.disabled = true;
    btn.textContent = "Creazione in corso...";

    fetch(URL_MISSIONI, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(payload)
    })
    .then(function (response) {
        return leggiRispostaJson(response);
    })
    .then(function (missioneCreata) {
        mostraSuccesso("Missione creata correttamente.");

        var missioneId = primoValoreValido([
            missioneCreata.id,
            missioneCreata.missioneId,
            missioneCreata.missioneID
        ]);

        if (missioneId !== null && missioneId !== undefined && missioneId !== "") {
            window.location.href = "DettagliMissione.html?id=" + missioneId;
        } else {
            window.location.href = "ListaMissioni.html";
        }
    })
    .catch(function (errore) {
        console.error("Errore creazione missione:", errore);
        mostraErrore(errore.message);

        btn.disabled = false;
        btn.textContent = "Crea missione";
    });
}

function valoriCheckboxSelezionati(nomeCheckbox) {
    var checkboxes = document.querySelectorAll("input[name='" + nomeCheckbox + "']:checked");
    var valori = [];

    for (var i = 0; i < checkboxes.length; i++) {
        valori.push(parseInt(checkboxes[i].value));
    }

    return valori;
}

function fetchJson(url) {
    return fetch(url, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
    .then(function (response) {
        return leggiRispostaJson(response);
    });
}

function leggiRispostaJson(response) {
    return response.text()
        .then(function (testo) {
            var data = null;

            if (testo !== null && testo !== "") {
                data = JSON.parse(testo);
            }

            if (!response.ok) {
                var messaggio = "Errore HTTP: " + response.status;

                if (data !== null) {
                    messaggio =
                        data.message ||
                        data.messaggio ||
                        data.error ||
                        data.errore ||
                        messaggio;
                }

                throw new Error(messaggio);
            }

            return data;
        });
}

function elementoDisponibile(elemento) {
    if (elemento === null || elemento === undefined) {
        return false;
    }

    /*
        Se non esiste il campo stato, lo considero disponibile.
        Questo serve per non bloccare le squadre, perché magari nella tua classe Squadra
        non hai proprio il campo stato.
    */
    if (elemento.stato === null || elemento.stato === undefined) {
        return true;
    }

    return String(elemento.stato).toLowerCase() === "libero";
}

function primoValoreValido(valori) {
    for (var i = 0; i < valori.length; i++) {
        if (valori[i] !== null && valori[i] !== undefined && valori[i] !== "") {
            return valori[i];
        }
    }

    return null;
}

function bloccaCreazione(messaggio) {
    mostraErrore(messaggio);

    document.getElementById("btn-crea-missione").disabled = true;
    document.getElementById("squadraId").disabled = true;

    var mezzi = document.querySelectorAll("input[name='mezziIds']");
    var materiali = document.querySelectorAll("input[name='materialiIds']");

    for (var i = 0; i < mezzi.length; i++) {
        mezzi[i].disabled = true;
    }

    for (var j = 0; j < materiali.length; j++) {
        materiali[j].disabled = true;
    }
}

function mostraErrore(messaggio) {
    var box = document.getElementById("messaggio-errore");
    box.textContent = messaggio;
    box.classList.remove("d-none");
}

function mostraSuccesso(messaggio) {
    var box = document.getElementById("messaggio-successo");
    box.textContent = messaggio;
    box.classList.remove("d-none");
}

function pulisciMessaggi() {
    document.getElementById("messaggio-errore").classList.add("d-none");
    document.getElementById("messaggio-successo").classList.add("d-none");
}

function nascondiCaricamento() {
    document.getElementById("messaggio-caricamento").classList.add("d-none");
}

function valore(val) {
    if (val === null || val === undefined || val === "") {
        return "-";
    }

    return val;
}

function valoreInput(val) {
    if (val === null || val === undefined) {
        return "";
    }

    return val;
}