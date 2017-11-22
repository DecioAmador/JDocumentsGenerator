package com.github.decioamador.jdocsgen.table;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.github.decioamador.jdocsgen.model.DataProduct;
import com.github.decioamador.jdocsgen.model.product.Product;
import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

public class TableGeneratorDemo {

    public static void main(final String[] args) throws Exception {

        // Workbook
        final Workbook wb = new SXSSFWorkbook();

        // Titles
        final String[] titles = new String[] { "Reference", "Name(Zulu)", "Description", "Prices" };

        // Fields
        final String[] fields = new String[] { "uuid", "basicInfo.name", "basicInfo.description", "prices" };

        // Products
        final List<Product> fruits = Arrays.asList(DataProduct.getProductsFruits());
        final List<Product> vegetables = Arrays.asList(DataProduct.getProductsVegetables());
        final List<Product> proteins = Arrays.asList(DataProduct.getProductsProteins());
        final List<Product> dairies = Arrays.asList(DataProduct.getProductsDairies());
        final List<Product> oils = Arrays.asList(DataProduct.getProductsOils());

        // Translator
        final TranslatorCollection transCollection = new TranslatorCollection();

        // Prints by calling toString
        transCollection.setRawPrint(new HashSet<String>());
        transCollection.getRawPrint().addAll(Arrays.asList("uuid", "basicInfo.description", "prices"));

        // Translate the name
        transCollection.setResourceBundleMap(new HashMap<>());
        transCollection.getResourceBundleMap().put("basicInfo.name", DataProduct.getResourceBundleProductsNames());
        final Translator translator = new TranslatorHelper(transCollection);

        // Options
        final TableOptions options = new TableOptions();
        options.setAutoSize(true);
        options.setInitPosRow(2);
        options.setInitPosCol(1);
        options.setAggregate(true);
        options.setPrevailTitlesStyle(true);
        options.setSeperatorAgg("; ");

        // Style for fields
        final CellStyle styleFields = wb.createCellStyle();
        final BorderStyle border = BorderStyle.MEDIUM;
        styleFields.setBorderBottom(border);
        styleFields.setBorderTop(border);
        styleFields.setBorderLeft(border);
        styleFields.setBorderRight(border);
        options.setFieldsStyle(styleFields);

        // Style for titles
        final CellStyle styleTitles = wb.createCellStyle();
        styleTitles.cloneStyleFrom(styleFields);
        styleTitles.setAlignment(HorizontalAlignment.CENTER);
        final short color = IndexedColors.GOLD.getIndex();
        styleTitles.setRightBorderColor(color);
        styleTitles.setLeftBorderColor(color);
        styleTitles.setTopBorderColor(color);
        styleTitles.setBottomBorderColor(color);
        options.setTitlesStyle(styleTitles);

        // Generate table based document
        try (TableGenerator tg = new TableGenerator(wb);
                OutputStream os = Files.newOutputStream(Paths.get(String.format(".%cdemo.xlsx", File.separatorChar)))) {

            // Generate sheets
            tg.generateTable("Fruits", options, fruits, titles, fields, translator);
            tg.generateTable("Vegetables", options, vegetables, titles, fields, translator);
            tg.generateTable("Proteins", options, proteins, titles, fields, translator);
            tg.generateTable("Dairies", options, dairies, titles, fields, translator);
            tg.generateTable("Oils", options, oils, titles, fields, translator);

            // Write document
            tg.write(os);
        }
    }

}