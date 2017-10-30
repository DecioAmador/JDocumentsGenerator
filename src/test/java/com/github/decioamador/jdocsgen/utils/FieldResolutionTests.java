package com.github.decioamador.jdocsgen.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.decioamador.jdocsgen.JDocsGenException;
import com.github.decioamador.jdocsgen.model.DataAnimal;
import com.github.decioamador.jdocsgen.model.DataProduct;
import com.github.decioamador.jdocsgen.model.animal.Animal;
import com.github.decioamador.jdocsgen.model.animal.Farm;
import com.github.decioamador.jdocsgen.model.animal.Pet;
import com.github.decioamador.jdocsgen.model.animal.Transport;
import com.github.decioamador.jdocsgen.model.product.BasicInfo;
import com.github.decioamador.jdocsgen.model.product.Category;
import com.github.decioamador.jdocsgen.model.product.Product;

public class FieldResolutionTests {

    static Stream<Arguments> resolveFieldArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - BasicInfo/String
        final Object obj1 = DataProduct.getProductsGrains()[2];
        final String field1 = "basicInfo.description";
        final Object expected1 = "A cereal grain, it is the most widely consumed staple food for a large "
                + "part of the world's human population, especially in Asia.";

        builder.add(Arguments.of(obj1, field1, expected1));

        // Arg2 - String
        final Object obj2 = DataAnimal.getAnimals1()[5];
        final String field2 = "specie";
        final Object expected2 = "Colibri delphinae";

        builder.add(Arguments.of(obj2, field2, expected2));

        // Arg3 - null
        final Farm nullPets = new Farm();
        nullPets.setPets(null);
        final Object obj3 = nullPets;
        final String field3 = "pets";
        final Object expected3 = null;

        builder.add(Arguments.of(obj3, field3, expected3));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("resolveFieldArguments")
    public void resolveField(final Object obj, final String field, final Object expected) throws Exception {

        final Object result = FieldResolution.resolveField(obj, field);

        if (result != null) {
            assertFalse(result.getClass().isArray());
        }
        assertEquals(expected, result);
    }

    static Stream<Arguments> resolveFieldExceptionsArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - The method doesn't exist
        final Object obj1 = DataProduct.getCategories()[0];
        final String field1 = "something";
        final Class<?> expected1 = NoSuchMethodException.class;

        builder.add(Arguments.of(obj1, field1, expected1));

        // Arg2 - Exception thrown when method invoked
        final Object obj2 = new Object() {
            @SuppressWarnings("unused")
            public void getPotatoes() throws Exception {
                throw new Exception("For the purpose of testing");
            }
        };
        final String field2 = "potatoes";
        final Class<?> expected2 = InvocationTargetException.class;

        builder.add(Arguments.of(obj2, field2, expected2));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("resolveFieldExceptionsArguments")
    public void resolveFieldExceptions(final Object obj, final String field, final Class<?> exception)
            throws Exception {

        try {
            FieldResolution.resolveField(obj, field);
            fail("It is supposed to give " + exception);
        } catch (final JDocsGenException e) {
            assertTrue(exception.isInstance(e.getCause()),
                    String.format("The cause is %s and should be %s", e.getCause(), exception));
        }
    }

    static Stream<Arguments> resolveFieldAggregationArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - Association<Farm>/List<Farm>/List<Animal>/String
        final Object obj1 = DataAnimal.getAssociation1();
        final String field1 = "members.animals.specie";

        final List<Animal> animals = new ArrayList<>();
        final List<Farm> farms = DataAnimal.getAssociation1().getMembers();
        for (final Farm f : farms) {
            if (f != null) {
                for (final Animal a : f.getAnimals()) {
                    if (a != null) {
                        animals.add(a);
                    } else {
                        animals.add(null);
                    }
                }
            }
        }

        final List<String> species = new ArrayList<>();
        for (final Animal a : animals) {
            if (a != null) {
                species.add(a.getSpecie());
            } else {
                species.add(null);
            }
        }
        final Object[] expected1 = species.toArray();
        builder.add(Arguments.of(obj1, field1, expected1, true));

        // Arg2 - Association<Farm>/List<Farm>/List<Animal>/Set<Transport>
        final Object obj2 = DataAnimal.getAssociation1();
        final String field2 = "members.animals.transport";

        final List<Transport> transports = new ArrayList<>();
        animals.stream().filter((final Animal a) -> a != null)
                .forEach((final Animal a) -> transports.addAll(a.getTransport()));

        final Object[] expected2 = transports.toArray();
        builder.add(Arguments.of(obj2, field2, expected2, true));

        // Arg3 - BasicInfo/String
        final Object obj3 = DataProduct.getProductsGrains()[2];
        final String field3 = "basicInfo.name";
        final Object expected3 = "Rice";

        builder.add(Arguments.of(obj3, field3, expected3, false));

        // Arg4 - Empty Collection
        final Category noProds = new Category(20L, "No Products", "empty");
        noProds.setProducts(new HashSet<>());
        final Object obj4 = noProds;
        final String field4 = "products";
        final Object expected4 = null;

        builder.add(Arguments.of(obj4, field4, expected4, false));

        // Arg5 - Empty Array
        final Farm noPets = new Farm();
        noPets.setPets(new Pet[0]);
        final Object obj5 = noPets;
        final String field5 = "pets";
        final Object expected5 = null;

        builder.add(Arguments.of(obj5, field5, expected5, false));

        // Arg6 - null
        final Farm nullPets = new Farm();
        nullPets.setPets(null);
        final Object obj6 = nullPets;
        final String field6 = "pets";
        final Object expected6 = null;

        builder.add(Arguments.of(obj6, field6, expected6, false));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("resolveFieldAggregationArguments")
    public void resolveFieldAggregation(final Object obj, final String field, final Object expected,
            final boolean expectArray) throws Exception {

        final Object result = FieldResolution.resolveFieldAggregation(obj, field);
        if (result == null) {
            assertEquals(expected, result);

        } else {
            assertEquals(expectArray, result.getClass().isArray());

            if (expectArray) {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                final Comparator<Object> comp = (final Object a, final Object b) -> {
                    if (a == null) {
                        return 1;
                    } else if (b == null) {
                        return -1;
                    } else {
                        return ((Comparable) a).compareTo((Comparable) b);
                    }
                };

                // Check if contains the same elements
                final Object[] expectedArray = (Object[]) expected;
                Arrays.sort(expectedArray, comp);

                final Object[] resultArray = (Object[]) result;
                Arrays.sort(resultArray, comp);

                assertArrayEquals(expectedArray, resultArray);
            } else {
                assertEquals(expected, result);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("resolveFieldExceptionsArguments")
    public void resolveFieldAggregationExceptions(final Object obj, final String field, final Class<?> exception)
            throws Exception {

        try {
            FieldResolution.resolveFieldAggregation(obj, field);
            fail("It is supposed to give " + exception);
        } catch (final JDocsGenException e) {
            assertTrue(exception.isInstance(e.getCause()),
                    String.format("The cause is %s and should be %s", e.getCause(), exception));
        }
    }

    static Stream<Arguments> resolveMethodAggregationArguments() throws Exception {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - Array and the elements are NOT arrays or collections
        final Object paramObj1 = DataProduct.getProductsFruits();
        final Method paramMth1 = Product.class.getDeclaredMethod("getUuid");

        final Product[] prods = DataProduct.getProductsFruits();
        final String[] expected1 = new String[prods.length];
        for (int i = 0; i < DataProduct.getProductsFruits().length; i++) {
            if (prods[i] != null) {
                expected1[i] = prods[i].getUuid();
            }
        }
        builder.add(Arguments.of(paramObj1, paramMth1, expected1, true));

        // Arg2 - Array and the elements are collections
        final Category[] categoties = new Category[] { DataProduct.getCategories()[4], DataProduct.getCategories()[5],
                DataProduct.getCategories()[6] };
        final Object paramObj2 = categoties;
        final Method paramMth2 = Category.class.getDeclaredMethod("getProducts");

        final Object[] expected2 = Stream
                .concat(Arrays.stream(DataProduct.getProductsGrains()), Arrays.stream(DataProduct.getProductsOils()))
                .toArray();
        builder.add(Arguments.of(paramObj2, paramMth2, expected2, true));

        // Arg3 - Not array
        final Object paramObj3 = DataProduct.getProductsOils()[0].getBasicInfo();
        final Method paramMth3 = BasicInfo.class.getDeclaredMethod("getName");

        final Object expected3 = DataProduct.getProductsOils()[0].getBasicInfo().getName();
        builder.add(Arguments.of(paramObj3, paramMth3, expected3, false));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("resolveMethodAggregationArguments")
    public void resolveMethodAggregation(final Object obj, final Method m, final Object expected,
            final boolean expectArray) throws Exception {

        final Method resolveMthAgg = FieldResolution.class.getDeclaredMethod("resolveMethodAggregation", Object.class,
                Method.class);
        resolveMthAgg.setAccessible(true);

        final Object result = resolveMthAgg.invoke(null, obj, m);
        assertEquals(expectArray, result.getClass().isArray());

        if (expectArray) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            final Comparator<Object> comp = (final Object a, final Object b) -> {
                if (a == null) {
                    return 1;
                } else if (b == null) {
                    return -1;
                } else {
                    return ((Comparable) a).compareTo((Comparable) b);
                }
            };

            // Check if contains the same elements
            final Object[] expectedArray = (Object[]) expected;
            Arrays.sort(expectedArray, comp);

            final Object[] resultArray = (Object[]) result;
            Arrays.sort(resultArray, comp);

            assertArrayEquals(expectedArray, resultArray);
        } else {
            assertEquals(expected, result);
        }
    }

    static Stream<Arguments> commonDataAggregation() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - Full arrays
        final Object[] param1 = new Object[3];
        param1[0] = new Object[] { 1, 2, 3 };
        param1[1] = new Object[] { 4, 5, 6 };
        param1[2] = new Object[] { 7, 8, 9 };

        final Object[] expected1 = new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        builder.add(Arguments.of(param1, expected1));

        // Arg2 - Full array and empty array
        final Object[] param2 = new Object[2];
        param2[0] = new Object[] { "ab", "xz" };
        param2[1] = new Object[] {};

        final Object[] expected2 = new Object[] { "ab", "xz" };
        builder.add(Arguments.of(param2, expected2));

        // Arg3 - Full array and null
        final Object[] param3 = new Object[2];
        param3[0] = new Object[] { Transport.AIR, Transport.AQUATIC };
        param3[1] = null;

        final Object[] expected3 = new Object[] { Transport.AIR, Transport.AQUATIC };
        builder.add(Arguments.of(param3, expected3));

        // Arg4 - Full array and array with null elements
        final Category[] categories = DataProduct.getCategories();
        final Object[] param4 = new Object[2];
        param4[0] = new Object[] { categories[0], categories[1] };
        param4[1] = new Object[] { null };

        final Object[] expected4 = new Object[] { DataProduct.getCategories()[0], DataProduct.getCategories()[1],
                null };
        builder.add(Arguments.of(param4, expected4));

        return builder.build();
    }

    static Stream<Arguments> aggregateElementsArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1
        final Object[] param1 = new Object[0];
        final Object[] expected1 = new Object[0];
        builder.add(Arguments.of(param1, expected1));

        // Arg2
        final Object[] param2 = null;
        final Object[] expected2 = null;
        builder.add(Arguments.of(param2, expected2));

        // Arg3
        final Object[] param3 = new Object[2];
        final Object[] expected3 = new Object[2];
        builder.add(Arguments.of(param3, expected3));

        return builder.build();
    }

    static Stream<Arguments> aggregateArraysAndCollectionsArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1
        final Object[] param1 = new Object[2];
        final Object[] expected1 = new Object[0];
        builder.add(Arguments.of(param1, expected1));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource({ "aggregateElementsArguments", "commonDataAggregation" })
    public void aggregateElements(final Object[] param, final Object[] expected) throws Exception {
        final Method aggElements = FieldResolution.class.getDeclaredMethod("aggregateElements", Object[].class);
        aggElements.setAccessible(true);

        final Object[] result = (Object[]) aggElements.invoke(null, new Object[] { param });
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource({ "aggregateArraysAndCollectionsArguments", "commonDataAggregation" })
    public void aggregateArraysAndCollections(final Object[] param, final Object[] expected) throws Exception {
        final Method aggArray = FieldResolution.class.getDeclaredMethod("aggregateArrays", Object[].class);
        aggArray.setAccessible(true);

        Object[] result = (Object[]) aggArray.invoke(null, new Object[] { param });
        assertArrayEquals(expected, result);

        // If you have collections it must be the same result
        final Method aggCollection = FieldResolution.class.getDeclaredMethod("aggregateCollections", Object[].class);
        aggCollection.setAccessible(true);

        // Pass arrays to collections
        Object[] aux;
        for (int i = 0; i < param.length; i++) {
            aux = (Object[]) param[i];
            if (aux == null) {
                param[i] = aux;
            } else {
                param[i] = Arrays.asList(aux);
            }
        }

        result = (Object[]) aggCollection.invoke(null, new Object[] { param });
        assertArrayEquals(expected, result);
    }

    static Stream<Arguments> transformToGetArguments() {
        return Stream.of(Arguments.of("uuid", "getUuid"), Arguments.of("product", "getProduct"),
                Arguments.of("animal", "getAnimal"), Arguments.of("farm", "getFarm"));
    }

    @ParameterizedTest
    @MethodSource("transformToGetArguments")
    public void transformToGet(final String param, final String expected) throws Exception {
        final Method m = FieldResolution.class.getDeclaredMethod("transformToGet", String.class);
        m.setAccessible(true);

        final Object result = m.invoke(null, param);
        assertEquals(expected, result);
    }

}
