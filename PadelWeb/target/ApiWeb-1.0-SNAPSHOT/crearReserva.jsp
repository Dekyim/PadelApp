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

        <!-- Cédula del jugador -->
        <div class="form-input-material">
            <label for="cedulaUsuario">Jugador</label>
            <c:choose>
                <c:when test="${not empty cedulaUsuarioSeleccionada}">
                    <input type="text" name="cedulaUsuario" id="cedulaUsuario"
                           value="${cedulaUsuarioSeleccionada}" readonly>
                </c:when>
                <c:otherwise>
                    <input type="text" name="cedulaUsuario" id="cedulaUsuario" placeholder="Ingrese la cédula" required>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Cancha -->
        <div class="form-input-material">
            <label for="numeroCancha">Cancha</label>
            <select name="numeroCancha" id="numeroCancha" required>
                <c:forEach var="cancha" items="${canchas}">
                    <option value="${cancha.numero}"
                            <c:if test="${numeroCanchaSeleccionada != null && numeroCanchaSeleccionada == cancha.numero}">selected</c:if>>
                        Cancha ${cancha.numero}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Fecha -->
        <div class="form-input-material">
            <label for="fecha">Fecha</label>
            <input type="date" name="fecha" id="fecha" required
                   value="${fechaSeleccionada != null ? fechaSeleccionada : param.fecha != null ? param.fecha : ''}">
        </div>

        <!-- Horario de inicio -->
        <div class="form-input-material">
            <label for="horarioInicio">Horario de Inicio</label>
            <select name="horarioInicio" id="horarioInicio" required>
                <% for (String bloque : bloquesHorarios) { %>
                <option value="<%= bloque %>"
                        <%= request.getAttribute("horarioSeleccionado") != null && request.getAttribute("horarioSeleccionado").equals(bloque) ? "selected" : "" %>>
                    <%= bloque %>
                </option>
                <% } %>
            </select>
        </div>

        <!-- Método de pago -->
        <div class="form-input-material">
            <label for="metodoPago">Método de Pago</label>
            <select name="metodoPago" id="metodoPago" required>
                <option value="efectivo" <c:if test="${param.metodoPago == 'efectivo'}">selected</c:if>>Efectivo</option>
                <option value="transferencia" <c:if test="${param.metodoPago == 'transferencia'}">selected</c:if>>Transferencia</option>
                <option value="tarjeta" <c:if test="${param.metodoPago == 'tarjeta'}">selected</c:if>>Tarjeta</option>
                <option value="mercado_pago" <c:if test="${param.metodoPago == 'mercado_pago'}">selected</c:if>>Mercado Pago</option>
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