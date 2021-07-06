package maja.zmaja.assetsservice.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="user_roles")
@Entity
public class UserRoles extends BaseEntity{
    
    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public UserRoles(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public UserRoles() {
        
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
