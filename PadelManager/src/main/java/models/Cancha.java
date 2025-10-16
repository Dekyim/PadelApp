package models;

public class Cancha {

    private int id;              // autoincremental, clave primaria
    private boolean esTechada;
    private double precio;
    private boolean estaDisponible;
    private int numero;          // único, ingresado por el usuario
    private CanchaHorario horario;

    // Constructor para insertar nueva cancha (id se genera en la BD)
    public Cancha(boolean esTechada, double precio, boolean estaDisponible, int numero, CanchaHorario horario) {
        this.esTechada = esTechada;
        this.precio = precio;
        this.estaDisponible = estaDisponible;
        this.numero = numero;
        this.horario = horario;

    }

    // Constructor para cargar cancha desde BD (ya tiene id)
    public Cancha(int id, boolean esTechada, double precio, boolean estaDisponible, int numero, CanchaHorario horario) {
        this.id = id;
        this.esTechada = esTechada;
        this.precio = precio;
        this.estaDisponible = estaDisponible;
        this.numero = numero;
        this.horario = horario;
    }


    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isEsTechada() {
        return esTechada;
    }

    public void setEsTechada(boolean esTechada) {
        this.esTechada = esTechada;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public CanchaHorario getHorario() {
        return horario;
    }

    public void setHorario(CanchaHorario horario) {
        this.horario = horario;
    }
}

