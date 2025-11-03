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
    <link rel="stylesheet" href="css/panelReserva.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerUsuario.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Mis reservas</h1>
    </div>

    <div class="container mt-3">
        <% if (mensajeExito != null) { %>
        <div class="alert success"><%= mensajeExito %></div>
        <% } else if (mensajeError != null) { %>
        <div class="alert error"><%= mensajeError %></div>
        <% } %>
    </div>

    <!-- Filtros -->
    <form method="get" action="reservasUsuario" class="filtros filtros-reserva">
        <div class="grupo-filtros">
            <input type="date" name="fecha" value="${param.fecha}" placeholder="Fecha">

            <select name="numeroCancha">
                <option value="">Todas las canchas</option>
                <c:forEach var="cancha" items="${listaCanchas}">
                    <option value="${cancha.numero}" <c:if test="${param.numeroCancha == cancha.numero}">selected</c:if>>
                        Cancha Nº ${cancha.numero}
                    </option>
                </c:forEach>
            </select>

            <select name="ordenFecha">
                <option value="">Orden por fecha</option>
                <option value="asc" <c:if test="${param.ordenFecha == 'asc'}">selected</c:if>>Ascendente</option>
                <option value="desc" <c:if test="${param.ordenFecha == 'desc'}">selected</c:if>>Descendente</option>
            </select>

            <select name="estadoPago">
                <option value="">Todas</option>
                <option value="pagadas" <c:if test="${param.estadoPago == 'pagadas'}">selected</c:if>>Pagadas</option>
                <option value="nopagadas" <c:if test="${param.estadoPago == 'nopagadas'}">selected</c:if>>No pagadas</option>
            </select>

            <select name="estadoActiva">
                <option value="">Todas</option>
                <option value="activas" <c:if test="${param.estadoActiva == 'activas'}">selected</c:if>>Activas</option>
                <option value="noactivas" <c:if test="${param.estadoActiva == 'noactivas'}">selected</c:if>>No activas</option>
            </select>

            <select name="metodoPago">
                <option value="">Todos los métodos</option>
                <option value="efectivo" <c:if test="${param.metodoPago == 'efectivo'}">selected</c:if>>Efectivo</option>
                <option value="transferencia" <c:if test="${param.metodoPago == 'transferencia'}">selected</c:if>>Transferencia</option>
                <option value="tarjeta" <c:if test="${param.metodoPago == 'tarjeta'}">selected</c:if>>Tarjeta</option>
            </select>

            <!-- 🔄 Solo íconos en botones -->
            <button type="submit" class="btn-icon" title="Filtrar">
                <i class="fi fi-rr-filter"></i>
            </button>
            <a href="reservasUsuario" class="btn-icon" title="Limpiar">
                <i class="fi fi-rr-cross-small"></i>
            </a>
        </div>
    </form>

    <!-- 🔄 CAMBIO NUEVO: Lista de reservas con estilo admin -->
    <section class="listaReservas">
        <% if (listaReservas != null && !listaReservas.isEmpty()) {
            for (Reserva reserva : listaReservas) { %>
        <div class="card-reserva">
            <div class="reserva-info">
                <div class="fecha">
                    <i class="fi fi-rr-clock"></i>
                    <span><%= reserva.getFecha() %></span>
                    <p><strong><%= reserva.getHorarioInicio() %> - <%= reserva.getHorarioFinal() %></strong></p>
                </div>

                <div class="detalles">
                    <h3>Tu reserva</h3>
                    <p><i class="fi fi-rr-basketball"></i> Cancha Nº <%= reserva.getNumeroCancha() %></p>
                    <p><i class="fi fi-rr-credit-card"></i> <%= reserva.getMetodoPago().getValue() %></p>
                    <span class="estado <%= reserva.isEstaPagada() ? "pagada" : "nopagada" %>">
                        <%= reserva.isEstaPagada() ? "Pagada" : "Pendiente" %>
                    </span>
                    <span class="estado <%= reserva.isEstaActiva() ? "activa" : "inactiva" %>">
                        <%= reserva.isEstaActiva() ? "Activa" : "Cancelada" %>
                    </span>
                </div>
            </div>

            <div class="acciones-card">
                <form action="editarReserva" method="get">
                    <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                    <button type="submit" class="edit"><i class="fi fi-rr-edit"></i></button>
                </form>
                <form action="cancelarReserva" method="post" onsubmit="return confirm('¿Cancelar esta reserva?')">
                    <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                    <button type="submit" class="delete"><i class="fi fi-rr-trash"></i></button>
                </form>
            </div>
        </div>
        <% } } else { %>
        <p class="sinReservas">No tenés reservas registradas todavía 😢</p>
        <% } %>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
