package org.example.apiweb;

import dao.GrupoDAO;
import dao.ParticipantesGrupoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Grupo;
import models.ParticipantesGrupo;
import models.Usuario;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "CrearGrupoJugadorServlet", value = "/creargrupojugador")
public class CrearGrupoJugadorServlet extends HttpServlet {

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

        List<String> categorias = Arrays.asList("primera", "segunda", "tercera", "cuarta", "quinta", "desconoce");
        request.setAttribute("categoriasDisponibles", categorias);
        request.setAttribute("cedulaUsuario", cedulaUsuario);

        request.getRequestDispatcher("/crearGrupoJugador.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String idCreador = (usuario != null) ? usuario.getCedula() : null;

        if (idCreador == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Time horaDesde = Time.valueOf(request.getParameter("horaDesde") + ":00");
            Time horaHasta = Time.valueOf(request.getParameter("horaHasta") + ":00");
            String[] categoriasSeleccionadas = request.getParameterValues("categorias");
            int cupos = Integer.parseInt(request.getParameter("cupos"));
            String descripcion = request.getParameter("descripcion");

            String categoriasStr = String.join(",", categoriasSeleccionadas);

            Grupo grupo = new Grupo();
            grupo.setIdCreador(idCreador);
            grupo.setHoraDesde(horaDesde);
            grupo.setHoraHasta(horaHasta);
            grupo.setCategoria(categoriasStr);
            grupo.setCupos(cupos);
            grupo.setDescripcion(descripcion);
            grupo.setEstado("abierto");

            GrupoDAO grupoDAO = new GrupoDAO();
            grupoDAO.crearGrupo(grupo);

            ParticipantesGrupoDAO participantesDAO = new ParticipantesGrupoDAO();
            ParticipantesGrupo creador = new ParticipantesGrupo(grupo.getIdGrupo(), idCreador, true);
            participantesDAO.agregarParticipante(creador);

            response.sendRedirect("grupojugador");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al crear el grupo.");
            doGet(request, response);
        }
    }
}