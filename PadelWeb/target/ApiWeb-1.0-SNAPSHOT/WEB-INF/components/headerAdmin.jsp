<aside class="sidebar">

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <h1 class="logo"><a href="<%= request.getContextPath() %>/inicioAdmin">PadelManager</a></h1>
    <nav class="menu">
        <a href="<%= request.getContextPath() %>/inicioAdmin"><i class="fi fi-rr-home"></i> Inicio</a>
        <a href="<%= request.getContextPath() %>/users"><i class="fi fi-rr-users"></i> Usuarios</a>
        <a href="<%= request.getContextPath() %>/cancha"><i class="fi fi-rr-basketball"></i> Canchas</a>
        <a href="<%= request.getContextPath() %>/reserva"><i class="fi fi-rr-calendar"></i> Reservas</a>
        <a href="<%= request.getContextPath() %>/verPerfilAdmin"><i class="fi fi-rr-user"></i> Ver Perfil</a>
        <a href="<%= request.getContextPath() %>/logout"><i class="fi fi-rr-exit"></i> Cerrar sesión</a>
    </nav>
</aside>
