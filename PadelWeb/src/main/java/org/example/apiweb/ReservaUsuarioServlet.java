package org.example.apiweb;

import dao.ReservaDAO;
import dao.CanchaDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Reserva;
import models.Usuario;
import models.Cancha;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

@WebServlet("/reservasUsuario")
public class ReservaUsuarioServlet extends HttpServlet {

    private ReservaDAO reservaDAO;
    private CanchaDAO canchaDAO;

    @Override
    public void init() {
        reservaDAO = new ReservaDAO();
        canchaDAO = new CanchaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("authUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) sesion.getAttribute("authUser");
        String cedulaUsuario = usuario.getCedula();

        String fechaParam = request.getParameter("fecha");
        String numeroCanchaParam = request.getParameter("numeroCancha");
        String ordenFecha = request.getParameter("ordenFecha");
        String estadoPago = request.getParameter("estadoPago");
        String estadoActiva = request.getParameter("estadoActiva");
        String metodoPago = request.getParameter("metodoPago");

        if (ordenFecha == null || ordenFecha.isEmpty()) {
            ordenFecha = "desc";
        }

        Vector<Reserva> listaReservas;

        try {
            if (fechaParam != null && !fechaParam.isEmpty()) {
                java.sql.Date fecha = java.sql.Date.valueOf(fechaParam);
                listaReservas = "asc".equalsIgnoreCase(ordenFecha)
                        ? reservaDAO.listarReservasPorFechaJugadorAsc(fecha, cedulaUsuario)
                        : reservaDAO.listarReservasPorFechaJugador(fecha, cedulaUsuario);
            } else {
                listaReservas = "asc".equalsIgnoreCase(ordenFecha)
                        ? reservaDAO.listarReservasPorUsuarioAsc(cedulaUsuario)
                        : reservaDAO.listarReservasPorUsuario(cedulaUsuario);
            }

            if (numeroCanchaParam != null && !numeroCanchaParam.isEmpty()) {
                int numeroCancha = Integer.parseInt(numeroCanchaParam);
                listaReservas.removeIf(r -> r.getNumeroCancha() != numeroCancha);
            }

            if ("pagadas".equalsIgnoreCase(estadoPago)) {
                listaReservas.removeIf(r -> !r.isEstaPagada());
            } else if ("nopagadas".equalsIgnoreCase(estadoPago)) {
                listaReservas.removeIf(r -> r.isEstaPagada());
            }

            if ("activas".equalsIgnoreCase(estadoActiva)) {
                listaReservas.removeIf(r -> !r.isEstaActiva());
            } else if ("noactivas".equalsIgnoreCase(estadoActiva)) {
                listaReservas.removeIf(r -> r.isEstaActiva());
            }

            if (metodoPago != null && !metodoPago.isEmpty()) {
                listaReservas.removeIf(r -> !r.getMetodoPago().getValue().equalsIgnoreCase(metodoPago));
            }

            Vector<Cancha> listaCanchas = canchaDAO.listarCancha();

            request.setAttribute("listaReservas", listaReservas);
            request.setAttribute("listaCanchas", listaCanchas);
            request.setAttribute("nombreUsuario", usuario.getNombre());

            request.getRequestDispatcher("usuarioReserva.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al filtrar reservas", e);
        }
    }

}
