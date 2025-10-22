<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    String[] bloquesHorarios = {
            "07:00", "08:30", "10:00", "11:30", "13:00",
            "14:30", "16:00", "17:30", "19:00", "20:30"
    };
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Reserva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<form class="login-form" action="${pageContext.request.contextPath}/crearreserva" method="post">
    <h1>Crear Reserva</h1>

    <div class="form-grid">
        <div class="form-input-material">
            <label for="cedulaUsuario">Jugador</label>
            <input type="text" name="cedulaUsuario" id="cedulaUsuario" placeholder="Ingrese la cédula" required autocomplete="off">
        </div>

        <div class="form-input-material">
            <label for="numeroCancha">Cancha</label>
            <select name="numeroCancha" id="numeroCancha" required>
                <c:forEach var="cancha" items="${canchas}">
                    <option value="${cancha.numero}">${cancha.numero}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-input-material">
            <label for="fecha">Fecha</label>
            <input type="date" name="fecha" id="fecha" required>
        </div>

        <div class="form-input-material">
            <label for="horarioInicio">Horario de Inicio</label>
            <select name="horarioInicio" id="horarioInicio" required>
                <% for (String bloque : bloquesHorarios) { %>
                <option value="<%= bloque %>"><%= bloque %></option>
                <% } %>
            </select>
        </div>

        <div class="form-input-material">
            <label for="metodoPago">Método de Pago</label>
            <select name="metodoPago" id="metodoPago" required>
                <option value="efectivo">Efectivo</option>
                <option value="transferencia">Transferencia</option>
                <option value="tarjeta">Tarjeta</option>
                <option value="mercado_pago">Mercado Pago</option>
            </select>
        </div>
    </div>

    <button type="submit" class="btn">Reservar</button>

    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
</form>
</body>
</html>
