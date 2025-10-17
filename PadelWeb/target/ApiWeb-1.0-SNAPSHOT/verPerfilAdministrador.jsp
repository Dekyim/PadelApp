<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
%>
<html>
<head>
    <title>Perfil del Usuario</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/perfil.css">
    <script>
        function habilitarEdicion() {
            const inputs = document.querySelectorAll(".editable");
            inputs.forEach(input => input.removeAttribute("readonly"));
            document.getElementById("guardarBtn").removeAttribute("disabled");
        }
    </script>
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="card perfil-card mx-auto shadow-lg">
        <div class="card-body text-center">
            <img src="${pageContext.request.contextPath}/img/perfil-default.png" alt="Foto de perfil" class="rounded-circle perfil-img mb-3">
            <h3 class="card-title mb-1"><i class="bi bi-person-circle"></i> <%= usuario.getNombre() %> <%= usuario.getApellido() %></h3>
            <p class="text-muted mb-3"><i class="bi bi-envelope"></i> <%= usuario.getCorreo() %></p>

            <form action="verPerfilAdmin" method="post" class="text-start">
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-credit-card"></i> Cédula</label>
                    <input type="text" name="cedula" class="form-control editable" value="<%= usuario.getCedula() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Nombre</label>
                    <input type="text" name="nombre" class="form-control editable" value="<%= usuario.getNombre() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-person"></i> Apellido</label>
                    <input type="text" name="apellido" class="form-control editable" value="<%= usuario.getApellido() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-envelope"></i> Correo</label>
                    <input type="email" name="correo" class="form-control editable" value="<%= usuario.getCorreo() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-telephone"></i> Teléfono</label>
                    <input type="text" name="telefono" class="form-control editable" value="<%= usuario.getTelefono() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label"><i class="bi bi-calendar-check"></i> Fecha de ingreso</label>
                    <input type="date" name="fechaIngreso" class="form-control editable" value="<%= usuario.getFechaIngreso() %>" readonly>
                </div>

                <div class="d-flex justify-content-between mt-4">
                    <button type="button" class="btn btn-outline-secondary" onclick="habilitarEdicion()">
                        <i class="bi bi-pencil-square"></i> Editar datos
                    </button>
                    <button type="submit" class="btn btn-success" id="guardarBtn" disabled>
                        <i class="bi bi-save"></i> Guardar cambios
                    </button>
                </div>
            </form>

            <a href="inicioAdministrador.jsp" class="btn btn-primary mt-4"><i class="bi bi-arrow-left-circle"></i> Volver al inicio</a>
        </div>
    </div>
</div>
</body>
</html>
