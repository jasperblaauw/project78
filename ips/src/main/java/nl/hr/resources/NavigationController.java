package nl.hr.resources;

import java.util.List;
import java.util.Map;
import nl.hr.core.Building;
import nl.hr.core.Direction;
import nl.hr.core.Person;
import nl.hr.core.Room;
import nl.hr.service.PersonMoverService;
import nl.hr.service.RouteCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class NavigationController {

  private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);

  private final Map<Integer, Person> people = Building.getPeople();
  private final Map<String, Room> rooms = Building.getRooms();
  // I wont use DI, because I don't think I will test this class. We will just manual test it.
  private PersonMoverService personMoverService = new PersonMoverService();
  private RouteCalculatorService routeCalculatorService = new RouteCalculatorService();

  // for testing
  @GetMapping("/rooms")
  public Map<String, Room> getPeopleInEachRoom() {
    return rooms;
  }

  // for testing
  @GetMapping("/rooms/{roomName}/people")
  public List<Person> getPeopleInRoom(@PathVariable(value = "roomName") String roomName) {
    return rooms.get(roomName).getPeople();
  }

  // Example http://localhost:8080/people/1/passedThrough/chokePoints/1000
  @GetMapping("/people/{personId}/passedThrough/chokePoints/{chokePointId}")
  public void receiveChokePointData(@PathVariable(value = "chokePointId") int chokePointId,
      @PathVariable(value = "personId") int personId) {
    if (people.containsKey(personId)) { // just a check otherwise nullPointer
      // with the chokePoint id, and the room the person was previously in
      // I can determine which room the person is going to.

      Person person = people.get(personId);

      personMoverService.movePerson(chokePointId, person);

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

  // Example: http://localhost:8080/people/1/navigateTo/room3
  @GetMapping("/people/{personId}/navigateTo/{roomName}")
  public Direction getDirection(@PathVariable(value = "personId") int personId,
      @PathVariable(value = "roomName") String roomName) {
    if (personIdAndRoomNameExist(personId, roomName)) {
      Person person = people.get(personId);
      person.setNavigating(true); // person only connects to this url to get directions
      // so navigation is always true
      person.setCreateNewDirection(true);

      logger.info("roomName and personId good");

      person.setRoomToNavigateTo(rooms.get(roomName));

      if (person.isCreateNewDirection()) {
        Direction direction = getDirectionForPerson(person);
        person.setDirection(direction);

        person.setCreateNewDirection(false);
      }

      return person.getDirection();
    }

    logger.info("sending nothing, because illegal input");

    return null; // if you pass illegal input
  }

  private Direction getDirectionForPerson(Person person) {
    Room currentRoomPerson = person.getCurrentRoom();
    Room roomToNavigateTo = person.getRoomToNavigateTo();

    logger.info(currentRoomPerson.toString());
    logger.info(roomToNavigateTo.toString());

    // I return .get(0), because I want to return just 1 direction
    return routeCalculatorService.roomToRoomDirection(currentRoomPerson, roomToNavigateTo).get(0);
  }

  private boolean personIdAndRoomNameExist(int personId, String roomName) {
    return people.containsKey(personId) && rooms.containsKey(roomName);  // just a check
    // otherwise nullPointer
  }

}
