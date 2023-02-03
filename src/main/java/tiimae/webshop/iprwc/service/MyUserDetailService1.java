package tiimae.webshop.iprwc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;

@Component
public class MyUserDetailService1 implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepo.findByEmail(email);

        if (userRes.isEmpty()) {
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        }

        User user = userRes.get();

        List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();

        for (Role role : user.getRoles()) {
            listAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                listAuthorities
        );
    }
}
