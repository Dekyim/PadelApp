package org.example.apiweb;

import dao.ReservaDAO;
import dao.JugadorDAO;
import dao.CanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Reserva;
import models.Jugador;
import models.Cancha;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "VerReservaUsuarioServlet", value = "/verReservaUsuario")
public class VerReservaUsuarioServlet extends HttpServlet {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final CanchaDAO canchaDAO = new CanchaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cedula = request.getParameter("cedula");
        System.out.println("Cédula recibida: " + cedula);

        if (cedula != null && !cedula.isEmpty()) {
            try {
                String fechaStr = request.getParameter("fecha");
                String horarioStr = request.getParameter("horario");
                String numeroCanchaStr = request.getParameter("numeroCancha");
                String pagadaStr = request.getParameter("pagada");
                String activaStr = request.getParameter("activa");
                List<Time> horariosInicio = reservaDAO.obtenerHorariosInicioUnicos();
                request.setAttribute("horariosInicio", horariosInicio);


                List<Reserva> reservas = reservaDAO.listarReservasPorUsuario(cedula);

                if (fechaStr != null && !fechaStr.isEmpty()) {
                    java.sql.Date fecha = java.sql.Date.valueOf(fechaStr);
                    reservas.removeIf(r -> !r.getFecha().equals(fecha));
                }

                if (horarioStr != null && !horarioStr.isEmpty()) {
                    java.sql.Time horario = java.sql.Time.valueOf(horarioStr + ":00");

                    // Validar que el horario existe en la BD
                    List<Time> horariosValidos = reservaDAO.obtenerHorariosInicioUnicos();
                    if (horariosValidos.contains(horario)) {
                        reservas.removeIf(r -> !r.getHorarioInicio().equals(horario));
                    } else {
                        System.out.println("⚠️ Horario no válido: " + horario);
                    }
                }


                if (numeroCanchaStr != null && !numeroCanchaStr.isEmpty()) {
                    int numeroCancha = Integer.parseInt(numeroCanchaStr);
                    reservas.removeIf(r -> r.getNumeroCancha() != numeroCancha);
                }

                if (pagadaStr != null && !pagadaStr.isEmpty()) {
                    boolean pagada = Boolean.parseBoolean(pagadaStr);
                    reservas.removeIf(r -> r.isEstaPagada() != pagada);
                }

                if (activaStr != null && !activaStr.isEmpty()) {
                    boolean activa = Boolean.parseBoolean(activaStr);
                    reservas.removeIf(r -> r.isEstaActiva() != activa);
                }

                System.out.println("Reservas encontradas: " + reservas.size());

                Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedula);
                String nombreCompleto = (jugador != null)
                        ? jugador.getNombre() + " " + jugador.getApellido()
                        : "";
                System.out.println("Nombre del jugador: " + nombreCompleto);

                Map<Integer, Double> preciosPorReserva = new HashMap<>();
                for (Reserva reserva : reservas) {
                    System.out.println("Procesando reserva ID: " + reserva.getId() + " | ID cancha: " + reserva.getIdCancha());

                    Double precio = canchaDAO.obtenerPrecioPorId(reserva.getIdCancha());
                    System.out.println("Precio obtenido: " + precio);

                    preciosPorReserva.put(reserva.getId(), precio);
                }

                double totalPagado = 0;
                for (Reserva reserva : reservas) {
                    Double precio = preciosPorReserva.get(reserva.getId());
                    if (reserva.isEstaPagada() && precio != null) {
                        totalPagado += precio;
                    }
                }
                request.setAttribute("totalPagado", totalPagado);


                request.setAttribute("cedulaUsuario", cedula);
                request.setAttribute("nombreUsuario", nombreCompleto);
                request.setAttribute("reservas", reservas);
                request.setAttribute("preciosPorReserva", preciosPorReserva);

                request.getRequestDispatcher("/verReservaUsuario.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace(); // muestra el error completo en consola
                throw new ServletException("Error al obtener reservas del usuario", e);
            }
        } else {
            System.out.println("Cédula vacía o nula. Redirigiendo a /users");
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idReservaStr = request.getParameter("idReserva");
            String cedula = request.getParameter("cedula");
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

            if (cedula != null && !cedula.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/verReservaUsuario?cedula=" + cedula);
            } else {
                response.sendRedirect(request.getContextPath() + "/verReservaUsuario");
            }

        } catch (Exception e) {
            throw new ServletException("Error al actualizar la reserva", e);
        }
    }
}

