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
                        <a class="collapse-item" href="${contextPath}/opt?action=details&id=${userid}">Profilo</a>
                    </div>
                </div>
            </li>


            <!-- Divider -->
            <hr class="sidebar-divider">





        </ul>
        <!-- End of Sidebar -->