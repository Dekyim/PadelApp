package org.example.apiweb;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.FotoCanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "SubirFotoCanchaServlet", value = "/subirFotoCancha")
@MultipartConfig
public class SubirFotoCanchaServlet extends HttpServlet {

    private final FotoCanchaDAO fotoDAO = new FotoCanchaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        Part imagenPart = request.getPart("fotoCancha");

        if (idStr == null || imagenPart == null || imagenPart.getSize() == 0) {
            request.setAttribute("mensajeError", "No se seleccionó ninguna imagen.");
            request.getRequestDispatcher("cancha").forward(request, response);
            return;
        }

        int idCancha;
        try {
            idCancha = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "ID de cancha inválido.");
            request.getRequestDispatcher("cancha").forward(request, response);
            return;
        }

        if (!imagenPart.getContentType().startsWith("image/")) {
            request.setAttribute("mensajeError", "El archivo no es una imagen válida.");
            request.getRequestDispatcher("cancha").forward(request, response);
            return;
        }

        File tempFile = File.createTempFile("cancha_", ".img");
        imagenPart.write(tempFile.getAbsolutePath());

        try {
            Cloudinary cloudinary = CloudinaryConfig.getInstance();
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                    "folder", "perfil_canchas",
                    "public_id", "cancha_" + idCancha,
                    "overwrite", true
            ));

            String url = (String) uploadResult.get("secure_url");
            fotoDAO.guardarFoto(idCancha, url);

            request.setAttribute("mensajeExito", "Foto de cancha actualizada correctamente.");
            request.getRequestDispatcher("cancha").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al subir la imagen.");
            request.getRequestDispatcher("cancha").forward(request, response);
        } finally {
            tempFile.delete();
        }
    }
}

