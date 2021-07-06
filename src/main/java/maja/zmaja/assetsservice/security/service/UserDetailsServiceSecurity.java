package maja.zmaja.assetsservice.security.service;

import maja.zmaja.assetsservice.dao.UserRepository;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceSecurity implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceSecurity.class);
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        
            User user = userRepository.findByUsername(s);
            if (user == null)
                throw  new UsernameNotFoundException("User " + s + " don't exist" );
            UserDetailsSecurity userDetailsSecurity = new UserDetailsSecurity();
            
            return userDetailsSecurity.build(user);
        
    }
}
