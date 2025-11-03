<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 3/11/2025
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Grupos del Jugador</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tarjetaGrupo.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="contenedor-grupos">

    <!-- Botón para crear nuevo grupo -->
    <div class="crear-grupo-btn mb-4">
        <form action="${pageContext.request.contextPath}/creargrupo" method="get">
            <button type="submit" class="btn btn-success">
                <i class="fi fi-rr-plus"></i> Crear nuevo grupo
            </button>
        </form>
    </div>

    <!-- ✅ Sección: Mis grupos -->
    <h3 class="mb-3">Mis grupos</h3>
    <c:choose>
        <c:when test="${not empty gruposDelJugador}">
            <c:forEach var="grupo" items="${gruposDelJugador}">
                <c:set var="cedulasParticipantes" value="${participantesPorGrupo[grupo.idGrupo]}" />
                <c:set var="cuposRestantes" value="${grupo.cupos + 1 - fn:length(cedulasParticipantes)}" />

                <div class="tarjeta-grupo mb-4">
                    <div class="info-grupo">
                        <p><b>Horario:</b> ${grupo.horaDesde} a ${grupo.horaHasta}</p>

                        <c:set var="categoriasArray" value="${fn:split(grupo.categoria, ',')}" />
                        <p><b>Categorías permitidas:</b></p>
                        <div class="mb-2">
                            <c:forEach var="cat" items="${categoriasArray}">
                                <c:set var="categoria" value="${fn:toUpperCase(fn:substring(cat, 0, 1))}${fn:substring(cat, 1, fn:length(cat))}" />
                                <span class="badge bg-primary me-1">${categoria}</span>
                            </c:forEach>
                        </div>

                        <p><b>Descripción:</b> ${grupo.descripcion}</p>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center">No estás inscrito en ningún grupo.</p>
        </c:otherwise>
    </c:choose>

    <!-- ✅ Sección: Grupos disponibles -->
    <h3 class="mt-5 mb-3">Grupos disponibles</h3>
    <c:choose>
        <c:when test="${not empty gruposDisponibles}">
            <c:forEach var="grupo" items="${gruposDisponibles}">
                <c:set var="cedulasParticipantes" value="${participantesPorGrupo[grupo.idGrupo]}" />
                <c:set var="cuposRestantes" value="${grupo.cupos + 1 - fn:length(cedulasParticipantes)}" />

                <div class="tarjeta-grupo mb-4">
                    <div class="info-grupo">
                        <p><b>Horario:</b> ${grupo.horaDesde} a ${grupo.horaHasta}</p>

                        <c:set var="categoriasArray" value="${fn:split(grupo.categoria, ',')}" />
                        <p><b>Categorías permitidas:</b></p>
                        <div class="mb-2">
                            <c:forEach var="cat" items="${categoriasArray}">
                                <c:set var="categoria" value="${fn:toUpperCase(fn:substring(cat, 0, 1))}${fn:substring(cat, 1, fn:length(cat))}" />
                                <span class="badge bg-primary me-1">${categoria}</span>
                            </c:forEach>
                        </div>

                        <p><b>Descripción:</b> ${grupo.descripcion}</p>
                    </div>

                    <div class="cupos">
                        <c:forEach var="i" begin="1" end="${cuposRestantes}">
                            <form action="${pageContext.request.contextPath}/unirsegrupo" method="post" class="form-cupo">
                                <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                                <input type="hidden" name="cedulaUsuario" value="${cedulaUsuario}" />
                                <button type="submit" class="btn btn-outline-primary me-2" title="Unirse al grupo">
                                    <i class="fi fi-rr-plus"></i> Unirse
                                </button>
                            </form>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center">No hay grupos disponibles para tu categoría.</p>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>