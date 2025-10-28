package org.example.apiweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import dao.*;

@WebServlet(name = "CancelarReservaServlet", value = "/cancelarReserva")
public class CancelarReservaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idReserva");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                ReservaDAO dao = new ReservaDAO();
                dao.cancelarReserva(id);

                request.setAttribute("mensajeExito", "Reserva cancelada correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Ocurrió un error al cancelar la reserva.");
            }
        } else {
            request.setAttribute("mensajeError", "ID de reserva inválido.");
        }

        request.getRequestDispatcher("/reserva").forward(request, response);
    }
}
