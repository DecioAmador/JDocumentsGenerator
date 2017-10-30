package com.github.decioamador.jdocsgen.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import com.github.decioamador.jdocsgen.model.animal.Animal;
import com.github.decioamador.jdocsgen.model.animal.Association;
import com.github.decioamador.jdocsgen.model.animal.Cat;
import com.github.decioamador.jdocsgen.model.animal.Cow;
import com.github.decioamador.jdocsgen.model.animal.Crocodile;
import com.github.decioamador.jdocsgen.model.animal.Dog;
import com.github.decioamador.jdocsgen.model.animal.Farm;
import com.github.decioamador.jdocsgen.model.animal.Pet;
import com.github.decioamador.jdocsgen.model.animal.Sheep;
import com.github.decioamador.jdocsgen.model.animal.Violetear;

public class DataAnimal {

    private static final Association<Farm>[] ASSOCIATIONS;

    private static final Association<Farm> ASSOCIATION1;
    private static final Association<Farm> ASSOCIATION2;
    private static final Association<Farm> ASSOCIATION3;

    private static final Farm[] FARMS1;
    private static final Farm[] FARMS2;
    private static final Farm[] FARMS3;

    private static final Animal[] ANIMALS1;
    private static final Animal[] ANIMALS2;
    private static final Animal[] ANIMALS3;

    private static final Pet[] PETS1;
    private static final Pet[] PETS2;
    private static final Pet[] PETS3;

    static {
        PETS1 = createPets1();
        PETS2 = createPets2();
        PETS3 = createPets3();

        ANIMALS1 = createAnimals1();
        ANIMALS2 = createAnimals2();
        ANIMALS3 = createAnimals3();

        FARMS1 = createFarms1();
        FARMS2 = createFarms2();
        FARMS3 = createFarms3();

        ASSOCIATION1 = createAssociation1();
        ASSOCIATION2 = createAssociation2();
        ASSOCIATION3 = createAssociation3();

        ASSOCIATIONS = createAssociations();
    }

    private static Association<Farm>[] createAssociations() {
        @SuppressWarnings("unchecked")
        final Association<Farm>[] associations = new Association[3];

        associations[0] = getAssociation1();
        associations[1] = getAssociation2();
        associations[2] = getAssociation3();

        return associations;
    }

    private static Association<Farm> createAssociation1() {
        final Association<Farm> association1 = new Association<>();
        association1.setName("White Star Farm");
        association1.setMembers(new ArrayList<>());
        Collections.addAll(association1.getMembers(), getFarms1());
        Collections.addAll(association1.getMembers(), getFarms2());
        return association1;
    }

    private static Association<Farm> createAssociation2() {
        final Association<Farm> association2 = new Association<>();
        association2.setName("Three Ravens");
        association2.setMembers(new ArrayList<>());
        Collections.addAll(association2.getMembers(), getFarms2());
        Collections.addAll(association2.getMembers(), getFarms3());
        return association2;
    }

    private static Association<Farm> createAssociation3() {
        final Association<Farm> association3 = new Association<>();
        association3.setName("Vista Ridge Farm");
        association3.setMembers(new ArrayList<>());
        Collections.addAll(association3.getMembers(), getFarms1());
        Collections.addAll(association3.getMembers(), getFarms3());
        return association3;
    }

    private static Farm[] createFarms1() {
        final Farm[] farm1 = new Farm[3];

        farm1[0] = new Farm();
        farm1[0].setAnimals(Arrays.asList(getAnimals1()));
        farm1[0].setPets(getPets1());
        farm1[0].setFounded(LocalDateTime.of(2000, 1, 1, 16, 13));

        farm1[1] = null;

        farm1[2] = new Farm();
        farm1[2].setAnimals(Arrays.asList(getAnimals2()));
        farm1[2].setPets(getPets2());
        farm1[2].setFounded(LocalDateTime.of(1990, 5, 14, 10, 55));

        return farm1;
    }

    private static Farm[] createFarms2() {
        final Farm[] farm2 = new Farm[3];

        farm2[0] = new Farm();
        farm2[0].setAnimals(Arrays.asList(getAnimals2()));
        farm2[0].setPets(getPets2());
        farm2[0].setFounded(LocalDateTime.of(1995, 3, 25, 14, 32));

        farm2[1] = new Farm();
        farm2[1].setAnimals(Arrays.asList(getAnimals3()));
        farm2[1].setPets(getPets3());
        farm2[1].setFounded(LocalDateTime.of(1997, 3, 22, 3, 7));

        farm2[2] = null;

        return farm2;
    }

    private static Farm[] createFarms3() {
        final Farm[] farm3 = new Farm[3];

        farm3[0] = null;

        farm3[1] = new Farm();
        farm3[1].setAnimals(Arrays.asList(getAnimals3()));
        farm3[1].setPets(getPets3());
        farm3[1].setFounded(LocalDateTime.of(1992, 2, 2, 14, 2));

        farm3[2] = new Farm();
        farm3[2].setAnimals(Arrays.asList(getAnimals1()));
        farm3[2].setPets(getPets1());
        farm3[2].setFounded(LocalDateTime.of(1994, 4, 4, 16, 4));

        return farm3;
    }

    private static Animal[] createAnimals1() {
        final Animal[] animals1 = new Animal[6];

        animals1[0] = null;

        Calendar cal = Calendar.getInstance();
        cal.set(2001, 11, 1, 15, 15);
        animals1[1] = new Cow(942.34, cal);

        cal = Calendar.getInstance();
        cal.set(2001, 10, 2, 16, 14);
        animals1[2] = new Cow(1023.84, cal);

        cal = Calendar.getInstance();
        cal.set(2001, 9, 3, 17, 13);
        animals1[3] = new Sheep(73.81, cal);

        cal = Calendar.getInstance();
        cal.set(2001, 8, 4, 18, 12);
        animals1[4] = new Crocodile(425.48, cal);

        cal = Calendar.getInstance();
        cal.set(2001, 7, 5, 19, 11);
        animals1[5] = new Violetear(0.389, cal);

        return animals1;
    }

    private static Animal[] createAnimals2() {
        final Animal[] animals2 = new Animal[6];

        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 1, 1, 1);
        animals2[0] = new Cow(942.34, cal);

        cal = Calendar.getInstance();
        cal.set(2000, 2, 2, 2, 2);
        animals2[1] = new Cow(1023.84, cal);

        animals2[2] = null;

        cal = Calendar.getInstance();
        cal.set(2000, 3, 3, 3, 3);
        animals2[3] = new Sheep(73.81, cal);

        cal = Calendar.getInstance();
        cal.set(2000, 4, 4, 4, 4);
        animals2[4] = new Sheep(120.16, cal);

        cal = Calendar.getInstance();
        cal.set(2000, 5, 5, 5, 5);
        animals2[5] = new Violetear(0.234, cal);

        return animals2;
    }

    private static Animal[] createAnimals3() {
        final Animal[] animals3 = new Animal[6];

        Calendar cal = Calendar.getInstance();
        cal.set(1999, 1, 2, 3, 4);
        animals3[0] = new Cow(942.34, cal);

        cal = Calendar.getInstance();
        cal.set(1999, 4, 3, 2, 1);
        animals3[1] = new Cow(1023.84, cal);

        cal = Calendar.getInstance();
        cal.set(1999, 4, 2, 3, 1);
        animals3[2] = new Sheep(73.81, cal);

        cal = Calendar.getInstance();
        cal.set(1999, 1, 3, 2, 4);
        animals3[3] = new Sheep(120.16, cal);

        cal = Calendar.getInstance();
        cal.set(1999, 1, 1, 2, 4);
        animals3[4] = new Crocodile(425.48, cal);

        animals3[5] = null;

        return animals3;
    }

    private static Pet[] createPets1() {
        final Pet[] pets1 = new Pet[4];

        Calendar cal = Calendar.getInstance();
        cal.set(1998, 1, 4, 1, 4);
        pets1[0] = new Dog(22.13, "Buddy", cal);

        cal = Calendar.getInstance();
        cal.set(1998, 3, 2, 3, 2);
        pets1[1] = new Dog(17.49, "Duke", cal);

        cal = Calendar.getInstance();
        cal.set(1998, 3, 2, 1, 1);
        pets1[2] = new Cat(13.71, "Tigger", cal);

        pets1[3] = null;

        return pets1;
    }

    private static Pet[] createPets2() {
        final Pet[] pets2 = new Pet[4];

        Calendar cal = Calendar.getInstance();
        cal.set(1997, 1, 2, 3, 4);
        pets2[0] = new Dog(17.49, "Daisy", cal);

        cal = Calendar.getInstance();
        cal.set(1997, 4, 1, 1, 4);
        pets2[1] = new Dog(17.49, "Teddy", cal);

        pets2[2] = null;

        cal = Calendar.getInstance();
        cal.set(1997, 3, 1, 2, 4);
        pets2[3] = new Dog(17.49, "Rocky", cal);
        return pets2;
    }

    private static Pet[] createPets3() {
        final Pet[] pets3 = new Pet[4];

        pets3[0] = null;

        Calendar cal = Calendar.getInstance();
        cal.set(1996, 1, 4, 3, 2);
        pets3[1] = new Cat(17.49, "Oliver", cal);

        cal = Calendar.getInstance();
        cal.set(1996, 2, 4, 3, 1);
        pets3[2] = new Cat(17.49, "Angel", cal);

        cal = Calendar.getInstance();
        cal.set(1996, 4, 1, 2, 3);
        pets3[3] = new Cat(17.49, "Kitty", cal);
        return pets3;
    }

    public static Association<Farm>[] getAssociations() {
        return ASSOCIATIONS;
    }

    public static Association<Farm> getAssociation1() {
        return ASSOCIATION1;
    }

    public static Association<Farm> getAssociation2() {
        return ASSOCIATION2;
    }

    public static Association<Farm> getAssociation3() {
        return ASSOCIATION3;
    }

    public static Farm[] getFarms1() {
        return FARMS1;
    }

    public static Farm[] getFarms2() {
        return FARMS2;
    }

    public static Farm[] getFarms3() {
        return FARMS3;
    }

    public static Animal[] getAnimals1() {
        return ANIMALS1;
    }

    public static Animal[] getAnimals2() {
        return ANIMALS2;
    }

    public static Animal[] getAnimals3() {
        return ANIMALS3;
    }

    public static Pet[] getPets1() {
        return PETS1;
    }

    public static Pet[] getPets2() {
        return PETS2;
    }

    public static Pet[] getPets3() {
        return PETS3;
    }

}
