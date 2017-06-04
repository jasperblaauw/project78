package nl.hr.server;

import org.junit.Before;
import org.junit.Test;

import static nl.hr.server.Direction.*;
import static nl.hr.server.Person.Type.NURSE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class RoomTest {

    private Room room1;
    private Room room2;
    Person nurse;


    @Before
    public void setup() {
        room1 = new Room(1, "room1");
        room2 = new Room(2, "room2");

        room1.getAdjacentRooms().put("room2", room2);
        room2.getAdjacentRooms().put("room1", room1);

        room1.getAdjacentRoomDirection().put(room2, NORTH);
        room2.getAdjacentRoomDirection().put(room1, SOUTH);

        nurse = new Person(1, NURSE);
    }

    @Test
    public void addPerson_PersonShouldKnowRoom() {
        room1.addPerson(nurse);

        assertThat(nurse.getCurrentRoom(), is(room1));
    }

    @Test
    public void addPersonToDifferentRooms() {
        room1.addPerson(nurse);
        room2.addPerson(nurse);

        assertThat(room2.getPeople(), hasItem(nurse));
        assertThat(room1.getPeople(), not(hasItem(nurse)));
        assertThat(nurse.getCurrentRoom(), is(room2));
    }

    @Test
    public void getAdjacentRoomDirection() {
        Direction directionRoom1ToRoom2 = room1.getAdjacentRoomDirection().get(room2);

        assertThat(directionRoom1ToRoom2, is(NORTH));
    }

}
