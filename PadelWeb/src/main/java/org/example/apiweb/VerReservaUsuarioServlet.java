package org.example.apiweb;

import dao.ReservaDAO;
import dao.JugadorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Reserva;
import models.Jugador;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VerReservaUsuarioServlet", value = "/verReservaUsuario")
public class VerReservaUsuarioServlet extends HttpServlet {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cedula = request.getParameter("cedula");

        if (cedula != null && !cedula.isEmpty()) {
            try {
                // Obtener reservas del usuario
                List<Reserva> reservas = reservaDAO.listarReservasPorUsuario(cedula);

                // Obtener nombre del usuario
                Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedula);
                String nombreCompleto = (jugador != null) ? jugador.getNombre() + " " + jugador.getApellido() : "";

                request.setAttribute("cedulaUsuario", cedula);
                request.setAttribute("nombreUsuario", nombreCompleto);
                request.setAttribute("reservas", reservas);

                request.getRequestDispatcher("verReservaUsuario.jsp").forward(request, response);

            } catch (Exception e) {
                throw new ServletException("Error al obtener reservas del usuario", e);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }
}