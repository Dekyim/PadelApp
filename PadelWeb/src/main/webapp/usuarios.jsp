<%@ page import="models.Jugador" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de usuarios</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="css/panelUsuarios.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerAdmin.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Panel de usuarios</h1>
    </div>

    <div class="busquedaAgregar">
        <form action="users" method="get" class="d-flex align-items-center">
            <i class="fi fi-rr-search"></i>
            <input type="text" name="buscar" id="buscar" placeholder="Buscar usuario"
                   class="form-control me-2" value="<%= request.getParameter("buscar") != null ? request.getParameter("buscar") : "" %>">
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>

    <form action="${pageContext.request.contextPath}/agregarUsuario" method="post">
        <input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
        <button type="submit" class="btn-agregar" title="Agregar usuario">
            <i class="fi fi-rr-plus"></i>
        </button>
    </form>

    <section class="listaUsuarios">
        <%
            List<Jugador> jugadores = (List<models.Jugador>) request.getAttribute("jugadores");
            Integer paginaActual = (Integer) request.getAttribute("paginaActual");
            Integer totalPaginas = (Integer) request.getAttribute("totalPaginas");
            Map<String, String> fotosPorCedula = (Map<String, String>) request.getAttribute("fotosPorCedula");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (jugadores != null && !jugadores.isEmpty()) {
                for (models.Jugador j : jugadores) {
                    String urlFoto = fotosPorCedula.get(j.getCedula());
                    if (urlFoto == null || urlFoto.isEmpty()) {
                        urlFoto = "https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg";
                    }
        %>
        <div class="card-usuario">
            <div class="usuario-info">
                <img src="<%= urlFoto %>" alt="Foto de <%= j.getNombre() %>" class="foto-usuario">
                <div class="detalles">
                    <h3 class="<%= (j.isEstaBaneado() || j.isEstaDeBaja()) ? "nombre-alerta" : "" %>">
                        <%= j.getNombre() + " " + j.getApellido() %>
                    </h3>
                    <p><i class="fi fi-rr-id-badge"></i> <%= j.getCedula() %></p>
                    <p><i class="fi fi-rr-envelope"></i> <%= j.getCorreo() != null ? j.getCorreo() : "Sin correo" %></p>
                    <p><i class="fi fi-rr-phone-call"></i> <%= j.getTelefono() != null ? j.getTelefono() : "Sin teléfono" %></p>
                    <p><i class="fi fi-rr-calendar"></i>
                        <%= j.getFechaNacimiento() != null ? sdf.format(j.getFechaNacimiento()) : "Sin fecha" %>
                    </p>

                    <!-- Badges Bootstrap -->
                    <div class="badges">
                        <span class="badge bg-warning text-dark">
                            <i class="fi fi-rr-trophy"></i> <%= j.getCategoria() != null ? j.getCategoria() : "Sin categoría" %>
                        </span>
                        <span class="badge bg-primary">
                            <i class="fi fi-rr-user"></i> <%= j.getGenero() != null ? j.getGenero() : "Sin género" %>
                        </span>
                    </div>

                    <!-- Estado -->
                    <span class="badge <%= j.isEstaBaneado() ? "bg-danger" : (j.isEstaDeBaja() ? "bg-danger" : "bg-success") %>">
                        <%= j.isEstaBaneado() ? "Baneado" : (j.isEstaDeBaja() ? "De baja" : "Activo") %>
                    </span>
                </div>
            </div>

            <div class="acciones-card">
                <form action="users" method="post" style="display:inline;">
                    <input type="hidden" name="accion" value="editar">
                    <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                    <button type="submit" title="Editar" class="btn btn-link text-primary p-0"><i class="fi fi-rr-edit"></i></button>
                </form>

                <form action="users" method="post" style="display:inline;">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                    <button type="submit" title="Eliminar" class="btn btn-link text-danger p-0"
                            onclick="return confirm('¿Eliminar a <%= j.getNombre() %>?')">
                        <i class="fi fi-rr-trash"></i>
                    </button>
                </form>

                <form action="users" method="post" style="display:inline;">
                    <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                    <% if (j.isEstaBaneado()) { %>
                    <input type="hidden" name="accion" value="desbanear">
                    <button type="submit" title="Desbanear" class="btn btn-link text-success p-0"
                            onclick="return confirm('¿Desbanear a <%= j.getNombre() %>?')">
                        <i class="fi fi-rr-unlock"></i>
                    </button>
                    <% } else { %>
                    <input type="hidden" name="accion" value="banear">
                    <button type="submit" title="Banear" class="btn btn-link text-danger p-0"
                            onclick="return confirm('¿Banear a <%= j.getNombre() %>?')">
                        <i class="fi fi-rr-lock"></i>
                    </button>
                    <% } %>
                </form>

                <form action="users" method="post" style="display:inline;">
                    <input type="hidden" name="cedula" value="<%= j.getCedula() %>">
                    <% if (j.isEstaDeBaja()) { %>
                    <input type="hidden" name="accion" value="reactivar">
                    <button type="submit" title="Reactivar" class="btn btn-link text-success p-0"
                            onclick="return confirm('¿Reactivar a <%= j.getNombre() %>?')">
                        <i class="fi fi-rr-user-add"></i>
                    </button>
                    <% } else { %>
                    <input type="hidden" name="accion" value="darDeBaja">
                    <button type="submit" title="Dar de baja" class="btn btn-link text-danger p-0"
                            onclick="return confirm('¿Dar de baja a <%= j.getNombre() %>?')">
                        <i class="fi fi-rr-user-delete"></i>
                    </button>
                    <% } %>
                </form>

                <button type="button" title="Ver reservas"
                        onclick="window.location.href='${pageContext.request.contextPath}/verReservaUsuario?cedula=<%= j.getCedula() %>'">
                    <i class="fi fi-rr-document"></i>
                </button>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="mensaje-vacio">
            <i class="fi fi-rr-users"></i>
            <p>No hay usuarios registrados</p>
        </div>
        <% } %>
    </section>
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
