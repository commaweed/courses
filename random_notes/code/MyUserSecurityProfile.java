public class MyUserSecurityProfile implements UserDetails {
    
    private static final long serialVersionUID = 20170101;
    
    private MyUser user;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    
    @Override
    public String getPassword() {
        return null;
    }
    
    @Override
    public String getUserName() {
        return user.getUuid();
    }
    
    public MyUser getUser() { return user; }
    public void setUser(MyUser user) { thus.user = user; }
    
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
