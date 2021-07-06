package maja.zmaja.assetsservice.dao;

import maja.zmaja.assetsservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
    public Boolean existsByUsername(String username);
    public List<User> findAll();
}
