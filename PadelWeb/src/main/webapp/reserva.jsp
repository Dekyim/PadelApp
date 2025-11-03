<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="models.Reserva" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    List<Reserva> listaReservas = (List<Reserva>) request.getAttribute("listaReservas");
    Map<String, String> nombresUsuarios = (Map<String, String>) request.getAttribute("nombresUsuarios");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Reservas</title>
    <link rel="stylesheet" href="css/panelReserva.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<%@ include file="/WEB-INF/components/headerAdmin.jsp" %>

<!-- ✅ CONTENIDO PRINCIPAL -->
<main class="panel">

    <% if (mensajeExito != null) { %>
    <div class="alert success"><%= mensajeExito %></div>
    <% } else if (mensajeError != null) { %>
    <div class="alert error"><%= mensajeError %></div>
    <% } %>

    <!-- 🔍 FILTROS -->
    <form method="get" action="reserva" class="filtros">
        <input type="text" name="cedulaUsuario" placeholder="Buscar por nombre o cédula..." value="${param.cedulaUsuario}">
        <input type="date" name="fecha" value="${param.fecha}">
        <select name="metodoPago">
            <option value="">Método de pago</option>
            <option value="efectivo">Efectivo</option>
            <option value="transferencia">Transferencia</option>
            <option value="tarjeta">Tarjeta</option>
        </select>
        <select name="estadoPago">
            <option value="">¿Pagada?</option>
            <option value="pagadas">Sí</option>
            <option value="nopagadas">No</option>
        </select>
        <div class="iconos">
            <button type="submit"><i class="fi fi-rr-search"></i></button>
            <a href="reserva" class="btn-limpiar"><i class="fi fi-rr-cross-small"></i></a>
        </div>
    </form>

    <a href="crearreserva" class="btn-nueva"><i class="fi fi-rr-plus"></i></a>

    <!-- 🧾 LISTA DE RESERVAS -->
    <section class="listaReservas">
        <% if (listaReservas != null && !listaReservas.isEmpty()) {
            for (Reserva reserva : listaReservas) {
                String nombre = nombresUsuarios.get(reserva.getCedulaUsuario());
        %>
        <div class="card-reserva">
            <div class="reserva-info">
                <div class="fecha">
                    <i class="fi fi-rr-clock"></i>
                    <span><%= reserva.getFecha() %></span>
                    <p><strong><%= reserva.getHorarioInicio() %> - <%= reserva.getHorarioFinal() %></strong></p>
                </div>

                <div class="detalles">
                    <h3><%= nombre %></h3>
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
                <form action="cancelarReserva" method="post">
                    <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                    <button type="submit" class="delete" onclick="return confirm('¿Cancelar la reserva de <%= nombre %>?')">
                        <i class="fi fi-rr-trash"></i>
                    </button>
                </form>
            </div>
        </div>
        <% } } else { %>
        <div class="mensaje-vacio">
            <i class="fi fi-rr-calendar-x"></i>
            <p>No tenés reservas registradas todavía 😟</p>
        </div>
        <% } %>
    </section>
</main>

</body>
</html>
