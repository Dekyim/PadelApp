package org.example.apiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Invalida la sesión actual si existe
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirige al HelloServlet (el que decide a dónde mandar al usuario)
        resp.sendRedirect(req.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Permitir también método POST
        doGet(req, resp);
    }
}
