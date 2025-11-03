package org.example.apiweb;

import dao.CanchaDAO;
import dao.ReservaDAO;
import dao.UsuarioDAO;
import dao.JugadorDAO;
import jakarta.mail.MessagingException;
import models.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EnviarCorreo;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
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
            request.setAttribute("jugadores", jugadores);
            Vector<Cancha> todas = canchaDAO.listarCancha();
            Vector<Cancha> disponibles = new Vector<>();

            for (Cancha c : todas) {
                if (canchaDAO.canchaDisponible(c.getNumero())) {
                    disponibles.add(c);
                }
            }
            request.setAttribute("canchas", disponibles);

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
        if (numeroCanchaParam != null && !numeroCanchaParam.isEmpty()) {
            int numero = Integer.parseInt(numeroCanchaParam);
            Vector<Time> horarios = null;
            try {
                horarios = canchaDAO.obtenerHorariosPorNumero(numero);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("horariosCanchaSeleccionada", horarios);
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
            if (!horarioStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
                horarioStr = horarioStr + ":00";
            }
            Time horarioInicio = Time.valueOf(horarioStr);
            if (horarioStr == null || horarioStr.isEmpty()) {
                throw new ServletException("Horario de inicio no especificado.");
            }


            long nuevoInicio = horarioInicio.getTime();
            long nuevoFin = nuevoInicio + (90 * 60 * 1000);
            Time horarioFinal = new Time(nuevoFin);

            MetodoPago metodoPago = MetodoPago.fromString(request.getParameter("metodoPago"));

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

            Reserva reserva = new Reserva(cedulaUsuario, numeroCancha, fecha, horarioInicio, horarioFinal, null, metodoPago, false, true);

            reservaDAO.crearReserva(reserva);
            String correo = usuario.getCorreo();
            String nombre = usuario.getNombre();
            try {
                EnviarCorreo.enviar(
                        correo,
                        "Confirmación de reserva",
                        "Hola " + nombre + ",\n\nTu reserva de la cancha " + numeroCancha + ", para el día " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(fecha) + " a las " + horarioInicio + ", ha sido agendada con éxito.\n\nSaludos,\nEl equipo de PadelManager."
                );
                System.out.println("Confirmación enviada a " + correo);
            } catch (MessagingException e) {
                System.err.println("Error al enviar correo: " + e.getMessage());
            }
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