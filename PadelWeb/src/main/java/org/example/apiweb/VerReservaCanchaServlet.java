package org.example.apiweb;

import dao.ReservaDAO;
import dao.CanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Reserva;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "VerReservaCanchaServlet", value = "/verReservaCancha")
public class VerReservaCanchaServlet extends HttpServlet {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final CanchaDAO canchaDAO = new CanchaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numeroStr = request.getParameter("numeroCancha");
        String fechaStr = request.getParameter("fecha");
        String horarioStr = request.getParameter("horario");
        String pagadaStr = request.getParameter("pagada");
        String activaStr = request.getParameter("activa");

        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numeroCancha = Integer.parseInt(numeroStr);
                List<Reserva> reservas = reservaDAO.listarReservasPorCancha(numeroCancha);

                // Filtros combinados
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    java.sql.Date fecha = java.sql.Date.valueOf(fechaStr);
                    reservas.removeIf(r -> !r.getFecha().equals(fecha));
                }

                if (horarioStr != null && !horarioStr.isEmpty()) {
                    java.sql.Time horario = java.sql.Time.valueOf(horarioStr + ":00");
                    List<Time> horariosValidos = reservaDAO.obtenerHorariosInicioUnicos();
                    if (horariosValidos.contains(horario)) {
                        reservas.removeIf(r -> !r.getHorarioInicio().equals(horario));
                    } else {
                        System.out.println("⚠️ Horario no válido: " + horario);
                    }
                }

                if (pagadaStr != null && !pagadaStr.isEmpty()) {
                    boolean pagada = Boolean.parseBoolean(pagadaStr);
                    reservas.removeIf(r -> r.isEstaPagada() != pagada);
                }

                if (activaStr != null && !activaStr.isEmpty()) {
                    boolean activa = Boolean.parseBoolean(activaStr);
                    reservas.removeIf(r -> r.isEstaActiva() != activa);
                }

                // Precios y total
                Map<Integer, Double> preciosPorReserva = new HashMap<>();
                double totalGanado = 0;
                for (Reserva r : reservas) {
                    Double precio = canchaDAO.obtenerPrecioPorId(r.getIdCancha());
                    preciosPorReserva.put(r.getId(), precio);
                    if (r.isEstaPagada() && precio != null) {
                        totalGanado += precio;
                    }
                }

                List<Time> horariosInicio = reservaDAO.obtenerHorariosInicioUnicos();

                request.setAttribute("numeroCancha", numeroCancha);
                request.setAttribute("reservas", reservas);
                request.setAttribute("preciosPorReserva", preciosPorReserva);
                request.setAttribute("totalGanado", totalGanado);
                request.setAttribute("horariosInicio", horariosInicio);

                request.getRequestDispatcher("verReservaCancha.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException("Error al obtener reservas de la cancha", e);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/cancha");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idReservaStr = request.getParameter("idReserva");
            String numeroStr = request.getParameter("numeroCancha");
            String accion = request.getParameter("accion");

            if (idReservaStr != null && !idReservaStr.isEmpty() && accion != null) {
                int idReserva = Integer.parseInt(idReservaStr);

                switch (accion) {
                    case "pagar":
                        if (reservaDAO.obtenerEstadoActiva(idReserva)) {
                            reservaDAO.pagarReserva(idReserva);
                        }
                        break;
                    case "despagar":
                        if (reservaDAO.obtenerEstadoActiva(idReserva)) {
                            reservaDAO.despagarReserva(idReserva);
                        }
                        break;
                    case "activar":
                        reservaDAO.activarReserva(idReserva);
                        break;
                    case "desactivar":
                        reservaDAO.cancelarReserva(idReserva);
                        break;
                    default:
                        break;
                }
            }

            if (numeroStr != null && !numeroStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/verReservaCancha?numeroCancha=" + numeroStr);
            } else {
                response.sendRedirect(request.getContextPath() + "/verReservaCancha");
            }

        } catch (Exception e) {
            throw new ServletException("Error al actualizar la reserva", e);
        }
    }
}

