package org.example.apiweb;

import dao.GrupoDAO;
import dao.ParticipantesGrupoDAO;
import dao.JugadorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Grupo;
import models.ParticipantesGrupo;
import models.Usuario;
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

                String horaDesdeStr = grupo.getHoraDesde().toString().substring(0, 5);
                String horaHastaStr = grupo.getHoraHasta().toString().substring(0, 5);

                request.setAttribute("grupoEditado", grupo);
                request.setAttribute("cantidadActual", cantidadActual);
                request.setAttribute("horaDesdeStr", horaDesdeStr);
                request.setAttribute("horaHastaStr", horaHastaStr);
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
            String horaDesdeStr = request.getParameter("horaDesde");
            String horaHastaStr = request.getParameter("horaHasta");
            String[] categoriasSeleccionadas = request.getParameterValues("categorias");
            String categoriasStr = categoriasSeleccionadas != null
                    ? String.join(",", categoriasSeleccionadas)
                    : "";
            int cupos = Integer.parseInt(request.getParameter("cupos"));
            String descripcion = request.getParameter("descripcion");

            Time horaDesde = null;
            Time horaHasta = null;

            if (horaDesdeStr != null && horaDesdeStr.matches("\\d{2}:\\d{2}")) {
                horaDesde = Time.valueOf(horaDesdeStr + ":00");
            }
            if (horaHastaStr != null && horaHastaStr.matches("\\d{2}:\\d{2}")) {
                horaHasta = Time.valueOf(horaHastaStr + ":00");
            }

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

                Grupo grupoExistente = grupoDAO.obtenerGrupoPorId(idGrupo);
                int cantidadActual = participantesDAO.obtenerCedulasPorGrupo(idGrupo).size();
                if (cupos < cantidadActual) {
                    request.setAttribute("mensajeError", "No puedes asignar menos cupos que los participantes actuales.");
                    doGet(request, response);
                    return;
                }

                if (grupo.getHoraDesde() == null) grupo.setHoraDesde(grupoExistente.getHoraDesde());
                if (grupo.getHoraHasta() == null) grupo.setHoraHasta(grupoExistente.getHoraHasta());
                if (grupo.getCategoria() == null || grupo.getCategoria().isEmpty()) {
                    grupo.setCategoria(grupoExistente.getCategoria());
                }

                grupoDAO.actualizarGrupo(grupo);
            } else {
                grupoDAO.crearGrupo(grupo);
                ParticipantesGrupo creador = new ParticipantesGrupo(grupo.getIdGrupo(), idCreador, true);
                participantesDAO.agregarParticipante(creador);
            }

            response.sendRedirect(request.getContextPath() + "/grupo");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al crear o actualizar el grupo.");
            doGet(request, response);
        }
    }
}
