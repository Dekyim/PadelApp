package org.example.apiweb;

import dao.ReservaDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

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

        response.setContentType("text/html");

        try {
            // Obtener cédula de la sesión si existe
            HttpSession sesion = request.getSession(false);
            String cedulaUsuario = (sesion != null) ? (String) sesion.getAttribute("cedula") : null;

            // Si no hay cédula, usar un valor por defecto para pruebas
            if (cedulaUsuario == null) {
                cedulaUsuario = "666"; // cédula de prueba
            }

            int reservasActivas = reservaDAO.totalReservasPorUsuario(cedulaUsuario);
            request.setAttribute("reservasActivas", reservasActivas);

            // Forward al JSP
            request.getRequestDispatcher("inicioUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}