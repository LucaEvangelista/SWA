document.addEventListener("DOMContentLoaded", function () {
    caricaDettaglioOperatore();
});

function caricaDettaglioOperatore() {

	//legge l'id dalla URL
    var parametri = new URLSearchParams(window.location.search);
    var id = parametri.get("id");

	//controllo validità id
    if (id === null || id === "") {
        mostraErrore("ID operatore mancante nella URL.");
        return;
    }

	//crea la chiamata REST
    var url = "/soccorso_Web_SWA/rest/operatori/" + id;

	fetch(url)
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error("Errore HTTP: " + response.status);
	        }

	        return response.json();
	    })
	    .then(function (data) {
	        console.log("JSON ricevuto:", data);
			
			mostraOperatore(data.Operatore);
			
			caricaPatenti(id);
			caricaAbilita(id);
			caricaStorico(id);
			
			caricaSelectPatenti(id);
			caricaSelectAbilita(id);
	    })
	    .catch(function (errore) {
	        console.error("Errore durante il caricamento del dettaglio:", errore);
	        mostraErrore("Errore durante il caricamento del dettaglio operatore.");
	    });
}

function mostraOperatore(op) {
    document.getElementById("op-name").textContent = op.name;
    document.getElementById("op-surname").textContent = op.surname;
    document.getElementById("op-email").textContent = op.email;
    document.getElementById("op-age").textContent = op.age;
}

function caricaPatenti(id) {
	var url = "/soccorso_Web_SWA/rest/operatori/" + id + "/patenti";
	
	fetch(url)
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error("Errore HTTP patenti: " + response.status);
	        }

	        return response.json();
	    })
	    .then(function (patenti) {
	        console.log("JSON patenti ricevuto:", patenti);

	        mostraPatenti(patenti);
	    })
	    .catch(function (errore) {
	        console.error("Errore durante il caricamento delle patenti:", errore);

	        var lista = document.getElementById("lista-patenti");
	        lista.innerHTML = "<li class='list-group-item text-danger'>Errore nel caricamento delle patenti</li>";
	    });
}

function mostraPatenti(patenti) {
    var lista = document.getElementById("lista-patenti");
    lista.innerHTML = "";

    if (patenti === null || patenti.length === 0) {
        lista.innerHTML = "<li class='list-group-item'>Nessuna patente associata</li>";
        return;
    }

    patenti.forEach(function (patente) {
        var riga = document.createElement("li");
        riga.className = "list-group-item";

        riga.textContent = patente.tipologia;

        lista.appendChild(riga);
    });
}

/* ======================= SELECT PER AGGIUNGERE PATENTI ======================= */
function caricaSelectPatenti(operatoreId) {
    var urlTuttePatenti = "/soccorso_Web_SWA/rest/patenti/list";
    var urlPatentiOperatore = "/soccorso_Web_SWA/rest/operatori/" + operatoreId + "/patenti";

    Promise.all([
        fetch(urlTuttePatenti),
        fetch(urlPatentiOperatore)
    ])
        .then(function (responses) {
            var responseTutte = responses[0];
            var responseOperatore = responses[1];

            if (!responseTutte.ok) {
                throw new Error("Errore HTTP caricamento tutte le patenti: " + responseTutte.status);
            }

            if (!responseOperatore.ok) {
                throw new Error("Errore HTTP caricamento patenti operatore: " + responseOperatore.status);
            }

            return Promise.all([
                responseTutte.json(),
                responseOperatore.json()
            ]);
        })
        .then(function (risultati) {
            var tuttePatenti = risultati[0];
            var patentiOperatore = risultati[1];

            var select = document.getElementById("select-patenti");
            select.innerHTML = "";

            var optionDefault = document.createElement("option");
            optionDefault.value = "";
            optionDefault.textContent = "Seleziona una patente";
            select.appendChild(optionDefault);

            /*
                Creo un Set con gli ID delle patenti già possedute.
                Così posso controllare velocemente quali patenti escludere.
            */
            var idPatentiOperatore = new Set();

            patentiOperatore.forEach(function (patente) {
                idPatentiOperatore.add(Number(patente.id));
            });

            /*
                Filtro tutte le patenti, tenendo solo quelle che
                NON sono già presenti tra le patenti dell'operatore.
            */
            var patentiDisponibili = tuttePatenti.filter(function (patente) {
                return !idPatentiOperatore.has(Number(patente.id));
            });

            if (patentiDisponibili.length === 0) {
                var optionVuota = document.createElement("option");
                optionVuota.value = "";
                optionVuota.textContent = "Nessuna patente disponibile";
                select.appendChild(optionVuota);
                return;
            }

            patentiDisponibili.forEach(function (patente) {
                var option = document.createElement("option");

                option.value = patente.id;
                option.textContent = patente.tipologia;

                select.appendChild(option);
            });
        })
        .catch(function (errore) {
            console.error("Errore caricamento select patenti:", errore);

            var select = document.getElementById("select-patenti");
            select.innerHTML = "<option value=''>Errore caricamento patenti</option>";
        });
}
/* ======================= FINE SELECT PER AGGIUNGERE PATENTI ======================= */

 /* ======================= AGGIUNTA PATENTE ======================= */
				   
	function aggiungiPatenteSelezionata() {
       var parametri = new URLSearchParams(window.location.search);
       var operatoreId = parametri.get("id");

       var patenteId = document.getElementById("select-patenti").value;

       if (operatoreId === null || operatoreId === "") {
           mostraMessaggioPatenti("ID operatore mancante nella URL.", true);
           return;
       }

       if (patenteId === null || patenteId === "") {
           mostraMessaggioPatenti("Seleziona una patente.", true);
           return;
       }

       aggiungiPatente(operatoreId, patenteId);
   }

   function aggiungiPatente(operatoreId, patenteId) {
       var url = "/soccorso_Web_SWA/rest/operatori/patenti";

       var dati = {
           patenteRif: parseInt(patenteId),
           operatoreRif: parseInt(operatoreId)
       };

       fetch(url, {
           method: "POST",
           headers: {
               "Content-Type": "application/json"
           },
           body: JSON.stringify(dati)
       })
           .then(function (response) {
               if (!response.ok) {
                   throw new Error("Errore HTTP aggiunta patente: " + response.status);
               }

               return response.json();
           })
           .then(function (data) {
               console.log("Risposta aggiunta patente:", data);

               mostraMessaggioPatenti("Patente aggiunta correttamente.", false);

               document.getElementById("select-patenti").value = "";

               caricaPatenti(operatoreId);
			   caricaSelectPatenti(operatoreId);
           })
           .catch(function (errore) {
               console.error("Errore durante l'aggiunta della patente:", errore);

               mostraMessaggioPatenti("Errore durante l'aggiunta della patente.", true);
           });
   }

   function mostraMessaggioPatenti(messaggio, errore) {
       var contenitore = document.getElementById("messaggio-patenti");

       if (errore) {
           contenitore.innerHTML = "<div class='alert alert-danger'>" + messaggio + "</div>";
       } else {
           contenitore.innerHTML = "<div class='alert alert-success'>" + messaggio + "</div>";
       }
   }
   
   /* ======================= FINE AGGIUNTA PATENTE ======================= */
   /* ======================= SELECT PER AGGIUNGERE ABILITA ======================= */

   function caricaSelectAbilita(operatoreId) {
       var urlTutteAbilita = "/soccorso_Web_SWA/rest/abilita/list";
       var urlAbilitaOperatore =
           "/soccorso_Web_SWA/rest/operatori/" + operatoreId + "/abilita";

       Promise.all([
           fetch(urlTutteAbilita),
           fetch(urlAbilitaOperatore)
       ])
           .then(function (responses) {
               var responseTutte = responses[0];
               var responseOperatore = responses[1];

               if (!responseTutte.ok) {
                   throw new Error(
                       "Errore HTTP caricamento di tutte le abilità: "
                       + responseTutte.status
                   );
               }

               if (!responseOperatore.ok) {
                   throw new Error(
                       "Errore HTTP caricamento abilità operatore: "
                       + responseOperatore.status
                   );
               }

               return Promise.all([
                   responseTutte.json(),
                   responseOperatore.json()
               ]);
           })
           .then(function (risultati) {
               var tutteAbilita = risultati[0];
               var abilitaOperatore = risultati[1];

               var select = document.getElementById("select-abilita");

               if (select === null) {
                   console.error(
                       "Elemento HTML con id 'select-abilita' non trovato."
                   );
                   return;
               }

               select.innerHTML = "";

               var optionDefault = document.createElement("option");
               optionDefault.value = "";
               optionDefault.textContent = "Seleziona un'abilità";
               select.appendChild(optionDefault);

               /*
                * Salviamo gli ID delle abilità che l'operatore
                * possiede già.
                */
               var idAbilitaOperatore = new Set();

               abilitaOperatore.forEach(function (abilita) {
                   idAbilitaOperatore.add(Number(abilita.id));
               });

               /*
                * Manteniamo solamente le abilità non ancora
                * associate all'operatore.
                */
               var abilitaDisponibili = tutteAbilita.filter(function (abilita) {
                   return !idAbilitaOperatore.has(Number(abilita.id));
               });

               if (abilitaDisponibili.length === 0) {
                   var optionVuota = document.createElement("option");
                   optionVuota.value = "";
                   optionVuota.textContent = "Nessuna abilità disponibile";
                   select.appendChild(optionVuota);
                   return;
               }

               abilitaDisponibili.forEach(function (abilita) {
                   var option = document.createElement("option");

                   option.value = abilita.id;
                   option.textContent = abilita.tipologia;

                   select.appendChild(option);
               });
           })
           .catch(function (errore) {
               console.error(
                   "Errore durante il caricamento della select abilità:",
                   errore
               );

               var select = document.getElementById("select-abilita");

               if (select !== null) {
                   select.innerHTML =
                       "<option value=''>Errore caricamento abilità</option>";
               }
           });
   }

   /* ======================= FINE SELECT ABILITA ======================= */


   /* ======================= AGGIUNTA ABILITA ======================= */

   function aggiungiAbilitaSelezionata() {
       var parametri = new URLSearchParams(window.location.search);
       var operatoreId = parametri.get("id");

       var select = document.getElementById("select-abilita");

       if (select === null) {
           console.error(
               "Elemento HTML con id 'select-abilita' non trovato."
           );
           return;
       }

       var abilitaId = select.value;

       if (operatoreId === null || operatoreId === "") {
           mostraMessaggioAbilita(
               "ID operatore mancante nella URL.",
               true
           );
           return;
       }

       if (abilitaId === null || abilitaId === "") {
           mostraMessaggioAbilita(
               "Seleziona un'abilità.",
               true
           );
           return;
       }

       aggiungiAbilita(operatoreId, abilitaId);
   }

   function aggiungiAbilita(operatoreId, abilitaId) {
       var url = "/soccorso_Web_SWA/rest/operatori/abilita";

       var dati = {
           abilitaRif: parseInt(abilitaId),
           operatoreRif: parseInt(operatoreId)
       };

       fetch(url, {
           method: "POST",
           headers: {
               "Content-Type": "application/json"
           },
           body: JSON.stringify(dati)
       })
           .then(function (response) {
               if (!response.ok) {
                   return response.json()
                       .catch(function () {
                           return null;
                       })
                       .then(function (erroreServer) {
                           var messaggio =
                               "Errore HTTP aggiunta abilità: "
                               + response.status;

                           if (
                               erroreServer !== null
                               && erroreServer.message !== undefined
                           ) {
                               messaggio = erroreServer.message;
                           }

                           throw new Error(messaggio);
                       });
               }

               return response.json();
           })
           .then(function (data) {
               console.log("Risposta aggiunta abilità:", data);

               mostraMessaggioAbilita(
                   "Abilità aggiunta correttamente.",
                   false
               );

               document.getElementById("select-abilita").value = "";

               /*
                * Aggiorna sia la lista visibile sia la select,
                * rimuovendo l'abilità appena assegnata.
                */
               caricaAbilita(operatoreId);
               caricaSelectAbilita(operatoreId);
           })
           .catch(function (errore) {
               console.error(
                   "Errore durante l'aggiunta dell'abilità:",
                   errore
               );

               mostraMessaggioAbilita(errore.message, true);
           });
   }

   function mostraMessaggioAbilita(messaggio, errore) {
       var contenitore =
           document.getElementById("messaggio-abilita");

       if (contenitore === null) {
           console.error(
               "Elemento HTML con id 'messaggio-abilita' non trovato."
           );
           return;
       }

       if (errore) {
           contenitore.innerHTML =
               "<div class='alert alert-danger'>"
               + messaggio
               + "</div>";
       } else {
           contenitore.innerHTML =
               "<div class='alert alert-success'>"
               + messaggio
               + "</div>";
       }
   }

   /* ======================= FINE AGGIUNTA ABILITA ======================= */
				   
function caricaAbilita(id) {
	var url = "/soccorso_Web_SWA/rest/operatori/" + id + "/abilita";
	
	fetch(url)
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error("Errore HTTP abilita: " + response.status);
	        }

	        return response.json();
	    })
	    .then(function (abilita) {
	        console.log("JSON patenti ricevuto:", abilita);

	        mostraAbilita(abilita);
	    })
	    .catch(function (errore) {
	        console.error("Errore durante il caricamento delle abilita:", errore);

	        var lista = document.getElementById("lista-abilita");
	        lista.innerHTML = "<li class='list-group-item text-danger'>Errore nel caricamento delle abilita</li>";
	    });
}

function mostraAbilita(abilita) {
    var lista = document.getElementById("lista-abilita");
    lista.innerHTML = "";

    if (abilita === null || abilita.length === 0) {
        lista.innerHTML = "<li class='list-group-item'>Nessuna abilita associata</li>";
        return;
    }

    abilita.forEach(function (a) {
        var riga = document.createElement("li");
        riga.className = "list-group-item";

        riga.textContent = a.tipologia;

        lista.appendChild(riga);
    });
}

function caricaStorico(id) {
    var url = "/soccorso_Web_SWA/rest/operatori/" + id + "/storico";

    fetch(url)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Errore HTTP storico: " + response.status);
            }

            return response.json();
        })
        .then(function (storicoMissioni) {
            console.log("JSON storico missioni ricevuto:", storicoMissioni);

            mostraStorico(storicoMissioni);
        })
        .catch(function (errore) {
            console.error("Errore durante il caricamento dello storico missioni:", errore);

            var corpoTabella = document.getElementById("corpo-storico-missioni");

            corpoTabella.innerHTML = `
                <tr>
                    <td colspan="2" class="text-center text-danger">
                        Errore nel caricamento dello storico missioni
                    </td>
                </tr>
            `;
        });
}

function mostraStorico(storicoMissioni) {

    var corpoTabella = document.getElementById("corpo-storico-missioni");

    corpoTabella.innerHTML = "";

    if (storicoMissioni === null || storicoMissioni.length === 0) {
        corpoTabella.innerHTML = `
            <tr>
                <td colspan="2" class="text-center">
                    Nessuna missione nello storico
                </td>
            </tr>
        `;
        return;
    }

    storicoMissioni.forEach(function (missione) {

        var riga = document.createElement("tr");

        riga.innerHTML = `
            <td>${missione.obiettivo}</td>
            <td>${missione.posizione}</td>
        `;

        corpoTabella.appendChild(riga);
    });
}



function mostraErrore(messaggio) {
    document.getElementById("messaggio-errore").textContent = messaggio;
}