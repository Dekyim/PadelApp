<%@ page import="models.Jugador" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de control</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="css/panel.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerAdmin.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Gestión de usuarios</h1>
    </div>

    <div class="busquedaAgregar">
        <form action="users" method="get" class="d-flex align-items-center">
            <i class="fi fi-rr-search"></i>
            <input type="text" name="buscar" id="buscar" placeholder="Buscar usuario"
                   class="form-control me-2" value="<%= request.getParameter("buscar") != null ? request.getParameter("buscar") : "" %>">
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>


    <form action="${pageContext.request.contextPath}/registro" method="post">
        <input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
        <button type="submit" class="btn-agregar">Agregar usuario</button>
    </form>



    <div class="listaUser">
        <ul>
            <%
                List<Jugador> jugadores = (List<models.Jugador>) request.getAttribute("jugadores");
                Integer paginaActual = (Integer) request.getAttribute("paginaActual");
                Integer totalPaginas = (Integer) request.getAttribute("totalPaginas");

                if (jugadores != null && !jugadores.isEmpty()) {
                    for (models.Jugador j : jugadores) {
            %>
            <li>
                <span><%= j.getNombre() + " " + j.getApellido() %> - <%= j.getCedula() %></span>
                <div>
                    <form action="users" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                        <button type="submit" title="Eliminar" onclick="return confirm('¿Eliminar a <%= j.getNombre() %>?')">
                            <i class="fi fi-rr-trash"></i>
                        </button>
                    </form>
                    <form action="users" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="editar">
                        <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                        <button type="submit" title="Editar">
                            <i class="fi fi-rr-user-pen"></i>
                        </button>
                    </form>

                    <button title="Ver"><i class="fi fi-rr-document"></i></button>
                </div>
            </li>
            <%
                }
            } else {
            %>
            <li>No hay usuarios disponibles.</li>
            <% } %>
        </ul>
    </div>
</div>

<nav aria-label="Paginación de usuarios" class="mt-4">
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

