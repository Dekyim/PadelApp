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
            // Obtener todos los jugadores
            Vector<Jugador> jugadores = new JugadorDAO().listarJugadores();

            // Obtener el número de página actual desde el parámetro ?page=
            int paginaActual = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    paginaActual = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {}
            }

            // Calcular total de páginas
            int totalJugadores = jugadores.size();
            int totalPaginas = (int) Math.ceil((double) totalJugadores / USUARIOS_POR_PAGINA);

            // Asegurar que la página actual esté dentro del rango
            if (paginaActual < 1) paginaActual = 1;
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;

            // Calcular índices de inicio y fin
            int inicio = (paginaActual - 1) * USUARIOS_POR_PAGINA;
            int fin = Math.min(inicio + USUARIOS_POR_PAGINA, totalJugadores);

            // Sublista de jugadores de la página actual
            List<Jugador> paginaJugadores = new ArrayList<>(jugadores.subList(inicio, fin));

            // Convertir a nombres
            List<String> nombres = new ArrayList<>();
            paginaJugadores.forEach(j -> nombres.add(j.getNombre() + " " + j.getApellido()));

            // Pasar datos al JSP
            request.setAttribute("nombres", nombres);
            request.setAttribute("paginaActual", paginaActual);
            request.setAttribute("totalPaginas", totalPaginas);

            // Redirigir al JSP
            request.getRequestDispatcher("usuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar usuarios", e);
        }
    }
}
