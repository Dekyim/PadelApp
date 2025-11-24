<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PadelManager - Inicio</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="css/index.css" rel="stylesheet">
</head>
<body>

<%
    String mensajeError = (String) session.getAttribute("mensajeError");
    if (mensajeError != null) {
%>
<div id="mensajeErrorOverlay">
    <div class="mensajeErrorBox">
        <button class="cerrarError" onclick="document.getElementById('mensajeErrorOverlay').remove()">×</button>
        <%= mensajeError %>
    </div>
</div>
<%
        session.removeAttribute("mensajeError");
    }
%>

<%@include file="/WEB-INF/components/header.jsp"%>

<main class="hero text-center">
    <div class="hero-content container">
        <h2 class="tituloPrincipal mb-3">Bienvenido</h2>
        <p class="lead mb-4">
            Gestiona usuarios, canchas y más desde un solo lugar.<br>
            Rápido, moderno y fácil de usar.
        </p>
        <a href="login" class="btn-principal">Comenzar</a>
    </div>
</main>

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

<section id="faq">
    <h2 class="text-center mb-4">Preguntas Frecuentes</h2>

    <div class="faq-section">
        <div class="accordion" id="faqAccordion">

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading1">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse1" aria-expanded="false" aria-controls="collapse1">
                        ¿Cómo reservo una cancha?
                    </button>
                </h2>
                <div id="collapse1" class="accordion-collapse collapse" aria-labelledby="heading1"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        Puedes reservar una cancha desde el panel principal seleccionando fecha, hora y ubicación. Luego confirma tu reserva.
                    </div>
                </div>
            </div>

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading2">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse2" aria-expanded="false" aria-controls="collapse2">
                        ¿Puedo modificar o cancelar una reserva?
                    </button>
                </h2>
                <div id="collapse2" class="accordion-collapse collapse" aria-labelledby="heading2"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        Sí, desde tu perfil puedes editar o cancelar reservas activas siempre que sea con al menos 2 horas de anticipación.
                    </div>
                </div>
            </div>

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading3">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse3" aria-expanded="false" aria-controls="collapse3">
                        ¿Cómo puedo unirme o crear un grupo de juego?
                    </button>
                </h2>
                <div id="collapse3" class="accordion-collapse collapse" aria-labelledby="heading3"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        En la sección “Grupos” puedes buscar grupos existentes o crear uno nuevo invitando a otros jugadores.
                    </div>
                </div>
            </div>

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading4">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse4" aria-expanded="false" aria-controls="collapse4">
                        ¿Cómo actualizo mi información o foto de perfil?
                    </button>
                </h2>
                <div id="collapse4" class="accordion-collapse collapse" aria-labelledby="heading4"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        Accede a tu perfil y haz clic en “Editar”. Desde allí puedes modificar tus datos personales y subir una nueva foto.
                    </div>
                </div>
            </div>

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading5">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse5" aria-expanded="false" aria-controls="collapse5">
                        ¿Puedo filtrar las reservas o buscar canchas específicas?
                    </button>
                </h2>
                <div id="collapse5" class="accordion-collapse collapse" aria-labelledby="heading5"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        Sí, puedes usar los filtros por fecha, ubicación, tipo de cancha y disponibilidad para encontrar lo que necesitas.
                    </div>
                </div>
            </div>

            <div class="accordion-item">
                <h2 class="accordion-header" id="heading6">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapse6" aria-expanded="false" aria-controls="collapse6">
                        ¿Qué sucede si cierro sesión?
                    </button>
                </h2>
                <div id="collapse6" class="accordion-collapse collapse" aria-labelledby="heading6"
                     data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        Al cerrar sesión, se cerrará tu sesión activa y deberás iniciar sesión nuevamente para acceder a tus reservas y perfil.
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>

<!-- Opiniones -->
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

<!-- Bootstrap JS bundle (sin integrity para evitar bloqueo) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function cerrarMensajeError() {
        const mensaje = document.getElementById("mensajeError");
        if (mensaje) {
            mensaje.style.display = "none";
        }
    }
</script>

</body>
</html>
