<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");

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
    <title>Perfil del Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="css/perfil.css">

    <script>
        function habilitarEdicion() {
            const inputs = document.querySelectorAll(".editable");
            inputs.forEach(input => input.removeAttribute("readonly"));
            document.getElementById("guardarBtn").removeAttribute("disabled");
        }

        document.addEventListener("DOMContentLoaded", function () {
            const imagen = document.getElementById("imagenPerfil");
            const bloque = document.getElementById("bloqueSubida");

            imagen.addEventListener("click", function () {
                bloque.style.display = bloque.style.display === "none" || bloque.style.display === "" ? "block" : "none";
            });
        });
    </script>
</head>
<body class="bg-light">

<%@include file="/WEB-INF/components/headerAdmin.jsp"%>

<div class="container py-5">
    <div class="card perfil-card mx-auto shadow-lg">
        <div class="card-body text-center">

            <% if (mensajeFoto != null) { %>
            <div class="alert alert-info text-center"><%= mensajeFoto %></div>
            <% } %>
            <% if (mensaje != null) { %>
            <div class="alert alert-success text-center"><%= mensaje %></div>
            <% } %>

            <!-- Formulario para subir nueva foto -->
            <form action="subirFotoPerfil" method="post" enctype="multipart/form-data" class="mb-4 text-center">
                <input type="hidden" name="cedula" value="<%= usuario.getCedula() %>">
                <img src="<%= fotoPerfil %>" alt="Foto de perfil" class="rounded-circle perfil-img mb-2" id="imagenPerfil" style="cursor: pointer;">
                <div id="mensajeActualizar" class="text-muted small">Haz clic en la imagen para actualizar</div>

                <div id="bloqueSubida" style="display: none;" class="mt-3">
                    <input type="file" name="fotoPerfil" id="fotoPerfil" class="form-control mt-2" accept="image/*" required>
                    <button type="submit" class="btn btn-primary mt-2">
                        <i class="bi bi-cloud-upload"></i> Subir nueva foto
                    </button>
                </div>
            </form>

            <h3 class="card-title mb-1"><i class="bi bi-person-circle"></i> <%= usuario.getNombre() %> <%= usuario.getApellido() %></h3>
            <p class="text-muted mb-3"><i class="bi bi-envelope"></i> <%= usuario.getCorreo() %></p>

            <!-- Formulario de edición -->
            <form action="verPerfilAdmin" method="post" class="text-start">
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-credit-card"></i> Cédula</label>
                    <input type="text" name="cedula" class="form-control editable" value="<%= usuario.getCedula() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Nombre</label>
                    <input type="text" name="nombre" class="form-control editable" value="<%= usuario.getNombre() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Apellido</label>
                    <input type="text" name="apellido" class="form-control editable" value="<%= usuario.getApellido() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-envelope"></i> Correo</label>
                    <input type="email" name="correo" class="form-control editable" value="<%= usuario.getCorreo() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-telephone"></i> Teléfono</label>
                    <input type="text" name="telefono" class="form-control editable" value="<%= usuario.getTelefono() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-calendar-check"></i> Fecha de ingreso</label>
                    <input type="date" name="fechaIngreso" class="form-control editable" value="<%= usuario.getFechaIngreso() %>" readonly>
                </div>

                <div class="d-flex justify-content-between mt-4">
                    <button type="button" class="btn btn-outline-secondary" onclick="habilitarEdicion()">
                        <i class="bi bi-pencil-square"></i> Editar datos
                    </button>
                    <button type="submit" class="btn btn-success" id="guardarBtn" disabled>
                        <i class="bi bi-save"></i> Guardar cambios
                    </button>
                </div>
            </form>

            <a href="inicioAdmin" class="btn btn-primary mt-4"><i class="bi bi-arrow-left-circle"></i> Volver al inicio</a>
        </div>
    </div>
</div>

</body>
</html>

