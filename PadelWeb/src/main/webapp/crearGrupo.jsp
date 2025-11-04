<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 3/11/2025
  Time: 17:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Grupo</title>
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crearGrupo.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerAdmin.jsp"%>

<div class="contenedor-crear-grupo">
    <h2 class="titulo-crear">Crear nuevo grupo</h2>

    <form action="${pageContext.request.contextPath}/creargrupo" class="login-form" method="post">
        <label for="cedulaJugador">Cédula del jugador:</label>
        <input type="text" name="cedulaJugador" required />

        <label for="horaDesde">Hora desde:</label>
        <input type="time" name="horaDesde" required />

        <label for="horaHasta">Hora hasta:</label>
        <input type="time" name="horaHasta" required />

        <label>Categorías permitidas:</label>
        <div class="checkboxes">
            <c:forEach var="cat" items="${categoriasDisponibles}">
                <div>
                    <input type="checkbox" name="categorias" value="${cat}" checked />
                    <label>${cat}</label>
                </div>
            </c:forEach>
        </div>

        <label for="cupos">Cantidad de cupos:</label>
        <select name="cupos" required>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
        </select>

        <label for="descripcion">Descripción:</label>
        <textarea name="descripcion" rows="3" required></textarea>

        <button type="submit">Crear grupo</button>
    </form>
</div>

</body>
</html>
