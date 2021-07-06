package maja.zmaja.assetsservice.entity.request;

import java.io.Serializable;
import java.util.*;
public class SignupRequest implements Serializable {
    
    private String username;
    private String password;
    private Set<String> roles;

    public SignupRequest(String username, String password, Set roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
