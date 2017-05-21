package nl.hr.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.hr.server.Person.Type.NURSE;

public class RouteCalculator {

    private Map<String, Room> rooms;

    public RouteCalculator(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public List<Person> findAllNurses() {
        List<Person> nurses = new ArrayList<>();

        rooms.forEach((name, room) -> room.getPeople().stream()
                .filter(person -> person.getType() == NURSE)
                .forEach(nurses::add));

        return nurses;
    }

    // Should rooms have people, and work via that, or
    // bi directional, that a person also has a currentRoom?
    // Answer: I made it bidirectional. The room will automatically set the current
    // room the person is in.

    // Bidirectional is necessary because if you get a person, you want to know immediately where
    // he or she is.

    public Person findClosestNurseFor(Room room) {
        List<Person> nursesInAdjacentRooms = new ArrayList<>();

        room.getAdjacentRooms()
                .forEach((name, adjacentRoom) -> adjacentRoom.getPeople().stream()
                        .filter(person -> person.getType() == NURSE)
                        .forEach(nursesInAdjacentRooms::add));

        if (nursesInAdjacentRooms.size() > 0) {
            return nursesInAdjacentRooms.get(0);
        } else {
            Map<String, Room> rooms = room.getAdjacentRooms();
            for (Room adjacentRoom : rooms.values()) {
                Person nurseInAdjacentAdjacentRoom = findClosestNurseFor(adjacentRoom);
                if (nurseInAdjacentAdjacentRoom != null) {
                    return nurseInAdjacentAdjacentRoom;
                }
            }
            return null;
        }
    }

    public List<Direction> calculateRouteClosestNurse(Room room) {
        List<Direction> directions = new ArrayList<>();

        Person closestNurse = findClosestNurseFor(room);
//        closestNurse.get

        return directions;
    }

}
