package ch.supsi.dti.isin.jwt.service;
import java.io.Serializable;

public class JwtAddLocationResponse implements Serializable{
    private static final long serialVersionUID = 1250166598152483598L;

    private final String username;
    private final String location;

    public JwtAddLocationResponse(String username, String location) {
        this.username = username;
        this.location = "Location added: "+ location;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLocation(){
        return this.location;
    }
}
