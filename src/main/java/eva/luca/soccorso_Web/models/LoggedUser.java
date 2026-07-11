package eva.luca.soccorso_Web.models;

public class LoggedUser {

    private int id;
    private String nome;
    private String email;
    private String ruolo;

    public LoggedUser() {
    	
    }

    public LoggedUser(int id, String nome, String email, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ruolo = ruolo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
