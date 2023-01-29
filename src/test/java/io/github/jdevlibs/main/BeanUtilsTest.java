package io.github.jdevlibs.main;

import io.github.jdevlibs.utils.BeanUtils;
import io.github.jdevlibs.model.AnimalHome;
import io.github.jdevlibs.model.Dog;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class BeanUtilsTest {
    public static void main(String[] args) {
        testGetValue();
        System.out.println("++++++++++++++++++++++++++++++++");
        testSetValue();
    }

    private static void testGetValue() {
        Dog dog = new Dog();
        dog.setAge(10);
        dog.setHome(new AnimalHome());
        dog.getHome().setOwner("Supot");
        dog.setName("Lockok");
        dog.setSpicy("Animal");

        Object value = BeanUtils.getValue(dog, "age");
        System.out.println("Age : " + value);

        value = BeanUtils.getValue(dog, "name");
        System.out.println("Name : " + value);

        value = BeanUtils.getValue(dog, "Spicy");
        System.out.println("Spicy : " + value);

        value = BeanUtils.getValue(dog, "home");
        System.out.println("home : " + value);

        value = BeanUtils.getValue(dog, "home.name");
        System.out.println("home.name : " + value);

        value = BeanUtils.getValue(dog, "home.owner");
        System.out.println("home.owner : " + value);
    }

    private static void testSetValue() {
        Dog dog = new Dog();
        BeanUtils.setValue(dog, "age", 50);
        BeanUtils.setValue(dog, "name", "Lockok");
        BeanUtils.setValue(dog, "walkStep", "4 leg");
        BeanUtils.setValue(dog, "spicy", "Animal");
        BeanUtils.setValue(dog, "home.owner", "Supot");
        BeanUtils.setValue(dog, "home.name", "my home");

        System.out.println(dog);
    }
}
