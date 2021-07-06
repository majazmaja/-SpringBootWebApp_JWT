package maja.zmaja.assetsservice.dao;

import maja.zmaja.assetsservice.entity.Role;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    
    public List<UserRoles> findByUser(User user);
    public UserRoles findUserRolesByUserAndAndRole(User user, Role role);
   
}
