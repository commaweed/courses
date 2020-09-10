@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserService userService;
    
    @Override
    public UserDetails loadUserByUserName(String authenticationToken) throws UsernameNotFoundException {
        MyUser user = userService.login(authenticationToken);
        MyUserSecuirtyProfile userProfile = new MyUserSecuirtyProfile();
        userProfile.setUser(user);
        return userProfile;
    }
}
