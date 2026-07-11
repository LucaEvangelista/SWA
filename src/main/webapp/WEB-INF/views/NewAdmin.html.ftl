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
                    Crea nuovo amministratore
                </h1>

                <a href="${contextPath}/opt?action=listAd"
                   class="btn btn-primary btn-sm shadow-sm">
                    Vai alla lista
                </a>

            </div>


            <!-- Riga principale -->
            <div class="row">

                <!-- ================= CARD CREAZIONE OPERATORE ================= -->
                <div class="col-lg-8 col-md-10">

                    <div class="card shadow mb-4">

                        <!-- Header card -->
                        <div class="card-header py-3">

                            <h6 class="m-0 font-weight-bold text-primary">
                                Inserisci dati amministratore
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <!-- Form creazione operatore -->
                            <form action="${contextPath}/opt"
                                  method="post"
                                  id="inserimentoAd">

                                <input type="hidden"
                                       name="action"
                                       value="insertA">

                                <!-- Nome -->
                                <div class="form-group">

                                    <label for="admin-name">
                                        Name:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="admin-name"
                                           id="admin-name"
                                           required>

                                </div>

                                <!-- Email -->
                                <div class="form-group">

                                    <label for="admin-email">
                                        Email address:
                                    </label>

                                    <input type="email"
                                           class="form-control"
                                           name="admin-email"
                                           id="admin-email"
                                           required>

                                </div>

                                <!-- Password temporanea -->
                                <div class="form-group">

                                    <label for="admin-passkey">
                                        Password temporanea:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="admin-passkey"
                                           placeholder="Inserisci una password temporanea"
                                           id="admin-passkey"
                                           required>

                                </div>

                                <!-- Pulsanti -->
                                <div class="d-flex justify-content-end mt-4">

                                    <a href="${contextPath}/opt?action=listAd"
                                       class="btn btn-secondary mr-2">
                                        Annulla
                                    </a>

                                    <button type="submit"
                                            class="btn btn-primary">
                                        Salva amministratore
                                    </button>

                                </div>

                            </form>

                        </div>
                        <!-- Fine body card -->

                    </div>
                    <!-- Fine card -->

                </div>
                <!-- =============== FINE CARD CREAZIONE OPERATORE =============== -->

            </div>
            <!-- Fine row -->

        </div>
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

    <#include "/layouts/admin_footer.ftl">

</div>
<!-- End of Content Wrapper -->
