package org.example.apiweb;

import dao.CanchaDAO;
import dao.ReservaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cancha;
import models.Reserva;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;

@WebServlet(name = "ReservaServlet", value = "/reserva")
public class ReservaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CanchaDAO canchaDAO = new CanchaDAO();
        Vector<Cancha> listaCanchas = null;
        try {
            listaCanchas = canchaDAO.listarCancha();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("listaCanchas", listaCanchas);

        ReservaDAO dao = new ReservaDAO();
        Vector<Reserva> reservas;

        String cedula = request.getParameter("cedulaUsuario");
        String canchaStr = request.getParameter("numeroCancha");
        String fechaStr = request.getParameter("fecha");

        try {
            if (cedula != null && !cedula.isEmpty() && fechaStr != null && !fechaStr.isEmpty()) {
                Date fecha = Date.valueOf(fechaStr);
                reservas = dao.listarReservasPorFechaJugador(fecha, cedula);
            } else if (cedula != null && !cedula.isEmpty()) {
                reservas = dao.listarReservasPorUsuario(cedula);
            } else if (canchaStr != null && !canchaStr.isEmpty()) {
                int numeroCancha = Integer.parseInt(canchaStr);
                reservas = dao.listarReservasPorCancha(numeroCancha);
            } else if (fechaStr != null && !fechaStr.isEmpty()) {
                Date fecha = Date.valueOf(fechaStr);
                reservas = dao.listarReservasPorFecha(fecha);
            } else {
                reservas = dao.listarTodasLasReservas();
            }

            request.setAttribute("listaReservas", reservas);
            request.getRequestDispatcher("/reserva.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al filtrar reservas", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("idReserva");

        if ("cancelar".equals(accion) && idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                ReservaDAO dao = new ReservaDAO();
                dao.cancelarReserva(id); // no devuelve nada

                request.setAttribute("mensajeExito", "Reserva cancelada correctamente.");
                doGet(request, response);
                return;

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Ocurrió un error al intentar cancelar la reserva.");
                doGet(request, response);
                return;
            }
        }

        response.sendRedirect("reserva");
    }
}
