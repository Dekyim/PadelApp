package org.example.apiweb;

import dao.ReservaDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import models.Usuario;

@WebServlet("/inicioUsers")
public class inicioUsuarioServlet extends HttpServlet {

    private ReservaDAO reservaDAO;

    @Override
    public void init() {
        reservaDAO = new ReservaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        try {
            HttpSession sesion = request.getSession(false);

            if (sesion == null || sesion.getAttribute("authUser") == null) {
                // Si no hay sesión, redirigir al login
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Usuario usuario = (Usuario) sesion.getAttribute("authUser");

            String cedulaUsuario = usuario.getCedula();
            String nombreUsuario = usuario.getNombre();

            int reservasActivas = reservaDAO.totalReservasPorUsuario(cedulaUsuario);

            request.setAttribute("reservasActivas", reservasActivas);
            request.setAttribute("nombreUsuario", nombreUsuario);

            request.getRequestDispatcher("inicioUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
