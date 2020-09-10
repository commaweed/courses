@RestController
@RequestMapping(value="/test")
public class MainController {
    
    @RequestMapping(value="/table", method=RequestMethod.GET, produces="text/html")
    public String testRenderHtml() {
        return "test";
    }
    
    @RequestMapping(value="xslt", method=RequestMethod.GET, produces="text/html")
    public ModelAndView renderXslt(HttpServletRequest request) {
        String xmlFile = "resources/blah.xml";
        String contextPath = request.getServletContext().getRealPath("");
        String xmlFilePath = contextPath + File.separator + xmlFile;
        
        Source source = new StreamSource(new File(xmlFilePath));
        
        ModelAndView model = new ModelAndView("xxEntry");
        model.addObject("xmlSource", source);
        return model;
    }
    
}
