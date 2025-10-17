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
import models.Jugador;

@WebServlet(name = "userServlet", value = "/users")
public class UsersServlet extends HttpServlet {

    private static final int USUARIOS_POR_PAGINA = 9;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            JugadorDAO dao = new JugadorDAO();
            Vector<Jugador> jugadores = dao.listarJugadores();

            int paginaActual = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    paginaActual = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {}
            }

            int totalJugadores = jugadores.size();
            int totalPaginas = (int) Math.ceil((double) totalJugadores / USUARIOS_POR_PAGINA);

            if (paginaActual < 1) paginaActual = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            int inicio = (paginaActual - 1) * USUARIOS_POR_PAGINA;
            int fin = Math.min(inicio + USUARIOS_POR_PAGINA, totalJugadores);

            List<Jugador> paginaJugadores = new ArrayList<>(jugadores.subList(inicio, fin));

            request.setAttribute("jugadores", paginaJugadores);
            request.setAttribute("paginaActual", paginaActual);
            request.setAttribute("totalPaginas", totalPaginas);

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
                System.out.println("Jugador con cÃ©dula " + cedula + " eliminado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException("Error al eliminar jugador", e);
            }
        }

        // Redirige nuevamente a la lista
        response.sendRedirect("users");
    }
}