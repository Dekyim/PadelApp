<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Jugador, models.Usuario" %>
<%
    Jugador jugador = (Jugador) request.getAttribute("jugador");
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

    // Determinar URL de regreso según rol
    String urlVolver = "inicioUsers"; // valor por defecto
    if (usuario != null && usuario.esAdministrador()) {
        urlVolver = "inicioAdmin";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Perfil del Jugador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/perfilJugador.css">
    <script>
        function habilitarEdicion() {
            document.querySelectorAll(".editable").forEach(el => el.removeAttribute("readonly"));
            document.querySelectorAll(".editable-select").forEach(el => el.removeAttribute("disabled"));
            document.getElementById("guardarBtn").removeAttribute("disabled");
        }
    </script>
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="card perfil-card mx-auto shadow-lg">
        <div class="card-body text-center">
            <img src="${pageContext.request.contextPath}/img/perfil-default.png" alt="Foto de perfil" class="rounded-circle perfil-img mb-3">
            <h3 class="card-title mb-3"><i class="bi bi-person-circle"></i> Jugador</h3>

            <form action="verPerfilJugador" method="post" class="text-start">
                <!-- Cédula: no se modifica -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-credit-card"></i> Cédula</label>
                    <input type="text" name="cedula" class="form-control" value="<%= jugador.getCedula() %>" readonly>
                </div>

                <!-- Nombre -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Nombre</label>
                    <input type="text" name="nombre" class="form-control editable" value="<%= jugador.getNombre() %>" readonly>
                </div>

                <!-- Apellido -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Apellido</label>
                    <input type="text" name="apellido" class="form-control editable" value="<%= jugador.getApellido() %>" readonly>
                </div>

                <!-- Correo -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-envelope"></i> Correo</label>
                    <input type="email" name="correo" class="form-control editable" value="<%= jugador.getCorreo() %>" readonly>
                </div>

                <!-- Teléfono -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-telephone"></i> Teléfono</label>
                    <input type="text" name="telefono" class="form-control editable" value="<%= jugador.getTelefono() %>" readonly>
                </div>

                <!-- Fecha de nacimiento -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-calendar"></i> Fecha de nacimiento</label>
                    <input type="date" name="fechaNacimiento" class="form-control editable" value="<%= jugador.getFechaNacimiento() %>" readonly>
                </div>

                <!-- Categoría -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-award"></i> Categoría</label>
                    <select name="categoria" class="form-select editable-select" disabled required>
                        <option value="Primera categoría" <%= "Primera categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Primera categoría</option>
                        <option value="Segunda categoría" <%= "Segunda categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Segunda categoría</option>
                        <option value="Tercera categoría" <%= "Tercera categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Tercera categoría</option>
                        <option value="Cuarta categoría" <%= "Cuarta categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Cuarta categoría</option>
                        <option value="Quinta categoría" <%= "Quinta categoría".equals(jugador.getCategoria()) ? "selected" : "" %>>Quinta categoría</option>
                        <option value="Desconoce" <%= "Desconoce".equals(jugador.getCategoria()) ? "selected" : "" %>>Desconoce</option>
                    </select>
                </div>

                <!-- Género -->
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-gender-ambiguous"></i> Género</label>
                    <select name="genero" class="form-select editable-select" disabled>
                        <option value="Masculino" <%= jugador.getGenero().equals("Masculino") ? "selected" : "" %>>Masculino</option>
                        <option value="Femenino" <%= jugador.getGenero().equals("Femenino") ? "selected" : "" %>>Femenino</option>
                        <option value="Otro" <%= jugador.getGenero().equals("Otro") ? "selected" : "" %>>Otro</option>
                    </select>
                </div>

                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-outline-secondary" onclick="habilitarEdicion()">
                        <i class="bi bi-pencil-square"></i> Editar datos
                    </button>
                    <button type="submit" class="btn btn-success" id="guardarBtn" disabled>
                        <i class="bi bi-save"></i> Guardar cambios
                    </button>
                </div>
            </form>

            <!-- Botón de volver según rol -->
            <a href="<%= urlVolver %>" class="btn btn-primary mt-4">
                <i class="bi bi-arrow-left-circle"></i> Volver al inicio
            </a>

        </div>
    </div>
</div>
</body>
</html>
