package org.example.apiweb;

import dao.JugadorDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Usuario;
import models.Jugador;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/verPerfilJugador")
public class VerPerfilServletJugador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("authUser");
        JugadorDAO jugadorDAO = new JugadorDAO();

        String cedulaParam = request.getParameter("cedula");
        Jugador jugador;

        if (usuario.esAdministrador()) {
            // Admin puede pasar la cédula del jugador que quiere editar
            if (cedulaParam == null || cedulaParam.isEmpty()) {
                response.sendRedirect("inicioAdmin");
                return;
            }
            jugador = jugadorDAO.obtenerJugadorPorCedula(cedulaParam);
        } else {
            // Jugador normal solo puede editar su propio perfil
            jugador = jugadorDAO.obtenerJugadorPorCedula(usuario.getCedula());
        }

        if (jugador == null) {
            if (usuario.esAdministrador()) {
                response.sendRedirect("inicioAdmin");
            } else {
                response.sendRedirect("inicioUsers");
            }
            return;
        }

        request.setAttribute("jugador", jugador);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilJugador.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("authUser");
        JugadorDAO jugadorDAO = new JugadorDAO();

        // Obtener cédula: admins pueden editar cualquier jugador, usuarios normales solo su perfil
        String cedula = request.getParameter("cedula");
        if (!usuario.esAdministrador()) {
            cedula = usuario.getCedula();
        }

        Jugador jugador = jugadorDAO.obtenerJugadorPorCedula(cedula);
        if (jugador == null) {
            if (usuario.esAdministrador()) {
                response.sendRedirect("inicioAdmin");
            } else {
                response.sendRedirect("inicioUsers");
            }
            return;
        }

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String categoria = request.getParameter("categoria");
        String genero = request.getParameter("genero");

        // Actualizar datos en memoria
        jugador.setNombre(nombre);
        jugador.setApellido(apellido);
        jugador.setCorreo(correo);
        jugador.setTelefono(telefono);
        jugador.setCategoria(categoria);
        jugador.setGenero(genero);

        if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
            jugador.setFechaNacimiento(Date.valueOf(fechaNacimiento));
        }

        // Guardar cambios en la BD
        jugadorDAO.actualizarJugador(jugador);

        // Si es jugador normal, también actualizar los datos en sesión
        if (!usuario.esAdministrador()) {
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);
            session.setAttribute("authUser", usuario);
        }

        // Mostrar mensaje de éxito
        request.setAttribute("jugador", jugador);
        request.setAttribute("mensaje", "Datos actualizados correctamente.");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilJugador.jsp");
        dispatcher.forward(request, response);
    }
}

