package org.example.apiweb;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            GrupoDAO grupoDAO = new GrupoDAO();
            JugadorDAO jugadorDAO = new JugadorDAO();

            Grupo grupo = grupoDAO.obtenerGrupoPorId(idGrupo);
            List<ParticipantesGrupo> participantes = dao.obtenerParticipantesPorGrupo(idGrupo);

            if (participantes.size()  > grupo.getCupos()) {
                grupoDAO.cerrarGrupo(idGrupo);

                List<Jugador> jugadores = new ArrayList<>();
                for (ParticipantesGrupo p : participantes) {
                    Jugador j = jugadorDAO.obtenerJugadorPorCedula(p.getIdJugador());
                    if (j != null) jugadores.add(j);
                }

                Jugador creador = jugadorDAO.obtenerJugadorPorCedula(grupo.getIdCreador());
                String nombreCreador = creador.getNombre();
                String correoCreador = creador.getCorreo();

                String mensaje = "Hola " + nombreCreador + ",\n\n" +
                        "Tu grupo se ha completado. Estos son los jugadores que se unieron y sus correos:\n\n";

                for (Jugador j : jugadores) {
                    if (!j.getCedula().equals(grupo.getIdCreador())) {
                        mensaje += "- " + j.getNombre() + " — " + j.getCorreo() + "\n";
                    }
                }

                mensaje += "\nDetalles del grupo:\n" +
                        "- Categoría: " + grupo.getCategoria() + "\n" +
                        "- Horario: " + grupo.getHoraDesde() + " - " + grupo.getHoraHasta() + "\n" +
                        "- Descripción: " + grupo.getDescripcion() + "\n\n" +
                        "Saludos,\nEl equipo de PadelManager.";

                try {
                    utils.EnviarCorreo.enviar(correoCreador, "Tu grupo se ha completado", mensaje);
                } catch (jakarta.mail.MessagingException e) {
                    System.err.println("Error al enviar correo al creador: " + e.getMessage());
                }

                for (Jugador j : jugadores) {
                    if (!j.getCedula().equals(grupo.getIdCreador())) {
                        String mensajeJugador = "Hola " + j.getNombre() + ",\n\n" +
                                "El grupo al que te uniste se completó.\n\n" +
                                "Contactá al creador para coordinar:\n" +
                                "- " + nombreCreador + " — " + correoCreador + "\n\n" +
                                "Detalles del grupo:\n" +
                                "- Horario: " + grupo.getHoraDesde() + " - " + grupo.getHoraHasta() + "\n" +
                                "- Descripción: " + grupo.getDescripcion() + "\n\n" +
                                "Saludos,\nEl equipo de PadelManager.";

                        try {
                            utils.EnviarCorreo.enviar(j.getCorreo(), "Confirmación de grupo completo", mensajeJugador);
                        } catch (jakarta.mail.MessagingException e) {
                            System.err.println("Error al enviar correo a " + j.getCorreo() + ": " + e.getMessage());
                        }
                    }
                }
            }

            response.sendRedirect("grupojugador");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "No se pudo unir al grupo.");
            request.getRequestDispatcher("/grupojugador.jsp").forward(request, response);
        }
    }
}
