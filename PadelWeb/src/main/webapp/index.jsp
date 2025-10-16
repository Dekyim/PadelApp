<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - PadelManager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet"
          href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<%@include file="/WEB-INF/components/header.jsp"%>

<main class="hero text-center py-5">
    <div class="hero-content container">
        <h2 class="tituloPrincipal mb-3">Bienvenido</h2>
        <p class="lead mb-4">
            Gestiona usuarios, canchas y más desde un solo lugar.<br>
            Rápido, moderno y fácil de usar.
        </p>
        <a href="login" class="btn btn-primary btn-lg">Comenzar</a>
    </div>
</main>

<!-- Sección de estadísticas generales -->
<section class="links container my-5 d-flex justify-content-center gap-4 flex-wrap">
    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-chart-histogram fs-1 mb-3"></i>
        <h3>Reservas históricas</h3>
        <p>Más de <strong><%= request.getAttribute("totalReservas") %></strong> reservas realizadas desde el inicio de la plataforma.</p>
    </div>

    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-basketball fs-1 mb-3"></i>
        <h3>Canchas registradas</h3>
        <p>Actualmente contamos con <strong><%= request.getAttribute("totalCanchas") %></strong> canchas activas en el sistema.</p>
    </div>

    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-users fs-1 mb-3"></i>
        <h3>Usuarios activos</h3>
        <p>Hay <strong><%= request.getAttribute("totalUsuarios") %></strong> jugadores disfrutando del sistema PadelManager.</p>
    </div>
</section>

<!-- Sección de comentarios -->
<section class="container my-5">
    <h3 class="text-center mb-4">Opiniones de la comunidad</h3>
    <div class="row justify-content-center">
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm h-100">
                <p>“PadelManager me facilita muchísimo reservar y seguir mis partidos.”</p>
                <h6 class="text-muted mt-2">— Lucía Fernández</h6>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm h-100">
                <p>“Excelente diseño, rápido y muy intuitivo. Ideal para clubes.”</p>
                <h6 class="text-muted mt-2">— Martín Gómez</h6>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card p-3 shadow-sm h-100">
                <p>“Me encanta que puedo ver mi historial de reservas y torneos fácilmente.”</p>
                <h6 class="text-muted mt-2">— Sofía Pérez</h6>
            </div>
        </div>
    </div>

    <div class="text-center mt-4">
        <a href="https://forms.gle/Jpg35JtpTGshTPkD8" target="_blank" class="btn-link">
            Dejar mi comentario
        </a>
    </div>
</section>

<%@include file="/WEB-INF/components/footer.jsp"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-czL6JuqcKJfSTNn2tPbdm1cA4y3Z7rU2q2GVxPzrcTtGkh3qHuvZ4z5gDMSlB/xK"
        crossorigin="anonymous"></script>

</body>
</html>

