package nl.hr.service;

import static nl.hr.core.Person.Type.ELDERLY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import nl.hr.core.Person;
import nl.hr.core.Room;
import org.junit.Before;
import org.junit.Test;

public class PersonMoverServiceTest {

    private PersonMoverService personMoverService;

    private Room room1;
    private Room room2;
    private Person person;

    @Before
    public void setup() {
        person = new Person(1, ELDERLY);

        room1 = new Room(111);
        room2 = new Room(222);

        room1.getAdjacentRooms().put("room2", room2);
        room2.getAdjacentRooms().put("room1", room1);

        room1.getChokePointIds().add(1000);
        room2.getChokePointIds().add(1000);

        room1.addPerson(person);

        personMoverService = new PersonMoverService();
    }

    @Test
    public void movePerson() {
        assertThat(person.getCurrentRoom(), is(room1));

        personMoverService.movePerson(1000, person);

        assertThat(person.getCurrentRoom(), is(room2));
    }

}
