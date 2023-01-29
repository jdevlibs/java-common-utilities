package io.github.jdevlibs.model;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class AnimalHome {
    private String name;
    private String owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "AnimalHome{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
