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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularioGrupo.css">
</head>
<body>

<h2>Crear nuevo grupo</h2>

<form action="${pageContext.request.contextPath}/creargrupo" method="post">
    <input type="hidden" name="idCreador" value="${cedulaUsuario}" />

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
    <input type="number" name="cupos" min="1" required />

    <label for="descripcion">Descripción:</label>
    <textarea name="descripcion" rows="3" required></textarea>

    <button type="submit">Crear grupo</button>
</form>

</body>
</html>