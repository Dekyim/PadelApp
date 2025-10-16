package org.example.apiweb;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "inicioAdministradorServlet", value = "/inicioAdministrador")
public class inicioAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        try {

            int totalUsuariosMes = 0;
            double totalIngresosMes = 0.0;
            int reservasActivas = 0;
            int reservasPagadas = 0;
            int reservasNoPagadas = 0;
            int totalCanchas = 0;
            int canchasTechadas = 0;
            int canchasDisponibles = 0;


            request.setAttribute("totalUsuariosMes", totalUsuariosMes);
            request.setAttribute("totalIngresosMes", totalIngresosMes);
            request.setAttribute("reservasActivas", reservasActivas);
            request.setAttribute("reservasPagadas", reservasPagadas);
            request.setAttribute("reservasNoPagadas", reservasNoPagadas);
            request.setAttribute("totalCanchas", totalCanchas);
            request.setAttribute("canchasTechadas", canchasTechadas);
            request.setAttribute("canchasDisponibles", canchasDisponibles);


            request.getRequestDispatcher("/inicioAdministrador.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
    }
}
