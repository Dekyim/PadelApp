package org.example.apiweb;

import dao.ReservaDAO;
import dao.CanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Reserva;
import models.MetodoPago;
import models.Cancha;
import models.Usuario;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Vector;

@WebServlet("/editarReserva")
public class EditarReservaServlet extends HttpServlet {

    private Reserva buscarReservaPorId(int idReserva) {
        ReservaDAO dao = new ReservaDAO();
        Vector<Reserva> todas = dao.listarTodasLasReservas();
        for (Reserva r : todas) {
            if (r.getId() == idReserva) {
                return r;
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idReserva");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("reserva");
            return;
        }

        int id = Integer.parseInt(idStr);
        Reserva reserva = buscarReservaPorId(id);

        if (reserva == null) {
            response.sendRedirect("reserva");
            return;
        }

        CanchaDAO canchaDAO = new CanchaDAO();
        Vector<Cancha> listaCanchas = null;
        try {
            listaCanchas = canchaDAO.listarCancha();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("reserva", reserva);
        request.setAttribute("listaCanchas", listaCanchas);
        request.getRequestDispatcher("/editarReserva.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String cedula = request.getParameter("cedulaUsuario");
            int numeroCancha = Integer.parseInt(request.getParameter("numeroCancha"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            Time horarioInicio = Time.valueOf(request.getParameter("horarioInicio") + ":00");
            String metodoPagoStr = request.getParameter("metodoPago");
            boolean estaPagada = request.getParameter("estaPagada") != null;
            boolean estaActiva = request.getParameter("estaActiva") != null;

            long nuevoInicio = horarioInicio.getTime();
            long nuevoFin = nuevoInicio + (90 * 60 * 1000);
            Time horarioFinal = new Time(nuevoFin);
            Time horaCancelacion = null;
            MetodoPago metodoPago = MetodoPago.fromString(metodoPagoStr);

            ReservaDAO dao = new ReservaDAO();
            Vector<Reserva> todas = dao.listarTodasLasReservas();
            boolean hayConflicto = false;

            for (Reserva r : todas) {
                if (r.getId() == id) continue;
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
                request.setAttribute("mensajeError", "Ya existe una reserva en ese horario para esa cancha.");
                CanchaDAO canchaDAO = new CanchaDAO();
                Vector<Cancha> listaCanchas = canchaDAO.listarCancha();
                Reserva reservaOriginal = buscarReservaPorId(id);
                request.setAttribute("reserva", reservaOriginal);
                request.setAttribute("listaCanchas", listaCanchas);
                request.getRequestDispatcher("/editarReserva.jsp").forward(request, response);
                return;
            }

            Reserva reserva = new Reserva(
                    cedula, numeroCancha, fecha, horarioInicio, horarioFinal,
                    horaCancelacion, metodoPago, estaPagada, estaActiva
            );
            reserva.setId(id);
            dao.actualizarReserva(reserva);

            HttpSession session = request.getSession();
            session.setAttribute("mensajeExito", "Reserva actualizada correctamente.");

            Usuario usuario = (Usuario) session.getAttribute("authUser");
            if (usuario != null && usuario.esAdministrador()) {
                response.sendRedirect("reserva");
            } else {
                response.sendRedirect("reservasUsuario");
            }

            return;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al actualizar la reserva.");
            request.getRequestDispatcher("/editarReserva.jsp").forward(request, response);
        }
    }



}
