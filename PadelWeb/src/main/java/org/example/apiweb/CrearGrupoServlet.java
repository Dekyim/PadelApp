package org.example.apiweb;

import dao.GrupoDAO;
import dao.ParticipantesGrupoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Grupo;
import models.ParticipantesGrupo;
import models.Usuario;
import dao.JugadorDAO;
import models.Jugador;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "CrearGrupoServlet", value = "/creargrupo")
public class CrearGrupoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String cedulaUsuario = (usuario != null) ? usuario.getCedula() : null;

        if (cedulaUsuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String idGrupoStr = request.getParameter("idGrupo");
        if (idGrupoStr != null && !idGrupoStr.isEmpty()) {
            try {
                int idGrupo = Integer.parseInt(idGrupoStr);
                GrupoDAO grupoDAO = new GrupoDAO();
                Grupo grupo = grupoDAO.obtenerGrupoPorId(idGrupo);
                ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();
                int cantidadActual = participantesDAO.obtenerCedulasPorGrupo(idGrupo).size();

                request.setAttribute("grupoEditado", grupo);
                request.setAttribute("cantidadActual", cantidadActual);
            } catch (Exception e) {
                request.setAttribute("mensajeError", "No se pudo cargar el grupo para edición.");
            }
        }


        List<String> categorias = Arrays.asList("primera", "segunda", "tercera", "cuarta", "quinta", "desconoce");
        request.setAttribute("categoriasDisponibles", categorias);
        request.setAttribute("cedulaUsuario", cedulaUsuario);

        request.getRequestDispatcher("/crearGrupo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idGrupoStr = request.getParameter("idGrupo");
        boolean esEdicion = idGrupoStr != null && !idGrupoStr.isEmpty();

        String cedulaJugador = request.getParameter("cedulaJugador");
        JugadorDAO jugadorDAO = new JugadorDAO();
        Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedulaJugador);

        if (jugador == null) {
            request.setAttribute("mensajeError", "La cédula ingresada no corresponde a ningún jugador registrado.");
            doGet(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String idCreador = request.getParameter("cedulaJugador");


        if (idCreador == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Time horaDesde = Time.valueOf(request.getParameter("horaDesde") + ":00");
            Time horaHasta = Time.valueOf(request.getParameter("horaHasta") + ":00");
            String[] categoriasSeleccionadas = request.getParameterValues("categorias");
            String categoriasStr = String.join(",", categoriasSeleccionadas);
            int cupos = Integer.parseInt(request.getParameter("cupos"));
            String descripcion = request.getParameter("descripcion");

            GrupoDAO grupoDAO = new GrupoDAO();
            ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();

            Grupo grupo = new Grupo();
            grupo.setIdCreador(idCreador);
            grupo.setHoraDesde(horaDesde);
            grupo.setHoraHasta(horaHasta);
            grupo.setCategoria(categoriasStr);
            grupo.setCupos(cupos);
            grupo.setDescripcion(descripcion);
            grupo.setEstado("abierto");

            if (esEdicion) {
                int idGrupo = Integer.parseInt(idGrupoStr);
                grupo.setIdGrupo(idGrupo);

                int cantidadActual = participantesDAO.obtenerCedulasPorGrupo(idGrupo).size();
                if (cupos < cantidadActual) {
                    request.setAttribute("mensajeError", "No puedes asignar menos cupos que los participantes actuales.");
                    doGet(request, response);
                    return;
                }

                grupoDAO.actualizarGrupo(grupo);
            } else {
                grupoDAO.crearGrupo(grupo);
                ParticipantesGrupo creador = new ParticipantesGrupo(grupo.getIdGrupo(), idCreador, true);
                participantesDAO.agregarParticipante(creador);
            }

            response.sendRedirect("grupo");


        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al crear el grupo.");
            doGet(request, response);
        }
    }
}