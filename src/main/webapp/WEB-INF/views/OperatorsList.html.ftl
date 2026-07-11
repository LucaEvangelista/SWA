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
                    Lista operatori
                </h1>

            </div>


            <!-- ================= CARD LISTA OPERATORI ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Operatori registrati
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if opt?? && opt?size gt 0>
                    
                            <div class="row mb-3">
            					<div class="col-md-4">
                					<label for="statusFilter" class="form-label">Filtra per stato</label>

                					<select id="statusFilter" class="form-control">
                    		 			<option value="">Tutti</option>
                    		 			<option value="libero">Libero</option>
                   			 			<option value="occupato">Occupato</option>
                					</select>
           				 		</div>
        					</div>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="operatorsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th>Surname</th>
                                        <th>Email</th>
                                        <th>Status</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list opt as o>

                                        <tr>
                                            <td>${o.name}</td>
                                            <td>${o.surname}</td>
                                            <td>${o.email}</td>
                                            <td>${o.status}</td>

                                            <td class="text-center">

                                                <a href="${contextPath}/opt?action=details&id=${o.id}"
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
                            Nessun operatore trovato.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA OPERATORI =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
