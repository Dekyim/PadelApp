<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.sql.Time" %>

<%
    String cedula = (String) request.getAttribute("cedulaUsuario");
    String nombre = (String) request.getAttribute("nombreUsuario");
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");
    Map<Integer, Double> preciosPorReserva = (Map<Integer, Double>) request.getAttribute("preciosPorReserva");
    Double totalPagado = (Double) request.getAttribute("totalPagado");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reservas del Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panel.css">
</head>
<body>

<%@ include file="/WEB-INF/components/headerAdmin.jsp" %>

<div class="panel">
    <div class="titulo text-center">
        <h1 class="tituloGestion">Reservas del Usuario</h1>
    </div>

    <div class="info-usuario">
        <h5><strong>Cédula:</strong> <%= cedula %></h5>
        <h5><strong>Nombre:</strong> <%= nombre %></h5>
    </div>
    <div class="filtros-reservas mb-4">
        <form method="get" action="verReservaUsuario" class="row g-3 align-items-end">
            <input type="hidden" name="cedula" value="<%= cedula %>">

            <div class="col-md-3">
                <label for="fecha" class="form-label">Fecha</label>
                <input type="date" class="form-control" id="fecha" name="fecha" value="<%= request.getParameter("fecha") != null ? request.getParameter("fecha") : "" %>">
            </div>

            <div class="col-md-2">
                <label for="horario" class="form-label">Hora Inicio</label>
                <select class="form-select" id="horario" name="horario">
                    <option value="">Todos</option>
                    <%
                        List<Time> horariosInicio = (List<Time>) request.getAttribute("horariosInicio");
                        String horarioSeleccionado = request.getParameter("horario");
                        for (Time h : horariosInicio) {
                            String horaStr = h.toString().substring(0,5); // HH:mm
                    %>
                    <option value="<%= horaStr %>" <%= horaStr.equals(horarioSeleccionado) ? "selected" : "" %>>
                        <%= horaStr %>
                    </option>
                    <% } %>
                </select>
            </div>


            <div class="col-md-2">
                <label for="numeroCancha" class="form-label">N° Cancha</label>
                <input type="number" class="form-control" id="numeroCancha" name="numeroCancha" min="1" value="<%= request.getParameter("numeroCancha") != null ? request.getParameter("numeroCancha") : "" %>">
            </div>

            <div class="col-md-2">
                <label for="pagada" class="form-label">Pagada</label>
                <select class="form-select" id="pagada" name="pagada">
                    <option value="">Todas</option>
                    <option value="true" <%= "true".equals(request.getParameter("pagada")) ? "selected" : "" %>>Sí</option>
                    <option value="false" <%= "false".equals(request.getParameter("pagada")) ? "selected" : "" %>>No</option>
                </select>
            </div>

            <div class="col-md-2">
                <label for="activa" class="form-label">Activa</label>
                <select class="form-select" id="activa" name="activa">
                    <option value="">Todas</option>
                    <option value="true" <%= "true".equals(request.getParameter("activa")) ? "selected" : "" %>>Sí</option>
                    <option value="false" <%= "false".equals(request.getParameter("activa")) ? "selected" : "" %>>No</option>
                </select>
            </div>

            <div class="col-md-1">
                <button type="submit" class="btn btn-primary w-100">Filtrar</button>
            </div>
            <div class="col-md-1">
                <a href="verReservaUsuario?cedula=<%= cedula %>" class="btn btn-outline-secondary">Limpiar filtros</a>
            </div>

        </form>
    </div>

    <div class="tabla-reservas">
        <% if (reservas != null && !reservas.isEmpty()) { %>
        <table class="table table-borderless align-middle">
            <thead>
            <tr>
                <th>Número de Reserva</th>
                <th>Fecha</th>
                <th>Hora Inicio</th>
                <th>Hora Fin</th>
                <th>Cancha</th>
                <th>Pagada</th>
                <th>Activa</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <% for (Reserva reserva : reservas) { %>
            <tr>
                <td><%= reserva.getId() %></td>
                <td><%= reserva.getFecha() %></td>
                <td><%= reserva.getHorarioInicio() %></td>
                <td><%= reserva.getHorarioFinal() %></td>
                <td>
                    <%= reserva.getNumeroCancha() %>
                    <% Double precio = preciosPorReserva.get(reserva.getId()); %>
                    <span class="text-muted">
                    (<%= precio != null ? "$" + String.format("%.2f", precio) : "Precio no disponible" %>)
                    </span>

                </td>
                <td>
                    <% if (reserva.isEstaPagada()) { %>
                    <span class="badge bg-success">Sí</span>
                    <% } else { %>
                    <span class="badge bg-danger">No</span>
                    <% } %>
                </td>
                <td>
                    <% if (reserva.isEstaActiva()) { %>
                    <span class="badge bg-success">Sí</span>
                    <% } else { %>
                    <span class="badge bg-secondary">No</span>
                    <% } %>
                </td>
                <td>
                    <form action="verReservaUsuario" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <input type="hidden" name="cedula" value="<%= cedula %>">
                        <% if (reserva.isEstaActiva()) { %>
                        <% if (reserva.isEstaPagada()) { %>
                        <button type="submit" name="accion" value="despagar" class="btn btn-warning btn-sm">Cancelar</button>
                        <% } else { %>
                        <button type="submit" name="accion" value="pagar" class="btn btn-success btn-sm">Pagar</button>
                        <% } %>
                        <% } else { %>
                        <button class="btn btn-secondary btn-sm" disabled>No disponible</button>
                        <% } %>
                    </form>

                    <form action="verReservaUsuario" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <input type="hidden" name="cedula" value="<%= cedula %>">
                        <% if (reserva.isEstaActiva()) { %>
                        <button type="submit" name="accion" value="desactivar" class="btn btn-danger btn-sm">Desactivar</button>
                        <% } else { %>
                        <button type="submit" name="accion" value="activar" class="btn btn-primary btn-sm">Activar</button>
                        <% } %>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <div class="alert alert-info text-center mt-3">
            No hay reservas registradas para este usuario.
        </div>
        <% } %>
    </div>
    <div class="text-center mt-4">
        <div class="alert alert-success d-inline-block px-4 py-3">
            <h5 class="mb-0">💰 <strong>Pagos totales del jugador:</strong> $<%= String.format("%.2f", totalPagado) %></h5>
        </div>
    </div>

    <div class="text-center mt-4">
        <a href="users" class="btn btn-secondary">Volver</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
