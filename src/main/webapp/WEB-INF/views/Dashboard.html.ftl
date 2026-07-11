<#include "/layouts/admin_header.ftl">
    <#include "/layouts/admin_navbar.ftl">

<!-- Content Wrapper -->
<div id="content-wrapper" class="d-flex flex-column">

    <!-- Main Content -->
    <div id="content">

        <#include "/layouts/admin_topbar.ftl">

        <!-- ================= CONTENUTO PAGINA ================= -->
        <div class="container-fluid">

            <!-- Page Heading -->
            <div class="d-sm-flex align-items-center justify-content-between mb-4">

                <h1 class="h3 mb-0 text-gray-800">
                    Dashboard
                </h1>


            </div>

            <!-- Content Row -->
            <div class="row">

                <!-- Richieste totali -->
                <div class="col-xl-3 col-md-6 mb-4">

                    <div class="card border-left-primary shadow h-100 py-2">

                        <div class="card-body">

                            <div class="row no-gutters align-items-center">

                                <div class="col mr-2">

                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                        Richieste totali
                                    </div>

                                    <div class="h5 mb-0 font-weight-bold text-gray-800">
                                        ${all?size!0}
                                    </div>

                                </div>

                                <div class="col-auto">
                                    <i class="fa-regular fa-hand fa-2x text-gray-300"></i>
                                </div>

                            </div>

                        </div>

                    </div>

                </div>

                <!-- Missioni attive -->
                <div class="col-xl-3 col-md-6 mb-4">

                    <div class="card border-left-success shadow h-100 py-2">

                        <div class="card-body">

                            <div class="row no-gutters align-items-center">

                                <div class="col mr-2">

                                    <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                        Missioni attive
                                    </div>

                                    <div class="h5 mb-0 font-weight-bold text-gray-800">
                                        ${attive?size!0}
                                    </div>

                                </div>

                                <div class="col-auto">
                                    <i class="fas fa-truck-medical fa-2x text-gray-300"></i>
                                </div>

                            </div>

                        </div>

                    </div>

                </div>

                <!-- Percentuale missioni completate -->
                <div class="col-xl-3 col-md-6 mb-4">

                    <div class="card border-left-info shadow h-100 py-2">

                        <div class="card-body">

                            <div class="row no-gutters align-items-center">

                                <div class="col mr-2">

                                    <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                        Missioni completate
                                    </div>

                                    <div class="row no-gutters align-items-center">

                                        <div class="col-auto">

                                            <div class="h5 mb-0 mr-3 font-weight-bold text-gray-800">
                                                ${percentualeTerminate!0}%
                                            </div>

                                        </div>

                                        <div class="col">

                                            <div class="progress progress-sm mr-2">

                                                <div class="progress-bar bg-info"
                                                     role="progressbar"
                                                     style="width: ${percentualeTerminate!0}%"
                                                     aria-valuenow="${percentualeTerminate!0}"
                                                     aria-valuemin="0"
                                                     aria-valuemax="100">
                                                </div>

                                            </div>

                                        </div>

                                    </div>

                                </div>

                                <div class="col-auto">
                                    <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                </div>

                            </div>

                        </div>

                    </div>

                </div>

                <!-- Richieste pendenti -->
                <div class="col-xl-3 col-md-6 mb-4">

                    <div class="card border-left-warning shadow h-100 py-2">

                        <div class="card-body">

                            <div class="row no-gutters align-items-center">

                                <div class="col mr-2">

                                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                        Richieste pendenti
                                    </div>

                                    <div class="h5 mb-0 font-weight-bold text-gray-800">
                                        ${pending?size!0}
                                    </div>

                                </div>

                                <div class="col-auto">
                                    <i class="fas fa-comments fa-2x text-gray-300"></i>
                                </div>

                            </div>

                        </div>

                    </div>

                </div>

            </div>
            <!-- Fine Content Row -->
            
            <hr class="my-4">
            
            <!-- Inizio Content Row -->
			<div class="row">
			<!-- ================= CARD LISTA RICHIESTE ================= -->
			<div class="col-12">
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Richieste di soccorso in esecuzione
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if inEsecuzione?? && inEsecuzione?size gt 0>
                    

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="requestsExecutionTable"
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
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list inEsecuzione as ie>

                                        <tr>
                                            <td>${ie.nomePersona!"-"}</td>
                                            <td>${ie.mailPersona!"-"}</td>
                                            <td>${ie.indirizzo!"-"}</td>
                                            <td>${ie.descrizione!"-"}</td>
                                            <td>${ie.fase!"-"}</td>
                                            <td>${ie.createdAt?datetime("yyyy-MM-dd'T'HH:mm")?string("dd/MM/yyyy HH:mm")!"-"}</td>
                                            <td>
                                            	<#if ie.workingAt??>
    												${ie.workingAt?datetime("yyyy-MM-dd'T'HH:mm")?string("dd/MM/yyyy HH:mm")}
												<#else>
    												-
												</#if>
											</td>


                                            <td class="text-center">

                                                <a href="${contextPath}/rqs?action=details&id=${ie.id}"
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
                            Nessuna richiesta attualmente in esecuzione.
                        </p>

                    </#if>

                </div>

            </div>
            </div>
            <!-- =============== FINE CARD LISTA RICHIESTE =============== -->
			</div>

        </div>
        <!-- /.container-fluid -->
        <!-- ================= FINE CONTENUTO PAGINA ================= -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">