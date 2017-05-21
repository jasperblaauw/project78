package nl.hr.server;

import org.junit.Before;
import org.junit.Test;

import static nl.hr.server.Person.Type.*;
import static org.junit.Assert.fail;

public class PersonMoverTest {

    private PersonMover personMover;

    @Before
    public void setup() {
        Person person = new Person(1, ELDERLY);

        Room room1 = new Room(111, "room1");
        Room room2 = new Room(222, "room2");

        room1.getAdjacentRooms().put("room2", room2);
        room2.getAdjacentRooms().put("room1", room1);

        room1.addPerson(person);

        personMover = new PersonMover();
    }

    @Test
    public void movePerson() {
        personMover.processChokepointData(1000, 1);
        fail("I need to move a person to a new room now");
    }

}
