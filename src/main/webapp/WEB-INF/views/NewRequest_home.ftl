<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Soccorso Web - Richiesta di soccorso</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          crossorigin="anonymous">

    <style>
        body {
            min-height: 100vh;
            background-color: #f8f9fa;
        }

        .main-section {
            min-height: calc(100vh - 72px);
        }

        .vertical-divider {
            border-left: 1px solid #dee2e6;
        }

        @media (max-width: 767.98px) {
            .vertical-divider {
                border-left: 0;
                border-top: 1px solid #dee2e6;
                margin-top: 2rem;
                padding-top: 2rem;
            }
        }
    </style>
</head>

<body>

<!-- ================= NAVBAR ================= -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container">

        <!-- Brand -->
        <a class="navbar-brand fw-bold text-primary"
           href="${contextPath}/prqs?action=insert">
            Soccorso Web
        </a>

        <!-- Bottone responsive -->
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarContent"
                aria-controls="navbarContent"
                aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Contenuto navbar -->
        <div class="collapse navbar-collapse" id="navbarContent">

            <!-- Bottone login -->
            <div class="ms-auto">
                <a href="${contextPath}/auth?action=login"
                   class="btn btn-outline-primary">
                    Login
                </a>
            </div>

        </div>

    </div>
</nav>
<!-- =============== FINE NAVBAR =============== -->


<!-- ================= CONTENUTO PRINCIPALE ================= -->
<main class="main-section d-flex align-items-center py-5">
    <div class="container">

        <div class="row align-items-center justify-content-center">

            <!-- ================= DESCRIZIONE SITO ================= -->
            <div class="col-md-6 pe-md-5">

                <h1 class="display-5 fw-bold text-primary mb-4">
                    Soccorso Web
                </h1>

                <p class="lead text-dark mb-4">
					<#include "/texts/descrizione_sito1.txt">
                </p>

                <p class="text-muted mb-0">
					<#include "/texts/descrizione_sito2.txt">
                </p>

            </div>
            <!-- =============== FINE DESCRIZIONE SITO =============== -->


            <!-- ================= FORM RICHIESTA ================= -->
            <div class="col-md-6 ps-md-5 vertical-divider">

                <div class="card shadow border-0">
                    <div class="card-body p-4">

                        <h2 class="h4 fw-bold text-primary mb-4">
                            Invia una richiesta di soccorso
                        </h2>
                        
                        <#if error??>
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </#if>
                        
                        <#if success??>
                            <div class="alert alert-success">
                                ${success}
                            </div>
                        </#if>

                        <form action="${contextPath}/prqs"
                              method="post"
                              id="inserimento">
                              
                        <input type="hidden"
                               name="action"
                               value="insert">

                            <!-- Nome completo -->
                            <div class="mb-3">
                                <label for="request-name" class="form-label">
                                    Nome completo
                                </label>

                                <input type="text"
                                       class="form-control"
                                       name="request-name"
                                       id="request-name"
                                       required>
                            </div>

                            <!-- Email -->
                            <div class="mb-3">
                                <label for="request-email" class="form-label">
                                    Email
                                </label>

                                <input type="email"
                                       class="form-control"
                                       name="request-email"
                                       id="request-email"
                                       required>
                            </div>

                            <!-- Indirizzo -->
                            <div class="mb-3">
                                <label for="request-address" class="form-label">
                                    Indirizzo
                                </label>

                                <input type="text"
                                       class="form-control"
                                       name="request-address"
                                       id="request-address"
                                       required>
                            </div>

                            <!-- Descrizione -->
                            <div class="mb-4">
                                <label for="request-description" class="form-label">
                                    Descrizione del problema
                                </label>

                                <textarea class="form-control"
                                          name="request-description"
                                          id="request-description"
                                          rows="4"
                                          required></textarea>
                            </div>

                            <!-- Submit -->
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">
                                    Invia richiesta
                                </button>
                            </div>

                        </form>

                    </div>
                </div>

            </div>
            <!-- =============== FINE FORM RICHIESTA =============== -->

        </div>

    </div>
</main>
<!-- =============== FINE CONTENUTO PRINCIPALE =============== -->


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous">
</script>

</body>
</html>
