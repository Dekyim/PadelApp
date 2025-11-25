package org.example.apiweb;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cancha;
import models.Reserva;
import models.Usuario;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

@WebServlet(name = "ReservaServlet", value = "/reserva")
public class ReservaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CanchaDAO canchaDAO = new CanchaDAO();
        ReservaDAO reservaDAO = new ReservaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Vector<Reserva> reservas;
        Map<String, String> nombresUsuarios = new HashMap<>();
        Map<Integer, Integer> idToNumeroCancha = new HashMap<>();

        String cedula = request.getParameter("cedulaUsuario");
        String canchaStr = request.getParameter("numeroCancha");
        String fechaStr = request.getParameter("fecha");
        String ordenFecha = request.getParameter("ordenFecha");
        String metodoPago = request.getParameter("metodoPago");
        String estadoPago = request.getParameter("estadoPago");
        String estadoActiva = request.getParameter("estadoActiva");

        if (ordenFecha == null || ordenFecha.isEmpty()) {
            ordenFecha = "desc";
        }

        try {
            boolean hayFiltros = (cedula != null && !cedula.isEmpty()) ||
                    (canchaStr != null && !canchaStr.isEmpty()) ||
                    (fechaStr != null && !fechaStr.isEmpty()) ||
                    (metodoPago != null && !metodoPago.isEmpty()) ||
                    (estadoPago != null && !estadoPago.isEmpty()) ||
                    (estadoActiva != null && !estadoActiva.isEmpty());

            if (hayFiltros) {
                if (cedula != null && !cedula.isEmpty() && fechaStr != null && !fechaStr.isEmpty()) {
                    Date fecha = Date.valueOf(fechaStr);
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarReservasPorFechaJugadorAsc(fecha, cedula)
                            : reservaDAO.listarReservasPorFechaJugador(fecha, cedula);
                } else if (cedula != null && !cedula.isEmpty()) {
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarReservasPorUsuarioAsc(cedula)
                            : reservaDAO.listarReservasPorUsuario(cedula);
                } else if (canchaStr != null && !canchaStr.isEmpty()) {
                    int numeroCancha = Integer.parseInt(canchaStr);
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarReservasPorCanchaAsc(numeroCancha)
                            : reservaDAO.listarReservasPorCancha(numeroCancha);
                } else if (fechaStr != null && !fechaStr.isEmpty()) {
                    Date fecha = Date.valueOf(fechaStr);
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarReservasPorFechaAsc(fecha)
                            : reservaDAO.listarReservasPorFecha(fecha);
                } else if (metodoPago != null && !metodoPago.isEmpty()) {
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarReservasPorMetodoPagoAsc(metodoPago)
                            : reservaDAO.listarReservasPorMetodoPago(metodoPago);
                } else if (estadoActiva != null && !estadoActiva.isEmpty()) {
                    if ("activas".equalsIgnoreCase(estadoActiva)) {
                        reservas = "asc".equalsIgnoreCase(ordenFecha)
                                ? reservaDAO.listarReservasActivasAsc()
                                : reservaDAO.listarReservasActivas();
                    } else if ("noactivas".equalsIgnoreCase(estadoActiva)) {
                        reservas = "asc".equalsIgnoreCase(ordenFecha)
                                ? reservaDAO.listarReservasNoActivasAsc()
                                : reservaDAO.listarReservasNoActivas();
                    } else {
                        reservas = "asc".equalsIgnoreCase(ordenFecha)
                                ? reservaDAO.listarTodasLasReservasAsc()
                                : reservaDAO.listarTodasLasReservas();
                    }
                } else {
                    reservas = "asc".equalsIgnoreCase(ordenFecha)
                            ? reservaDAO.listarTodasLasReservasAsc()
                            : reservaDAO.listarTodasLasReservas();
                }

                if (estadoPago != null && !estadoPago.isEmpty()) {
                    reservas.removeIf(r ->
                            ("pagadas".equalsIgnoreCase(estadoPago) && !r.isEstaPagada()) ||
                                    ("nopagadas".equalsIgnoreCase(estadoPago) && r.isEstaPagada())
                    );
                }

            } else {
                reservas = "asc".equalsIgnoreCase(ordenFecha)
                        ? reservaDAO.listarTodasLasReservasAsc()
                        : reservaDAO.listarTodasLasReservas();
            }

            int reservasPorPagina = 12;
            int paginaActual = 1;

            String paginaParam = request.getParameter("page");
            if (paginaParam != null && !paginaParam.isEmpty()) {
                try {
                    paginaActual = Integer.parseInt(paginaParam);
                    if (paginaActual < 1) paginaActual = 1;
                } catch (NumberFormatException ignored) {}
            }

            int totalReservas = reservas.size();
            int totalPaginas = (int) Math.ceil((double) totalReservas / reservasPorPagina);

            int inicio = (paginaActual - 1) * reservasPorPagina;
            int fin = Math.min(inicio + reservasPorPagina, totalReservas);

            List<Reserva> reservasPagina = reservas.subList(inicio, fin);

            for (Reserva reserva : reservasPagina) {
                String cedulaUsuario = reserva.getCedulaUsuario();
                if (!nombresUsuarios.containsKey(cedulaUsuario)) {
                    Vector<Usuario> posibles = usuarioDAO.listarUsuarios(cedulaUsuario);
                    for (Usuario u : posibles) {
                        if (u.getCedula().equals(cedulaUsuario)) {
                            String nombreCompleto = u.getNombre() + " " + u.getApellido();
                            nombresUsuarios.put(cedulaUsuario, nombreCompleto);
                            break;
                        }
                    }
                }

                int idCancha = reserva.getIdCancha();
                if (!idToNumeroCancha.containsKey(idCancha)) {
                    Integer numero = canchaDAO.obtenerNumeroPorId(idCancha);
                    if (numero != null) {
                        idToNumeroCancha.put(idCancha, numero);
                    }
                }
            }

            Vector<Cancha> listaCanchas = canchaDAO.listarCancha();
            request.setAttribute("listaCanchas", listaCanchas);
            request.setAttribute("listaReservas", reservasPagina);
            request.setAttribute("nombresUsuarios", nombresUsuarios);
            request.setAttribute("idToNumeroCancha", idToNumeroCancha);
            request.setAttribute("paginaActual", paginaActual);
            request.setAttribute("totalPaginas", totalPaginas);

            request.getRequestDispatcher("/reserva.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al procesar reservas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("idReserva");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                ReservaDAO dao = new ReservaDAO();

                switch (accion) {
                    case "cancelar":
                        dao.cancelarReserva(id);
                        request.setAttribute("mensajeExito", "Reserva cancelada correctamente.");
                        break;
                    default:
                        request.setAttribute("mensajeError", "Acci贸n no reconocida.");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Ocurri贸 un error al procesar la acci贸n.");
            }

            doGet(request, response);
            return;
        }

        response.sendRedirect("reserva");
    }
}