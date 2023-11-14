package md.akdev.loyality_cms.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String uuid;
    private String phoneNumber;
    //private String firstName;

    /*Оставим на будущее*/
    //private Set<Role> roles;
    // @Override
    //public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return uuid; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return phoneNumber; }

}