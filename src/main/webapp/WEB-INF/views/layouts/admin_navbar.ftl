       <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${contextPath}/dshb?action=home">
                <div class="sidebar-brand-icon">
                    <i class="fa-regular fa-hospital"></i>
                </div>
                <div class="sidebar-brand-text mx-3">Soccorso Web</div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="${contextPath}/dshb?action=home">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Principale
            </div>

            <!-- Nav Item - Richieste Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseRichieste"
                    aria-expanded="true" aria-controls="collapseRichieste">
                    <i class="fa-solid fa-question"></i>
                    <span>Richieste</span>
                </a>
                <div id="collapseRichieste" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/rqs?action=list">Lista</a>
                    </div>
                </div>
            </li>

            <!-- Nav Item - operatori Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseOperatori"
                    aria-expanded="true" aria-controls="collapseOperatori">
                    <i class="fa-solid fa-user"></i>
                    <span>Operatori</span>
                </a>
                <div id="collapseOperatori" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/opt?action=list">Lista</a>
                        <a class="collapse-item" href="${contextPath}/opt?action=insert">Aggiungi</a>
                    </div>
                </div>
            </li>

            <!-- Nav Item - admin Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseAmministratori"
                    aria-expanded="true" aria-controls="collapseAmministratori">
                    <i class="fa-solid fa-user"></i>
                    <span>Amministratori</span>
                </a>
                <div id="collapseAmministratori" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/opt?action=listAd">Lista</a>
                        <a class="collapse-item" href="${contextPath}/opt?action=insertAd">Aggiungi</a>
                    </div>
                </div>
            </li>
            
            <!-- Nav Item - Missioni Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseMissioni"
                    aria-expanded="true" aria-controls="collapseMissioni">
                    <i class="fa-solid fa-car"></i>
                    <span>Missioni</span>
                </a>
                <div id="collapseMissioni" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/mss?action=list">Lista</a>
                    </div>
                </div>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Utilities
            </div>

            <!-- Nav Item - Mezzi Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseMezzi"
                    aria-expanded="true" aria-controls="collapseMezzi">
                    <i class="fa-solid fa-car"></i>
                    <span>Mezzi</span>
                </a>
                <div id="collapseMezzi" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/ogm?action=list">Lista</a>
                        <a class="collapse-item" href="${contextPath}/ogm?action=insertMz">Aggiungi</a>
                    </div>
                </div>
            </li>

			<!-- Nav Item - Materiali Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseMateriali"
                    aria-expanded="true" aria-controls="collapseMateriali">
                    <i class="fa-solid fa-wrench"></i>
                    <span>Materiali</span>
                </a>
                <div id="collapseMateriali" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/ogm?action=list">Lista</a>
                        <a class="collapse-item" href="${contextPath}/ogm?action=insertMa">Aggiungi</a>
                    </div>
                </div>
            </li>
            
			<!-- Nav Item - Squadre Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseSquadre"
                    aria-expanded="true" aria-controls="collapseSquadre">
                    <i class="fa-solid fa-shield"></i>
                    <span>Squadre</span>
                </a>
                <div id="collapseSquadre" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Opzioni:</h6>
                        <a class="collapse-item" href="${contextPath}/sqd?action=list">Lista</a>
                        <a class="collapse-item" href="${contextPath}/sqd?action=insert">Aggiungi</a>
                    </div>
                </div>
            </li>

        </ul>
        <!-- End of Sidebar -->