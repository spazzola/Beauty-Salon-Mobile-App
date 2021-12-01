package pomalowane.user.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pomalowane.user.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private Long id;
    private String userName;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String role;
    private List<GrantedAuthority> autorities;


    public MyUserDetails() {

    }

    public MyUserDetails(User user) {
        this.id = user.getId();
        this.userName = user.getLogin();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.autorities = Arrays.stream(user.getRole().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return autorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Long getId() {
        return id;
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

}