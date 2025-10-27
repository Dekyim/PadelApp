package models;

public class FotoPerfilUsuario {
    private int id;
    private String cedulaUsuario;
    private String urlFoto;

    public FotoPerfilUsuario() {
    }

    public FotoPerfilUsuario(String cedulaUsuario, String urlFoto) {
        this.cedulaUsuario = cedulaUsuario;
        this.urlFoto = urlFoto;
    }

    public FotoPerfilUsuario(int id, String cedulaUsuario, String urlFoto) {
        this.id = id;
        this.cedulaUsuario = cedulaUsuario;
        this.urlFoto = urlFoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "FotoPerfilUsuario{" +
                "id=" + id +
                ", cedulaUsuario='" + cedulaUsuario + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }
}
