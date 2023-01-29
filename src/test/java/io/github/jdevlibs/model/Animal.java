package io.github.jdevlibs.model;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class Animal {
    private static final String ST_FIELD = "Animal";

    private Long id;
    private String name;
    private String spicy;
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpicy() {
        return spicy;
    }

    public void setSpicy(String spicy) {
        this.spicy = spicy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", spicy='" + spicy + '\'' +
                ", age=" + age +
                ", ST_FIELD=" + ST_FIELD +
                '}';
    }
}
