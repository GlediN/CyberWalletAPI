package com.example.cyberwalletapi.jwt;
import com.example.cyberwalletapi.repositories.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    final UserDAO userDao;
    private com.example.cyberwalletapi.entities.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userDetail = userDao.findByEmailId(username);
        if (!Objects.isNull(userDetail))
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found");
    }

    public com.example.cyberwalletapi.entities.User getUserDetail() {
        return userDetail;
    }

}
