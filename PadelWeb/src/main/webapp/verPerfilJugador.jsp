<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Jugador, models.Usuario" %>

<%
    Jugador jugador = (Jugador) request.getAttribute("jugador");
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
    String urlVolver = "inicioUsers";
    if (usuario != null && usuario.esAdministrador()) {
        urlVolver = "inicioAdmin";
    }

    String fotoPerfil = (String) request.getAttribute("fotoPerfil");
    if (fotoPerfil == null || fotoPerfil.isEmpty()) {
        fotoPerfil = "https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg";
    }

    String mensajeFoto = (String) request.getAttribute("mensajeFoto");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil del Jugador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/perfilJugador.css">

    <script>
        function habilitarEdicion() {
            document.querySelectorAll(".editable").forEach(el => el.removeAttribute("readonly"));
            document.querySelectorAll(".editable-select").forEach(el => el.removeAttribute("disabled"));
            document.getElementById("guardarBtn").removeAttribute("disabled");
        }
    </script>
</head>
<body>
<% if (usuario != null && usuario.esAdministrador()) { %>
<%@include file="/WEB-INF/components/headerAdmin.jsp"%>
<% } else { %>
<%@include file="/WEB-INF/components/headerUsuario.jsp"%>
<% } %>



<div class="container mt-4">

    <% if (mensajeFoto != null) { %>
    <div class="alert alert-info text-center"><%= mensajeFoto %></div>
    <% } %>

    <% if (mensaje != null) { %>
    <div class="alert alert-success text-center"><%= mensaje %></div>
    <% } %>

    <!-- Formulario para subir nueva foto -->
    <form action="subirFotoPerfil" method="post" enctype="multipart/form-data" class="foto-perfil-form">
        <input type="hidden" name="cedula" value="<%= jugador.getCedula() %>">
        <h1 class="perfil-titulo"><i class="bi bi-person-circle"></i> Foto de perfil</h1>

        <div class="form-input-material text-center">
            <img src="<%= fotoPerfil %>" alt="Foto de perfil" class="perfil-img mb-2" id="imagenPerfil" style="cursor: pointer;">
            <div id="mensajeActualizar" class="text-muted small">Haz clic en la imagen para actualizar</div>

            <div id="bloqueSubida" style="display: none;" class="mt-3">
                <input type="file" name="fotoPerfil" id="fotoPerfil" class="form-control mt-2" accept="image/*" required>
                <button type="submit" class="btn btn-primary mt-2">
                    <i class="bi bi-cloud-upload"></i> Subir nueva foto
                </button>
            </div>
        </div>
    </form>


    <!-- Formulario para editar datos del jugador -->
    <form class="login-form" action="verPerfilJugador" method="post">
        <h2 class="perfil-titulo"><i class="bi bi-pencil-square"></i> Datos del jugador</h2>

        <div class="form-grid">
            <div class="form-input-material">
                <label for="cedula">Cédula</label>
                <input type="text" id="cedula" name="cedula" value="<%= jugador.getCedula() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="nombre">Nombre</label>
                <input class="editable" type="text" id="nombre" name="nombre" value="<%= jugador.getNombre() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="apellido">Apellido</label>
                <input class="editable" type="text" id="apellido" name="apellido" value="<%= jugador.getApellido() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="correo">Correo</label>
                <input class="editable" type="email" id="correo" name="correo" value="<%= jugador.getCorreo() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="telefono">Teléfono</label>
                <input class="editable" type="text" id="telefono" name="telefono" value="<%= jugador.getTelefono() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="fechaNacimiento">Fecha de nacimiento</label>
                <input class="editable" type="date" id="fechaNacimiento" name="fechaNacimiento" value="<%= jugador.getFechaNacimiento() %>" readonly>
            </div>

            <div class="form-input-material">
                <label for="categoria">Categoría</label>
                <select id="categoria" name="categoria" class="editable-select" disabled required>
                    <option value="Primera categoría" <%= "Primera categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Primera categoría</option>
                    <option value="Segunda categoría" <%= "Segunda categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Segunda categoría</option>
                    <option value="Tercera categoría" <%= "Tercera categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Tercera categoría</option>
                    <option value="Cuarta categoría" <%= "Cuarta categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Cuarta categoría</option>
                    <option value="Quinta categoría" <%= "Quinta categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Quinta categoría</option>
                    <option value="Desconoce" <%= "Desconoce".equals(jugador.getCategoria()) ? "selected" : "" %>>Desconoce</option>
                </select>
            </div>

            <div class="form-input-material">
                <label for="genero">Género</label>
                <select id="genero" name="genero" class="editable-select" disabled>
                    <option value="Masculino" <%= "Masculino".equals(jugador.getGenero()) ? "selected" : "" %>>Masculino</option>
                    <option value="Femenino" <%= "Femenino".equals(jugador.getGenero()) ? "selected" : "" %>>Femenino</option>
                    <option value="Otro" <%= "Otro".equals(jugador.getGenero()) ? "selected" : "" %>>Otro</option>
                </select>
            </div>
        </div>

        <div class="acciones mt-3">
            <button type="button" class="btn btn-secondary" onclick="habilitarEdicion()">
                <i class="bi bi-pencil"></i> Editar datos
            </button>
            <button type="submit" class="btn btn-success" id="guardarBtn" disabled>
                <i class="bi bi-save"></i> Guardar cambios
            </button>
        </div>

        <div class="volver-wrapper mt-4">
            <a href="<%= urlVolver %>" class="volver-link">
                <i class="bi bi-arrow-left-circle"></i> Volver al inicio
            </a>
        </div>
    </form>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const imagen = document.getElementById("imagenPerfil");
        const bloque = document.getElementById("bloqueSubida");

        imagen.addEventListener("click", function () {
            bloque.style.display = bloque.style.display === "none" || bloque.style.display === "" ? "block" : "none";
        });
    });
</script>



</body>
</html>