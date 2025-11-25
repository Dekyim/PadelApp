package org.example.apiweb;

import dao.CanchaDAO;
import dao.FotoCanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cancha;
import models.Usuario;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@WebServlet(name = "CanchaUsuarioServlet", value = "/canchaUsuario")
public class CanchaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession(false);
            Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;

            if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            Vector<Cancha> canchas = new CanchaDAO().listarCancha();
            request.setAttribute("listaCanchas", canchas);

            FotoCanchaDAO fotoDAO = new FotoCanchaDAO();
            Map<Integer, String> fotosPorId = new HashMap<>();
            for (Cancha cancha : canchas) {
                String url = fotoDAO.obtenerFotoPorId(cancha.getId());
                fotosPorId.put(cancha.getId(), url);
            }
            request.setAttribute("fotosPorId", fotosPorId);

            request.setAttribute("usuario", usuario);
            request.setAttribute("cedulaUsuario", usuario.getCedula());
            request.setAttribute("esAdmin", usuario.esAdministrador());

            request.getRequestDispatcher("/canchaUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar las canchas", e);
        }
    }
}
