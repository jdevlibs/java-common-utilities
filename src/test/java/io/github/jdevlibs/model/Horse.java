package io.github.jdevlibs.model;

import java.math.BigDecimal;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class Horse extends Animal {
    private int speed;
    private AnimalHome home;
    private BigDecimal price;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public AnimalHome getHome() {
        return home;
    }

    public void setHome(AnimalHome home) {
        this.home = home;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Horse{" +
                "speed=" + speed +
                ", home=" + home +
                ", price=" + price +
                "} " + super.toString();
    }
}
