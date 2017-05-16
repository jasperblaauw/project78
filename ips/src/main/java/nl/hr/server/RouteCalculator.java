package nl.hr.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.hr.server.Person.Type.*;

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

    // Should I search real oop, like a room has a person, and work via that
    // , or bi directional, that a person also has a room?

    public List<Person> findClosestNurseFor(Person elderly, Room room) {
        List<Person> nursesInAdjacentRooms = new ArrayList<>();

        room.getAdjacentRooms()
                .forEach((name, adjacentRoom) -> adjacentRoom.getPeople().stream()
                .filter(person -> person.getType() == NURSE)
                .forEach(nursesInAdjacentRooms::add));

        if (nursesInAdjacentRooms.size() > 0) {
            return nursesInAdjacentRooms;
        } else {
            Map<String, Room> rooms = room.getAdjacentRooms();
            for (Room adjacentRoom : rooms.values()) {
                List<Person> nursesInAdjacentAdjacentRoom =  findClosestNurseFor(elderly, adjacentRoom);
                if (nursesInAdjacentAdjacentRoom.size() > 0) {
                    return nursesInAdjacentAdjacentRoom;
                }
            }
            return null;
        }
    }

}
