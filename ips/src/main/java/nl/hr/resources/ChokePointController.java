package nl.hr.resources;

import nl.hr.server.PersonMover;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class ChokePointController {

    @RequestMapping()
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    // Example: http://localhost:8080/chokepoint?chokepointId=1&personId=2
    @GetMapping("/chokepoint")
    public void getChokepointData(@RequestParam(value = "chokepointId") int chokepointId,
                      @RequestParam(value = "personId") int personId) {
        // with the chokepoint id, and the room the person was previously in
        // I can determine which room the person is going to.

        new PersonMover().processChokepointData(chokepointId, personId);
    }

}
