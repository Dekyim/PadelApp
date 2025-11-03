<%@ page import="models.Cancha" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Cancha> listaCanchas = (List<Cancha>) request.getAttribute("listaCanchas");
    Map<Integer, String> fotosPorId = (Map<Integer, String>) request.getAttribute("fotosPorId");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Canchas</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panelCanchas.css">
</head>
<body>

<div class="panel">
    <%@include file="/WEB-INF/components/headerAdmin.jsp"%>

    <div class="titulo">
        <h1 class="tituloGestion">Panel de canchas</h1>
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

    <form action="agregarCancha" method="get">
        <button type="submit" class="btn-agregar" title="Agregar cancha">
            <i class="fi fi-rr-plus"></i>
        </button>
    </form>

    <div class="listaUser">
        <ul class="grid-canchas">
            <% if (listaCanchas != null && !listaCanchas.isEmpty()) {
                for (Cancha cancha : listaCanchas) {
                    String urlFoto = fotosPorId.get(cancha.getId());
                    if (urlFoto == null || urlFoto.isEmpty()) {
                        urlFoto = "https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg";
                    }
            %>
            <li class="tarjeta-usuario" onclick="toggleAcciones(this)">
                <img src="<%= urlFoto %>" alt="Cancha <%= cancha.getNumero() %>" class="foto-usuario">
                <span class="nombre-usuario">Cancha N° <%= cancha.getNumero() %></span>

                <div class="acciones" style="display: none;">
                    <form action="cancha" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="numero" value="<%= cancha.getNumero() %>">
                        <button type="submit" title="Eliminar"
                                onclick="return confirm('¿Eliminar la cancha Nro <%= cancha.getNumero() %>?')">
                            <i class="fi fi-rr-trash"></i>
                        </button>
                    </form>

                    <form action="editarCancha" method="get" style="display:inline;">
                        <input type="hidden" name="numero" value="<%= cancha.getNumero() %>">
                        <button type="submit" title="Editar">
                            <i class="fi fi-rr-user-pen"></i>
                        </button>
                    </form>

                    <button type="button" title="Ver"
                            onclick="window.location.href='${pageContext.request.contextPath}/verReservaCancha?numeroCancha=<%= cancha.getNumero() %>'">
                        <i class="fi fi-rr-document"></i>
                    </button>
                </div>
            </li>
            <% } } else { %>
            <li>No hay canchas disponibles.</li>
            <% } %>
        </ul>
    </div>
</div>

<script>
    function toggleAcciones(elemento) {
        const acciones = elemento.querySelector(".acciones");
        acciones.style.display = acciones.style.display === "none" || acciones.style.display === "" ? "flex" : "none";
    }
</script>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>