<%@ page import="java.util.List" %>

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
    List<String> nombres = (List<String>) request.getAttribute("listaJugadores");
    // Configuración de paginación
    int usuariosPorPagina = 12;
    int totalUsuarios = nombres.size();
    int totalPaginas = (int) Math.ceil((double) totalUsuarios / usuariosPorPagina);

    int paginaActual = 1;
    if (request.getParameter("page") != null) {
        try {
            paginaActual = Integer.parseInt(request.getParameter("page"));
            if (paginaActual < 1) paginaActual = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;
        } catch (NumberFormatException e) {
            paginaActual = 1;
        }
    }

    int inicio = (paginaActual - 1) * usuariosPorPagina;
    int fin = Math.min(inicio + usuariosPorPagina, totalUsuarios);
    List<String> pagina = nombres.subList(inicio, fin);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de control</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Flaticon Icons -->
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">

    <!-- Tu hoja de estilos -->
    <link rel="stylesheet" href="css/panel.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerPanel.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Gestión de usuarios</h1>
    </div>

    <div class="busquedaAgregar">
        <i class="fi fi-rr-search"></i>
        <input type="text" id="buscar" placeholder="Buscar usuario">
        <button class="btn-agregar">Agregar usuario</button>
    </div>

    <div class="listaUser">
        <ul>
            <% if (nombres != null && !nombres.isEmpty()) {
                for (String nombre : nombres) { %>
            <li>
                <span><%= nombre %></span>
                <div>
                    <button title="Eliminar"><i class="fi fi-rr-trash"></i></button>
                    <button title="Editar"><i class="fi fi-rr-user-pen"></i></button>
                    <button title="Ver"><i class="fi fi-rr-document"></i></button>
                </div>
            </li>
            <%  }
            } else { %>
            <li>No hay usuarios disponibles.</li>
            <% } %>

        </ul>
    </div>
</div>
<!-- Paginación -->
<nav aria-label="Paginación de usuarios">
    <ul class="pagination">
            <% if (paginaActual > 1) { %>
        <li class="page-item">
            <a class="page-link" href="?page=<%= paginaActual - 1 %>">Anterior</a>
        </li>
            <% } else { %>
        <li class="page-item disabled"><span class="page-link">Anterior</span></li>
            <% } %>

            <% for (int i = 1; i <= totalPaginas; i++) { %>
        <li class="page-item <%= (i == paginaActual) ? "active" : "" %>">
            <a class="page-link" href="?page=<%= i %>"><%= i %></a>
        </li>
            <% } %>

            <% if (paginaActual < totalPaginas) { %>
        <li class="page-item">
            <a class="page-link" href="?page=<%= paginaActual + 1 %>">Siguiente</a>
        </li>
            <% } else { %>
        <li class="page-item disabled"><span class="page-link">Siguiente</span></li>
            <% } %>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
