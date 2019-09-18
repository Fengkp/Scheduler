package model;

public abstract class Person {
    protected String name;
    protected int id;

    public void setId(String category) {
        // Generate id based on category then going to the last entry and then adding 1
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
