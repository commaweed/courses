@Service
public class MyUserServiceImpl implements MyUserService {
    
    @Override
    public MyUser login(String authenticationToken) {
        MyUser user = new MyUser();
        user.setUuid(authenticationToken);
        return user;
    }
    
}
