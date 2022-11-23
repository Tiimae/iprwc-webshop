package tiimae.webshop.iprwc.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tiimae.webshop.iprwc.models.User;

import java.util.Collection;

@Getter
@Setter
public class JwtUser implements UserDetails {
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;


    private static final long serialVersionUID = 1L;
    private User user;
    public JwtUser(String username, String password, Boolean enabled,  Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
