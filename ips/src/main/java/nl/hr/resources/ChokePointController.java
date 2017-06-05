package nl.hr.resources;

import nl.hr.service.PersonMoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class ChokePointController {

    @Autowired
    private PersonMoverService personMoverService;

    // to be used for testing
    @RequestMapping()
    public String test(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    // Example: http://localhost:8080/chokePoint?chokePointId=1&personId=2
    @GetMapping("/chokePoint")
    public void getChokePointData(
            @RequestParam(value = "chokePointId") int chokePointId,
            @RequestParam(value = "personId") int personId) {

        // with the chokePoint id, and the room the person was previously in
        // I can determine which room the person is going to.
        personMoverService.movePerson(chokePointId, personId);
    }

}
