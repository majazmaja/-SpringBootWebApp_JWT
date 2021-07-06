package maja.zmaja.assetsservice.entity;

import maja.zmaja.assetsservice.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Table(name="role")
@Entity
public class Role extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserRole role;

    public Role(UserRole role) {
        this.role = role;
    }

    public Role() {
        
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
  
}
