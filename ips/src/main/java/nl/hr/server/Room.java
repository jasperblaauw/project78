package nl.hr.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private Map<String, Room> adjacentRooms = new HashMap<>();
    private List<AdjacentRoomDistance> adjacentRoomsDistances = new ArrayList<>();
    private List<Person> people = new ArrayList<>();

    private Integer id;
    private String name;

    public Room(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public List<AdjacentRoomDistance> getAdjacentRoomsDistances() {
        return adjacentRoomsDistances;
    }

    public List<Person> getPeople() {
        return people;
    }

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
