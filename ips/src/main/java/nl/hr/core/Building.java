package nl.hr.core;

import java.util.HashMap;
import java.util.Map;

import static nl.hr.core.Direction.*;
import static nl.hr.core.Person.Type.ELDERLY;
import static nl.hr.core.Person.Type.NURSE;

public class Building {

    private static final Map<Integer, Person> people = new HashMap<>();
    private static final Map<String, Room> rooms = new HashMap<>();

    static {
        people.put(1, new Person(1, ELDERLY));
        people.put(2, new Person(2, NURSE));

        // create rooms and add room configuration
        Room room1 = new Room(1);
        Room room2 = new Room(2);
        Room room3 = new Room(3);

        room1.getAdjacentRooms().put("room2", room2);
        room2.getAdjacentRooms().put("room1", room1);

        room1.getAdjacentRoomDirection().put(room2, NORTH);
        room2.getAdjacentRoomDirection().put(room1, SOUTH);

        room2.getAdjacentRoomDirection().put(room3, WEST);
        room3.getAdjacentRoomDirection().put(room2, EAST);

        room2.getAdjacentRooms().put("room3", room3);
        room3.getAdjacentRooms().put("room2", room2);

        room2.getAdjacentRoomsDistances().add(
                new Room.AdjacentRoomDistance(room1, room3, 40));

        rooms.put("room1", room1);
        rooms.put("room2", room2);
        rooms.put("room3", room3);
    }

    public static Map<Integer, Person> getPeople() {
        return people;
    }

    public static Map<String, Room> getRooms() {
        return rooms;
    }

}
