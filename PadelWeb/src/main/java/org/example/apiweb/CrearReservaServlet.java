package org.example.apiweb;

import dao.CanchaDAO;
import dao.ReservaDAO;
import dao.UsuarioDAO;
import dao.JugadorDAO;
import models.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Vector;

@WebServlet(name = "CrearReservaServlet", value = "/crearreserva")
public class CrearReservaServlet extends HttpServlet {

    private final CanchaDAO canchaDAO = new CanchaDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();

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

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

        // Solo si NO es administrador, se asigna la cédula
        if (usuario != null && !usuario.esAdministrador()) {
            request.setAttribute("cedulaUsuarioSeleccionada", usuario.getCedula());
        }

        String numeroCanchaParam = request.getParameter("numeroCancha");
        if (numeroCanchaParam != null && !numeroCanchaParam.isEmpty()) {
            request.setAttribute("numeroCanchaSeleccionada", numeroCanchaParam);
        }

        request.getRequestDispatcher("/crearReserva.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

            String cedulaUsuario;
            if (usuario != null && !usuario.esAdministrador()) {
                cedulaUsuario = usuario.getCedula();
            } else {
                cedulaUsuario = request.getParameter("cedulaUsuario");
            }

            int numeroCancha = Integer.parseInt(request.getParameter("numeroCancha"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            String horarioStr = request.getParameter("horarioInicio");
            Time horarioInicio = Time.valueOf(horarioStr + ":00");

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
                request.setAttribute("cedulaUsuarioSeleccionada", cedulaUsuario);
                request.setAttribute("numeroCanchaSeleccionada", numeroCancha);
                request.setAttribute("fechaSeleccionada", request.getParameter("fecha"));
                request.setAttribute("horarioSeleccionado", horarioStr);
                request.getRequestDispatcher("/crearReserva.jsp").forward(request, response);
                return;
            }

            // Crear reserva
            Reserva reserva = new Reserva(
                    cedulaUsuario,
                    numeroCancha,
                    fecha,
                    horarioInicio,
                    horarioFinal,
                    null,
                    metodoPago,
                    false,
                    true
            );

            reservaDAO.crearReserva(reserva);

            // Redirigir según tipo de usuario
            if (usuario != null && usuario.esAdministrador()) {
                response.sendRedirect(request.getContextPath() + "/reserva");
            } else {
                response.sendRedirect(request.getContextPath() + "/canchaUsuario");
            }

        } catch (Exception e) {
            throw new ServletException("Error al procesar la reserva", e);
        }
    }
}