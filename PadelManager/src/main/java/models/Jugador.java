package models;
import java.util.Date;

public class Jugador extends Usuario {
    private Date fechaNacimiento;
    private String categoria;
    private String genero;
    private int incumplePago;
    private boolean estaBaneado;

    public Jugador(String cedula, String nombre, String apellido, String correo, String telefono, String contraseniaCuenta, Date fechaNacimiento, String categoria, String genero, int incumplePago, boolean estaBaneado) {
        super(cedula, nombre, apellido, correo, telefono, contraseniaCuenta);
        this.fechaNacimiento = fechaNacimiento;
        this.categoria = categoria;
        this.genero = genero;
        this.incumplePago = incumplePago;
        this.estaBaneado = estaBaneado;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int isIncumplePago() {
        return incumplePago;
    }

    public void setIncumplePago(int incumplePago) {
        this.incumplePago = incumplePago;
    }

    public boolean isEstaBaneado() {
        return estaBaneado;
    }

    public void setEstaBaneado(boolean estaBaneado) {
        this.estaBaneado = estaBaneado;
    }
}