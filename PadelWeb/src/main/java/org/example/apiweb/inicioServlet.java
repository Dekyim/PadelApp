package org.example.apiweb;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.*;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private CanchaDAO canchaDAO;
    private ReservaDAO reservaDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        canchaDAO = new CanchaDAO();
        reservaDAO = new ReservaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int totalUsuarios = usuarioDAO.totalUsuarios();
        int totalCanchas = canchaDAO.totalCanchas();
        int totalReservas = reservaDAO.totalReservas();

        request.setAttribute("totalUsuarios", totalUsuarios);
        request.setAttribute("totalCanchas", totalCanchas);
        request.setAttribute("totalReservas", totalReservas);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
