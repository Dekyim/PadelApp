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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@WebServlet(name = "EditarCanchaServlet", value = "/editarCancha")
@MultipartConfig
public class EditarCanchaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String numeroStr = request.getParameter("numero");

        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                CanchaDAO dao = new CanchaDAO();

                Vector<Cancha> todas = dao.listarCancha();
                Cancha cancha = todas.stream()
                        .filter(c -> c.getNumero() == numero)
                        .findFirst()
                        .orElse(null);

                if (cancha != null) {
                    // Cargar imagen actual
                    String urlFoto = new FotoCanchaDAO().obtenerFotoPorId(cancha.getId());
                    request.setAttribute("cancha", cancha);
                    request.setAttribute("urlFoto", urlFoto);
                    request.getRequestDispatcher("editarCancha.jsp").forward(request, response);
                } else {
                    response.sendRedirect("cancha");
                }

            } catch (Exception e) {
                throw new ServletException("Error al cargar cancha para edición", e);
            }
        } else {
            response.sendRedirect("cancha");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            int numero = Integer.parseInt(request.getParameter("numero"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            boolean esTechada = request.getParameter("esTechada") != null;
            boolean estaDisponible = request.getParameter("estaDisponible") != null;

            // Obtener horarios seleccionados
            String[] horariosSeleccionados = request.getParameterValues("horarios");

            Vector<Time> horarios = new Vector<>();
            if (horariosSeleccionados != null) {
                for (String h : horariosSeleccionados) {
                    horarios.add(Time.valueOf(h + ":00"));
                }
            }

            CanchaHorario canchaHorario = new CanchaHorario(0, horarios);
            Cancha canchaActualizada = new Cancha(0, esTechada, precio, estaDisponible, numero, canchaHorario);

            CanchaDAO dao = new CanchaDAO();
            dao.actualizarCancha(canchaActualizada);
            dao.actualizarHorariosCancha(canchaActualizada.getId(), horarios);


            // Manejar imagen si se subió
            Part imagenPart = request.getPart("fotoCancha");
            if (imagenPart != null && imagenPart.getSize() > 0 && imagenPart.getContentType().startsWith("image/")) {
                File tempFile = File.createTempFile("cancha_", ".img");
                imagenPart.write(tempFile.getAbsolutePath());

                try {
                    Cloudinary cloudinary = CloudinaryConfig.getInstance();
                    Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                            "folder", "perfil_canchas",
                            "public_id", "cancha_" + numero,
                            "overwrite", true
                    ));

                    String url = (String) uploadResult.get("secure_url");
                    new FotoCanchaDAO().guardarFoto(numero, url);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServletException("Error al subir imagen de cancha", e);
                } finally {
                    tempFile.delete();
                }
            }

            response.sendRedirect("cancha");

        } catch (Exception e) {
            throw new ServletException("Error al actualizar cancha", e);
        }
    }
}
