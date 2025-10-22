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

        Vector<Reserva> listaReservas = reservaDAO.listarReservasPorUsuario(cedulaUsuario);
        Vector<Cancha> listaCanchas = null;
        try {
            listaCanchas = canchaDAO.listarCancha();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        request.setAttribute("listaReservas", listaReservas);
        request.setAttribute("listaCanchas", listaCanchas);
        request.setAttribute("nombreUsuario", usuario.getNombre());

        request.getRequestDispatcher("usuarioReserva.jsp").forward(request, response);
    }
}
