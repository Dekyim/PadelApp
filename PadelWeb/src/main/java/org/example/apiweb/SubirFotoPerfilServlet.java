package org.example.apiweb;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import models.Usuario;
import org.example.apiweb.CloudinaryConfig;
import dao.FotoPerfilUsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import models.Usuario;

@WebServlet(name = "SubirFotoPerfilServlet", value = "/subirFotoPerfil")
@MultipartConfig
public class SubirFotoPerfilServlet extends HttpServlet {

    private final FotoPerfilUsuarioDAO fotoDAO = new FotoPerfilUsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("authUser") : null;
        String cedula = request.getParameter("cedula");
        Part imagenPart = request.getPart("fotoPerfil");
        boolean esAdmin = usuario != null && usuario.esAdministrador();
        boolean esSuPropioPerfil = esAdmin && usuario.getCedula().equals(cedula);



        if (cedula == null || imagenPart == null || imagenPart.getSize() == 0) {
            request.setAttribute("mensajeFoto", "No se seleccionó ninguna imagen.");
            request.getRequestDispatcher("/verPerfilJugador?cedula=" + cedula).forward(request, response);
            return;
        }

        if (!imagenPart.getContentType().startsWith("image/")) {
            request.setAttribute("mensajeFoto", "El archivo no es una imagen válida.");
            request.getRequestDispatcher("/verPerfilJugador?cedula=" + cedula).forward(request, response);
            return;
        }

        File tempFile = File.createTempFile("pfp_", ".img");
        imagenPart.write(tempFile.getAbsolutePath());

        try {
            Cloudinary cloudinary = CloudinaryConfig.getInstance();
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "folder", "perfil_usuarios",
                    "public_id", "usuario_" + cedula,
                    "overwrite", true
            ));

            String url = (String) uploadResult.get("secure_url");
            fotoDAO.guardarFoto(cedula, url);

            String urlFoto = fotoDAO.obtenerFotoPorCedula(cedula);
            request.setAttribute("fotoPerfil", urlFoto);
            request.setAttribute("mensajeFoto", "Foto actualizada correctamente.");

            if (esSuPropioPerfil) {
                // El administrador está editando su propia foto
                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("/verPerfilAdministrador.jsp").forward(request, response);
            } else {
                // El administrador está editando la foto de otro jugador, o es un jugador normal
                request.setAttribute("cedula", cedula);
                response.sendRedirect("verPerfilJugador?cedula=" + cedula);
            }







        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeFoto", "Error al subir la imagen.");
            String destino = "/verPerfilJugador?cedula=" + cedula;
            if (usuario != null && usuario.esAdministrador()) {
                String urlFoto = fotoDAO.obtenerFotoPorCedula(cedula);
                request.setAttribute("usuario", usuario);
                request.setAttribute("fotoPerfil", urlFoto);
                request.getRequestDispatcher("/verPerfilAdministrador.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(destino).forward(request, response);
            }


        } finally {
            tempFile.delete();
        }
    }
}