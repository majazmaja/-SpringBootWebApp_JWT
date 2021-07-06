package maja.zmaja.assetsservice.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import maja.zmaja.assetsservice.dao.RoleRepository;
import maja.zmaja.assetsservice.dao.UserRolesRepository;
import maja.zmaja.assetsservice.entity.Role;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserRoles;
import maja.zmaja.assetsservice.enums.UserRole;
import maja.zmaja.assetsservice.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class UserDetailsSecurity implements UserDetails {
    
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsSecurity.class);
    
    @Autowired
    private  UserRolesRepository userRolesRepository;
    
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
   

    public UserDetailsSecurity(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDetailsSecurity() {
    }

    public  UserDetails build(User user) {
      
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());

        return new UserDetailsSecurity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
