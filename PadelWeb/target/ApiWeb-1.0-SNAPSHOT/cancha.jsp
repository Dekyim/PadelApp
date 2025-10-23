

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
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Canchas</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="css/panel.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerAdmin.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Panel de canchas</h1>
    </div>

    <div class="container mt-3">
        <% if (mensajeExito != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensajeExito %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } else if (mensajeError != null) { %>
      f  <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= mensajeError %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
    </div>

    <button type="submit" class="btn-agregar" title="Agregar cancha">
        <i class="fi fi-rr-plus"></i>
    </button>


    <div class="listaUser">
        <ul>
            <% if (numeroCancha != null && !numeroCancha.isEmpty()) {
                for (Integer numeroCanchas : numeroCancha) { %>
            <li>
                <span>Cancha Nro <%= numeroCanchas %></span>
                <div>
                    <form action="cancha" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="numero" value="<%= numeroCanchas %>">
                        <button type="submit" title="Eliminar"
                                onclick="return confirm('¿Eliminar la cancha Nro <%= numeroCanchas %>?')">
                            <i class="fi fi-rr-trash"></i>
                        </button>
                    </form>

                    <form action="editarCancha" method="get" style="display:inline;">
                        <input type="hidden" name="numero" value="<%= numeroCanchas %>">
                        <button type="submit" title="Editar">
                            <i class="fi fi-rr-user-pen"></i>
                        </button>
                    </form>

                    <button title="Ver"><i class="fi fi-rr-document"></i></button>
                </div>
            </li>
            <% } } else { %>
            <li>No hay canchas disponibles.</li>
            <% } %>
        </ul>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
