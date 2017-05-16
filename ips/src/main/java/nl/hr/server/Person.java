package nl.hr.server;

public class Person {

    private Integer id;
    private Type type;
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

    public enum Type {
        NURSE, ELDERLY
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }

}
