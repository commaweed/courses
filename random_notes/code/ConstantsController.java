@RestController
@RequestMapping(value="/constants")
public class ConstantsController {

    @Autowired
    private Environment env;
    
    @RequestMapping(value="/all", method=RequestMethod.GET, produces="application/json")
    public ClientSideConstants getAllClientSideConstants() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();
        
        ClientSideConstants constants = new ClientSideConstants();
        
        constants.setCurrentUser(user);
        constants.setVersion(env.getRequiredProperty("version"));
        
        return constants;
    }

}
