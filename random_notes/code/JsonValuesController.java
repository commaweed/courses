@RestController
@RequestMapping(value="/myjson") 
public class MyJsonValuesController {

    @RequestMapping(value="/all", method=RequestMethod.GET, produces="application/json")
    public List<Map<String, Object>> getJsonValues() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            ClassPathResource jsonFile = new ClassPathResource("values.json");
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(jsonFile.getFile(), new TypeReference<List<Map>>(){});
        } catch (Exception e) {
            // log error
        }
        return result;
    }
    

}
