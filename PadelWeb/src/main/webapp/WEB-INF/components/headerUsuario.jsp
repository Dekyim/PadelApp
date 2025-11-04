<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<aside class="sidebar">
    <h1 class="logo"><a href="<%= request.getContextPath() %>/inicioUsers">PadelManager</a></h1>
    <nav class="menu">
        <a href="<%= request.getContextPath() %>/inicioUsers"><i class="fi fi-rr-home"></i> Inicio</a>
        <a href="<%= request.getContextPath() %>/canchaUsuario"><i class="fi fi-rr-users"></i> Canchas</a>
        <a href="<%= request.getContextPath() %>/reservasUsuario"><i class="fi fi-rr-calendar"></i> Reservas </a>
        <a href="<%= request.getContextPath() %>/grupojugador"><i class="fi fi-rr-calendar"></i> Grupo</a>
        <a href="<%= request.getContextPath() %>/verPerfilJugador"><i class="fi fi-rr-user"></i> Ver Perfil </a>
        <a href="<%= request.getContextPath() %>/logout"><i class="fi fi-rr-exit"></i> Cerrar sesión</a>
    </nav>
</aside>
