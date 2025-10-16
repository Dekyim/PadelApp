package models;

import java.sql.Time;
import java.util.Vector;

public class CanchaHorario {
    private Vector<Time> horarios;
    private int idCancha;  // referencia al id autoincremental de Cancha

    public CanchaHorario(int idCancha, Vector<Time> horarios){
        this.idCancha = idCancha;
        this.horarios = horarios;
    }

    public Vector<Time> getHorarios() {
        return horarios;
    }

    public void setHorarios(Vector<Time> horarios) {
        this.horarios = horarios;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }
}