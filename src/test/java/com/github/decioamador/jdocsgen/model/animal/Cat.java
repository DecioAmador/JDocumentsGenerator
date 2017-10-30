package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Cat extends Animal implements Pet {

    private final String name;

    public Cat(final double weight, final String name, final Calendar birthdate) {
        super("Animalia", "F. silvestris", weight, EnumSet.of(Transport.TERRESTRIAL), birthdate);
        this.name = name;
    }

    @Override
    public void petting() {
        System.out.println("Petting a cat...");
    }

    @Override
    public String getName() {
        return name;
    }

}
