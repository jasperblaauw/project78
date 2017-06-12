package nl.hr.service;

import nl.hr.core.Person;
import nl.hr.core.Room;

import java.util.Map;

//@Service
public class PersonMoverService {

    public void movePerson(int chokePointId, Person person) {
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
