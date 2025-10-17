package org.example.apiweb;
import dao.JugadorDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Jugador;

import java.io.IOException;

@WebServlet("/verPerfilJugador")
public class VerPerfilServletJugador extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object authUser = (session != null) ? session.getAttribute("authUser") : null;

        if (authUser instanceof Jugador jugador) {
            request.setAttribute("jugador", jugador);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilJugador.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    // Guardar cambios
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object authUser = (session != null) ? session.getAttribute("authUser") : null;

        if (!(authUser instanceof Jugador jugador)) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener datos del formulario
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String categoria = request.getParameter("categoria");
        String genero = request.getParameter("genero");

        // Convertir checkboxes a tipos compatibles con la clase
        int incumplePago = request.getParameter("incumplePago") != null ? 1 : 0;
        boolean estaBaneado = request.getParameter("estaBaneado") != null;

        // Actualizar objeto
        jugador.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));
        jugador.setCategoria(categoria);
        jugador.setGenero(genero);
        jugador.setIncumplePago(incumplePago);
        jugador.setEstaBaneado(estaBaneado);

        // Persistir cambios
        JugadorDAO dao = new JugadorDAO();
        dao.actualizarJugador(jugador);

        // Actualizar sesión
        session.setAttribute("authUser", jugador);

        // Mostrar perfil actualizado
        request.setAttribute("jugador", jugador);
        request.setAttribute("mensaje", "Datos actualizados correctamente.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilJugador.jsp");
        dispatcher.forward(request, response);
    }


}
