package maja.zmaja.assetsservice.entity.response;

import maja.zmaja.assetsservice.entity.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private String username;
    private List<String> roles;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public JwtResponse(String jwttoken, String username, List<String> roles) {
        this.jwttoken = jwttoken;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getJwttoken() {
        return jwttoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
