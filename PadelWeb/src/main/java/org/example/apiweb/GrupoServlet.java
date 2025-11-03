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

                // Asegurar también la foto y nombre del creador
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