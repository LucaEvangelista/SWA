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
                    Lista richieste
                </h1>

            </div>


            <!-- ================= CARD LISTA RICHIESTE ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Richieste di soccorso registrate
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if rqs?? && rqs?size gt 0>
                    
	                    <div class="row mb-3">
	    					<div class="col-md-4">
			        			<label for="faseFilter" class="form-label">Filtra per fase</label>
			
			        			<select id="faseFilter" class="form-control">
			           				<option value="">Tutte</option>
			            			<option value="attiva">Attiva</option>
			            			<option value="in esecuzione">In esecuzione</option>
			            			<option value="terminata">Terminata</option>
			            			<option value="rifiutata">Rifiutata</option>
			        			</select>
			    			</div>
						</div>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="requestsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Place</th>
                                        <th>Description</th>
                                        <th>Fase</th>
                                        <th>Ricezione</th>
                                        <th>Inizio lavorazione</th>
                                        <th>Chiusura</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list rqs as r>

                                        <tr>
                                            <td>${r.nomePersona!"-"}</td>
                                            <td>${r.mailPersona!"-"}</td>
                                            <td>${r.indirizzo!"-"}</td>
                                            <td>${r.descrizione!"-"}</td>
                                            <td>${r.fase!"-"}</td>
                                            <td>${r.createdAt?datetime("yyyy-MM-dd'T'HH:mm")?string("dd/MM/yyyy HH:mm")!"-"}</td>
                                            <td>
                                            	<#if r.workingAt??>
    												${r.workingAt?datetime("yyyy-MM-dd'T'HH:mm")?string("dd/MM/yyyy HH:mm")}
												<#else>
    												-
												</#if>
											</td>
                                            <td>
                                            	<#if r.closedAt??>
    												${r.closedAt?datetime("yyyy-MM-dd'T'HH:mm")?string("dd/MM/yyyy HH:mm")}
												<#else>
    												-
												</#if>
											</td>

                                            <td class="text-center">

                                                <a href="${contextPath}/rqs?action=details&id=${r.id}"
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

                        <p class="mb-0 text-center">
                            Nessuna richiesta trovata.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA RICHIESTE =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
