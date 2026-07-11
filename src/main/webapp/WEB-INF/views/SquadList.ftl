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
                    Lista squadre
                </h1>

                <a href="${contextPath}/sqd?action=insert"
                   class="btn btn-primary btn-sm shadow-sm">
                    <i class="fas fa-plus fa-sm text-white-50 mr-1"></i>
                    Nuova squadra
                </a>

            </div>


            <!-- ================= CARD LISTA SQUADRE ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Squadre registrate
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if sqds?? && sqds?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="squadsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list sqds as s>

                                        <tr>
                                            <td>${s.nome!"-"}</td>

                                            <td class="text-center">

                                                <a href="${contextPath}/sqd?action=details&id=${s.squadraId}"
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
                            Nessuna squadra trovata.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA SQUADRE =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
