package org.example.apiweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dao.CanchaDAO;
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
            CanchaDAO dao = new CanchaDAO();
            Vector<Cancha> canchas = dao.listarCancha();

            List<Integer> numeroCanchas = new ArrayList<>();
            for (Cancha c : canchas) {
                numeroCanchas.add(c.getNumero());
            }

            request.setAttribute("listaCanchas", numeroCanchas);
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