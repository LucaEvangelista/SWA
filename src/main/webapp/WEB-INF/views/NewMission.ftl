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
                    Crea nuova missione
                </h1>

                <a href="${contextPath}/rqs?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <#if rqst??>

                <!-- Form creazione missione -->
                <form action="${contextPath}/mss"
                      method="post"
                      id="inserimentoMissione">

                    <input type="hidden"
                           name="action"
                           value="saveMission">

                    <input type="hidden"
                           name="richiestaId"
                           value="${rqst.id}">

                    <input type="hidden"
                           name="obiettivo"
                           value="${rqst.descrizione!''}">

                    <input type="hidden"
                           name="posizione"
                           value="${rqst.indirizzo!''}">


                    <!-- Riga informazioni richiesta -->
                    <div class="row">

                        <!-- ================= CARD DATI RICHIESTA ================= -->
                        <div class="col-lg-8 col-md-10">

                            <div class="card shadow mb-4">

                                <!-- Header card -->
                                <div class="card-header py-3">

                                    <h6 class="m-0 font-weight-bold text-primary">
                                        Dati richiesta
                                    </h6>

                                </div>

                                <!-- Body card -->
                                <div class="card-body">

                                    <div class="mb-3">

                                        <p class="mb-2">
                                            <strong>Obiettivo missione:</strong>
                                            ${rqst.descrizione!"-"}
                                        </p>

                                        <p class="mb-0">
                                            <strong>Posizione missione:</strong>
                                            ${rqst.indirizzo!"-"}
                                        </p>

                                    </div>

                                </div>
                                <!-- Fine body card -->

                            </div>
                            <!-- Fine card -->

                        </div>
                        <!-- =============== FINE CARD DATI RICHIESTA =============== -->

                    </div>
                    <!-- Fine row -->


                    <!-- Riga assegnazioni -->
                    <div class="row">

                        <!-- ================= CARD SQUADRA ================= -->
                        <div class="col-lg-8 col-md-10">

                            <div class="card shadow mb-4">

                                <!-- Header card -->
                                <div class="card-header py-3">

                                    <h6 class="m-0 font-weight-bold text-primary">
                                        Squadra da assegnare
                                    </h6>

                                </div>

                                <!-- Body card -->
                                <div class="card-body">

                                    <div class="form-group">

                                        <label for="squadraId">
                                            Squadra:
                                        </label>

                                        <select name="squadraId"
                                                id="squadraId"
                                                class="form-control"
                                                required>

                                            <option value="">
                                                -- Seleziona una squadra --
                                            </option>

                                            <#if squadre?? && squadre?size gt 0>
                                                <#list squadre as sq>
                                                    <option value="${sq.squadraId}">
                                                        ${sq.nome!"-"}
                                                    </option>
                                                </#list>
                                            </#if>

                                        </select>

                                    </div>

                                </div>
                                <!-- Fine body card -->

                            </div>
                            <!-- Fine card -->

                        </div>
                        <!-- =============== FINE CARD SQUADRA =============== -->

                    </div>
                    <!-- Fine row -->


                    <!-- Riga mezzi/materiali -->
                    <div class="row">

                        <!-- ================= CARD MEZZI ================= -->
                        <div class="col-lg-6">

                            <div class="card shadow mb-4">

                                <!-- Header card -->
                                <div class="card-header py-3">

                                    <h6 class="m-0 font-weight-bold text-primary">
                                        Mezzi da assegnare
                                    </h6>

                                </div>

                                <!-- Body card -->
                                <div class="card-body">

                                    <#if mezzi?? && mezzi?size gt 0>

                                        <#list mezzi as mz>

                                            <div class="form-check mb-2">

                                                <input class="form-check-input"
                                                       type="checkbox"
                                                       name="mezzi"
                                                       value="${mz.id}"
                                                       id="mezzo_${mz.id}">

                                                <label class="form-check-label"
                                                       for="mezzo_${mz.id}">
                                                    ${mz.tipologia!"-"}
                                                    <#if mz.seriale??>
                                                        <span class="text-muted">
                                                            (${mz.seriale})
                                                        </span>
                                                    </#if>
                                                </label>

                                            </div>

                                        </#list>

                                    <#else>

                                        <p class="text-muted mb-0">
                                            Nessun mezzo disponibile.
                                        </p>

                                    </#if>

                                </div>
                                <!-- Fine body card -->

                            </div>
                            <!-- Fine card -->

                        </div>
                        <!-- =============== FINE CARD MEZZI =============== -->


                        <!-- ================= CARD MATERIALI ================= -->
                        <div class="col-lg-6">

                            <div class="card shadow mb-4">

                                <!-- Header card -->
                                <div class="card-header py-3">

                                    <h6 class="m-0 font-weight-bold text-primary">
                                        Materiali da assegnare
                                    </h6>

                                </div>

                                <!-- Body card -->
                                <div class="card-body">

                                    <#if materiali?? && materiali?size gt 0>

                                        <#list materiali as mt>

                                            <div class="form-check mb-2">

                                                <input class="form-check-input"
                                                       type="checkbox"
                                                       name="materiali"
                                                       value="${mt.id}"
                                                       id="materiale_${mt.id}">

                                                <label class="form-check-label"
                                                       for="materiale_${mt.id}">
                                                    ${mt.tipologia!"-"}
                                                    <#if mt.seriale??>
                                                        <span class="text-muted">
                                                            (${mt.seriale})
                                                        </span>
                                                    </#if>
                                                </label>

                                            </div>

                                        </#list>

                                    <#else>

                                        <p class="text-muted mb-0">
                                            Nessun materiale disponibile.
                                        </p>

                                    </#if>

                                </div>
                                <!-- Fine body card -->

                            </div>
                            <!-- Fine card -->

                        </div>
                        <!-- =============== FINE CARD MATERIALI =============== -->

                    </div>
                    <!-- Fine row -->


                    <!-- Pulsanti -->
                    <div class="d-flex justify-content-end mb-4">

                        <a href="${contextPath}/rqs?action=list"
                           class="btn btn-secondary mr-2">
                            Annulla
                        </a>

                        <button type="submit"
                                class="btn btn-primary">
                            Crea missione
                        </button>

                    </div>

                </form>

            <#else>

                <div class="alert alert-warning">
                    Nessuna richiesta selezionata per la creazione della missione.
                </div>

                <a href="${contextPath}/rqs?action=list"
                   class="btn btn-primary">
                    Torna alla lista richieste
                </a>

            </#if>

        </div>
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->

    <#include "/layouts/admin_footer.ftl">

</div>
<!-- End of Content Wrapper -->
