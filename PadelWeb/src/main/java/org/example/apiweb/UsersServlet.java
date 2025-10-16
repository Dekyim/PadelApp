package org.example.apiweb;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import dao.JugadorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Jugador;


@WebServlet(name = "userServlet", value = "/users")
public class UsersServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {

            List<String> nombres = new ArrayList<>();
            Vector<Jugador> jugadores = new JugadorDAO().listarJugadores();
            jugadores.forEach(jugador -> {
                nombres.add(jugador.getNombre() + " " + jugador.getApellido());
            });
            request.setAttribute("listaJugadores", nombres);
            request.getRequestDispatcher("usuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



    public void destroy() {
    }
}