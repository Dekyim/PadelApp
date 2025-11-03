<%--
  Created by IntelliJ IDEA.
  User: Matías
  Date: 27/10/2025
  Time: 6:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.ArrayList" %>

<%
    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");

    List<String> horariosDia = new ArrayList<>();
    java.time.LocalTime hora = java.time.LocalTime.of(7, 0);
    while (hora.isBefore(java.time.LocalTime.of(22, 0))) {
        horariosDia.add(hora.toString());
        hora = hora.plusMinutes(90);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Agregar Cancha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarCancha.css">
</head>
<body>

<%@include file="/WEB-INF/components/headerAdmin.jsp"%>

<form class="login-form" action="agregarCancha" method="post" enctype="multipart/form-data">
    <h1><i class="bi bi-plus-circle"></i> Agregar Nueva Cancha</h1>

    <% if (mensajeExito != null) { %>
    <div class="alert alert-success"><%= mensajeExito %></div>
    <% } else if (mensajeError != null) { %>
    <div class="alert alert-danger"><%= mensajeError %></div>
    <% } %>

    <div class="form-input-material">
        <label for="numero">Nro° de Cancha</label>
        <input type="number" id="numero" name="numero" required>
    </div>

    <div class="form-input-material">
        <label for="precio">Precio ($)</label>
        <input type="number" step="0.01" id="precio" name="precio" required>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="esTechada" name="esTechada">
        <label class="form-check-label" for="esTechada">Cancha techada</label>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="estaDisponible" name="estaDisponible" checked>
        <label class="form-check-label" for="estaDisponible">Disponible para reserva</label>
    </div>

    <div class="form-input-material horarios-box">
        <label>Horarios disponibles</label>
        <table>
            <tbody>
            <%
                int columnas = 3;
                int count = 0;
                for (String h : horariosDia) {
                    if (count % columnas == 0) { %><tr><% }
            %>
                <td>
                    <input type="checkbox" id="horario_<%= h %>" name="horarios" value="<%= h %>">
                    <label for="horario_<%= h %>"><%= h %></label>
                </td>
                <%
                    count++;
                    if (count % columnas == 0) { %></tr><% }
            }

                if (count % columnas != 0) {
                    for (int i = 0; i < columnas - (count % columnas); i++) {
            %><td></td><% } %></tr><%
                }
            %>
            </tbody>
        </table>
    </div>

    <%--<div class="imagen-cancha text-center mb-4">
        <img src="https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg"
             alt="Foto de la cancha" class="foto-cancha" id="previewImagen" style="cursor:pointer;">

        <div id="panelImagen" style="margin-top: 10px; display: none;">
            <input type="file" id="fotoCancha" name="fotoCancha" accept="image/*" style="display:none;">
            <button type="button" class="btn btn-sm btn-outline-primary" onclick="document.getElementById('fotoCancha').click()">Seleccionar imagen</button>
            <button type="button" class="btn btn-sm btn-outline-danger" onclick="cancelarImagen()">Cancelar</button>
        </div>

        <p class="form-text">Haz clic en la imagen para subir una foto</p>
    </div>--%>


    <div class="acciones">
        <a href="cancha" class="btn btn-outline-secondary">Cancelar</a>
        <button type="submit" class="btn btn-success">Agregar Cancha</button>
    </div>
</form>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%--<script>
    const preview = document.getElementById("previewImagen");
    const panel = document.getElementById("panelImagen");
    const input = document.getElementById("fotoCancha");

    preview.addEventListener("click", () => {
        panel.style.display = "block";
    });

    input.addEventListener("change", () => {
        const file = input.files[0];
        if (file && file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = e => {
                preview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    function cancelarImagen() {
        input.value = "";
        panel.style.display = "none";
        preview.src = "https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg";
    }
</script>--%>

</body>
</html>
