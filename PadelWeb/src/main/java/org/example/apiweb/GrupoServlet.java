package org.example.apiweb;

import dao.GrupoDAO;
import dao.ParticipantesGrupoDAO;
import dao.JugadorDAO;
import dao.FotoPerfilUsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Grupo;
import models.Jugador;
import models.ParticipantesGrupo;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "GrupoServlet", value = "/grupo")
public class GrupoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        GrupoDAO grupoDAO = new GrupoDAO();
        ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();
        FotoPerfilUsuarioDAO fotoDAO = new FotoPerfilUsuarioDAO();
        JugadorDAO jugadorDAO = new JugadorDAO();

        try {
            List<Grupo> listaGrupos = grupoDAO.listarGruposAbiertos();
            Map<String, String> fotosJugadores = new HashMap<>();
            Map<String, String> nombresJugadores = new HashMap<>();
            Map<Integer, List<String>> participantesPorGrupo = new HashMap<>();

            for (Grupo grupo : listaGrupos) {
                int idGrupo = grupo.getIdGrupo();
                List<ParticipantesGrupo> participantes = participantesDAO.obtenerParticipantesPorGrupo(idGrupo);
                List<String> cedulasParticipantes = new ArrayList<>();

                for (ParticipantesGrupo p : participantes) {
                    String cedula = p.getIdJugador();
                    cedulasParticipantes.add(cedula);

                    if (!fotosJugadores.containsKey(cedula)) {
                        String fotoUrl = fotoDAO.obtenerFotoPorCedula(cedula);
                        fotosJugadores.put(cedula, fotoUrl != null ? fotoUrl : "img/default.png");
                    }

                    if (!nombresJugadores.containsKey(cedula)) {
                        Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedula);
                        String nombre = jugador != null
                                ? jugador.getNombre() + " " + jugador.getApellido()
                                : cedula;
                        nombresJugadores.put(cedula, nombre);
                    }
                }

                participantesPorGrupo.put(idGrupo, cedulasParticipantes);

                String cedulaCreador = grupo.getIdCreador();
                if (!fotosJugadores.containsKey(cedulaCreador)) {
                    String fotoUrl = fotoDAO.obtenerFotoPorCedula(cedulaCreador);
                    fotosJugadores.put(cedulaCreador, fotoUrl != null ? fotoUrl : "img/default.png");
                }

                if (!nombresJugadores.containsKey(cedulaCreador)) {
                    Jugador creador = jugadorDAO.obtenerJugadorPorCedula(cedulaCreador);
                    String nombre = creador != null
                            ? creador.getNombre() + " " + creador.getApellido()
                            : cedulaCreador;
                    nombresJugadores.put(cedulaCreador, nombre);
                }
            }

            request.setAttribute("listaGrupos", listaGrupos);
            request.setAttribute("fotosJugadores", fotosJugadores);
            request.setAttribute("nombresJugadores", nombresJugadores);
            request.setAttribute("participantesPorGrupo", participantesPorGrupo);

            request.getRequestDispatcher("/grupo.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar grupos.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("idGrupo");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idGrupo = Integer.parseInt(idStr);
                GrupoDAO grupoDAO = new GrupoDAO();

                switch (accion) {
                    case "cerrar":
                        grupoDAO.cerrarGrupo(idGrupo);
                        request.setAttribute("mensajeExito", "Grupo cerrado correctamente.");

                        ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();
                        JugadorDAO jugadorDAO = new JugadorDAO();

                        Grupo grupo = grupoDAO.obtenerGrupoPorId(idGrupo);
                        List<ParticipantesGrupo> participantes = participantesDAO.obtenerParticipantesPorGrupo(idGrupo);
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
                        break;

                    case "eliminar":
                        grupoDAO.eliminarGrupo(idGrupo);
                        request.setAttribute("mensajeExito", "Grupo eliminado correctamente.");
                        break;

                    default:
                        request.setAttribute("mensajeError", "Acción no reconocida.");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Ocurrió un error al procesar la acción.");
            }

            doGet(request, response);
            return;
        }

        response.sendRedirect("grupo");
    }
}