package nl.hr.resources;

import nl.hr.core.Building;
import nl.hr.core.Direction;
import nl.hr.core.Person;
import nl.hr.core.Room;
import nl.hr.service.PersonMoverService;
import nl.hr.service.RouteCalculatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class NavigationController {

    private final Map<Integer, Person> people = Building.getPeople();
    private final Map<String, Room> rooms = Building.getRooms();
    // I wont use DI, because I dont think I will test this class. We will just manual test it.
    private PersonMoverService personMoverService = new PersonMoverService(people);
    private RouteCalculatorService routeCalculatorService = new RouteCalculatorService(rooms);

    // for testing
    @RequestMapping()
    public String test(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    // Example http://localhost:8080/people/1/passedThrough/chokePoints/1?status=finished
    @GetMapping("/people/{personId}/passedThrough/chokePoints/{chokePointId}?")
    public void receiveChokePointData(@PathVariable(value = "chokePointId") int chokePointId,
                                      @PathVariable(value = "personId") int personId) {
        if (people.containsKey(personId)) { // just a check otherwise nullPointer
            // with the chokePoint id, and the room the person was previously in
            // I can determine which room the person is going to.
            personMoverService.movePerson(chokePointId, personId);

            Person person = people.get(personId);
            if (person.isNavigating()) {
                if (person.getRoomToNavigateTo() == person.getCurrentRoom()) {
                    person.setNavigating(false);
                    person.setRoomToNavigateTo(null);
                    person.setDirection(null);
                } else {
                    person.setCreateNewDirection(true); // pass chokePoint so create new
                    // direction to next door
                }
            }
        }
    }

    // Example: http://localhost:8080/people/1/navigateTo/room1?personId=1
    @GetMapping("/people/{personId}/navigateTo/{roomName}")
    public Direction getDirection(@PathVariable(value = "personId") int personId,
                                  @PathVariable(value = "roomName") String roomName) {
        if (personIdAndRoomNameExist(personId, roomName)) {
            Person person = people.get(personId);
            person.setNavigating(true); // person only connects to this url to get directions
            // so navigation is always true

            if (person.isCreateNewDirection()) {
                Direction direction = getDirectionForPerson(person);
                person.setDirection(direction);

                person.setCreateNewDirection(false);
            }

            person.setRoomToNavigateTo(rooms.get(roomName));

            return person.getDirection();
        }

        return null; // if you pass illegal input
    }

    private Direction getDirectionForPerson(Person person) {
        Room currentRoomPerson = person.getCurrentRoom();
        Room roomToNavigateTo = person.getRoomToNavigateTo();

        // I return .get(0), because I want to return just 1 direction
        return routeCalculatorService.roomToRoomDirection(currentRoomPerson, roomToNavigateTo).get(0);
    }

    private boolean personIdAndRoomNameExist(int personId, String roomName) {
        return people.containsKey(personId) && rooms.containsKey(roomName);  // just a check
        // otherwise nullPointer
    }

}
