<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <title>Registro</title>
</head>
<body>
<form class="login-form" action="${pageContext.request.contextPath}/agregarUsuario" method="post">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

    <h1>Registro</h1>
    <img src="${pageContext.request.contextPath}/img/IconoPadel.png" alt="iconoPadel">


    <div class="form-grid">
        <div>
            <div class="form-input-material">
                <input type="text" name="nombre" id="nombre" placeholder="Nombre" required>
            </div>
            <div class="form-input-material">
                <input type="text" name="apellido" id="apellido" placeholder="Apellido" required>
            </div>
            <div class="form-input-material">
                <input type="text" name="cedula" id="cedula" placeholder="Cédula" required>
            </div>
        </div>

        <div>
            <div class="form-input-material">
                <input type="tel" name="telefono" id="telefono" placeholder="Teléfono" required>
            </div>
            <div class="form-input-material">
                <input type="email" name="correo" id="correo" placeholder="Correo electrónico" required>
            </div>
            <div class="form-input-material">
                <input type="password" name="contrasenia" id="contrasenia" placeholder="Contraseña" required>
            </div>
        </div>

        <div>
            <div class="form-input-material">
                <input type="date" name="fechaNacimiento" id="fechaNacimiento" required>
            </div>
            <div class="form-input-material">
                <label for="categoria">Categoría</label>
                <input list="categoria" name="categoria" id="categoriaInput" required>
                <datalist id="categoria">
                    <option value="Primera categoría">
                    <option value="Segunda categoría">
                    <option value="Tercera categoría">
                    <option value="Cuarta categoría">
                    <option value="Quinta categoría">
                    <option value="Desconoce">
                </datalist>
            </div>
            <div class="form-input-material">
                <label for="genero">Género</label>
                <input list="genero" name="genero" id="generoInput" required>
                <datalist id="genero">
                    <option value="Femenino">
                    <option value="Masculino">
                </datalist>
            </div>
        </div>
    </div>

    <% if (request.getAttribute("errorRegistro") != null) { %>
    <div class="mensaje-error"><%= request.getAttribute("errorRegistro") %></div>
    <% } else if (request.getAttribute("mensajeExito") != null) { %>
    <div class="mensaje-exito"><%= request.getAttribute("mensajeExito") %></div>
    <% } %>

    <button type="submit" class="btn btn-primary btn-ghost">Registrar</button>


    <p>
        <a href="${pageContext.request.contextPath}/users">REGRESAR</a>
    </p>

</form>
</body>
</html>