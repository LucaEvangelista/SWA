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
                    Inserisci nuovo mezzo
                </h1>

                <a href="${contextPath}/ogm"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD CREAZIONE MEZZO ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Dati mezzo
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <!-- Form creazione mezzo -->
                            <form action="${contextPath}/ogm"
                                  method="post"
                                  id="inserimentoMezzo">

                                <input type="hidden"
                                       name="action"
                                       value="instMz">

                                <!-- Tipo mezzo -->
                                <div class="form-group">

                                    <label for="nome-mezzo">
                                        Tipo:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="nome-mezzo"
                                           id="nome-mezzo"
                                           required>

                                </div>

                                <!-- Seriale mezzo -->
                                <div class="form-group">

                                    <label for="seriale-mezzo">
                                        Seriale di cinque cifre:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="seriale-mezzo"
                                           id="seriale-mezzo"
                                           maxlength="5"
                                           required>

                                </div>

                                <!-- Pulsanti -->
                                <div class="d-flex justify-content-end mt-4">

                                    <a href="${contextPath}/ogm"
                                       class="btn btn-secondary mr-2">
                                        Annulla
                                    </a>

                                    <button type="submit"
                                            class="btn btn-primary">
                                        Salva mezzo
                                    </button>

                                </div>

                            </form>

                        </div>

                    </div>

                </div>
                <!-- =============== FINE CARD CREAZIONE MEZZO =============== -->

            </div>

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

<#include "/layouts/admin_footer.ftl">
