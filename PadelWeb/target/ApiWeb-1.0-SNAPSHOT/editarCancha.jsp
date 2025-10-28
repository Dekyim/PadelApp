<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Cancha, java.sql.Time, java.util.Vector" %>
<%
    Cancha cancha = (Cancha) request.getAttribute("cancha");
    String urlFoto = (String) request.getAttribute("urlFoto");

    if (cancha == null) {
        response.sendRedirect("cancha");
        return;
    }

    if (urlFoto == null || urlFoto.isEmpty()) {
        urlFoto = "https://res.cloudinary.com/doqev0ese/image/upload/v1761177930/Captura_de_pantalla_2025-10-22_210510_ni5giw.jpg";
    }

    java.util.List<String> horariosDia = new java.util.ArrayList<>();
    java.time.LocalTime hora = java.time.LocalTime.of(7, 0);
    while (hora.isBefore(java.time.LocalTime.of(22, 0))) {
        horariosDia.add(hora.toString());
        hora = hora.plusMinutes(90);
    }

    Vector<Time> horariosCancha = cancha.getHorario() != null
            ? cancha.getHorario().getHorarios()
            : new Vector<>();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Editar Cancha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarCancha.css">
    <style>
        .foto-cancha {
            width: 280px;
            height: 160px;
            object-fit: cover;
            border-radius: 12px;
            border: 3px solid #00b95e;
            box-shadow: 0 0 8px rgba(0, 185, 94, 0.4);
            transition: transform 0.2s ease;
        }
        .foto-cancha:hover {
            transform: scale(1.02);
        }
    </style>
</head>
<body>
<form class="login-form" action="editarCancha" method="post" enctype="multipart/form-data">
    <h1><i class="bi bi-pencil-square"></i> Editar Cancha N° <%= cancha.getNumero() %></h1>

    <input type="hidden" name="numero" value="<%= cancha.getNumero() %>">

    <!-- Imagen actual y panel de selección -->
    <div class="imagen-cancha text-center mb-4">
        <img src="<%= urlFoto %>" alt="Foto de la cancha" class="foto-cancha" id="previewImagen" style="cursor:pointer;">

        <div id="panelImagen" style="margin-top: 10px; display: none;">
            <input type="file" id="fotoCancha" name="fotoCancha" accept="image/*" style="display:none;">
            <button type="button" class="btn btn-sm btn-outline-primary" onclick="document.getElementById('fotoCancha').click()">Seleccionar imagen</button>
            <button type="button" class="btn btn-sm btn-outline-danger" onclick="cancelarImagen()">Cancelar</button>
        </div>

        <p class="form-text">Haz clic en la imagen para cambiarla</p>
    </div>

    <div class="form-input-material">
        <label for="precio">Precio ($)</label>
        <input type="number" step="0.01" id="precio" name="precio" value="<%= cancha.getPrecio() %>" required>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="esTechada" name="esTechada" <%= cancha.isEsTechada() ? "checked" : "" %>>
        <label class="form-check-label" for="esTechada">Cancha techada</label>
    </div>

    <div class="form-check">
        <input class="form-check-input" type="checkbox" id="estaDisponible" name="estaDisponible" <%= cancha.isEstaDisponible() ? "checked" : "" %>>
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
                    boolean seleccionado = horariosCancha.stream()
                            .anyMatch(t -> t.toString().startsWith(h));

                    if (count % columnas == 0) { %><tr><% }
            %>
                <td>
                    <input type="checkbox" id="horario_<%= h %>" name="horarios" value="<%= h %>" <%= seleccionado ? "checked" : "" %>>
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

    <div class="acciones">
        <a href="cancha" class="btn btn-outline-secondary">Volver</a>
        <button type="submit" class="btn btn-success">Guardar cambios</button>
    </div>
</form>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
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
        preview.src = "<%= urlFoto %>";
    }
</script>
</body>
</html>


