package eva.luca.soccorso_Web.models;

public class TokenData {

    private final String email;
    private final String ruolo;

    public TokenData(String email, String ruolo) {
        this.email = email;
        this.ruolo = ruolo;
    }


    public String getEmail() {
        return email;
    }

    public String getRuolo() {
        return ruolo;
    }
}
