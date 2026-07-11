<#include "/layouts/admin_header.ftl">
<#include "/layouts/admin_navbar.ftl">

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
                    Dettagli richiesta
                </h1>

                <a href="${contextPath}/rqs?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD DETTAGLI RICHIESTA ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Informazioni richiesta
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <#if rqst??>

                                <p>
                                    <strong>Name:</strong> ${rqst.nomePersona}
                                </p>

                                <p>
                                    <strong>Location:</strong> ${rqst.indirizzo}
                                </p>

                                <p>
                                    <strong>Emergency:</strong> ${rqst.descrizione}
                                </p>

                                <!-- Pulsanti azione -->
                                
                                <#if flg == "entrambi">
	                                <div class="d-flex justify-content-end mt-4">
	                                
	                                <form action="${contextPath}/rqs"
	                                	  method="post"
	                                	  class="mb-0 d-inline">
	                                	  
	                                      <input type="hidden"
	                                       name="action"
	                                       value="rifiutaRichiesta">
	                                       
	                                      <input type="hidden"
	                                       name="richiestaId"
	                                       value="${rqst.id}">
	                                       
	                                      <button type="submit"
	                                      		  class="btn btn-danger mr-2">
	                                      		  Rifiuta
	                                  		  <i class="fa-solid fa-x"></i>
	                              		  </button>
	                                    
	                                </form>
	                                    
	                                <form action="${contextPath}/mss"
	                                	  method="post"
	                                	  class="mb-0 d-inline">
	                                	  
	                                  <input type="hidden"
	                                         name="action"
	                                         value="creazioneMissione">
	                                         
	                                  <input type="hidden"
	                                       	 name="richiestaId"
	                                       	 value="${rqst.id}">
	                                       	 
	                                  <button type="submit"
	                                  		  class="btn btn-success">
	                                  		  Avvia Missione
	                              		  <i class="fa-solid fa-check"></i>
	                          		  </button>
	                                	  
	                                </form>
	
	                                </div>
                                </#if>
                                
                                <#if flg == "nessuno">
	                                <div class="d-flex justify-content-end mt-4">
	
	                                </div>
                                </#if>
                                
                                <#if flg == "singolo">
	                                <div class="d-flex justify-content-end mt-4">
	                                
	                                <form action="${contextPath}/mss"
	                                	  method="post"
	                                	  class="mb-0 d-inline">
	                                	  
	                                      <input type="hidden"
	                                       name="action"
	                                       value="dettagliMissione">
	                                       
	                                      <input type="hidden"
	                                       name="richiestaId"
	                                       value="${rqst.id}">
	                                       
	                                      <button type="submit"
	                                      		  class="btn btn-primary">
	                                      		  Vai alla missione
	                                  		 <i class="fa-solid fa-helmet-safety"></i>
	                              		  </button>
	                                    
	                                </form>
	                                </div>

                                </#if>

                            <#else>

                                <p class="mb-4">
                                    Richiesta non trovata.
                                </p>

                                <div class="d-flex justify-content-end">

                                    <a href="${contextPath}/rqs?action=list"
                                       class="btn btn-secondary">
                                        Return to list
                                    </a>

                                </div>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD DETTAGLI RICHIESTA =============== -->

            </div>
            <!-- Fine row principale -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
