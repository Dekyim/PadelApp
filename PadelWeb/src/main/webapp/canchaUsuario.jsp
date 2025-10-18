<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 18/10/2025
  Time: 5:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>

<%
    List<Integer> numeroCancha = (List<Integer>) request.getAttribute("listaCanchas");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de control</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">

    <link rel="stylesheet" href="css/panel.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerUsuario.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Gestión de canchas</h1>
    </div>

    <div class="busquedaAgregar">
        <i class="fi fi-rr-search"></i>
        <input type="text" id="buscar" placeholder="Buscar canchas">
        <button class="btn-agregar">Agregar canchas</button>
    </div>

    <div class="listaUser">
        <ul>
            <% if (numeroCancha != null && !numeroCancha.isEmpty()) {
                for (Integer numeroCanchas : numeroCancha) { %>
            <li>
                <span>Cancha Nro <%= numeroCanchas %></span>
            </li>
            <%  }
            } else { %>
            <li>No hay canchas disponibles.</li>
            <% } %>
        </ul>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>