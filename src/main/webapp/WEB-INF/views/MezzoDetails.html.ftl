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
                    Dettagli operatore
                </h1>

                <a href="${contextPath}/ogm?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD DETTAGLI MEZZO ================= -->
                <div class="col-md-6">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Dettagli mezzo
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <#if mez??>

                                <p>
                                    <strong>Name:</strong> ${mez.tipologia}
                                </p>

                                <p>
                                    <strong>Seriale:</strong> ${mez.seriale}
                                </p>

                                <p>
                                    <strong>Status:</strong> ${mez.status}
                                </p>

                            <#else>

                                <p class="mb-0">
                                    Nessun mezzo trovato.
                                </p>

                            </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD DETTAGLI MEZZO =============== -->

                <!-- ================= CARD MISSIONE MEZZO ================= -->
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

                        <#if attivaMZ??>

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
                                            <td>${attivaMZ.obiettivo}</td>
                                            <td class="text-center">

                                                <a href="${contextPath}/mss?action=details&id=${attivaMZ.missioneId}"
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
                <!-- =============== FINE CARD MISSIONE MEZZO =============== -->

            </div>
            <!-- Fine row principale -->
            
            <hr class="my-4">
            
            <!-- Inizio riga secondaria -->
            <div class="row">
                
                <!-- ================= CARD STORICO MEZZO ================= -->
                <div class="col-md-6">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Storico delle missioni
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                        <#if storicoMZ?? && storicoMZ?size gt 0>

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
                                
                                	<#list storicoMZ as sm>

                                        <tr>
                                            <td>${sm.obiettivo}</td>
                                            <td class="text-center">

                                                <a href="${contextPath}/mss?action=details&id=${sm.missioneId}"
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
                                    Non è stato usato in nessuna missione
                                </div>

                  		  </#if>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD STORICO MEZZO =============== -->
            
            </div>
            <!-- fine riga secondaria -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

    
<#include "/layouts/admin_footer.ftl">