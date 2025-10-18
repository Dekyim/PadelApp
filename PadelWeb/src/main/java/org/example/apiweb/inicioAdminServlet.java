package org.example.apiweb;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;

import dao.*;
import models.*;

@WebServlet("/inicioAdmin")
public class inicioAdminServlet extends HttpServlet {

    private ReservaDAO reservaDAO;
    private CanchaDAO canchaDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        reservaDAO = new ReservaDAO();
        canchaDAO = new CanchaDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // 📊 Totales desde los DAOs
            int totalCanchas = canchaDAO.totalCanchas();
            int totalCanchasTechadas = canchaDAO.totalCanchasTechadas();
            int totalCanchasDisponibles = canchaDAO.totalCanchasDisponibles();

            int totalReservasActivas = reservaDAO.totalReservasActivas();
            int totalReservasPagadas = reservaDAO.totalReservasPagadas();
            int totalReservasNoPagadas = reservaDAO.totalReservasNoPagadas();

            int totalUsuarios = usuarioDAO.totalUsuarios();

            LocalDate hoy = LocalDate.now();
            LocalDate inicioMes = hoy.withDayOfMonth(1);
            LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
            double totalIngresos = reservaDAO.totalIngresos(
                    Date.valueOf(inicioMes),
                    Date.valueOf(finMes)
            );

            request.setAttribute("reservasPagadas", totalReservasPagadas);
            request.setAttribute("reservasNoPagadas", totalReservasNoPagadas);
            request.setAttribute("reservasActivas", totalReservasActivas);

            request.setAttribute("totalCanchas", totalCanchas);
            request.setAttribute("canchasTechadas", totalCanchasTechadas);
            request.setAttribute("canchasDisponibles", totalCanchasDisponibles);

            request.setAttribute("totalUsuariosMes", totalUsuarios);
            request.setAttribute("totalIngresosMes", totalIngresos);

            RequestDispatcher dispatcher = request.getRequestDispatcher("inicioAdministrador.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
