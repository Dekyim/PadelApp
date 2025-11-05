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
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerUsuario.jsp"%>

<main class="contenedor-grupos">
    <c:if test="${not empty mensajeError}">
        <div id="mensajeErrorOverlay">
            <div class="mensajeErrorBox">
                <button class="cerrarError" onclick="document.getElementById('mensajeErrorOverlay').remove()">×</button>
                    ${mensajeError}
            </div>
        </div>
    </c:if>


    <form action="${pageContext.request.contextPath}/creargrupojugador" method="get">
        <button type="submit" class="btn-agregar-jugador">
            <i class="fi fi-rr-plus"></i>
        </button>
    </form>

    <h2 class="titulo-jugador">Grupos del Jugador</h2>

    <h3 class="mb-3">Mis grupos</h3>
    <c:choose>
        <c:when test="${not empty gruposDelJugador}">
            <c:forEach var="grupo" items="${gruposDelJugador}">
                <c:set var="cedulasParticipantes" value="${participantesPorGrupo[grupo.idGrupo]}" />
                <c:set var="cuposRestantes" value="${grupo.cupos + 1 - fn:length(cedulasParticipantes)}" />

                <div class="tarjeta-grupo">
                <div class="info-grupo mb-3">
                        <p><b>Horario:</b> ${grupo.horaDesde} a ${grupo.horaHasta}</p>
                        <p><b>Cupos restantes:</b> ${cuposRestantes}</p>

                        <p><b>Categorías permitidas:</b></p>
                        <div class="mb-2">
                            <c:forEach var="cat" items="${fn:split(grupo.categoria, ',')}">
                                <span class="badge bg-primary me-1">${cat}</span>
                            </c:forEach>
                        </div>

                        <p><b>Descripción:</b> ${grupo.descripcion}</p>
                    </div>

                    <div class="d-flex gap-3 flex-wrap">
                        <div class="jugador text-center">
                            <img src="${fotosJugadores[grupo.idCreador]}" class="foto-jugador" alt="Foto del creador">
                            <p>${nombresJugadores[grupo.idCreador]}</p>
                        </div>

                        <c:forEach var="cedula" items="${cedulasParticipantes}">
                            <c:if test="${cedula != grupo.idCreador}">
                                <div class="jugador text-center">
                                    <img src="${fotosJugadores[cedula]}" class="foto-jugador" alt="Foto del jugador">
                                    <p>${nombresJugadores[cedula]}</p>

                                    <c:if test="${grupo.idCreador == cedulaUsuario}">
                                        <form action="${pageContext.request.contextPath}/grupojugador" method="post" class="mt-2">
                                            <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                                            <input type="hidden" name="accion" value="expulsar" />
                                            <input type="hidden" name="cedulaJugador" value="${cedula}" />
                                            <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('¿Estás seguro de que querés expulsar a este jugador?')">
                                                <i class="fi fi-rr-user-delete"></i> Expulsar
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:forEach>

                    </div>
                    <div class="acciones-grupo mt-3 d-flex gap-2">
                    <c:if test="${grupo.idCreador == cedulaUsuario}">
                        <form action="${pageContext.request.contextPath}/grupojugador" method="post">
                            <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                            <input type="hidden" name="accion" value="cerrar" />
                            <button type="submit" class="btn btn-warning">
                                <i class="fi fi-rr-lock"></i> Cerrar
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/grupojugador" method="post">
                            <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                            <input type="hidden" name="accion" value="eliminar" />
                            <button type="submit" class="btn btn-danger">
                                <i class="fi fi-rr-trash"></i> Eliminar
                            </button>
                        </form>
                        <form action="${pageContext.request.contextPath}/creargrupojugador" method="get">
                            <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                            <button type="submit" class="btn btn-info">
                                <i class="fi fi-rr-edit"></i> Editar
                            </button>
                        </form>
                    </div>
                    </c:if>
                    <c:if test="${grupo.idCreador != cedulaUsuario}">
                        <form action="${pageContext.request.contextPath}/grupojugador" method="post" class="d-inline">
                            <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                            <input type="hidden" name="accion" value="salir" />
                            <button type="submit" class="btn btn-outline-secondary btn-sm btn-salir">
                                <i class="fi fi-rr-exit"></i> Salir del grupo
                            </button>
                        </form>
                    </c:if>

                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center">No estás inscrito en ningún grupo.</p>
        </c:otherwise>
    </c:choose>

    <h3 class="mt-5 mb-3">Grupos disponibles</h3>
    <c:choose>
        <c:when test="${not empty gruposDisponibles}">
            <c:forEach var="grupo" items="${gruposDisponibles}">
                <c:set var="cedulasParticipantes" value="${participantesPorGrupo[grupo.idGrupo]}" />
                <c:set var="cuposRestantes" value="${grupo.cupos + 1 - fn:length(cedulasParticipantes)}" />

                <div class="tarjeta-grupo">
                <div class="info-grupo mb-3">
                        <p><b>Horario:</b> ${grupo.horaDesde} a ${grupo.horaHasta}</p>
                        <p><b>Cupos restantes:</b> ${cuposRestantes}</p>

                        <p><b>Categorías permitidas:</b></p>
                        <div class="mb-2">
                            <c:forEach var="cat" items="${fn:split(grupo.categoria, ',')}">
                                <span class="badge bg-primary me-1">${cat}</span>
                            </c:forEach>
                        </div>

                        <p><b>Descripción:</b> ${grupo.descripcion}</p>
                    </div>

                    <div class="d-flex gap-3 flex-wrap align-items-center">
                        <div class="jugador text-center">
                            <img src="${fotosJugadores[grupo.idCreador]}" class="foto-jugador" alt="Foto del creador">
                            <p>${nombresJugadores[grupo.idCreador]}</p>
                        </div>

                        <c:forEach var="cedula" items="${cedulasParticipantes}">
                            <c:if test="${cedula != grupo.idCreador}">
                                <div class="jugador text-center">
                                    <img src="${fotosJugadores[cedula]}" class="foto-jugador" alt="Foto del jugador">
                                    <p>${nombresJugadores[cedula]}</p>
                                </div>
                            </c:if>
                        </c:forEach>

                        <div class="cupos ms-auto">
                            <c:forEach var="i" begin="1" end="${cuposRestantes}">
                                <form action="${pageContext.request.contextPath}/unirsegrupo" method="post" class="d-inline">
                                    <input type="hidden" name="idGrupo" value="${grupo.idGrupo}" />
                                    <input type="hidden" name="cedulaUsuario" value="${cedulaUsuario}" />
                                    <button type="submit" class="btn btn-outline-success btn-sm" title="Unirse al grupo">
                                        <i class="fi fi-rr-plus"></i>
                                    </button>
                                </form>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center">No hay grupos disponibles para tu categoría.</p>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>