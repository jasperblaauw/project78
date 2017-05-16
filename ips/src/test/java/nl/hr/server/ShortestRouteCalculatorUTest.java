package nl.hr.server;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static nl.hr.server.Person.Type.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ShortestRouteCalculatorUTest {

    private Map<String, Room> rooms;
    private Room room1;
    private Room room2;
    private Room room3;

    private RouteCalculator routeCalculator;

    @Before
    public void init() {
        room1 = new Room(1, "room1");
        room2 = new Room(2, "room2");
        room3 = new Room(3, "room3");

        room1.getAdjacentRooms().put("room2", room2);
        room2.getAdjacentRooms().put("room1", room1);

        room2.getAdjacentRooms().put("room3", room3);
        room3.getAdjacentRooms().put("room2", room2);

        room2.getAdjacentRoomsDistances().add(
                new Room.AdjacentRoomDistance(room1, room3, 40));

        rooms = new HashMap<>();
        rooms.put("room1", room1);
        rooms.put("room2", room2);
        rooms.put("room3", room3);

        routeCalculator = new RouteCalculator(rooms);
    }

    // It has to be that the distance between rooms is measured and not the length of a
    // room, because for situations like with hallway, the length of the hallway is useless.
    // What I need is the distance between rooms in the hallway.

    @Test
    public void checkRoomHasAdjacentRoom() {
        assertThat(room1.getAdjacentRooms().size(), is(1));
        assertThat(room2.getAdjacentRooms().size(), is(2));
    }

    @Test
    public void checkRoomHasAdjacentRoomByName() {
        assertThat(room1.getAdjacentRooms().get("room2"), is(room2));
    }

    @Test
    public void checkFindAllNursesRoom1() {
        Person nurse = new Person(1, NURSE);
        room1.getPeople().add(nurse);

        assertThat(routeCalculator.findAllNurses(), hasItem(nurse));
    }

    @Test
    public void checkFindAllNursesRoom1AndRoom2() {
        Person nurse1 = new Person(1, NURSE);
        Person nurse2 = new Person(2, NURSE);
        room1.getPeople().add(nurse1);
        room2.getPeople().add(nurse2);

        assertThat(routeCalculator.findAllNurses(), hasItems(nurse1, nurse2));
    }

    @Test
    public void checkElderlyRoom1FindNurseRoom3() {
        Person elderly = new Person(1, ELDERLY);
        Person nurse1 = new Person(2, NURSE);
        Person nurse2 = new Person(3, NURSE);

        Room room4 = new Room(4, "room4");
        room4.getAdjacentRooms().put("room3", room3);
        room3.getAdjacentRooms().put("room4", room4);

        room3.getAdjacentRoomsDistances().add(
                new Room.AdjacentRoomDistance(room2, room4, 40));

        rooms.put("room4", room4);

        room1.getPeople().add(elderly);
        room3.getPeople().add(nurse1);
        room4.getPeople().add(nurse2);

        assertThat(routeCalculator.findClosestNurseFor(elderly, room1), hasItems(nurse1));
    }
//
//    @Test
//    public void checkConnection() {
//        Integer id = 99; // id for elderly.
//        Room roomId = 2;
//
//
//    }

}
