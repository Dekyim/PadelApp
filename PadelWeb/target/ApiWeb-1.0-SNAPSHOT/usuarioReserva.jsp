<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Vector" %>
<%@ page import="models.Reserva" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    Vector<Reserva> listaReservas = (Vector<Reserva>) request.getAttribute("listaReservas");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Reservas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="css/panel.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerUsuario.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Mis reservas</h1>
    </div>

    <div class="container mt-3">
        <% if (mensajeExito != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensajeExito %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } else if (mensajeError != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= mensajeError %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
    </div>

    <!-- Lista de reservas -->
    <div class="listaUser">
        <ul>
            <% if (listaReservas != null && !listaReservas.isEmpty()) {
                for (Reserva reserva : listaReservas) { %>
            <li>
                <span>
                    Cancha Nº <%= reserva.getNumeroCancha() %><br>
                    Fecha: <%= reserva.getFecha() %><br>
                    Horario: <%= reserva.getHorarioInicio() %> - <%= reserva.getHorarioFinal() %><br>
                    Método de pago: <%= reserva.getMetodoPago().getValue() %><br>
                    Pagada: <%= reserva.isEstaPagada() ? "Sí" : "No" %><br>
                    Activa: <%= reserva.isEstaActiva() ? "Sí" : "No" %>
                </span>
                <div>
                    <form action="cancelarReserva" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <button type="submit" title="Cancelar"
                                onclick="return confirm('¿Cancelar esta reserva?')">
                            <i class="fi fi-rr-trash"></i>
                        </button>
                    </form>

                    <form action="editarReserva" method="get" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <button type="submit" title="Editar">
                            <i class="fi fi-rr-user-pen"></i>
                        </button>
                    </form>
                </div>
            </li>
            <% } } else { %>
            <li>No tenés reservas registradas.</li>
            <% } %>
        </ul>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
