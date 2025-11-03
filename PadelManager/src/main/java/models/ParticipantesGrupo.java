package models;

public class ParticipantesGrupo {
    private int idGrupo;
    private String idJugador;
    private boolean esCreador;

    public ParticipantesGrupo() {}

    public ParticipantesGrupo(int id_grupo, String id_jugador, boolean esCreador) {
        this.idGrupo = id_grupo;
        this.idJugador = id_jugador;
        this.esCreador = esCreador;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    public boolean isEsCreador() {
        return esCreador;
    }

    public void setEsCreador(boolean esCreador) {
        this.esCreador = esCreador;
    }
}