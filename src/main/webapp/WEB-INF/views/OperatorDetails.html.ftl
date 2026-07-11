<#include "/layouts/admin_header.ftl">
<#if ruolo?? && ruolo == "admin">
    <#include "/layouts/admin_navbar.ftl">
<#elseif ruolo?? && ruolo == "operator">
	<#include "/layouts/operator_navbar.ftl">
</#if>

<!-- Content Wrapper -->
<div id="content-wrapper" class="d-flex flex-column">

    <!-- Main Content -->
    <div id="content">

        <#include "/layouts/admin_topbar.ftl">


        <!-- ================= CONTENUTO PAGINA ================= -->
        <div class="container-fluid">

            <!-- Titolo pagina -->
            <div class="d-sm-flex align-items-center justify-content-between mb-4">

                <h1 class="h3 mb-0 text-gray-800">
                    Dettagli operatore
                </h1>
                
				<#if ruolo?? && ruolo == "admin">
	                <a href="${contextPath}/opt?action=list"
	                   class="btn btn-primary btn-sm shadow-sm">
	                    Return to list
	                </a>
                </#if>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD ANAGRAFICA OPERATORE ================= -->
                <div class="col-md-6">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Anagrafica operatore
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <#if opt??>

                                <p>
                                    <strong>Name:</strong> ${opt.name}
                                </p>

                                <p>
                                    <strong>Surname:</strong> ${opt.surname}
                                </p>

                                <p>
                                    <strong>Email:</strong> ${opt.email}
                                </p>

                                <p>
                                    <strong>Birthday:</strong> ${opt.age?datetime("yyyy-MM-dd")?string("dd/MM/yyyy")}
                                </p>

                            <#else>

                                <p class="mb-0">
                                    Nessun operatore trovato.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD ANAGRAFICA OPERATORE =============== -->

                <!-- ================= CARD MISSIONE OPERATORE ================= -->
                <div class="col-md-4">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Missione attiva
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                        <#if att??>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="operatorsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Obiettivo</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                        <tr>
                                            <td>${att.obiettivo}</td>
                                            <td class="text-center">

                                                <a href="${contextPath}/mss?action=details&id=${att.missioneId}"
                                                   class="btn btn-primary btn-sm">
                                                    <i class="fas fa-eye"></i>
                                                </a>

                                            </td>
                                        </tr>

                             		   </tbody>

                            		</table>

                       		 </div>
                       		 
                            <#else>

                                <div class="alert alert-secondary mb-0 text-center">
                                    Non ci sono missioni attive
                                </div>

                  		  </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD MISSIONE OPERATORE =============== -->

            </div>
            <!-- Fine row principale -->
            
            <hr class="my-4">
            
            <!-- Inizio riga secondaria -->
            <div class="row">
            

                <!-- ================= CARD PATENTI ================= -->
                <div class="col-md-3">

                    <div class="card shadow mb-4">

                        <!-- Header card Patenti -->
                        <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Patenti
                            </h6>

                            <!-- Bottone apertura modal patenti -->
                            <button type="button"
                                    class="btn btn-primary btn-sm"
                                    data-toggle="modal"
                                    data-target="#modalAggiuntaPatenti">
                                <i class="fas fa-plus"></i>
                            </button>

                        </div>

                        <!-- Body card Patenti -->
                        <div class="card-body">

                            <#if patOp?? && patOp?size gt 0>

                                <#list patOp as p>

                                    <div class="d-flex align-items-center justify-content-between mb-2">

                                        <!-- Patente posseduta -->
                                        <p class="mb-0">
                                            <strong>Type:</strong> ${p.tipologia}
                                        </p>

                                        <!-- Form rimozione patente -->
                                        <form action="${contextPath}/opt"
                                              method="post"
                                              class="mb-0 ml-2">

                                            <input type="hidden"
                                                   name="action"
                                                   value="removePatente">

                                            <input type="hidden"
                                                   name="operatorId"
                                                   value="${opt.id}">

                                            <input type="hidden"
                                                   name="patenteId"
                                                   value="${p.id}">

                                            <button type="submit"
                                                    class="btn btn-danger btn-sm">
                                                <i class="fas fa-trash"></i>
                                            </button>

                                        </form>

                                    </div>

                                </#list>

                            <#else>

                                <p class="mb-0">
                                    Nessuna patente posseduta.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD PATENTI =============== -->


                <!-- ================= CARD ABILITÀ ================= -->
                <div class="col-md-3">

                    <div class="card shadow mb-4">

                        <!-- Header card Abilità -->
                        <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Abilità
                            </h6>

                            <!-- Bottone apertura modal abilità -->
                            <button type="button"
                                    class="btn btn-primary btn-sm"
                                    data-toggle="modal"
                                    data-target="#modalAggiuntaAbilita">
                                <i class="fas fa-plus"></i>
                            </button>

                        </div>

                        <!-- Body card Abilità -->
                        <div class="card-body">

                            <#if comOp?? && comOp?size gt 0>

                                <#list comOp as c>

                                    <div class="d-flex align-items-center justify-content-between mb-2">

                                        <!-- Abilità posseduta -->
                                        <p class="mb-0">
                                            <strong>Type:</strong> ${c.tipologia}
                                        </p>

                                        <!-- Form rimozione abilità -->
                                        <form action="${contextPath}/opt"
                                              method="post"
                                              class="mb-0 ml-2">

                                            <input type="hidden"
                                                   name="action"
                                                   value="removeCompetenza">

                                            <input type="hidden"
                                                   name="operatorId"
                                                   value="${opt.id}">

                                            <input type="hidden"
                                                   name="competenzaId"
                                                   value="${c.id}">

                                            <button type="submit"
                                                    class="btn btn-danger btn-sm">
                                                <i class="fas fa-trash"></i>
                                            </button>

                                        </form>

                                    </div>

                                </#list>

                            <#else>

                                <p class="mb-0">
                                    Nessuna abilità posseduta.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD ABILITÀ =============== -->
                
                <!-- ================= CARD MISSIONE OPERATORE ================= -->
                <div class="col-md-4">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Storico delle missioni
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                        <#if sto?? && sto?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="operatorsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Obiettivo</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>
                                
                                	<#list sto as s>

                                        <tr>
                                            <td>${s.obiettivo}</td>
                                            <td class="text-center">

                                                <a href="${contextPath}/mss?action=details&id=${s.missioneId}"
                                                   class="btn btn-primary btn-sm">
                                                    <i class="fas fa-eye"></i>
                                                </a>

                                            </td>
                                        </tr>
                                        
                                        </#list>

                             		   </tbody>

                            		</table>

                       		 </div>
                       		 
                            <#else>

                                <div class="alert alert-secondary mb-0 text-center">
                                    Non ha ancora svolto nessuna missione
                                </div>

                  		  </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD MISSIONE OPERATORE =============== -->
            
            </div>
            <!-- fine riga secondaria -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->


    <!-- ================= MODAL AGGIUNTA PATENTI ================= -->
    <div class="modal fade"
         id="modalAggiuntaPatenti"
         tabindex="-1"
         role="dialog"
         aria-labelledby="modalAggiuntaPatentiLabel"
         aria-hidden="true">

        <div class="modal-dialog" role="document">

            <div class="modal-content">

                <!-- Form aggiunta patenti -->
                <form action="${contextPath}/opt"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="addPatenti">

                    <input type="hidden"
                           name="operatorId"
                           value="${opt.id}">

                    <!-- Header modal patenti -->
                    <div class="modal-header">

                        <h5 class="modal-title"
                            id="modalAggiuntaPatentiLabel">
                            Scegli le patenti da aggiungere
                        </h5>

                        <button type="button"
                                class="close"
                                data-dismiss="modal"
                                aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>

                    </div>

                    <!-- Body modal patenti -->
                    <div class="modal-body">

                        <#if pat?? && pat?size gt 0>

                            <#assign almenoUnaPatenteDisponibile = false>

                            <#list pat as p>

                                <#assign trovato = false>

                                <!-- Controllo se la patente è già posseduta -->
                                <#if patOp??>

                                    <#list patOp as op>

                                        <#if p.id == op.id>
                                            <#assign trovato = true>
                                        </#if>

                                    </#list>

                                </#if>

                                <!-- Mostro solo le patenti non possedute -->
                                <#if !trovato>

                                    <#assign almenoUnaPatenteDisponibile = true>

                                    <div class="form-check mb-2">

                                        <input class="form-check-input"
                                               type="checkbox"
                                               id="patente_${p.id}"
                                               name="patenti"
                                               value="${p.id}">

                                        <label class="form-check-label"
                                               for="patente_${p.id}">
                                            ${p.tipologia}
                                        </label>

                                    </div>

                                </#if>

                            </#list>

                            <#if !almenoUnaPatenteDisponibile>

                                <p class="mb-0">
                                    Nessuna patente disponibile.
                                </p>

                            </#if>

                        <#else>

                            <p class="mb-0">
                                Nessuna patente disponibile.
                            </p>

                        </#if>

                    </div>

                    <!-- Footer modal patenti -->
                    <div class="modal-footer">

                        <button type="button"
                                class="btn btn-secondary"
                                data-dismiss="modal">
                            Chiudi
                        </button>

                        <button type="submit"
                                class="btn btn-primary">
                            Aggiungi
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>
    <!-- =============== FINE MODAL AGGIUNTA PATENTI =============== -->


    <!-- ================= MODAL AGGIUNTA ABILITÀ ================= -->
    <div class="modal fade"
         id="modalAggiuntaAbilita"
         tabindex="-1"
         role="dialog"
         aria-labelledby="modalAggiuntaAbilitaLabel"
         aria-hidden="true">

        <div class="modal-dialog" role="document">

            <div class="modal-content">

                <!-- Form aggiunta abilità -->
                <form action="${contextPath}/opt"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="addCompetenze">

                    <input type="hidden"
                           name="operatorId"
                           value="${opt.id}">

                    <!-- Header modal abilità -->
                    <div class="modal-header">

                        <h5 class="modal-title"
                            id="modalAggiuntaAbilitaLabel">
                            Scegli le abilità da aggiungere
                        </h5>

                        <button type="button"
                                class="close"
                                data-dismiss="modal"
                                aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>

                    </div>

                    <!-- Body modal abilità -->
                    <div class="modal-body">

                        <#if com?? && com?size gt 0>

                            <#assign almenoUnaAbilitaDisponibile = false>

                            <#list com as c>

                                <#assign trovato = false>

                                <!-- Controllo se l'abilità è già posseduta -->
                                <#if comOp??>

                                    <#list comOp as co>

                                        <#if c.id == co.id>
                                            <#assign trovato = true>
                                        </#if>

                                    </#list>

                                </#if>

                                <!-- Mostro solo le abilità non possedute -->
                                <#if !trovato>

                                    <#assign almenoUnaAbilitaDisponibile = true>

                                    <div class="form-check mb-2">

                                        <input class="form-check-input"
                                               type="checkbox"
                                               id="competenza_${c.id}"
                                               name="competenze"
                                               value="${c.id}">

                                        <label class="form-check-label"
                                               for="competenza_${c.id}">
                                            ${c.tipologia}
                                        </label>

                                    </div>

                                </#if>

                            </#list>

                            <#if !almenoUnaAbilitaDisponibile>

                                <p class="mb-0">
                                    Nessuna abilità disponibile.
                                </p>

                            </#if>

                        <#else>

                            <p class="mb-0">
                                Nessuna abilità disponibile.
                            </p>

                        </#if>

                    </div>

                    <!-- Footer modal abilità -->
                    <div class="modal-footer">

                        <button type="button"
                                class="btn btn-secondary"
                                data-dismiss="modal">
                            Chiudi
                        </button>

                        <button type="submit"
                                class="btn btn-primary">
                            Aggiungi
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>
    <!-- =============== FINE MODAL AGGIUNTA ABILITÀ =============== -->

<#include "/layouts/admin_footer.ftl">