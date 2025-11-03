<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Reserva" %>
<%@ page import="java.sql.Time" %>

<%
    int numeroCancha = (int) request.getAttribute("numeroCancha");
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");
    Map<Integer, Double> preciosPorReserva = (Map<Integer, Double>) request.getAttribute("preciosPorReserva");
    Double totalGanado = (Double) request.getAttribute("totalGanado");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reservas Cancha <%= numeroCancha %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panelReservaCancha.css">
</head>
<body>
<!-- Botón hamburguesa funcional -->
<button id="toggleSidebar" class="sidebar-toggle">
    <span class="bar"></span>
    <span class="bar"></span>
    <span class="bar"></span>
</button>



<%@ include file="/WEB-INF/components/headerAdmin.jsp" %>

<div class="panel">
    <div class="titulo text-center">
        <h1 class="tituloGestion">Reservas Cancha Nro <%= numeroCancha %></h1>
    </div>

    <div class="filtros-reservas mb-4">
        <form method="get" action="verReservaCancha" class="row g-3 align-items-end">
            <input type="hidden" name="numeroCancha" value="<%= numeroCancha %>">

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
                            String horaStr = h.toString().substring(0,5);
                    %>
                    <option value="<%= horaStr %>" <%= horaStr.equals(horarioSeleccionado) ? "selected" : "" %>>
                        <%= horaStr %>
                    </option>
                    <% } %>
                </select>
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

            <div class="col-md-2 d-flex gap-2">
                <button type="submit" class="btn btn-primary flex-fill">Filtrar</button>
                <a href="verReservaCancha?numeroCancha=<%= numeroCancha %>" class="btn btn-outline-secondary flex-fill">Limpiar</a>
            </div>

        </form>
    </div>

    <div class="bloque-reservas mt-3">
        <% if (reservas != null && !reservas.isEmpty()) { %>
        <table class="table table-striped table-dark align-middle">
            <thead>
            <tr>
                <th>Jugador</th>
                <th>Fecha</th>
                <th>Hora Inicio</th>
                <th>Hora Fin</th>
                <th>Precio</th>
                <th>Método de Pago</th>
                <th>Pagado</th>
                <th>Activa</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <% for (Reserva r : reservas) { %>
            <tr>
                <td><%= r.getCedulaUsuario() %></td>
                <td><%= r.getFecha() %></td>
                <td><%= r.getHorarioInicio() %></td>
                <td><%= r.getHorarioFinal() %></td>
                <td>
                    <% Double precio = preciosPorReserva.get(r.getId()); %>
                    <%= precio != null ? "$" + String.format("%.2f", precio) : "Precio no disponible" %>
                </td>
                <td><%= r.getMetodoPago() %></td>
                <td>
                    <% if (r.isEstaPagada()) { %>
                    <span class="badge bg-success">Sí</span>
                    <% } else { %>
                    <span class="badge bg-danger">No</span>
                    <% } %>
                </td>
                <td>
                    <% if (r.isEstaActiva()) { %>
                    <span class="badge bg-success">Sí</span>
                    <% } else { %>
                    <span class="badge bg-secondary">No</span>
                    <% } %>
                </td>
                <td>
                    <form action="verReservaCancha" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= r.getId() %>">
                        <input type="hidden" name="numeroCancha" value="<%= numeroCancha %>">
                        <% if (r.isEstaActiva()) { %>
                        <% if (r.isEstaPagada()) { %>
                        <button type="submit" name="accion" value="despagar" class="btn btn-warning btn-sm">Cancelar</button>
                        <% } else { %>
                        <button type="submit" name="accion" value="pagar" class="btn btn-success btn-sm">Pagar</button>
                        <% } %>
                        <% } else { %>
                        <button class="btn btn-secondary btn-sm" disabled>No disponible</button>
                        <% } %>
                    </form>

                    <form action="verReservaCancha" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= r.getId() %>">
                        <input type="hidden" name="numeroCancha" value="<%= numeroCancha %>">
                        <% if (r.isEstaActiva()) { %>
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
            No hay reservas registradas para esta cancha.
        </div>
        <% } %>
    </div>

    <div class="text-center mt-4">
        <div class="alert alert-success d-inline-block px-4 py-3">
            <h5 class="mb-0">💰 <strong>Total ganado en esta cancha:</strong> $<%= String.format("%.2f", totalGanado) %></h5>
        </div>
    </div>

    <div class="text-center mt-4">
        <a href="cancha" class="btn btn-secondary">Volver</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const boton = document.getElementById('toggleSidebar');
        const sidebar = document.getElementById('sidebar');
        const panel = document.querySelector('.panel');

        boton.addEventListener('click', () => {
            boton.classList.toggle('active');
            sidebar.classList.toggle('oculto');
            panel.classList.toggle('shifted');
            // Mover el botón junto con el sidebar
            if (sidebar.classList.contains('oculto')) {
                boton.style.left = '20px';
            } else {
                boton.style.left = '240px';
            }
        });
    });
</script>

</body>
</html>