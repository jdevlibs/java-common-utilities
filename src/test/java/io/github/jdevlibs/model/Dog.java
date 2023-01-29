package io.github.jdevlibs.model;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class Dog extends Animal{
    private String walkStep;
    private AnimalHome home;

    public String getWalkStep() {
        return walkStep;
    }

    public void setWalkStep(String walkStep) {
        this.walkStep = walkStep;
    }

    public AnimalHome getHome() {
        return home;
    }

    public void setHome(AnimalHome home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "walkStep='" + walkStep + '\'' +
                ", home=" + home +
                "} " + super.toString();
    }
}
