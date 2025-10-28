<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Reserva" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    List<Reserva> listaReservas = (List<Reserva>) request.getAttribute("listaReservas");
    Map<String, String> nombresUsuarios = (Map<String, String>) request.getAttribute("nombresUsuarios");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");

    Integer paginaActual = (Integer) request.getAttribute("paginaActual");
    Integer totalPaginas = (Integer) request.getAttribute("totalPaginas");
    if (paginaActual == null) paginaActual = 1;
    if (totalPaginas == null) totalPaginas = 1;

    String ordenParam = request.getParameter("ordenFecha");
    if (ordenParam == null || ordenParam.isEmpty()) {
        ordenParam = "desc";
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Reservas</title>
    <link rel="stylesheet" href="css/panelReserva.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerAdmin.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Panel de reservas</h1>
    </div>

    <div class="container mt-3">
        <% if (mensajeExito != null) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensajeExito %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } else if (mensajeError != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= mensajeError %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } %>
    </div>

    <form method="get" action="reserva" class="mb-4">
        <div class="row">
            <div class="col"><input type="text" name="cedulaUsuario" class="form-control" placeholder="Nombre" value="${param.cedulaUsuario}"></div>
            <div class="col">
                <select name="numeroCancha" class="form-select">
                    <option value="">Cancha</option>
                    <c:forEach var="cancha" items="${listaCanchas}">
                        <option value="${cancha.numero}" <c:if test="${param.numeroCancha == cancha.numero}">selected</c:if>>Cancha Nº ${cancha.numero}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col"><input type="date" name="fecha" class="form-control" value="${param.fecha}"></div>
            <div class="col">
                <select name="ordenFecha" class="form-select">
                    <option value="">Orden por fecha</option>
                    <option value="asc" <%= "asc".equals(ordenParam) ? "selected" : "" %>>Ascendente</option>
                    <option value="desc" <%= "desc".equals(ordenParam) ? "selected" : "" %>>Descendente</option>
                </select>
            </div>
            <div class="col">
                <select name="metodoPago" class="form-select">
                    <option value="">Método de pago</option>
                    <option value="efectivo" <c:if test="${param.metodoPago == 'efectivo'}">selected</c:if>>Efectivo</option>
                    <option value="transferencia" <c:if test="${param.metodoPago == 'transferencia'}">selected</c:if>>Transferencia</option>
                    <option value="tarjeta" <c:if test="${param.metodoPago == 'tarjeta'}">selected</c:if>>Tarjeta</option>
                </select>
            </div>
            <div class="col">
                <select name="estadoPago" class="form-select">
                    <option value="">¿Pagada?</option>
                    <option value="pagadas" <c:if test="${param.estadoPago == 'pagadas'}">selected</c:if>>Sí</option>
                    <option value="nopagadas" <c:if test="${param.estadoPago == 'nopagadas'}">selected</c:if>>No</option>
                </select>
            </div>
            <div class="col">
                <select name="estadoActiva" class="form-select">
                    <option value="">¿Activa?</option>
                    <option value="activas" <c:if test="${param.estadoActiva == 'activas'}">selected</c:if>>Sí</option>
                    <option value="noactivas" <c:if test="${param.estadoActiva == 'noactivas'}">selected</c:if>>No</option>
                </select>
            </div>
            <div class="col">
                <button type="submit" class="btn btn-primary">Filtrar</button>
                <a href="reserva" class="btn btn-secondary">Limpiar</a>
            </div>
        </div>
    </form>

    <form action="${pageContext.request.contextPath}/crearreserva" method="get">
        <input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
<<<<<<< HEAD
        <button type="submit" class="btn-agregar"><i class="fi fi-rr-plus"></i></button>
=======
        <button type="submit" class="btn-agregar">+</button>
>>>>>>> cab707dc010e9444d0a8afdfdad4f57cf7d8e377
    </form>

    <div class="listaUser">
        <ul>
            <% if (listaReservas != null && !listaReservas.isEmpty()) {
                for (Reserva reserva : listaReservas) {
                    String nombreCompleto = nombresUsuarios.get(reserva.getCedulaUsuario());
            %>
            <li>
                <span>
                    <b>Usuario:</b> <%= nombreCompleto %> (<%= reserva.getCedulaUsuario() %>)<br>
                    <b>Cancha:</b> Nº <%= reserva.getNumeroCancha() %><br>
                    <b>Fecha:</b> <%= reserva.getFecha() %><br>
                    <b>Horario:</b> <%= reserva.getHorarioInicio() %> - <%= reserva.getHorarioFinal() %><br>
                    <b>Método:</b> <%= reserva.getMetodoPago().getValue() %><br>
                    <b>Pagada:</b> <%= reserva.isEstaPagada() ? "Sí" : "No" %><br>
                    <b>Activa:</b> <%= reserva.isEstaActiva() ? "Sí" : "No" %>
                </span>
                <div>
                    <form action="cancelarReserva" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <input type="hidden" name="accion" value="cancelar">
                        <button type="submit" title="Cancelar" onclick="return confirm('¿Cancelar la reserva de <%= nombreCompleto %>?')">
                            <i class="fi fi-rr-trash"></i>
                        </button>
                    </form>

                    <form action="editarReserva" method="get" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <button type="submit" title="Editar">
                            <i class="fi fi-rr-user-pen"></i>
                        </button>
                    </form>
                    <% if (reserva.isEstaActiva()) { %>
                    <% if (reserva.isEstaPagada()) { %>
                    <form action="reserva" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <input type="hidden" name="accion" value="despagar">
                        <button type="submit" title="Cancelar pago"
                                onclick="return confirm('¿Cancelar el pago de la reserva de <%= nombreCompleto %>?')">
                            <i class="fi fi-rr-cross-circle"></i>
                        </button>
                    </form>
                    <% } else { %>
                    <form action="reserva" method="post" style="display:inline;">
                        <input type="hidden" name="idReserva" value="<%= reserva.getId() %>">
                        <input type="hidden" name="accion" value="pagar">
                        <button type="submit" title="Pagar reserva"
                                onclick="return confirm('¿Confirmar pago de la reserva de <%= nombreCompleto %>?')">
                            <i class="fi fi-rr-money-check"></i>
                        </button>
                    </form>
                    <% } %>
                    <% } %>

                </div>
            </li>
            <% } } else { %>
            <li>No hay reservas registradas.</li>
            <% } %>
        </ul>
    </div>

    <!-- 🔽 PAGINACIÓN -->
    <nav aria-label="Paginación de reservas" class="mt-4">
        <ul class="pagination justify-content-center">
            <% if (paginaActual > 1) { %>
            <li class="page-item"><a class="page-link" href="?page=<%= paginaActual - 1 %>">Anterior</a></li>
            <% } else { %>
            <li class="page-item disabled"><span class="page-link">Anterior</span></li>
            <% } %>

            <% for (int i = 1; i <= totalPaginas; i++) { %>
            <li class="page-item <%= (i == paginaActual) ? "active" : "" %>">
                <a class="page-link" href="?page=<%= i %>"><%= i %></a>
            </li>
            <% } %>

            <% if (paginaActual < totalPaginas) { %>
            <li class="page-item"><a class="page-link" href="?page=<%= paginaActual + 1 %>">Siguiente</a></li>
            <% } else { %>
            <li class="page-item disabled"><span class="page-link">Siguiente</span></li>
            <% } %>
        </ul>
    </nav>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
