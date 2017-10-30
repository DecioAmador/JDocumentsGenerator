package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Crocodile extends Animal {

    public Crocodile(final double weight, final Calendar birthdate) {
        super("Animalia", "Crocodylus niloticus", weight, EnumSet.of(Transport.TERRESTRIAL, Transport.AQUATIC),
                birthdate);
    }

}
