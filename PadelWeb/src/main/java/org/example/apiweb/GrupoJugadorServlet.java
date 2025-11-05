package org.example.apiweb;

import dao.GrupoDAO;
import dao.JugadorDAO;
import dao.ParticipantesGrupoDAO;
import dao.FotoPerfilUsuarioDAO;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Grupo;
import models.Jugador;
import models.ParticipantesGrupo;
import models.Usuario;
import utils.EnviarCorreo;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "GrupoJugadorServlet", value = "/grupojugador")
public class GrupoJugadorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String error = request.getParameter("error");
        if ("yaEnGrupo".equals(error)) {
            request.setAttribute("mensajeError", "Ya estás inscrito en este grupo.");
        }

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String cedula = (usuario != null) ? usuario.getCedula() : null;

        if (cedula == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        JugadorDAO jugadorDAO = new JugadorDAO();
        Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedula);

        if (jugador == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String categoriaJugador = jugador.getCategoria();
        String categoriaJugadorPrimeraPalabra = categoriaJugador.trim().split(" ")[0].toLowerCase();

        GrupoDAO grupoDAO = new GrupoDAO();
        ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();
        FotoPerfilUsuarioDAO fotoDAO = new FotoPerfilUsuarioDAO();

        List<Grupo> todosLosGrupos = grupoDAO.listarGrupos();
        List<Grupo> gruposDelJugador = grupoDAO.obtenerGruposPorJugador(cedula).stream()
                .filter(g -> "abierto".equalsIgnoreCase(g.getEstado()))
                .toList();

        Set<Integer> idsDelJugador = new HashSet<>();
        for (Grupo g : gruposDelJugador) {
            idsDelJugador.add(g.getIdGrupo());
        }

        List<Grupo> gruposDisponibles = new ArrayList<>();
        Map<Integer, List<String>> participantesPorGrupo = new HashMap<>();
        Map<String, String> fotosJugadores = new HashMap<>();
        Map<String, String> nombresJugadores = new HashMap<>();

        for (Grupo grupo : todosLosGrupos) {
            int idGrupo = grupo.getIdGrupo();
            List<ParticipantesGrupo> participantes = participantesDAO.obtenerParticipantesPorGrupo(idGrupo);
            List<String> cedulasParticipantes = new ArrayList<>();

            for (ParticipantesGrupo p : participantes) {
                String cedulaParticipante = p.getIdJugador();
                cedulasParticipantes.add(cedulaParticipante);

                if (!fotosJugadores.containsKey(cedulaParticipante)) {
                    String fotoUrl = fotoDAO.obtenerFotoPorCedula(cedulaParticipante);
                    fotosJugadores.put(cedulaParticipante, fotoUrl != null ? fotoUrl : "img/default.png");
                }

                if (!nombresJugadores.containsKey(cedulaParticipante)) {
                    Jugador j = jugadorDAO.obtenerJugadorPorCedula(cedulaParticipante);
                    String nombre = j != null ? j.getNombre() + " " + j.getApellido() : cedulaParticipante;
                    nombresJugadores.put(cedulaParticipante, nombre);
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
                String nombre = creador != null ? creador.getNombre() + " " + creador.getApellido() : cedulaCreador;
                nombresJugadores.put(cedulaCreador, nombre);
            }

            if (!idsDelJugador.contains(idGrupo) && "abierto".equalsIgnoreCase(grupo.getEstado())) {
                List<String> categoriasPermitidas = Arrays.stream(grupo.getCategoria().split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .toList();

                if (categoriasPermitidas.contains(categoriaJugadorPrimeraPalabra)) {
                    gruposDisponibles.add(grupo);
                }
            }
        }

        request.setAttribute("gruposDelJugador", gruposDelJugador);
        request.setAttribute("gruposDisponibles", gruposDisponibles);
        request.setAttribute("participantesPorGrupo", participantesPorGrupo);
        request.setAttribute("fotosJugadores", fotosJugadores);
        request.setAttribute("nombresJugadores", nombresJugadores);
        request.setAttribute("categoriaJugador", categoriaJugador);
        request.setAttribute("cedulaUsuario", cedula);

        request.getRequestDispatcher("/grupoJugador.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String cedulaUsuario = (usuario != null) ? usuario.getCedula() : null;

        if (cedulaUsuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("idGrupo");

        if (accion != null && idStr != null && !idStr.isEmpty()) {
            try {
                int idGrupo = Integer.parseInt(idStr);
                GrupoDAO grupoDAO = new GrupoDAO();
                JugadorDAO jugadorDAO = new JugadorDAO();
                ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();

                Grupo grupo = grupoDAO.obtenerGrupoPorId(idGrupo);

                switch (accion) {
                    case "cerrar":
                    case "eliminar":
                        if (grupo == null || !cedulaUsuario.equals(grupo.getIdCreador())) {
                            request.setAttribute("mensajeError", "No tienes permiso para modificar este grupo.");
                            break;
                        }

                        if (accion.equals("cerrar")) {
                            grupoDAO.cerrarGrupo(idGrupo);
                            request.setAttribute("mensajeExito", "Grupo cerrado correctamente.");

                            List<ParticipantesGrupo> participantes = participantesDAO.obtenerParticipantesPorGrupo(idGrupo);
                            List<Jugador> jugadores = new ArrayList<>();
                            for (ParticipantesGrupo p : participantes) {
                                Jugador j = jugadorDAO.obtenerJugadorPorCedula(p.getIdJugador());
                                if (j != null) jugadores.add(j);
                            }

                            String correoCreador = usuario.getCorreo();
                            String nombreCreador = usuario.getNombre();

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
                                EnviarCorreo.enviar(correoCreador, "Tu grupo se ha completado", mensaje);
                            } catch (MessagingException e) {
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
                                        EnviarCorreo.enviar(j.getCorreo(), "Confirmación de grupo completo", mensajeJugador);
                                        System.out.println("Correo enviado a " + j.getCorreo());
                                    } catch (MessagingException e) {
                                        System.err.println("Error al enviar correo a " + j.getCorreo() + ": " + e.getMessage());
                                    }
                                }
                            }
                        } else {
                            grupoDAO.eliminarGrupo(idGrupo);
                            request.setAttribute("mensajeExito", "Grupo eliminado correctamente.");
                        }
                        break;

                    case "salir":
                        participantesDAO.eliminarParticipante(idGrupo, cedulaUsuario);
                        request.setAttribute("mensajeExito", "Has salido del grupo correctamente.");
                        break;

                    default:
                        request.setAttribute("mensajeError", "Acción no reconocida.");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Ocurrió un error al procesar la acción.");
            }
        }

        doGet(request, response);
    }
}
