package com.github.decioamador.jdocsgen.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.decioamador.jdocsgen.model.product.Category;
import com.github.decioamador.jdocsgen.model.product.Price;
import com.github.decioamador.jdocsgen.model.product.Product;

public class DataProduct {

    private static final Category[] CATEGORIES;

    private static final Product[] PRODUCTS_FRUITS;
    private static final Map<String, String> TRANSLATOR_FRUITS;

    private static final Product[] PRODUCTS_VEGETABLES;
    private static final Map<String, String> TRANSLATOR_VEGETABLES;

    private static final Product[] PRODUCTS_PROTEIN;
    private static final Map<String, String> TRANSLATOR_PROTEIN;

    private static final Product[] PRODUCTS_DAIRIES;
    private static final Map<String, String> TRANSLATOR_DAIRIES;

    private static final Product[] PRODUCTS_GRAINS;
    private static final Map<String, String> TRANSLATOR_GRAINS;

    private static final Product[] PRODUCTS_OILS;
    private static final Map<String, String> TRANSLATOR_OILS;

    private static final ResourceBundle RESOURCE_BUNDLE_PRODUCTS_NAMES;

    private static final Currency EURO;
    private static final Currency DOLAR;
    private static final Currency YUAN;
    private static final Currency LIBRA;

    static {
        EURO = Currency.getInstance("EUR");
        DOLAR = Currency.getInstance("USD");
        YUAN = Currency.getInstance("CNY");
        LIBRA = Currency.getInstance("GBP");

        PRODUCTS_FRUITS = createProductsFruits();
        TRANSLATOR_FRUITS = createFruitsTranslation();

        PRODUCTS_VEGETABLES = createProductsVegetables();
        TRANSLATOR_VEGETABLES = createVegetablesTranslation();

        PRODUCTS_PROTEIN = createProductsProtein();
        TRANSLATOR_PROTEIN = createProteinsTranslation();

        PRODUCTS_DAIRIES = createProductsDairies();
        TRANSLATOR_DAIRIES = createDairyTranslation();

        PRODUCTS_GRAINS = createProductsGrains();
        TRANSLATOR_GRAINS = createGrainsTranslation();

        PRODUCTS_OILS = createProductsOlis();
        TRANSLATOR_OILS = createOilsTranslation();

        CATEGORIES = createCategories();

        try {
            RESOURCE_BUNDLE_PRODUCTS_NAMES = createResourceBundleProducts();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Category[] createCategories() {
        final Category[] categories = new Category[7];
        categories[0] = new Category(1L, "Fruits",
                "The fruit group includes any whole fruit or 100 percent fruit juice.");
        categories[0].setProducts(Arrays.stream(getProductsFruits()).collect(Collectors.toSet()));

        categories[1] = new Category(2L, "Vegetables",
                "The vegetable group includes any whole vegetables or 100 percent vegetable juice. "
                        + "Vegetables may be raw, cooked, fresh, canned, frozen or dehydrated.");
        categories[1].setProducts(Arrays.stream(getProductsVegetables()).collect(Collectors.toSet()));

        categories[2] = new Category(3L, "Protein",
                "Meat, poultry, seafood, eggs, nuts, seeds, soy foods, beans, peas and any items "
                        + "made from these foods belong to the protein group.");
        categories[2].setProducts(Arrays.stream(getProductsProteins()).collect(Collectors.toSet()));

        categories[3] = new Category(4L, "Dairy",
                "All fluid milk and products made from milk, such as cheese, yogurt, ice cream and "
                        + "pudding, belong to the dairy group.");
        categories[3].setProducts(Arrays.stream(getProductsDairies()).collect(Collectors.toSet()));

        categories[4] = new Category(5L, "Grains",
                "Foods such as wheat, rice, oats, barley or cornmeal belong in the grains food group.");
        categories[4].setProducts(Arrays.stream(getProductsGrains()).collect(Collectors.toSet()));

        categories[5] = null;

        categories[6] = new Category(6L, "Oils",
                "The oil group includes healthy fats and foods that are naturally high in healthy oils, "
                        + "such as nuts, olives, avocados and some fish.");
        categories[6].setProducts(Arrays.stream(getProductsOils()).collect(Collectors.toSet()));

        return categories;
    }

    private static Product[] createProductsFruits() {
        final Product[] fruits = new Product[8];

        fruits[0] = new Product(UUID.randomUUID().toString(), "Apple", "Granny Smith, Royal Gala, Golden Delicious "
                + "and Pink Lady are just a few of the thousands of different kinds of apple that are grown around the world.");
        createDummyPrices(fruits[0], 1.52);

        fruits[1] = new Product(UUID.randomUUID().toString(), "Banana", "They are a great source of energy and contain "
                + "lots of vitamins and minerals, especially potassium, which is important to help cells, nerves and muscles "
                + "in your body to work properly and it helps to lower blood pressure.");
        createDummyPrices(fruits[1], 1.04);

        fruits[2] = new Product(UUID.randomUUID().toString(), "Kiwi",
                "A kiwi fruit is hairy on the outside and soft in the middle. "
                        + "It is one of the only fruits to be green when it is ripe.");
        createDummyPrices(fruits[2], 1.32);

        fruits[3] = new Product(UUID.randomUUID().toString(), "Orange",
                "Oranges grow best in countries such as Spain and Italy"
                        + " - where it's hot and sunny during the day and cooler at night.");

        fruits[4] = null;

        fruits[5] = new Product(UUID.randomUUID().toString(), "Pear",
                "They can be yellow, green, reddish or brown on the "
                        + "outside but they all have white, juicy flesh inside.");
        createDummyPrices(fruits[5], 1.73);

        fruits[6] = new Product(UUID.randomUUID().toString(), "Strawberry",
                "They are the only fruits to have their seeds on the outside - "
                        + "one strawberry can have as many as 200.");
        createDummyPrices(fruits[6], 2.05);

        fruits[7] = new Product(UUID.randomUUID().toString(), "Watermelon",
                "They contain lots of water and are really, really refreshing! In China, "
                        + "children love drinking watermelon juice in summer to help them stay cool.");
        createDummyPrices(fruits[7], 2.49);

        return fruits;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createFruitsTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Apple", "I-apula");
        map.put("Banana", "Ibhanana");
        map.put("Kiwi", "Ikiwi");
        map.put("Orange", "Iwolintshi");
        map.put("Pear", "Ipheya");
        map.put("Strawberry", "Ijikijolo");
        map.put("Watermelon", "Ikhabe");
        return map;
    }

    private static Product[] createProductsVegetables() {
        final Product[] vegetables = new Product[6];

        vegetables[0] = new Product(UUID.randomUUID().toString(), "Leek",
                "These are in the same family as onion and garlic – they are allium vegetables.");
        createDummyPrices(vegetables[0], 1.67);

        vegetables[1] = new Product(UUID.randomUUID().toString(), "Lettuce",
                "Fast to grow and harvest, enjoy lettuce each spring and fall for easy salads.");
        createDummyPrices(vegetables[1], 1.31);

        vegetables[2] = new Product(UUID.randomUUID().toString(), "Onion",
                "From sweet to pungent, an assortment of onions is ideal for the kitchen and for storage.");
        createDummyPrices(vegetables[2], 1.14);

        vegetables[3] = new Product(UUID.randomUUID().toString(), "Potato",
                "The potato is a starchy, tuberous crop from the perennial nightshade "
                        + "Solanum tuberosum. The word potato may refer either to the plant "
                        + "itself or to the edible tuber.");
        createDummyPrices(vegetables[3], 1.62);

        vegetables[4] = new Product(UUID.randomUUID().toString(), "Broccoli",
                "Broccoli is known to be a hearty and tasty vegetable which is rich in dozens of nutrients.");
        createDummyPrices(vegetables[4], 2.33);

        vegetables[5] = null;

        return vegetables;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createVegetablesTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Leek", "Isitshalo esihlobene no-anyanisi");
        map.put("Lettuce", "Ulethisi");
        map.put("Onion", "U-anyanini");
        map.put("Potato", "Iwolintshi");
        map.put("Broccoli", "Ipheya");
        return map;
    }

    private static Product[] createProductsProtein() {
        final Product[] proteins = new Product[6];

        proteins[0] = new Product(UUID.randomUUID().toString(), "Eggs", "Eggs are laid by female animals "
                + "of many different species, including birds, reptiles, amphibians, mammals, and fish.");
        createDummyPrices(proteins[0], 1.99);

        proteins[1] = new Product(UUID.randomUUID().toString(), "Chicken Breast", "A chicken breast is actually "
                + "the underside of a chicken when it is up and wandering about. Each chicken has two breasts.");
        createDummyPrices(proteins[1], 2.16);

        proteins[2] = null;

        proteins[3] = new Product(UUID.randomUUID().toString(), "Lean Beef",
                "In order to be considered lean, a cut of beef must meet these parameters (based on a 3 oz. cooked serving): "
                        + "Less than 10 grams of total fat, 4.5 grams or less of saturated fat and less than 95 mg of cholesterol");
        createDummyPrices(proteins[3], 3.17);

        proteins[4] = new Product(UUID.randomUUID().toString(), "Shrimp", "The term shrimp is used to refer to some "
                + "decapod crustaceans, although the exact animals covered can vary.");
        createDummyPrices(proteins[4], 12.79);

        proteins[5] = new Product(UUID.randomUUID().toString(), "Sea ​​bass",
                "Sea bass tastes great as both a dinner party dish or simple supper.");
        createDummyPrices(proteins[5], 8.99);

        return proteins;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createProteinsTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Eggs", "Amaqanda");
        map.put("Chicken Breast", "Inkukhu Breast");
        map.put("Lean Beef", "I-Bean Lean");
        map.put("Shrimp", "Izinhlanzi");
        map.put("Sea ​​bass", "Ama-sea bass");
        return map;
    }

    private static Product[] createProductsDairies() {
        final Product[] dairy = new Product[6];

        dairy[0] = new Product(UUID.randomUUID().toString(), "Butter", "Made by churning fresh or "
                + "fermented cream or milk. It is generally used as a spread and a condiment, as well as in "
                + "cooking, such as baking, sauce making, and pan frying. Butter consists of butterfat, milk proteins and water.");
        createDummyPrices(dairy[0], 2.41);

        dairy[1] = new Product(UUID.randomUUID().toString(), "Cheese", "A food derived from milk that is "
                + "produced in a wide range of flavors, textures, and forms by coagulation of the milk protein casein.");
        createDummyPrices(dairy[1], 10.53);

        dairy[2] = new Product(UUID.randomUUID().toString(), "Condensed milk", "Milk from which water "
                + "has been removed. It is most often found in the form of sweetened condensed milk, with sugar added.");
        createDummyPrices(dairy[2], 2.88);

        dairy[3] = new Product(UUID.randomUUID().toString(), "Milk", "A white liquid produced by the "
                + "mammary glands of mammals. It is the primary source of nutrition for young mammals before they are "
                + "able to digest other types of food.");
        createDummyPrices(dairy[3], 1.59);

        dairy[4] = null;

        dairy[5] = new Product(UUID.randomUUID().toString(), "Whipped cream",
                "Cream that has been beaten by a mixer, whisk, or fork until it is light and fluffy.");
        createDummyPrices(dairy[5], 3.47);

        return dairy;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createDairyTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Butter", "Ibhotela");
        map.put("Cheese", "Ushizi");
        map.put("Condensed milk", "Ubisi olukhishiwe");
        map.put("Milk", "Ubisi");
        map.put("Whipped cream", "Ukhilimu oqoshiwe");
        return map;
    }

    private static Product[] createProductsGrains() {
        final Product[] grains = new Product[6];

        grains[0] = null;

        grains[1] = new Product(UUID.randomUUID().toString(), "Wheat",
                "Wheat is a grass widely cultivated for its seed, a cereal grain which is a worldwide staple food.");
        createDummyPrices(grains[1], 0.67);

        grains[2] = new Product(UUID.randomUUID().toString(), "Rice",
                "A cereal grain, it is the most widely consumed staple food for a large"
                        + " part of the world's human population, especially in Asia.");
        createDummyPrices(grains[2], 0.43);

        grains[3] = new Product(UUID.randomUUID().toString(), "Oats",
                "While oats are suitable for human consumption as oatmeal and rolled oats, "
                        + "one of the most common uses is as livestock feed.");
        createDummyPrices(grains[3], 0.89);

        grains[4] = new Product(UUID.randomUUID().toString(), "Barley",
                "Barley a member of the grass family, is a major cereal grain grown in temperate climates globally.");
        createDummyPrices(grains[4], 0.92);

        grains[5] = new Product(UUID.randomUUID().toString(), "Corn",
                "Corn is a diet staple for many people around the world. "
                        + "It’s found as a side dish, in soup, in casseroles, and more.");
        createDummyPrices(grains[5], 0.51);

        return grains;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createGrainsTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Wheat", "Ngokolweni");
        map.put("Rice", "Ilayisi");
        map.put("Oats", "Izinhlamvu zefilishi");
        map.put("Barley", "Ibhali");
        map.put("Corn", "Ukolweni");
        return map;
    }

    private static Product[] createProductsOlis() {
        final Product[] oils = new Product[2];

        oils[0] = new Product(UUID.randomUUID().toString(), "Extra Virgin Olive", "Extra virgin olive oil "
                + "is made simply by crushing olives and extracting the juice. It is the only cooking oil that is made "
                + "without the use of chemicals and industrial refining.");
        createDummyPrices(oils[0], 9.32);

        oils[1] = null;

        return oils;
    }

    /**
     * English to Zulu
     * 
     * @return Map containing translation of English to Zulu
     */
    private static Map<String, String> createOilsTranslation() {
        final Map<String, String> map = new HashMap<>();
        map.put("Extra Virgin Olive", "Umnqumo Omunye WaseMvini");
        return map;
    }

    private static void createDummyPrices(final Product product, final double priceEuro) {
        Price price = new Price(priceEuro, EURO);
        product.addPrice(price);

        price = new Price(priceEuro * 1.178545, DOLAR);
        product.addPrice(price);

        price = new Price(priceEuro * 0.893749, LIBRA);
        product.addPrice(price);

        price = new Price(priceEuro * 7.80441693, YUAN);
        product.addPrice(price);
    }

    private static ResourceBundle createResourceBundleProducts() throws IOException {
        final Properties props = new Properties();

        // Fruits
        for (final Entry<String, String> entry : DataProduct.getTranslatorFruits().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        // Vegetables
        for (final Entry<String, String> entry : DataProduct.getTranslatorVegetables().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        // Proteins
        for (final Entry<String, String> entry : DataProduct.getTranslatorProtein().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        // Dairy
        for (final Entry<String, String> entry : DataProduct.getTranslatorDairies().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        // Oils
        for (final Entry<String, String> entry : DataProduct.getTranslatorOils().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        props.store(output, null);
        final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        return new PropertyResourceBundle(input);
    }

    // GET'S

    public static Category[] getCategories() {
        return CATEGORIES;
    }

    public static Product[] getProductsFruits() {
        return PRODUCTS_FRUITS;
    }

    public static Product[] getProductsVegetables() {
        return PRODUCTS_VEGETABLES;
    }

    public static Product[] getProductsProteins() {
        return PRODUCTS_PROTEIN;
    }

    public static Product[] getProductsDairies() {
        return PRODUCTS_DAIRIES;
    }

    public static Product[] getProductsGrains() {
        return PRODUCTS_GRAINS;
    }

    public static Product[] getProductsOils() {
        return PRODUCTS_OILS;
    }

    public static Map<String, String> getTranslatorFruits() {
        return TRANSLATOR_FRUITS;
    }

    public static Map<String, String> getTranslatorVegetables() {
        return TRANSLATOR_VEGETABLES;
    }

    public static Product[] getProductsProtein() {
        return PRODUCTS_PROTEIN;
    }

    public static Map<String, String> getTranslatorProtein() {
        return TRANSLATOR_PROTEIN;
    }

    public static Map<String, String> getTranslatorDairies() {
        return TRANSLATOR_DAIRIES;
    }

    public static Map<String, String> getTranslatorGrains() {
        return TRANSLATOR_GRAINS;
    }

    public static Map<String, String> getTranslatorOils() {
        return TRANSLATOR_OILS;
    }

    public static ResourceBundle getResourceBundleProductsNames() {
        return RESOURCE_BUNDLE_PRODUCTS_NAMES;
    }

    public static Currency getEuro() {
        return EURO;
    }

    public static Currency getDolar() {
        return DOLAR;
    }

    public static Currency getYuan() {
        return YUAN;
    }

    public static Currency getLibra() {
        return LIBRA;
    }

}
