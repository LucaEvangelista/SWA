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
                    Lista amministratori
                </h1>

            </div>


            <!-- ================= CARD LISTA AMMINISTRATORI ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Amministratori registrati
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if adm?? && adm?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="operatorsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th>Email</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list adm as a>

                                        <tr>
                                            <td>${a.name}</td>
                                            <td>${a.email}</td>
                                        </tr>

                                    </#list>

                                </tbody>

                            </table>

                        </div>

                    <#else>

                        <p class="mb-0 text-center">
                            Nessun amministratore trovato.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA AMMINISTRATORI =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
