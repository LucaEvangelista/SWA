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
                    Lista oggetti missione
                </h1>

            </div>


            <!-- ================= CARD LISTA MEZZI ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Mezzi registrati
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if mez?? && mez?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="mezziTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th>Seriale</th>
                                        <th>Status</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list mez as mz>

                                        <tr>
                                            <td>${mz.tipologia!"-"}</td>
                                            <td>${mz.seriale!"-"}</td>
                                            <td>${mz.status!"-"}</td>

                                            <td class="text-center">

                                                <a href="${contextPath}/ogm?action=detailsMz&id=${mz.id}"
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
                            Nessun mezzo trovato.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA MEZZI =============== -->


            <!-- ================= CARD LISTA MATERIALI ================= -->
            <div class="card shadow mb-4">

                <!-- Header card -->
                <div class="card-header py-3">

                    <h6 class="m-0 font-weight-bold text-primary">
                        Materiali registrati
                    </h6>

                </div>

                <!-- Body card -->
                <div class="card-body">

                    <#if mat?? && mat?size gt 0>

                        <div class="table-responsive">

                            <table class="table table-bordered table-striped"
                                   id="materialiTable"
                                   width="100%"
                                   cellspacing="0">

                                <thead>

                                    <tr>
                                        <th>Name</th>
                                        <th>Seriale</th>
                                        <th>Status</th>
                                        <th class="text-center">Details</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    <#list mat as m>

                                        <tr>
                                            <td>${m.tipologia!"-"}</td>
                                            <td>${m.seriale!"-"}</td>
                                            <td>${m.status!"-"}</td>
                                            
                                            <td class="text-center">

                                                <a href="${contextPath}/ogm?action=detailsMa&id=${m.id}"
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
                            Nessun materiale trovato.
                        </p>

                    </#if>

                </div>

            </div>
            <!-- =============== FINE CARD LISTA MATERIALI =============== -->

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
