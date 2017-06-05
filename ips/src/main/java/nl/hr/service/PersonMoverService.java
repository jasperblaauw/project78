package nl.hr.service;

import nl.hr.core.Person;
import nl.hr.core.Room;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PersonMoverService {

    private Map<Integer, Person> people; // Here you should set the room and people like in the test

    PersonMoverService(Map<Integer, Person> people) {
        this.people = people;
    }

    public void movePerson(int chokePointId, int personId) {
        Person person = people.get(personId);
        Room previousRoom = person.getCurrentRoom();

        Map<String, Room> adjacentRooms = previousRoom.getAdjacentRooms();
        for (Room room : adjacentRooms.values()) {
            for (int chokePointIdsInRoom : room.getChokePointIds()) {
                if (chokePointIdsInRoom == chokePointId) {
                    room.addPerson(person);
                }
            }
        }
    }

}
