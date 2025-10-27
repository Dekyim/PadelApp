package org.example.apiweb;

import java.io.IOException;
import java.util.*;

import dao.CanchaDAO;
import dao.FotoCanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cancha;

@WebServlet(name = "CanchaServlet", value = "/cancha")
public class CanchaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            // Verificamos si ya hay mensajes seteados desde doPost
            String mensajeExito = (String) request.getAttribute("mensajeExito");
            String mensajeError = (String) request.getAttribute("mensajeError");

            CanchaDAO dao = new CanchaDAO();
            Vector<Cancha> canchas = dao.listarCancha();
            request.setAttribute("listaCanchas", canchas);

            FotoCanchaDAO fotoDAO = new FotoCanchaDAO();
            Map<Integer, String> fotosPorId = new HashMap<>();
            for (Cancha c : canchas) {
                String url = fotoDAO.obtenerFotoPorId(c.getId());
                fotosPorId.put(c.getId(), url);
            }
            request.setAttribute("fotosPorId", fotosPorId);

            // Volvemos a setear los mensajes si existen
            if (mensajeExito != null) request.setAttribute("mensajeExito", mensajeExito);
            if (mensajeError != null) request.setAttribute("mensajeError", mensajeError);

            request.getRequestDispatcher("cancha.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar canchas", e);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        String numeroStr = request.getParameter("numero");

        if ("eliminar".equals(accion) && numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                Cancha cancha = new Cancha();
                cancha.setNumero(numero);

                CanchaDAO dao = new CanchaDAO();
                boolean eliminada = dao.desactivarCancha(cancha);

                if (eliminada) {
                    request.setAttribute("mensajeExito", "Cancha eliminada correctamente.");
                } else {
                    request.setAttribute("mensajeError", "No se puede eliminar la cancha porque tiene reservas activas.");
                }

                doGet(request, response);
                return;

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "OcurriÃ³ un error al intentar eliminar la cancha.");
                doGet(request, response);
                return;
            }
        }

        response.sendRedirect("cancha");
    }
}