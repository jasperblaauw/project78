package nl.hr.server;

public class Person {

    private Integer id;
    private Type type;
    private Room currentRoom; // Room object sets this variable (when a person enters a room)
    // otherwise you need to set this variable manually.

    // Later, maybe add status (for when a nurse is busy or available)

    public Person(Integer id, Type type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }

    public enum Type {
        NURSE, ELDERLY
    }

}
