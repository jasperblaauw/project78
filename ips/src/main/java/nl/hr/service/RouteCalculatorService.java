package nl.hr.service;

import nl.hr.core.Direction;
import nl.hr.core.Person;
import nl.hr.core.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.hr.core.Person.Type.NURSE;

public class RouteCalculatorService {

    public List<Person> findAllNurses(Map<String, Room> rooms) {
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
        Person closestNurse = findClosestNurseFor(room);
        Room roomNurse = closestNurse.getCurrentRoom();

        return roomToRoomDirection(room, roomNurse);
    }

    public List<Direction> roomToRoomDirection(Room room1, Room room2) {
        List<Direction> directions = new ArrayList<>();

        room1.getAdjacentRooms().get(room2);

        Direction direction = room1.getAdjacentRoomDirection().get(room2);
        if (direction != null) {
            System.out.println("inside if for loop");
            directions.add(direction);
        } else {
            System.out.println("inside else");
            for (Room adjacentRoom : room1.getAdjacentRooms().values()) {
                direction = room1.getAdjacentRoomDirection().get(adjacentRoom);
                directions.add(direction);

                System.out.println("inside else for loop");
                directions.addAll(roomToRoomDirection(adjacentRoom, room2));
            }
        }

        System.out.println("end of line");
        return directions;
    }

}
