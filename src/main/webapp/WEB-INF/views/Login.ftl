<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <title>Login - Soccorso Web</title>

    <link href="${contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
    <link href="${contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
</head>

<body class="bg-gradient-primary">

    <div class="container">

        <div class="row justify-content-center">

            <div class="col-xl-5 col-lg-6 col-md-8">

                <div class="card o-hidden border-0 shadow-lg my-5">

                    <div class="card-body p-5">

                        <div class="text-center">
                            <h1 class="h4 text-gray-900 mb-4">
                                Login Soccorso Web
                            </h1>
                        </div>

                        <#if error??>
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </#if>

                        <form class="user"
                              action="${contextPath}/auth"
                              method="post">

                            <input type="hidden"
                                   name="action"
                                   value="login">

                            <div class="form-group">
                                <input type="email"
                                       class="form-control form-control-user"
                                       name="email"
                                       placeholder="Inserisci la tua email"
                                       required>
                            </div>

                            <div class="form-group">
                                <input type="password"
                                       class="form-control form-control-user"
                                       name="passkey"
                                       placeholder="Inserisci la passkey"
                                       required>
                            </div>

                            <button type="submit"
                                    class="btn btn-primary btn-user btn-block">
                                Accedi
                            </button>

                        </form>

                        <hr>

                        <div class="text-center">
                            <a class="small" href="${contextPath}/prqs?action=insert">
                                Torna all'inserimento
                            </a>
                        </div>

                    </div>

                </div>

            </div>

        </div>

    </div>

    <script src="${contextPath}/vendor/jquery/jquery.min.js"></script>
    <script src="${contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="${contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>
    <script src="${contextPath}/js/sb-admin-2.min.js"></script>

</body>

</html>