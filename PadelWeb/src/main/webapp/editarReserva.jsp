<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Reserva" %>
<%@ page import="models.MetodoPago" %>
<%@ page import="models.Cancha" %>
<%@ page import="java.util.Vector" %>
<%@ page import="models.Usuario" %>

<%
    Reserva reserva = (Reserva) request.getAttribute("reserva");
    Vector<Cancha> listaCanchas = (Vector<Cancha>) request.getAttribute("listaCanchas");
    String mensajeError = (String) request.getAttribute("mensajeError");
    String mensajeExito = (String) request.getAttribute("mensajeExito");

    String[] bloquesHorarios = {
            "07:00", "08:30", "10:00", "11:30", "13:00",
            "14:30", "16:00", "17:30", "19:00", "20:30"
    };
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Reserva</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/editarReserva.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerAdmin.jsp"%>

<div class="container mt-5">
    <h2>Editar Reserva</h2>

    <!-- Mensajes -->
    <% if (mensajeError != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <%= mensajeError %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
    <% } else if (mensajeExito != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <%= mensajeExito %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
    </div>
    <% } %>

    <form action="editarReserva" method="post">
        <input type="hidden" name="id" value="<%= reserva.getId() %>">

        <div class="mb-3">
            <label>Cédula Usuario</label>
            <input type="text" name="cedulaUsuario" class="form-control" value="<%= reserva.getCedulaUsuario() %>" required>
        </div>

        <div class="mb-3">
            <label>Número de Cancha</label>
            <select name="numeroCancha" class="form-select" required>
                <% for (Cancha cancha : listaCanchas) { %>
                <option value="<%= cancha.getNumero() %>" <%= cancha.getNumero() == reserva.getNumeroCancha() ? "selected" : "" %>>
                    Cancha Nº <%= cancha.getNumero() %>
                </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label>Fecha</label>
            <input type="date" name="fecha" class="form-control" value="<%= reserva.getFecha() %>" required>
        </div>

        <div class="mb-3">
            <label>Horario de Inicio</label>
            <select name="horarioInicio" class="form-select" required>
                <% String actual = reserva.getHorarioInicio().toString().substring(0,5);
                    for (String bloque : bloquesHorarios) { %>
                <option value="<%= bloque %>" <%= bloque.equals(actual) ? "selected" : "" %>>
                    <%= bloque %>
                </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label>Método de Pago</label>
            <select name="metodoPago" class="form-select">
                <% for (MetodoPago m : MetodoPago.values()) { %>
                <option value="<%= m.getValue() %>" <%= m == reserva.getMetodoPago() ? "selected" : "" %>>
                    <%= m.getValue().replace("_", " ").toUpperCase() %>
                </option>
                <% } %>
            </select>
        </div>

        <div class="form-check">
            <input class="form-check-input" type="checkbox" name="estaPagada" <%= reserva.isEstaPagada() ? "checked" : "" %>>
            <label class="form-check-label">Pagada</label>
        </div>

        <div class="form-check">
            <input class="form-check-input" type="checkbox" name="estaActiva" <%= reserva.isEstaActiva() ? "checked" : "" %>>
            <label class="form-check-label">Activa</label>
        </div>

        <button type="submit" class="btn btn-primary mt-3">Guardar cambios</button>
        <%
            Usuario usuario = (Usuario) session.getAttribute("authUser");
            String destino = (usuario != null && usuario.esAdministrador()) ? "reserva" : "reservasUsuario";
        %>
        <a href="<%= destino %>" class="btn btn-secondary mt-3">Cancelar</a>

    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
