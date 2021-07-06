package maja.zmaja.assetsservice.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name="user")
@Entity
public class User extends  BaseEntity{
    
    @Column(name = "username", nullable = false, length = 255)
    private String username;


    @Column(name = "password", nullable = false,  length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Set<Role> roles)  {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User() {
        
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
