package maja.zmaja.assetsservice.dao;

import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserFilesRepository extends JpaRepository<UserFiles, Long> {
    
    public List<UserFiles> findUserFilesByUser(User user);
}
