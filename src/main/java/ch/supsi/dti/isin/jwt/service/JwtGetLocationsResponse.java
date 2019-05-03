package ch.supsi.dti.isin.jwt.service;
import java.io.Serializable;

public class JwtGetLocationsResponse implements Serializable {
    private static final long serialVersionUID = 1218166598152483598L;

    private final String username;
    private final String locations;

    public JwtGetLocationsResponse(String username, String locations) {
        this.username = username;
        this.locations = locations;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLocations() {
        return locations;
    }
}
