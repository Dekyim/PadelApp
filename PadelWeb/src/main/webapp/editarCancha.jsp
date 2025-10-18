<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 18/10/2025
  Time: 5:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Cancha, java.sql.Time, java.util.Vector" %>
<%
    Cancha cancha = (Cancha) request.getAttribute("cancha");
    if (cancha == null) {
        response.sendRedirect("cancha");
        return;
    }

    java.util.List<String> horariosDia = new java.util.ArrayList<>();
    java.time.LocalTime hora = java.time.LocalTime.of(7, 0);
    while (hora.isBefore(java.time.LocalTime.of(22, 0))) {
        horariosDia.add(hora.toString());
        hora = hora.plusMinutes(90);
    }

    Vector<Time> horariosCancha = cancha.getHorario() != null
            ? cancha.getHorario().getHorarios()
            : new Vector<>();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Editar Cancha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="card shadow-lg mx-auto" style="max-width: 600px;">
        <div class="card-body">
            <h3 class="text-center mb-4">Editar Cancha N° <%= cancha.getNumero() %></h3>

            <form action="editarCancha" method="post">
                <input type="hidden" name="numero" value="<%= cancha.getNumero() %>">

                <div class="mb-3">
                    <label class="form-label">Precio ($)</label>
                    <input type="number" step="0.01" class="form-control" name="precio" value="<%= cancha.getPrecio() %>" required>
                </div>

                <div class="form-check mb-2">
                    <input class="form-check-input" type="checkbox" name="esTechada" <%= cancha.isEsTechada() ? "checked" : "" %>>
                    <label class="form-check-label">Cancha techada</label>
                </div>

                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" name="estaDisponible" <%= cancha.isEstaDisponible() ? "checked" : "" %>>
                    <label class="form-check-label">Disponible para reserva</label>
                </div>

                <div class="mb-3">
                    <label class="form-label">Horarios disponibles</label>
                    <select name="horarios" multiple class="form-select" size="8">
                        <% for (String h : horariosDia) {
                            boolean seleccionado = horariosCancha.stream()
                                    .anyMatch(t -> t.toString().startsWith(h));
                        %>
                        <option value="<%= h %>" <%= seleccionado ? "selected" : "" %>>
                            <%= h %>
                        </option>
                        <% } %>
                    </select>
                    <div class="form-text">Use Ctrl (o Cmd) para seleccionar múltiples horarios</div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="cancha" class="btn btn-secondary">Volver</a>
                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
