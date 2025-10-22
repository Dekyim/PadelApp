package org.example.apiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dao.*;
import models.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Vector;

@WebServlet(name = "/CrearReservaServlet", value = "/crearreserva")
public class CrearReservaServlet extends HttpServlet {

    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final CanchaDAO canchaDAO = new CanchaDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    private void cargarDatosFormulario(HttpServletRequest request) throws ServletException {
        try {
            Vector<Jugador> jugadores = jugadorDAO.listarJugadores();
            Vector<Cancha> canchas = canchaDAO.listarCancha();
            request.setAttribute("jugadores", jugadores);
            request.setAttribute("canchas", canchas);
        } catch (Exception e) {
            throw new ServletException("Error al cargar datos del formulario", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarDatosFormulario(request);
        request.getRequestDispatcher("/crearReserva.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cedulaUsuario = request.getParameter("cedulaUsuario");
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            if (!usuarioDAO.existeUsuario(cedulaUsuario)) {
                cargarDatosFormulario(request);
                request.setAttribute("error", "El usuario no existe.");
                request.getRequestDispatcher("/crearReserva.jsp").forward(request, response);
                return;
            }

            int numeroCancha = Integer.parseInt(request.getParameter("numeroCancha"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            Time horarioInicio = Time.valueOf(request.getParameter("horarioInicio") + ":00");
            long nuevoInicio = horarioInicio.getTime();
            long nuevoFin = nuevoInicio + (90 * 60 * 1000);
            Time horarioFinal = new Time(nuevoFin);

            MetodoPago metodoPago = MetodoPago.fromString(request.getParameter("metodoPago"));

            // Validar conflicto de horario
            Vector<Reserva> todas = reservaDAO.listarTodasLasReservas();
            boolean hayConflicto = false;

            for (Reserva r : todas) {
                boolean mismaCancha = r.getNumeroCancha() == numeroCancha;
                boolean mismaFecha = r.getFecha().equals(fecha);
                long inicioExistente = r.getHorarioInicio().getTime();
                long finExistente = r.getHorarioFinal().getTime();
                boolean solapa = nuevoInicio < finExistente && nuevoFin > inicioExistente;

                if (mismaCancha && mismaFecha && solapa) {
                    hayConflicto = true;
                    break;
                }
            }

            if (hayConflicto) {
                cargarDatosFormulario(request);
                request.setAttribute("error", "Ya existe una reserva en ese horario para esa cancha.");
                request.getRequestDispatcher("/crearReserva.jsp").forward(request, response);
                return;
            }

            Reserva reserva = new Reserva(cedulaUsuario, numeroCancha, fecha, horarioInicio, horarioFinal, null, metodoPago, false, true);

            reservaDAO.crearReserva(reserva);
            response.sendRedirect(request.getContextPath() + "/reserva");

        } catch (Exception e) {
            throw new ServletException("Error al procesar la reserva", e);
        }
    }
}
