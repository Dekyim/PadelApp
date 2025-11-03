package models;

import java.sql.Time;
import java.sql.Timestamp;

public class Grupo {
    private int idGrupo;
    private String idCreador;
    private Time horaDesde;
    private Time horaHasta;
    private String categoria;
    private int cupos;
    private String descripcion;
    private String estado;
    private Timestamp fechaCreacion;

    public Grupo() {}

    public Grupo(int id_grupo, String id_creador, Time hora_desde, Time hora_hasta, String categoria, int cupos, String descripcion, String estado, Timestamp fecha_creacion) {
        this.idGrupo = id_grupo;
        this.idCreador = id_creador;
        this.horaDesde = hora_desde;
        this.horaHasta = hora_hasta;
        this.categoria = categoria;
        this.cupos = cupos;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fecha_creacion;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(String idCreador) {
        this.idCreador = idCreador;
    }

    public Time getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(Time horaDesde) {
        this.horaDesde = horaDesde;
    }

    public Time getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(Time horaHasta) {
        this.horaHasta = horaHasta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}