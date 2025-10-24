<%@ page import="java.util.Vector" %>
<%@ page import="models.Cancha" %>
<%@ page import="models.Usuario" %>

<%
    Vector<Cancha> listaCanchas = (Vector<Cancha>) request.getAttribute("listaCanchas");
    String cedulaUsuario = (String) request.getAttribute("cedulaUsuario");
    boolean esAdmin = (request.getAttribute("esAdmin") != null) && (boolean) request.getAttribute("esAdmin");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Canchas</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panel.css">
</head>
<body>

<div class="panel">
    <%@ include file="/WEB-INF/components/headerUsuario.jsp" %>

    <div class="titulo">
        <h1 class="tituloGestion">Panel de Canchas</h1>
    </div>

    <div class="listaUser">
        <ul>
            <% if (listaCanchas != null && !listaCanchas.isEmpty()) {
                for (Cancha cancha : listaCanchas) { %>
            <li onclick="window.location.href='${pageContext.request.contextPath}/crearreserva?numeroCancha=<%= cancha.getNumero() %>'">
                <img src="https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg" alt="Cancha <%= cancha.getNumero() %>">

                <span>Cancha Nro <%= cancha.getNumero() %></span>
            </li>
            <% } } else { %>
            <li>No hay canchas disponibles.</li>
            <% } %>
        </ul>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>