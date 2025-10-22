package org.example.apiweb;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.*;
import models.*;

@WebServlet("/agregarUsuario")
public class agregarUsuarioServlet extends HttpServlet {

    private JugadorDAO jugadorDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        jugadorDAO = new JugadorDAO();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("csrfToken") == null) {
            String token = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }

        request.getRequestDispatcher("agregarUsuario.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tokenForm = request.getParameter("csrfToken");
        String tokenSession = (String) request.getSession().getAttribute("csrfToken");

        if (tokenSession == null || !tokenSession.equals(tokenForm)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF Token inválido");
            return;
        }

        try {
            request.setCharacterEncoding("UTF-8");

            String cedula = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String correo = request.getParameter("correo");
            String contrasenia = request.getParameter("contrasenia");
            String telefono = request.getParameter("telefono");
            String fechaStr = request.getParameter("fechaNacimiento");
            String categoria = request.getParameter("categoria");
            String genero = request.getParameter("genero");

            if (usuarioDAO.existeUsuario(cedula)) {
                request.setAttribute("errorRegistro", "Ya existe un usuario con esa cédula.");
                request.getRequestDispatcher("/agregarUsuario.jsp").forward(request, response);
                return;
            }

            Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);

            String contraseniaHasheada = BCrypt.hashpw(contrasenia, BCrypt.gensalt());

            int incumplePago = 0;
            boolean estaBaneado = false;

            Jugador nuevoJugador = new Jugador(
                    cedula, nombre, apellido, correo, telefono,
                    contraseniaHasheada, fechaNacimiento, categoria, genero,
                    incumplePago, estaBaneado
            );

            jugadorDAO.altaJugador(nuevoJugador);

            request.setAttribute("mensajeExito", "Registro exitoso.");
            request.getRequestDispatcher("/usuarios.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorRegistro", "Error al registrar jugador: " + e.getMessage());
            request.getRequestDispatcher("/agregarUsuario.jsp").forward(request, response);
        }
    }
}