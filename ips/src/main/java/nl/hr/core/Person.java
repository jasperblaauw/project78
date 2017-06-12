package nl.hr.core;

public class Person {

    private Integer id;
    private Type type;
    private Room currentRoom; // Room object sets this variable (when a person enters a room)
    // otherwise you need to set this variable manually.

    // Later, maybe add status (for when a nurse is busy or available)

    private Room roomToNavigateTo;
    private Direction direction;
    private boolean navigating;
    private boolean createNewDirection;

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

    public Room getRoomToNavigateTo() {
        return roomToNavigateTo;
    }

    public void setRoomToNavigateTo(Room roomToNavigateTo) {
        this.roomToNavigateTo = roomToNavigateTo;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isNavigating() {
        return navigating;
    }

    public void setNavigating(boolean navigating) {
        this.navigating = navigating;
    }

    public boolean isCreateNewDirection() {
        return createNewDirection;
    }

    public void setCreateNewDirection(boolean createNewDirection) {
        this.createNewDirection = createNewDirection;
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
