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
                    Dettagli missione
                </h1>
            <#if ruolo??  && ruolo == "admin">

                <a href="${contextPath}/mss?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>
                
                <#if mission?? && mission.status?? && mission.status == "attiva">
			
			        <form action="${contextPath}/mss"
			              method="post"
			              class="mb-0">
			
			            <input type="hidden"
			                   name="action"
			                   value="terminaMissione">
			
			            <input type="hidden"
			                   name="missionId"
			                   value="${mission.missioneId}">
			
			            <input type="hidden"
			                   name="squadraId"
			                   value="${mission.squadraRif}">
			
			            <input type="hidden"
			                   name="richiestaId"
			                   value="${mission.richiestaRif}">
			
			            <button type="submit"
			                    class="btn btn-danger btn-sm shadow-sm">
			                Termina missione
			            </button>
			
			        </form>
			
			    </#if>
		    </#if>
			    

            </div>


            <!-- Riga dettagli missione -->
            <div class="row">

                <!-- ================= CARD DETTAGLI MISSIONE ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Informazioni missione
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <#if mission??>

                                <p>
                                    <strong>Obiettivo:</strong> ${mission.obiettivo!"-"}
                                </p>

                                <p>
                                    <strong>Posizione:</strong> ${mission.posizione!"-"}
                                </p>

                                <p>
                                    <strong>Squadra sul campo:</strong>
                                    <#if squad??>
                                        ${squad.nome!"-"}
                                    <#else>
                                        -
                                    </#if>
                                </p>

                            <#else>

                                <p class="mb-0">
                                    Nessuna missione trovata.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD DETTAGLI MISSIONE =============== -->

            </div>
            <!-- Fine riga dettagli missione -->


            <hr class="my-4">


            <!-- Riga comunicazioni -->
            <div class="row">

                <!-- ================= CARD COMUNICAZIONI ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card comunicazioni -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Comunicazioni
                            </h6>

                        </div>

                        <!-- Body card comunicazioni -->
                        <div class="card-body">

                            <div class="table-responsive">

                                <table class="table table-striped table-bordered mb-0"
                                       width="100%"
                                       cellspacing="0">

                                    <thead>
                                        <tr>
                                            <th>Comunicazione</th>
                                            <th>Ruolo</th>
                                            <th>Orario</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        <#if comunicazioni?? && comunicazioni?size gt 0>

                                            <#list comunicazioni as c>

                                                <tr>
                                                    <td>${c.contenuto!"-"}</td>
                                                    <td>${c.ruolo!"-"}</td>
                                                    <td>        
                                                    	<#if c.createdAt??>
   														 	${c.createdAt?datetime("yyyy-MM-dd'T'HH:mm:ss")?string("dd/MM/yyyy HH:mm")}
														<#else>
      													  	-
        												</#if>
												</td>
                                                </tr>

                                            </#list>

                                        <#else>

                                            <tr>
                                                <td>Nessuna comunicazione</td>
                                                <td> - </td>
                                                <td> - </td>
                                            </tr>

                                        </#if>

                                    </tbody>

                                </table>

                            </div>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD COMUNICAZIONI =============== -->

            </div>
            <!-- Fine riga comunicazioni -->

			<#if mission?? && (
		        (mission.status?? && mission.status == "attiva")
    		)>
			
            <!-- Riga form nuova comunicazione -->
            <div class="row">

                <!-- ================= CARD NUOVA COMUNICAZIONE ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card nuova comunicazione -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Inserisci comunicazione relativa alla missione
                            </h6>

                        </div>

                        <!-- Body card nuova comunicazione -->
                        <div class="card-body">

                            <#if mission??>

                                <form action="${contextPath}/mss"
                                      method="post"
                                      id="inserimentoComunicazione">

                                    <input type="hidden"
                                           name="action"
                                           value="addComunicazione">

                                    <input type="hidden"
                                           name="missionId"
                                           value="${mission.missioneId}">

                                    <div class="form-group">

                                        <label for="com-contenuto">
                                            Comunicazione:
                                        </label>

                                        <input type="text"
                                               class="form-control"
                                               name="com-contenuto"
                                               id="com-contenuto"
                                               required>

                                    </div>

                                    <div class="d-flex justify-content-end mt-4">

                                        <button type="submit"
                                                class="btn btn-primary">
                                            Invia comunicazione
                                        </button>

                                    </div>

                                </form>

                            <#else>

                                <p class="mb-0">
                                    Non è possibile inserire una comunicazione perché la missione non è disponibile.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD NUOVA COMUNICAZIONE =============== -->

            </div>
            <!-- Fine riga form nuova comunicazione -->
            <#else>
             <!-- Riga messaggio comunicazioni non disponibili -->
		    <div class="row">
		
		        <div class="col-lg-8 col-md-10">
		
		            <div class="card shadow mb-4">
		
		                <div class="card-header py-3">
		                    <h6 class="m-0 font-weight-bold text-secondary">
		                        Comunicazioni non disponibili
		                    </h6>
		                </div>
		
		                <div class="card-body">
		
		                    <p class="mb-0 text-muted">
		                        Non è possibile inserire nuove comunicazioni.
		                    </p>
		
		                </div>
		
		            </div>
		
		        </div>
		
		    </div>
		
		</#if>

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
