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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarCancha.css">
</head>
<body>
<form class="login-form" action="editarCancha" method="post">
    <h1><i class="bi bi-pencil-square"></i> Editar Cancha N° <%= cancha.getNumero() %></h1>

    <input type="hidden" name="numero" value="<%= cancha.getNumero() %>">

    <div class="form-input-material">
        <label for="precio">Precio ($)</label>
        <input type="number" step="0.01" id="precio" name="precio" value="<%= cancha.getPrecio() %>" required>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="esTechada" name="esTechada" <%= cancha.isEsTechada() ? "checked" : "" %>>
        <label class="form-check-label" for="esTechada">Cancha techada</label>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="estaDisponible" name="estaDisponible" <%= cancha.isEstaDisponible() ? "checked" : "" %>>
        <label class="form-check-label" for="estaDisponible">Disponible para reserva</label>
    </div>

    <div class="form-input-material">
        <label for="horarios">Horarios disponibles</label>
        <select id="horarios" name="horarios" multiple size="8">
            <% for (String h : horariosDia) {
                boolean seleccionado = horariosCancha.stream()
                        .anyMatch(t -> t.toString().startsWith(h));
            %>
            <option value="<%= h %>" <%= seleccionado ? "selected" : "" %>><%= h %></option>
            <% } %>
        </select>
        <p class="form-text">Use Ctrl (o Cmd) para seleccionar múltiples horarios</p>
    </div>

    <div class="acciones">
        <a href="cancha" class="btn btn-outline-secondary">Volver</a>
        <button type="submit" class="btn btn-success">Guardar cambios</button>
    </div>
</form>
</body>
</html>
