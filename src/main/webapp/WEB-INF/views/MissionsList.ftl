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
                    Lista missioni
                </h1>

            </div>


            <!-- ================= CARD LISTA MISSIONI ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Missioni registrate
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if mis?? && mis?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="missionsTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Obiettivo</th>
                                        <th>Posizione</th>
                                        <th>Status</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list mis as m>

                                        <tr>
                                            <td>${m.obiettivo!"-"}</td>
                                            <td>${m.posizione!"-"}</td>
                                            <td>${m.status!"-"}</td>

                                            <td class="text-center">

                                                <a href="${contextPath}/mss?action=details&id=${m.missioneId}"
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
                            Nessuna missione trovata.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA MISSIONI =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
