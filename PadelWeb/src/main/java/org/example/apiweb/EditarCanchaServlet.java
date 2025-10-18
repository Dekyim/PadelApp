package org.example.apiweb;

import dao.CanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cancha;
import models.CanchaHorario;

import java.io.IOException;
import java.sql.Time;
import java.util.Vector;

@WebServlet(name = "EditarCanchaServlet", value = "/editarCancha")
public class EditarCanchaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String numeroStr = request.getParameter("numero");

        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                CanchaDAO dao = new CanchaDAO();

                // Buscar la cancha por número
                Vector<Cancha> todas = dao.listarCancha();
                Cancha cancha = todas.stream()
                        .filter(c -> c.getNumero() == numero)
                        .findFirst()
                        .orElse(null);

                if (cancha != null) {
                    request.setAttribute("cancha", cancha);
                    request.getRequestDispatcher("editarCancha.jsp").forward(request, response);
                } else {
                    response.sendRedirect("cancha");
                }

            } catch (Exception e) {
                throw new ServletException("Error al cargar cancha para edición", e);
            }
        } else {
            response.sendRedirect("cancha");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            int numero = Integer.parseInt(request.getParameter("numero"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            boolean esTechada = request.getParameter("esTechada") != null;
            boolean estaDisponible = request.getParameter("estaDisponible") != null;

            // Obtener horarios seleccionados
            String[] horariosSeleccionados = request.getParameterValues("horarios");

            Vector<Time> horarios = new Vector<>();
            if (horariosSeleccionados != null) {
                for (String h : horariosSeleccionados) {
                    horarios.add(Time.valueOf(h + ":00"));
                }
            }

            // Crear objeto Cancha actualizado
            CanchaHorario canchaHorario = new CanchaHorario(0, horarios);
            Cancha canchaActualizada = new Cancha(0, esTechada, precio, estaDisponible, numero, canchaHorario);

            // Actualizar en la BD
            CanchaDAO dao = new CanchaDAO();
            dao.actualizarCancha(canchaActualizada);

            request.setAttribute("mensaje", "Cancha actualizada correctamente.");
            response.sendRedirect("cancha");

        } catch (Exception e) {
            throw new ServletException("Error al actualizar cancha", e);
        }
    }
}
