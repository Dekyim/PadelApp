<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>PadelManager - Inicio</title>

    <!-- Bootstrap 5.3.3 sin integrity (para evitar errores de hash) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Iconos -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Fuente -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Tu CSS -->
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
        <a href="login" class="btn btn-primary btn-lg">Comenzar</a>
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

<!-- Preguntas frecuentes -->
<section id="faq" class="py-5 ">
    <h2 class="text-center mb-4">Preguntas Frecuentes</h2>

    <div class="accordion" id="faqAccordion">

        <!-- CANCHAS -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq1">
                <button class="accordion-button" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse1" aria-expanded="true" aria-controls="collapse1">
                    ¿Cómo reservo una cancha?
                </button>
            </h2>
            <div id="collapse1" class="accordion-collapse collapse show" aria-labelledby="faq1" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Inicia sesión, accede a la sección <strong>"Canchas"</strong>, selecciona la cancha que prefieras y completa el formulario.
                </div>
            </div>
        </div>

        <!-- RESERVAS -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq2">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse2" aria-expanded="false" aria-controls="collapse2">
                    ¿Puedo modificar o cancelar una reserva?
                </button>
            </h2>
            <div id="collapse2" class="accordion-collapse collapse" aria-labelledby="faq2" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Sí. En la sección <strong>"Reservas"</strong> puedes ver todas tus reservas y, desde allí, actualizarlas o eliminarlas fácilmente según tus necesidades.
                </div>
            </div>
        </div>

        <!-- GRUPOS -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq3">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse3" aria-expanded="false" aria-controls="collapse3">
                    ¿Cómo puedo unirme o crear un grupo de juego?
                </button>
            </h2>
            <div id="collapse3" class="accordion-collapse collapse" aria-labelledby="faq3" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Ingresa a la sección <strong>"Grupos"</strong> para ver los grupos disponibles y unirte a uno. Si lo prefieres, también puedes crear tu propio grupo para organizar partidos con tus amigos.
                </div>
            </div>
        </div>

        <!-- PERFIL -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq4">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse4" aria-expanded="false" aria-controls="collapse4">
                    ¿Cómo actualizo mi información o foto de perfil?
                </button>
            </h2>
            <div id="collapse4" class="accordion-collapse collapse" aria-labelledby="faq4" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Dirígete a <strong>"Ver Perfil"</strong> para visualizar tus datos personales. Desde allí puedes actualizar tu información y cargar una nueva foto de perfil.
                </div>
            </div>
        </div>

        <!-- USABILIDAD GENERAL -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq5">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse5" aria-expanded="false" aria-controls="collapse5">
                    ¿Puedo filtrar las reservas o buscar canchas específicas?
                </button>
            </h2>
            <div id="collapse5" class="accordion-collapse collapse" aria-labelledby="faq5" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Sí, puedes utilizar los filtros de búsqueda disponibles en las seccion <strong>"Reservas"</strong> para encontrar exactamente lo que necesitas según fecha, cancha o estado de reserva.
                </div>
            </div>
        </div>

        <!-- SESIÓN -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq6">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse6" aria-expanded="false" aria-controls="collapse6">
                    ¿Qué sucede al cerrar sesión?
                </button>
            </h2>
            <div id="collapse6" class="accordion-collapse collapse" aria-labelledby="faq6" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Al cerrar sesión, tu cuenta se desconecta de forma segura del sistema y se protege tu información personal. Puedes volver a iniciar sesión cuando lo desees.
                </div>
            </div>
        </div>

        <!-- GENERAL -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="faq7">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#collapse7" aria-expanded="false" aria-controls="collapse7">
                    ¿PadelManager es gratuito?
                </button>
            </h2>
            <div id="collapse7" class="accordion-collapse collapse" aria-labelledby="faq7" data-bs-parent="#faqAccordion">
                <div class="accordion-body">
                    Sí, la versión básica de PadelManager es totalmente gratuita. Algunas funciones avanzadas o personalizadas podrían requerir una suscripción en el futuro.
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
