package org.example.apiweb;
import dao.FotoPerfilUsuarioDAO;
import dao.AdministradorDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Administrador;
import models.Usuario;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/verPerfilAdmin")
public class VerPerfilServletAdmin extends HttpServlet {

    private final FotoPerfilUsuarioDAO fotoDAO = new FotoPerfilUsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

        if (usuario != null) {
            String urlFoto = fotoDAO.obtenerFotoPorCedula(usuario.getCedula());
            request.setAttribute("usuario", usuario);
            request.setAttribute("fotoPerfil", urlFoto);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilAdministrador.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String fechaIngreso = request.getParameter("fechaIngreso");

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setFechaIngreso(Date.valueOf(fechaIngreso));

        if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;
            admin.setContraseniaCuenta(admin.getContraseniaCuenta());
            AdministradorDAO dao = new AdministradorDAO();
            dao.modificarAdministrador(admin);
        }

        session.setAttribute("authUser", usuario);
        
        request.setAttribute("usuario", usuario);
        request.setAttribute("mensaje", "Datos actualizados correctamente.");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/verPerfilAdministrador.jsp");
        dispatcher.forward(request, response);
    }
}
