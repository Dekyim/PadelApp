package org.example.apiweb;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import models.Usuario;
import java.io.IOException;

// Este filtro se aplica a todo menos login, registro y hello-servlet
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        HttpSession session = req.getSession(false);

        // Obtener usuario logueado (si existe)
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

        // --- 1️⃣ Permitir acceso libre a estas rutas ---
        if (path.endsWith("/login") ||
                path.endsWith("/registro") ||
                path.endsWith("/inicio") ||
                path.contains("css") ||
                path.contains("img")) {
            chain.doFilter(request, response);
            return;
        }


        // --- 2️⃣ Bloquear si no está logueado ---
        if (usuario == null) {
            if (path.endsWith("/verPerfilJugador") ||
                    path.endsWith("/logout") ||
                    path.endsWith("/editarReserva") ||
                    path.endsWith("/crearreserva") ||
                    path.endsWith("/cancelarReserva")) {

                System.out.println("[AuthFilter] Acceso bloqueado: usuario no autenticado → " + path);
                session = req.getSession(true);
                session.setAttribute("mensajeError", "Error: Intento ingresar a una página no autorizada");
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }
        }


        if (usuario == null || usuario.esAdministrador()) {
            if (path.endsWith("/reservasUsuario") ||
                    path.endsWith("/inicioUsers") ||
                    path.endsWith("/canchaUsuario")) {

                System.out.println("[AuthFilter] Acceso denegado: solo jugadores → " + path);
                session = req.getSession(true);
                session.setAttribute("mensajeError", "Error: Intento ingresar a una página no autorizada");
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }
        }


        // --- 3️⃣ Bloquear si es usuario normal y quiere ver el dashboard ---
        if (usuario == null || !usuario.esAdministrador()) {
            if (path.endsWith("/verReservaCancha") ||
                    path.endsWith("/verReservaUsuario") ||
                    path.endsWith("/verPerfilAdmin") ||
                    path.endsWith("/users") ||
                    path.endsWith("/reserva") ||
                    path.endsWith("/inicioAdmin") ||
                    path.endsWith("/editarCancha") ||
                    path.endsWith("/cancha") ||
                    path.endsWith("/agregarUsuario") ||
                    path.endsWith("/agregarCancha")) {

                System.out.println("[AuthFilter] Acceso denegado: solo administradores → " + path);
                session = req.getSession(true);
                session.setAttribute("mensajeError", "Error: Intento ingresar a una página no autorizada");
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }
        }


        chain.doFilter(request, response);
    }
}
