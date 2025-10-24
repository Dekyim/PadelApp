package org.example.apiweb;

import dao.ReservaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Reserva;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VerReservaCanchaServlet", value = "/verReservaCancha")
public class VerReservaCanchaServlet extends HttpServlet {

    private final ReservaDAO reservaDAO = new ReservaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numeroStr = request.getParameter("numeroCancha");

        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numeroCancha = Integer.parseInt(numeroStr);

                // Obtener reservas de esa cancha
                List<Reserva> reservas = reservaDAO.listarReservasPorCancha(numeroCancha);

                request.setAttribute("numeroCancha", numeroCancha);
                request.setAttribute("reservas", reservas);

                request.getRequestDispatcher("verReservaCancha.jsp").forward(request, response);

            } catch (Exception e) {
                throw new ServletException("Error al obtener reservas de la cancha", e);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/cancha");
        }
    }
}