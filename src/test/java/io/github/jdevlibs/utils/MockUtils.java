package io.github.jdevlibs.utils;

import io.github.jdevlibs.model.AnimalHome;
import io.github.jdevlibs.model.Dog;
import io.github.jdevlibs.model.Horse;

import java.math.BigDecimal;

/**
 * @author supot.jdev
 * @version 1.0
 */
public final class MockUtils {
    private MockUtils() {

    }

    public static Horse createHorse() {
        Horse horse = new Horse();
        horse.setName("Horse");
        horse.setSpicy("Equus ferus caballus");
        horse.setAge(5);
        horse.setSpeed(120);
        horse.setPrice(BigDecimal.valueOf(15000D));
        horse.setHome(new AnimalHome());
        horse.getHome().setName("Chiangmai");
        horse.getHome().setOwner("Supot Saelao");

        return horse;
    }

    public static Dog createDog() {
        Dog dog = new Dog();
        dog.setName("Dog");
        dog.setSpicy("Canis familiaris");
        dog.setAge(2);
        dog.setWalkStep("Slow");
        dog.setHome(new AnimalHome());
        dog.getHome().setName("Chiangmai");
        dog.getHome().setOwner("Supot Saelao");

        return dog;
    }
}
