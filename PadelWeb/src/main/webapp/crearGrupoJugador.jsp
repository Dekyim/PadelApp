<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 3/11/2025
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Grupo</title>
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crearGrupo.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerUsuario.jsp"%>

<div class="contenedor-crear-grupo">
    <h2 class="titulo-crear">Crear nuevo grupo</h2>

    <form action="${pageContext.request.contextPath}/creargrupojugador" class="login-form" method="post">
        <c:if test="${not empty grupoEditado}">
            <input type="hidden" name="idGrupo" value="${grupoEditado.idGrupo}" />
        </c:if>

        <input type="hidden" name="idCreador" value="${cedulaUsuario}" />

        <label for="horaDesde">Hora desde:</label>
        <input type="time" name="horaDesde" value="${grupoEditado.horaDesde}" required />

        <label for="horaHasta">Hora hasta:</label>
        <input type="time" name="horaHasta" value="${grupoEditado.horaHasta}" required />

        <label>Categorías permitidas:</label>
        <div class="checkboxes">
            <c:forEach var="cat" items="${categoriasDisponibles}">
                <c:set var="seleccionado" value="${grupoEditado != null && fn:contains(grupoEditado.categoria, cat)}" />
                <div>
                    <input type="checkbox" name="categorias" value="${cat}" ${seleccionado ? "checked" : ""} />
                    <label>${cat}</label>
                </div>
            </c:forEach>
        </div>

        <label for="cupos">Cantidad de cupos:</label>
        <select name="cupos" required>
            <c:forEach var="i" begin="${grupoEditado != null ? cantidadActual : 1}" end="3">
                <option value="${i}" ${grupoEditado != null && grupoEditado.cupos == i ? "selected" : ""}>${i}</option>
            </c:forEach>
        </select>

        <label for="descripcion">Descripción:</label>
        <textarea name="descripcion" rows="3" required>${grupoEditado.descripcion}</textarea>

        <button type="submit">
            <c:choose>
                <c:when test="${not empty grupoEditado}">Actualizar grupo</c:when>
                <c:otherwise>Crear grupo</c:otherwise>
            </c:choose>
        </button>
    </form>

</div>

</body>
</html>
