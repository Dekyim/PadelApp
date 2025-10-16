<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/login.css">
    <title>Login</title>
</head>
<body>
<form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
    <h1>Login</h1>
    <img src="${pageContext.request.contextPath}/img/IconoPadel.png" alt="iconoPadel">

    <div class="form-input-material">
        <input type="text" name="cedula" id="cedula" placeholder="Cédula" autocomplete="off" required />
    </div>

    <div class="form-input-material">
        <input type="password" name="contrasenia" id="contrasenia" placeholder="Contraseña" autocomplete="off" required />
    </div>

    <button type="submit" class="btn btn-primary btn-ghost">Enviar</button>

    <p style="color:red">${errorLogin}</p>

    <p>¿No tienes cuenta?
        <a href="${pageContext.request.contextPath}/registro.jsp">Crear cuenta nueva</a>
    </p>
</form>
</body>
</html>
