package org.example.apiweb;

import dao.GrupoDAO;
import dao.JugadorDAO;
import dao.ParticipantesGrupoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Grupo;
import models.Jugador;
import models.Usuario;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "GrupoJugadorServlet", value = "/grupojugador")
public class GrupoJugadorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        List<Grupo> todosLosGrupos = grupoDAO.listarGrupos();
        List<Grupo> gruposDelJugador = grupoDAO.obtenerGruposPorJugador(cedula);
        Set<Integer> idsDelJugador = new HashSet<>();
        for (Grupo g : gruposDelJugador) {
            idsDelJugador.add(g.getIdGrupo());
        }

        List<Grupo> gruposDisponibles = new ArrayList<>();
        Map<Integer, List<String>> participantesPorGrupo = new HashMap<>();

        for (Grupo grupo : todosLosGrupos) {
            List<String> cedulas = participantesDAO.obtenerCedulasPorGrupo(grupo.getIdGrupo());
            participantesPorGrupo.put(grupo.getIdGrupo(), cedulas);

            if (!idsDelJugador.contains(grupo.getIdGrupo())) {
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
        request.setAttribute("categoriaJugador", categoriaJugador);
        request.setAttribute("cedulaUsuario", cedula);

        request.getRequestDispatcher("/grupoJugador.jsp").forward(request, response);
    }
}