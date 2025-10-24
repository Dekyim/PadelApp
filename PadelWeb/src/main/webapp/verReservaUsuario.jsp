<%@ page import="java.util.List" %>
<%@ page import="models.Reserva" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");
    String cedulaUsuario = (String) request.getAttribute("cedulaUsuario");
    String nombreUsuario = (String) request.getAttribute("nombreUsuario");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservas Usuario <%= cedulaUsuario %></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/panel.css">
</head>
<body>
<div class="panel">
    <%@ include file="/WEB-INF/components/headerAdmin.jsp" %>

    <div class="titulo">
        <h1 class="tituloGestion">Reservas Usuario: <%= nombreUsuario %> (Cedula: <%= cedulaUsuario %>)</h1>
    </div>

    <div class="container mt-3">
        <% if (reservas != null && !reservas.isEmpty()) { %>
        <table class="table table-striped table-dark">
            <thead>
            <tr>
                <th>Cancha</th>
                <th>Fecha</th>
                <th>Horario Inicio</th>
                <th>Horario Fin</th>
                <th>Metodo de Pago</th>
                <th>Pagado</th>
                <th>Activa</th>
            </tr>
            </thead>
            <tbody>
            <% for (Reserva r : reservas) { %>
            <tr>
                <td><%= r.getNumeroCancha() %></td>
                <td><%= r.getFecha() %></td>
                <td><%= r.getHorarioInicio() %></td>
                <td><%= r.getHorarioFinal() %></td>
                <td><%= r.getMetodoPago() %></td>
                <td><%= r.isEstaPagada() ? "Si" : "No" %></td>
                <td><%= r.isEstaActiva() ? "Si" : "No" %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <div class="alert alert-info">No hay reservas para este usuario.</div>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
