package nl.hr.core;

import nl.hr.service.RouteCalculatorService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.hr.core.Direction.*;
import static nl.hr.core.Person.Type.ELDERLY;
import static nl.hr.core.Person.Type.NURSE;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RouteCalculatorTest {

    private Map<String, Room> rooms;
    private Room room1;
    private Room room2;
    private Room room3;

    private RouteCalculatorService routeCalculatorService;

    @Before
    public void setup() {
        room1 = new Room(1);
        room2 = new Room(2);
        room3 = new Room(3);

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

        rooms = new HashMap<>();
        rooms.put("room1", room1);
        rooms.put("room2", room2);
        rooms.put("room3", room3);

        routeCalculatorService = new RouteCalculatorService(rooms);
    }

    @Test
    public void roomHasAdjacentRoom() {
        assertThat(room1.getAdjacentRooms().size(), is(1));
        assertThat(room2.getAdjacentRooms().size(), is(2));
    }

    @Test
    public void roomHasAdjacentRoomByName() {
        assertThat(room1.getAdjacentRooms().get("room2"), is(room2));
    }

    @Test
    public void findAllNursesRoom1() {
        Person nurse = new Person(1, NURSE);
        room1.addPerson(nurse);

        assertThat(routeCalculatorService.findAllNurses(), hasItem(nurse));
    }

    @Test
    public void findAllNurses() {
        Person nurse1 = new Person(1, NURSE);
        Person nurse2 = new Person(2, NURSE);
        room1.addPerson(nurse1);
        room2.addPerson(nurse2);

        assertThat(routeCalculatorService.findAllNurses(), hasItems(nurse1, nurse2));
    }

    @Test
    public void findClosestNurse_ElderlyRoom1() {
        Person elderly = new Person(1, ELDERLY);
        Person nurse1 = new Person(2, NURSE);
        Person nurse2 = new Person(3, NURSE);

        room1.addPerson(elderly);
        room2.addPerson(nurse1);
        room3.addPerson(nurse2);

        assertThat(routeCalculatorService.findClosestNurseFor(elderly.getCurrentRoom()), is(nurse1));
        assertThat(nurse1.getCurrentRoom(), is(room2));
    }

    @Test
    public void findClosestNurse_ElderlyRoom1AndNurseInAdjacentAdjacentRoom() {
        Person elderly = new Person(1, ELDERLY);
        Person nurse1 = new Person(2, NURSE);
        Person nurse2 = new Person(3, NURSE);

        Room room4 = new Room(4);
        room4.getAdjacentRooms().put("room3", room3);
        room3.getAdjacentRooms().put("room4", room4);

        room3.getAdjacentRoomsDistances().add(
                new Room.AdjacentRoomDistance(room2, room4, 40));

        rooms.put("room4", room4);

        room1.addPerson(elderly);
        room3.addPerson(nurse1);
        room4.addPerson(nurse2);

        assertThat(routeCalculatorService.findClosestNurseFor(elderly.getCurrentRoom()), is(nurse1));
        assertThat(nurse1.getCurrentRoom(), is(room3));
    }

    @Test
    public void calculateRouteClosestNurseElderlyRoom1NurseRoom3() {
        Person elderly = new Person(1, ELDERLY);
        Person nurse = new Person(2, NURSE);

        room1.addPerson(elderly);
        room2.addPerson(nurse);

        assertThat(routeCalculatorService.calculateRouteClosestNurse(elderly.getCurrentRoom()), hasItem(NORTH));

        room3.addPerson(nurse); // removes nurse from old rome

        assertThat(routeCalculatorService.calculateRouteClosestNurse(elderly.getCurrentRoom()), hasItems(NORTH, WEST));
    }

    @Test
    public void calculateDirectionRoomToAdjacentRoom() {
        List<Direction> directions = routeCalculatorService.roomToRoomDirection(room1, room2);

        assertThat(directions.get(0), is(NORTH));
    }

    @Test
    public void calculateDirectionRoomToAdjacentOfAdjacentRoom() {
        List<Direction> directions = routeCalculatorService.roomToRoomDirection(room1, room3);

        assertThat(directions.get(0), is(NORTH)); // Hij gaat eerst naar room2 via NORTH
        assertThat(directions.get(1), is(WEST)); // Hij gaat van room2 naar room 3 via WEST
    }

}
