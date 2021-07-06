package maja.zmaja.assetsservice.dao;

import maja.zmaja.assetsservice.entity.Role;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserRoles;
import maja.zmaja.assetsservice.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    public  Role findByRole(UserRole role);
    
}
