package ch.supsi.dti.isin.jwt.security;

import java.io.Serializable;

public class  JwtRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8445943548965154779L;

    private String username;
    private String password;

    public JwtRegisterRequest() {
        super();
    }

    public JwtRegisterRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}