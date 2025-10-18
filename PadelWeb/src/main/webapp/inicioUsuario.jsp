<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet"
          href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerUsuario.jsp"%>

<main class="hero text-center py-5 bg-light">
    <div class="hero-content container">
        <h2 class="tituloPrincipal mb-3">
            ¡Bienvenido <%= request.getAttribute("nombreUsuario") != null
                ? request.getAttribute("nombreUsuario")
                : "jugador" %>!
        </h2>
        <p class="lead mb-4">
            Consulta tus reservas, estadísticas personales y canchas disponibles.
        </p>
    </div>
</main>

<section class="container my-4">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-sm border-0 text-center p-4" style="border-radius: 1rem;">
                <div class="d-flex justify-content-center align-items-center mb-3">
                    <i class="fi fi-rr-calendar-check fs-2 text-primary me-2"></i>
                    <h4 class="mb-0">Tus estadísticas</h4>
                </div>
                <p class="fs-5 mb-1">Reservas activas:</p>
                <h3 class="fw-bold text-success">
                    <%= request.getAttribute("reservasActivas") != null
                            ? request.getAttribute("reservasActivas")
                            : 0 %>
                </h3>
            </div>
        </div>
    </div>
</section>

<section class="links container my-5 d-flex justify-content-center gap-4 flex-wrap">
    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-user fs-1 mb-3 text-primary"></i>
        <h3>Usuarios</h3>
        <p>Administra tu cuenta o actualiza tus datos personales.</p>
        <a href="<%= request.getContextPath() %>/verPerfilJugador" class="btn btn-outline-primary mt-2">Ir al panel</a>
    </div>

    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-basketball fs-1 mb-3 text-success"></i>
        <h3>Canchas</h3>
        <p>Consulta la disponibilidad de canchas y realiza reservas.</p>
        <a href="<%= request.getContextPath() %>/canchaUsuario" class="btn btn-outline-primary mt-2">Ir al panel</a>
    </div>
</section>

<%@include file="/WEB-INF/components/footer.jsp"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-czL6JuqcKJfSTNn2tPbdm1cA4y3Z7rU2q2GVxPzrcTtGkh3qHuvZ4z5gDMSlB/xK"
        crossorigin="anonymous"></script>

</body>
</html>

