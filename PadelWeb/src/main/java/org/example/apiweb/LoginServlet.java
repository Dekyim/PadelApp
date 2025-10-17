package org.example.apiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Usuario;
import dao.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String csrfTokenSession = (String) req.getSession().getAttribute("csrfToken");
        String csrfTokenForm = req.getParameter("csrfToken");

        if (csrfTokenSession == null || csrfTokenForm == null || !csrfTokenSession.equals(csrfTokenForm)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solicitud no válida (CSRF detectado)");
            return;
        }
        String cedula = req.getParameter("cedula");
        String password = req.getParameter("contrasenia");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Vector<Usuario> usuarios = usuarioDAO.listarUsuarios(cedula);

        Usuario usuario = null;
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(cedula)) {
                usuario = u;
                break;
            }
        }

        if (usuario == null || !BCrypt.checkpw(password, usuario.getContraseniaCuenta())) {
            req.setAttribute("error", "Cédula o contraseña incorrecta.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();

        HttpSession session = req.getSession(true);
        session.setAttribute("authUser", usuario);
        session.setMaxInactiveInterval(30 * 60); // 30 minutos

        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);


        if (usuario.esAdministrador()) {
            resp.sendRedirect(req.getContextPath() + "/inicioAdmin"); // servlet admin
        } else {
            resp.sendRedirect(req.getContextPath() + "/inicioUsers"); // servlet usuario
        }

    }
}
