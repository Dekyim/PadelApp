package org.example.apiweb;

import dao.ParticipantesGrupoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.ParticipantesGrupo;
import models.Usuario;

import java.io.IOException;

@WebServlet(name = "UnirseGrupoServlet", value = "/unirsegrupo")
public class UnirseGrupoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String cedula = (usuario != null) ? usuario.getCedula() : null;

        if (cedula == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));

            ParticipantesGrupoDAO dao = new ParticipantesGrupoDAO();

            if (dao.existeParticipante(idGrupo, cedula)) {
                response.sendRedirect(request.getContextPath() + "/grupojugador?error=yaEnGrupo");
                return;
            }


            ParticipantesGrupo participante = new ParticipantesGrupo();
            participante.setIdGrupo(idGrupo);
            participante.setIdJugador(cedula);
            participante.setEsCreador(false);

            dao.agregarParticipante(participante);

            response.sendRedirect("grupojugador");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "No se pudo unir al grupo.");
            request.getRequestDispatcher("/grupojugador.jsp").forward(request, response);
        }
    }
}
