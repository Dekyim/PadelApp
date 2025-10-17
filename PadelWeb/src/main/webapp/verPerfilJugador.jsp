<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Jugador" %>
<%
    Jugador jugador = (Jugador) request.getAttribute("jugador");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Perfil del Jugador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/perfilJugador.css">
    <script>
        function habilitarEdicion() {
            document.querySelectorAll(".editable").forEach(el => el.removeAttribute("readonly"));
            document.querySelectorAll(".editable-select").forEach(el => el.removeAttribute("disabled"));
            document.getElementById("guardarBtn").removeAttribute("disabled");
        }
    </script>
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="card perfil-card mx-auto shadow-lg">
        <div class="card-body text-center">
            <img src="${pageContext.request.contextPath}/img/perfil-default.png" alt="Foto de perfil" class="rounded-circle perfil-img mb-3">
            <h3 class="card-title mb-3"><i class="bi bi-person-circle"></i> Jugador</h3>

            <form action="verPerfilJugador" method="post" class="text-start">
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-credit-card"></i> Cédula</label>
                    <input type="text" name="cedula" class="form-control" value="<%= jugador.getCedula() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-calendar"></i> Fecha de nacimiento</label>
                    <input type="date" name="fechaNacimiento" class="form-control editable" value="<%= jugador.getFechaNacimiento() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-award"></i> Categoría</label>
                    <input type="text" name="categoria" class="form-control editable" value="<%= jugador.getCategoria() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-gender-ambiguous"></i> Género</label>
                    <select name="genero" class="form-select editable-select" disabled>
                        <option value="Masculino" <%= jugador.getGenero().equals("Masculino") ? "selected" : "" %>>Masculino</option>
                        <option value="Femenino" <%= jugador.getGenero().equals("Femenino") ? "selected" : "" %>>Femenino</option>
                        <option value="Otro" <%= jugador.getGenero().equals("Otro") ? "selected" : "" %>>Otro</option>
                    </select>
                </div>
                <div class="form-check mb-2">
                    <input class="form-check-input editable-select" type="checkbox" name="estaBaneado"
                        <%= jugador.isEstaBaneado() ? "checked" : "" %> disabled>
                    <label class="form-check-label"><i class="bi bi-slash-circle"></i> ¿Está baneado?</label>
                </div>
                <div class="form-check mb-4">
                    <input class="form-check-input editable-select" type="checkbox" name="incumplePago"
                        <%= jugador.isIncumplePago() == 1 ? "checked" : "" %> disabled>
                    <label class="form-check-label"><i class="bi bi-cash"></i> ¿Incumple pago?</label>
                </div>

                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-outline-secondary" onclick="habilitarEdicion()">
                        <i class="bi bi-pencil-square"></i> Editar datos
                    </button>
                    <button type="submit" class="btn btn-success" id="guardarBtn" disabled>
                        <i class="bi bi-save"></i> Guardar cambios
                    </button>
                </div>
            </form>

            <a href="inicioJugador.jsp" class="btn btn-primary mt-4"><i class="bi bi-arrow-left-circle"></i> Volver al inicio</a>
        </div>
    </div>
</div>
</body>