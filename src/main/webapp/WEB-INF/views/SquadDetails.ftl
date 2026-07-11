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
                    <#if sqds??>
                        Membri della Squadra ${sqds.nome!""}
                    <#else>
                        Dettagli squadra
                    </#if>
                </h1>

                <a href="${contextPath}/sqd?action=list"
                   class="btn btn-primary btn-sm shadow-sm">
                    Return to list
                </a>

            </div>


            <#if sqds??>

                <!-- Riga principale -->
                <div class="row">

                    <!-- ================= CARD CAPISQUADRA ================= -->
                    <div class="col-lg-6">

                        <div class="card shadow mb-4">

                            <!-- Header card capisquadra -->
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">

                                <h6 class="m-0 font-weight-bold text-primary">
                                    Capisquadra
                                </h6>

                                <!-- Bottone apertura modal leader -->
                                <button type="button"
                                        class="btn btn-primary btn-sm"
                                        data-toggle="modal"
                                        data-target="#leaderModal">
                                    <i class="fas fa-plus"></i>
                                </button>

                            </div>

                            <!-- Body card capisquadra -->
                            <div class="card-body">

                                <#if leadApp?? && leadApp?size gt 0>

                                    <#list leadApp as o>

                                        <div class="d-flex align-items-center justify-content-between mb-2">

                                            <p class="mb-0">
                                                <strong>SquadLeader's Name:</strong> ${o.name!"-"}
                                            </p>

                                            <!-- Form rimozione leader -->
                                            <form action="${contextPath}/sqd"
                                                  method="post"
                                                  class="mb-0 ml-2">

                                                <input type="hidden"
                                                       name="action"
                                                       value="removeLeader">

                                                <input type="hidden"
                                                       name="leaderId"
                                                       value="${o.id}">

                                                <input type="hidden"
                                                       name="squadId"
                                                       value="${sqds.squadraId}">

                                                <button type="submit"
                                                        class="btn btn-danger btn-sm">
                                                    <i class="fas fa-trash"></i>
                                                </button>

                                            </form>

                                        </div>

                                    </#list>

                                <#else>

                                    <p class="mb-0">
                                        Non ci sono leader in questa squadra.
                                    </p>

                                </#if>

                            </div>

                        </div>

                    </div>
                    <!-- =============== FINE CARD CAPISQUADRA =============== -->


                    <!-- ================= CARD MEMBRI ================= -->
                    <div class="col-lg-6">

                        <div class="card shadow mb-4">

                            <!-- Header card membri -->
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">

                                <h6 class="m-0 font-weight-bold text-primary">
                                    Membri
                                </h6>

                                <!-- Bottone apertura modal membri -->
                                <button type="button"
                                        class="btn btn-primary btn-sm"
                                        data-toggle="modal"
                                        data-target="#operatorModal">
                                    <i class="fas fa-plus"></i>
                                </button>

                            </div>

                            <!-- Body card membri -->
                            <div class="card-body">

                                <#if opApp?? && opApp?size gt 0>

                                    <#list opApp as o>

                                        <div class="d-flex align-items-center justify-content-between mb-2">

                                            <p class="mb-0">
                                                <strong>Operator's Name:</strong> ${o.name!"-"}
                                            </p>

                                            <!-- Form rimozione operatore -->
                                            <form action="${contextPath}/sqd"
                                                  method="post"
                                                  class="mb-0 ml-2">

                                                <input type="hidden"
                                                       name="action"
                                                       value="removeOperator">

                                                <input type="hidden"
                                                       name="operatorId"
                                                       value="${o.id}">

                                                <input type="hidden"
                                                       name="squadId"
                                                       value="${sqds.squadraId}">

                                                <button type="submit"
                                                        class="btn btn-danger btn-sm">
                                                    <i class="fas fa-trash"></i>
                                                </button>

                                            </form>

                                        </div>

                                    </#list>

                                <#else>

                                    <p class="mb-0">
                                        Non ci sono membri appartenenti a questa squadra.
                                    </p>

                                </#if>

                            </div>

                        </div>

                    </div>
                    <!-- =============== FINE CARD MEMBRI =============== -->

                </div>
                <!-- Fine row principale -->

            <#else>

                <div class="card shadow mb-4">

                    <div class="card-body">
                        <p class="mb-0">
                            Nessuna squadra trovata.
                        </p>
                    </div>

                </div>

            </#if>

        </div>
        <!-- /.container-fluid -->
        <!-- =============== FINE CONTENUTO PAGINA =============== -->

    </div>
    <!-- End of Main Content -->


    <#if sqds??>

        <!-- ================= MODAL AGGIUNTA MEMBRI ================= -->
        <div class="modal fade"
             id="operatorModal"
             tabindex="-1"
             role="dialog"
             aria-labelledby="operatorModalLabel"
             aria-hidden="true">

            <div class="modal-dialog" role="document">

                <div class="modal-content">

                    <!-- Form aggiunta membri -->
                    <form action="${contextPath}/sqd"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="addMembri">

                        <input type="hidden"
                               name="squadId"
                               value="${sqds.squadraId}">

                        <!-- Header modal membri -->
                        <div class="modal-header">

                            <h5 class="modal-title"
                                id="operatorModalLabel">
                                Scegli i membri da aggiungere
                            </h5>

                            <button type="button"
                                    class="close"
                                    data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>

                        </div>

                        <!-- Body modal membri -->
                        <div class="modal-body">

                            <#if opNS?? && opNS?size gt 0>

                                <#list opNS as o>

                                    <div class="form-check mb-2">

                                        <input class="form-check-input"
                                               type="checkbox"
                                               id="operatore_${o.id}"
                                               name="operatore"
                                               value="${o.id}">

                                        <label class="form-check-label"
                                               for="operatore_${o.id}">
                                            ${o.name!"-"}
                                        </label>

                                    </div>

                                </#list>

                            <#else>

                                <p class="mb-0">
                                    Non ci sono membri disponibili da aggiungere.
                                </p>

                            </#if>

                        </div>

                        <!-- Footer modal membri -->
                        <div class="modal-footer">

                            <button type="button"
                                    class="btn btn-secondary"
                                    data-dismiss="modal">
                                Chiudi
                            </button>

                            <button type="submit"
                                    class="btn btn-primary">
                                Aggiungi
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </div>
        <!-- =============== FINE MODAL AGGIUNTA MEMBRI =============== -->


        <!-- ================= MODAL AGGIUNTA LEADER ================= -->
        <div class="modal fade"
             id="leaderModal"
             tabindex="-1"
             role="dialog"
             aria-labelledby="leaderModalLabel"
             aria-hidden="true">

            <div class="modal-dialog" role="document">

                <div class="modal-content">

                    <!-- Form aggiunta leader -->
                    <form action="${contextPath}/sqd"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="addLeader">

                        <input type="hidden"
                               name="squadId"
                               value="${sqds.squadraId}">

                        <!-- Header modal leader -->
                        <div class="modal-header">

                            <h5 class="modal-title"
                                id="leaderModalLabel">
                                Scegli i capisquadra da aggiungere
                            </h5>

                            <button type="button"
                                    class="close"
                                    data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>

                        </div>

                        <!-- Body modal leader -->
                        <div class="modal-body">

                            <#if opNS?? && opNS?size gt 0>

                                <#list opNS as o>

                                    <div class="form-check mb-2">

                                        <input class="form-check-input"
                                               type="checkbox"
                                               id="leader_${o.id}"
                                               name="operatore"
                                               value="${o.id}">

                                        <label class="form-check-label"
                                               for="leader_${o.id}">
                                            ${o.name!"-"}
                                        </label>

                                    </div>

                                </#list>

                            <#else>

                                <p class="mb-0">
                                    Non ci sono membri disponibili da aggiungere.
                                </p>

                            </#if>

                        </div>

                        <!-- Footer modal leader -->
                        <div class="modal-footer">

                            <button type="button"
                                    class="btn btn-secondary"
                                    data-dismiss="modal">
                                Chiudi
                            </button>

                            <button type="submit"
                                    class="btn btn-primary">
                                Aggiungi
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </div>
        <!-- =============== FINE MODAL AGGIUNTA LEADER =============== -->

    </#if>

<#include "/layouts/admin_footer.ftl">
