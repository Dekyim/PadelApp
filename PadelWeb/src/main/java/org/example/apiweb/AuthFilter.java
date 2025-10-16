/*package org.example.apiweb;

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
        if (path.endsWith("login") ||
                path.endsWith("registro") ||
                path.endsWith("/") ||
                path.contains("css") ||
                path.contains("img")) {
            chain.doFilter(request, response);
            return;
        }

        // --- 2️⃣ Bloquear si no está logueado ---
        if (usuario == null) {
            System.out.println("[AuthFilter] Acceso bloqueado: usuario no autenticado → " + path);
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // --- 3️⃣ Bloquear si es usuario normal y quiere ver el dashboard ---
        if (path.endsWith("dashboard.jsp") && !usuario.esAdministrador()) {
            System.out.println("[AuthFilter] Acceso denegado: usuario sin permisos de admin → " + path);
            resp.sendRedirect(req.getContextPath() + "/users");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
*/