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
                    Crea nuovo operatore
                </h1>

                <a href="${contextPath}/opt?action=list"
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
                                Inserisci dati operatore
                            </h6>

                        </div>

                        <!-- Body card -->
                        <div class="card-body">

                            <!-- Form creazione operatore -->
                            <form action="${contextPath}/opt"
                                  method="post"
                                  id="inserimentoOp">

                                <input type="hidden"
                                       name="action"
                                       value="insert">

                                <!-- Nome -->
                                <div class="form-group">

                                    <label for="operator-name">
                                        Name:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="operator-name"
                                           id="operator-name"
                                           required>

                                </div>

                                <!-- Cognome -->
                                <div class="form-group">

                                    <label for="operator-surname">
                                        Surname:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="operator-surname"
                                           id="operator-surname"
                                           required>

                                </div>

                                <!-- Email -->
                                <div class="form-group">

                                    <label for="operator-email">
                                        Email address:
                                    </label>

                                    <input type="email"
                                           class="form-control"
                                           name="operator-email"
                                           id="operator-email"
                                           required>

                                </div>
                                
                                <!-- Password temporanea -->
                                <div class="form-group">

                                    <label for="operator-passkey">
                                        Email address:
                                    </label>

                                    <input type="text"
                                           class="form-control"
                                           name="operator-passkey"
                                           placeholder="Inserisci una password temporanea"
                                           id="operator-passkey"
                                           required>

                                </div>

                                <!-- Data di nascita -->
                                <div class="form-group">

                                    <label for="operator-birthday">
                                        Birthday:
                                    </label>

                                    <input type="date"
                                           class="form-control"
                                           name="operator-birthday"
                                           id="operator-birthday"
                                           required>

                                </div>

                                <!-- Pulsanti -->
                                <div class="d-flex justify-content-end mt-4">

                                    <a href="${contextPath}/opt?action=list"
                                       class="btn btn-secondary mr-2">
                                        Annulla
                                    </a>

                                    <button type="submit"
                                            class="btn btn-primary">
                                        Salva operatore
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
