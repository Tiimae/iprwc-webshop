package tiimae.webshop.iprwc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DAO.repo.UserRepository;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
   
   @Autowired private UserDAO userDAO;
   @Autowired private UserRepository userRepository;

   public UserDetails loadUserById(UUID id) throws EntryNotFoundException {
      User user = this.userDAO.getUser(id);
      
      return loadUserByUsername(user.getEmail());
   }

   @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userResult = userRepository.findByEmail(email);

        if (userResult.isEmpty()) {
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        }

        User user = userResult.get();

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
