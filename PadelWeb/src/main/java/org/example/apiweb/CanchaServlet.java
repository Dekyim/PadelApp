package org.example.apiweb;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import dao.CanchaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cancha;


@WebServlet(name = "CanchaServlet", value = "/cancha")
public class CanchaServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {

            List<Integer> numeroCanchas = new ArrayList<>();
            Vector<Cancha> canchas = new CanchaDAO().listarCancha();
            canchas.forEach(Cancha -> {
                numeroCanchas.add(Cancha.getNumero());
            });
            request.setAttribute("listaCanchas", numeroCanchas);
            request.getRequestDispatcher("cancha.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



    public void destroy() {
    }
}