<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Jugador, models.Usuario" %>
<%
    Jugador jugador = (Jugador) request.getAttribute("jugador");
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

    String urlVolver = "inicioUsers";
    if (usuario != null && usuario.esAdministrador()) {
        urlVolver = "inicioAdmin";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil del Jugador</title>
    <!-- Bootstrap (opcional, lo dejo por los íconos/estilos base) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Usa este CSS que combina el de formularios + ajustes de perfil -->
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
<form class="login-form" action="verPerfilJugador" method="post">
    <h1 class="perfil-titulo"><i class="bi bi-person-circle"></i> Perfil del jugador</h1>
    <img src="https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg" alt="Foto de perfil" class="perfil-img">

    <!-- GRID: auto se adapta a 2-3 columnas según ancho -->
    <div class="form-grid">
        <!-- Cédula (solo lectura) -->
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
        <div class="form-input-material">
            <label for="genero">Género</label>
            <select id="genero" name="genero" class="editable-select" disabled>
                <option value="Masculino" <%= "Masculino".equals(jugador.getGenero()) ? "selected" : "" %>>Masculino</option>
                <option value="Femenino" <%= "Femenino".equals(jugador.getGenero()) ? "selected" : "" %>>Femenino</option>
                <option value="Otro" <%= "Otro".equals(jugador.getGenero()) ? "selected" : "" %>>Otro</option>
            </select>
        </div>
    </div>

    <div class="acciones">
        <button type="button" class="btn" onclick="habilitarEdicion()">
            <i class="bi bi-pencil-square"></i> Editar datos
        </button>
        <button type="submit" class="btn" id="guardarBtn" disabled>
            <i class="bi bi-save"></i> Guardar cambios
        </button>
    </div>

    <div class="volver-wrapper">
        <a href="<%= urlVolver %>" class="volver-link">
            <i class="bi bi-arrow-left-circle"></i> Volver al inicio
        </a>
    </div>
    </div>
</form>
</body>
</html>
