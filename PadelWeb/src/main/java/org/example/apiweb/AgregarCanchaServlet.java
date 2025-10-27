package org.example.apiweb;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.CanchaDAO;
import dao.FotoCanchaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cancha;
import models.CanchaHorario;
import org.example.apiweb.CloudinaryConfig;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Map;
import java.util.Vector;

@WebServlet(name = "AgregarCanchaServlet", value = "/agregarCancha")
@MultipartConfig
public class AgregarCanchaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            int numero = Integer.parseInt(request.getParameter("numero"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            boolean esTechada = request.getParameter("esTechada") != null;
            boolean estaDisponible = request.getParameter("estaDisponible") != null;

            String[] horariosSeleccionados = request.getParameterValues("horarios");
            Vector<Time> horarios = new Vector<>();
            if (horariosSeleccionados != null) {
                for (String h : horariosSeleccionados) {
                    horarios.add(Time.valueOf(h + ":00"));
                }
            }

            CanchaHorario canchaHorario = new CanchaHorario(0, horarios);
            Cancha nuevaCancha = new Cancha(0, esTechada, precio, estaDisponible, numero, canchaHorario);

            CanchaDAO dao = new CanchaDAO();
            dao.altaCancha(nuevaCancha); // esto setea el ID en nuevaCancha

            /*
            Part imagenPart = request.getPart("fotoCancha");
            if (imagenPart != null && imagenPart.getSize() > 0 && imagenPart.getContentType().startsWith("image/")) {
                File tempFile = File.createTempFile("cancha_", ".img");
                imagenPart.write(tempFile.getAbsolutePath());

                try {
                    Cloudinary cloudinary = CloudinaryConfig.getInstance();
                    Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                            "folder", "perfil_canchas",
                            "public_id", "cancha_" + nuevaCancha.getId(),
                            "overwrite", true
                    ));

                    String url = (String) uploadResult.get("secure_url");
                    new FotoCanchaDAO().guardarFoto(nuevaCancha.getId(), url);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServletException("Error al subir imagen de cancha", e);
                } finally {
                    tempFile.delete();
                }
            }
            */

            request.setAttribute("mensajeExito", "Cancha agregada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al agregar la cancha: " + e.getMessage());
        }

        request.getRequestDispatcher("/agregarCancha.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("/agregarCancha.jsp").forward(request, response);
    }
}