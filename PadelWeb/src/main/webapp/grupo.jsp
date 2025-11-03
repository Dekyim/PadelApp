<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 3/11/2025
  Time: 17:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Grupos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tarjetaGrupo.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="contenedor-grupos">

    <!-- Botón para crear nuevo grupo -->
    <div class="crear-grupo-btn">
        <form action="${pageContext.request.contextPath}/creargrupo" method="get">
            <button type="submit" class="btn-crear-grupo">
                <i class="fi fi-rr-plus"></i> Crear nuevo grupo
            </button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty listaGrupos}">
            <c:forEach var="grupo" items="${listaGrupos}">
                <c:set var="cedulasParticipantes" value="${participantesPorGrupo[grupo.idGrupo]}" />
                <c:set var="cuposRestantes" value="${grupo.cupos + 1 - fn:length(cedulasParticipantes)}" />

                <div class="tarjeta-grupo">
                    <div class="info-grupo">
                        <p><b>Horario:</b> ${grupo.horaDesde} a ${grupo.horaHasta}</p>

                        <c:set var="categorias" value="${grupo.categoria}" />
                        <c:set var="categoriasArray" value="${fn:split(categorias, ',')}" />
                        <p><b>Categorías permitidas:</b></p>
                        <div class="mb-2">
                            <c:forEach var="cat" items="${categoriasArray}">
                                <c:set var="categoria" value="${fn:toUpperCase(fn:substring(cat, 0, 1))}${fn:substring(cat, 1, fn:length(cat))}" />
                                <span class="badge bg-primary me-1">${categoria}</span>
                            </c:forEach>
                        </div>

                        <p><b>Descripción:</b> ${grupo.descripcion}</p>
                    </div>

                    <div class="jugador">
                        <img src="${fotosJugadores[grupo.idCreador]}" class="foto-jugador" alt="Foto del creador">
                        <p>${nombresJugadores[grupo.idCreador]}</p>
                    </div>

                    <c:forEach var="cedula" items="${cedulasParticipantes}">
                        <c:if test="${cedula != grupo.idCreador}">
                            <div class="jugador">
                                <img src="${fotosJugadores[cedula]}" class="foto-jugador" alt="Foto del jugador">
                                <p>${nombresJugadores[cedula]}</p>
                            </div>
                        </c:if>
                    </c:forEach>

                    <div class="cupos">
                        <c:forEach var="i" begin="1" end="${cuposRestantes}">
                            <form action="${pageContext.request.contextPath}/unirseGrupo" method="post" class="form-cupo">
                                <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                                <input type="hidden" name="cedulaUsuario" value="${sessionScope.cedulaUsuario}" />
                                <button type="submit" class="btn-cupo" title="Unirse al grupo">
                                    <i class="fi fi-rr-plus"></i>
                                </button>
                            </form>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center">No hay grupos disponibles en este momento.</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>