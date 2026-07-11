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
                    Crea nuova squadra
                </h1>

                <a href="${contextPath}/sqd?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD CREAZIONE SQUADRA ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Dati squadra
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <!-- Form creazione squadra -->
                            <form action="${contextPath}/sqd"
                                  method="post"
                                  id="inserimentoSquadra">

                                <!-- Nome squadra -->
                                <div class="form-group">

                                    <label for="squad-name">
                                        Name:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="squad-name"
                                           id="squad-name"
                                           required>

                                </div>

                                <!-- Pulsanti -->
                                <div class="d-flex justify-content-end mt-4">

                                    <a href="${contextPath}/sqd?action=list"
                                       class="btn btn-secondary mr-2">
                                        Annulla
                                    </a>

                                    <button type="submit"
                                            class="btn btn-primary">
                                        Salva squadra
                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD CREAZIONE SQUADRA =============== -->

            </div>

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
