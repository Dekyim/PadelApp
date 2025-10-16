

<%--
<%
    List<String> nombres = (List<String>) request.getAttribute("listaJugadores");
%>

<h1>Lista de Jugadores</h1>

<form action="<%= request.getContextPath() %>/logout" method="get">
    <button type="submit">Cerrar sesión</button>
</form>


<ul>
    <% if (nombres != null) {
        for (String nombre : nombres) { %>
    <li><%= nombre %></li>
    <%  }
    } else { %>
    <li>No hay jugadores disponibles.</li>
    <% } %>
</ul> --%>

<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> --%>
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
    <%@include file="/WEB-INF/components/headerPanel.jsp"%>

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
                <span><%= numeroCanchas %></span>
                <div>
                    <button title="Eliminar"><i class="fi fi-rr-trash"></i></button>
                    <button title="Editar"><i class="fi fi-rr-user-pen"></i></button>
                    <button title="Ver"><i class="fi fi-rr-document"></i></button>
                </div>
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