<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Administración - PadelManager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet"
          href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">

    <!-- Google Charts -->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart', 'bar']});
        google.charts.setOnLoadCallback(drawCharts);

        function drawCharts() {

            var dataReservas = google.visualization.arrayToDataTable([
                ['Tipo', 'Cantidad'],
                ['Pagadas', <%= request.getAttribute("reservasPagadas") %>],
                ['No pagadas', <%= request.getAttribute("reservasNoPagadas") %>]
            ]);

            var optionsReservas = {
                title: 'Reservas pagadas vs no pagadas',
                pieHole: 0.4
            };

            var chartReservas = new google.visualization.PieChart(document.getElementById('chartReservas'));
            chartReservas.draw(dataReservas, optionsReservas);

            var dataCanchas = google.visualization.arrayToDataTable([
                ['Tipo', 'Cantidad'],
                ['Totales', <%= request.getAttribute("totalCanchas") %>],
                ['Techadas', <%= request.getAttribute("canchasTechadas") %>],
                ['Disponibles', <%= request.getAttribute("canchasDisponibles") %>]
            ]);

            var optionsCanchas = {
                title: 'Canchas',
                chartArea: {width: '50%'},
                hAxis: {
                    title: 'Cantidad',
                    minValue: 0
                },
                vAxis: {
                    title: 'Tipo'
                },
                bars: 'horizontal'
            };

            var chartCanchas = new google.visualization.BarChart(document.getElementById('chartCanchas'));
            chartCanchas.draw(dataCanchas, optionsCanchas);
        }
    </script>
</head>
<body>

<%@include file="/WEB-INF/components/headerAdmin.jsp"%>

<main class="hero text-center py-5">
    <div class="hero-content container">
        <h2 class="tituloPrincipal mb-3">Panel de Control</h2>
        <p class="lead mb-4">
            Estadísticas generales y gestión del sistema PadelManager.
        </p>
    </div>
</main>

<section class="links container my-5 d-flex justify-content-center gap-4 flex-wrap">
    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-chart-histogram fs-1 mb-3"></i>
        <h3>Reservas</h3>
        <p>Ver las reservas, estado de reserva, horario seleccionado.</p>
        <a href="<%= request.getContextPath() %>/reserva" class="btn btn-outline-primary mt-2">Ir al panel</a>
    </div>

    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-users fs-1 mb-3"></i>
        <h3>Usuarios</h3>
        <p>Administra usuarios registrados y roles del sistema.</p>
        <a href="<%= request.getContextPath() %>/users" class="btn btn-outline-primary mt-2">Ir al panel</a>
    </div>

    <div class="link-card card p-4 text-center shadow-sm" style="width: 18rem;">
        <i class="fi fi-rr-basketball fs-1 mb-3"></i>
        <h3>Canchas</h3>
        <p>Gestiona disponibilidad, mantenimiento y horarios.</p>
        <a href="<%= request.getContextPath() %>/cancha" class="btn btn-outline-primary mt-2">Ir al panel</a>
    </div>
</section>

<!-- Pequeñas tarjetas de métricas -->
<section class="container my-5">
    <h3 class="text-center mb-4">Resumen del mes</h3>
    <div class="d-flex flex-wrap justify-content-center gap-4">
        <div class="mini-card">
            <h4>Usuarios / mes</h4>
            <p class="metric"><%= request.getAttribute("totalUsuariosMes") %></p>
        </div>
        <div class="mini-card">
            <h4>Dinero / mes</h4>
            <p class="metric">$<%= request.getAttribute("totalIngresosMes") %></p>
        </div>
        <div class="mini-card">
            <h4>Reservas actuales</h4>
            <p class="metric"><%= request.getAttribute("reservasActivas") %></p>
        </div>
    </div>
</section>

<section class="container my-5">
    <h3 class="text-center mb-4">Gráficos</h3>
    <div class="d-flex flex-column align-items-center gap-5">
        <div style="width: 80%; height: 500px;">
            <h4 class="text-center mb-3">Reservas pagadas vs no pagadas</h4>
            <div id="chartReservas" style="width: 100%; height: 450px;"></div>
        </div>
        <div style="width: 80%; height: 500px;">
            <h4 class="text-center mb-3">Canchas</h4>
            <div id="chartCanchas" style="width: 100%; height: 450px;"></div>
        </div>
    </div>
</section>



<%@include file="/WEB-INF/components/footer.jsp"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-czL6JuqcKJfSTNn2tPbdm1cA4y3Z7rU2q2GVxPzrcTtGkh3qHuvZ4z5gDMSlB/xK"
        crossorigin="anonymous"></script>

</body>
</html>
