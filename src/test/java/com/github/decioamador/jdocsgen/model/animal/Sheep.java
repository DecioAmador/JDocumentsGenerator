package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Sheep extends Animal {

    public Sheep(final double weight, final Calendar birthdate) {
        super("Animalia", "Ovis aries", weight, EnumSet.of(Transport.TERRESTRIAL), birthdate);
    }

}
