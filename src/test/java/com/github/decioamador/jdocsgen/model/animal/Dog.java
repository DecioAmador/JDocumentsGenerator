package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Dog extends Animal implements Pet {

    public Dog(final double weight, final String name, final Calendar birthdate) {
        super("Animalia", "C. lupus", weight, EnumSet.of(Transport.TERRESTRIAL), birthdate);
        this.name = name;
    }

    private final String name;

    @Override
    public void petting() {
        System.out.println("Petting a dog...");
    }

    @Override
    public String getName() {
        return name;
    }

}
