package nl.hr.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private Map<String, Room> adjacentRooms = new HashMap<>();
    private Map<Room, Direction> adjacentRoomDirection = new HashMap<>();
    private List<AdjacentRoomDistance> adjacentRoomsDistances = new ArrayList<>();
    private List<Person> people = new ArrayList<>();
    private List<Integer> chokePointIds = new ArrayList<>();

    private Integer id;

    public Room(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Map<String, Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public Map<Room, Direction> getAdjacentRoomDirection() {
        return adjacentRoomDirection;
    }

    public List<AdjacentRoomDistance> getAdjacentRoomsDistances() {
        return adjacentRoomsDistances;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void addPerson(Person person) {
        people.add(person);
        if (person.getCurrentRoom() != null) {
            person.getCurrentRoom().getPeople().remove(person); // when adding a person to a new room, remove person from old room
        }
        person.setCurrentRoom(this);
    }

    public List<Integer> getChokePointIds() {
        return chokePointIds;
    }

    // The distance between rooms needs to be measured and not the length of a
    // room, because for situations like with hallways, the length of the hallway is useless.
    // What we need is the distance between rooms in the hallway.
    public static class AdjacentRoomDistance {

        private Room room1;
        private Room room2;
        private int distance;

        public AdjacentRoomDistance(Room room1, Room room2, int distance) {
            this.room1 = room1;
            this.room2 = room2;
            this.distance = distance;
        }

        public Room getRoom1() {
            return room1;
        }

        public Room getRoom2() {
            return room2;
        }

        public int getDistance() {
            return distance;
        }

    }

}
