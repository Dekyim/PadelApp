package org.example.apiweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dao.JugadorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Jugador;

@WebServlet(name = "userServlet", value = "/users")
public class UsersServlet extends HttpServlet {

    private static final int USUARIOS_POR_PAGINA = 9;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        String csrfToken = (String) session.getAttribute("csrfToken");
        if (csrfToken == null) {
            csrfToken = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }
        request.setAttribute("csrfToken", csrfToken);

        response.setContentType("text/html;charset=UTF-8");

        try {
            JugadorDAO dao = new JugadorDAO();
            String buscar = request.getParameter("buscar");
            Vector<Jugador> jugadores;

            if (buscar != null && !buscar.trim().isEmpty()) {
                jugadores = dao.buscarJugadores(buscar.trim());
            } else {
                jugadores = dao.listarJugadores();
            }

            int paginaActual = 1;
            int totalPaginas = 1;
            List<Jugador> paginaJugadores;

            if (buscar == null || buscar.trim().isEmpty()) {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    try {
                        paginaActual = Integer.parseInt(pageParam);
                    } catch (NumberFormatException ignored) {}
                }

                int totalJugadores = jugadores.size();
                totalPaginas = (int) Math.ceil((double) totalJugadores / USUARIOS_POR_PAGINA);

                if (paginaActual < 1) paginaActual = 1;
                if (paginaActual > totalPaginas) paginaActual = totalPaginas;

                int inicio = (paginaActual - 1) * USUARIOS_POR_PAGINA;
                int fin = Math.min(inicio + USUARIOS_POR_PAGINA, totalJugadores);

                paginaJugadores = new ArrayList<>(jugadores.subList(inicio, fin));
            } else {
                paginaJugadores = new ArrayList<>(jugadores);
            }

            request.setAttribute("jugadores", paginaJugadores);
            request.setAttribute("paginaActual", paginaActual);
            request.setAttribute("totalPaginas", totalPaginas);
            request.setAttribute("buscar", buscar);

            request.getRequestDispatcher("usuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar usuarios", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String accion = request.getParameter("accion");
        String cedula = request.getParameter("cedula");

        if ("eliminar".equals(accion) && cedula != null && !cedula.isEmpty()) {
            try {
                JugadorDAO dao = new JugadorDAO();
                dao.eliminarJugador(cedula);
                System.out.println("Jugador con cédula " + cedula + " eliminado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException("Error al eliminar jugador", e);
            }
            response.sendRedirect("users");
            return;
        }

        if ("editar".equals(accion) && cedula != null && !cedula.isEmpty()) {
            JugadorDAO dao = new JugadorDAO();
            Jugador jugador = dao.obtenerJugadorPorCedula(cedula);

            if (jugador != null) {
                request.setAttribute("jugador", jugador);
                request.getRequestDispatcher("verPerfilJugador.jsp").forward(request, response);
                return;
            } else {
                response.sendRedirect("users");
                return;
            }
        }

        response.sendRedirect("users");
    }
}

