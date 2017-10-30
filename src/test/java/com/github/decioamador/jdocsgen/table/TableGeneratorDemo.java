package com.github.decioamador.jdocsgen.table;

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
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

public class TableGeneratorDemo {

    public static void main(final String[] args) throws Exception {

        // Workbook
        final Workbook wb = new SXSSFWorkbook();

        // Titles
        final List<String> titles = Arrays.asList("Reference", "Name(Zulu)", "Description", "Prices");

        // Fields
        final List<String> fields = Arrays.asList("uuid", "basicInfo.name", "basicInfo.description", "prices");

        // Products
        final List<Product> fruits = Arrays.asList(DataProduct.getProductsFruits());
        final List<Product> vegetables = Arrays.asList(DataProduct.getProductsVegetables());
        final List<Product> proteins = Arrays.asList(DataProduct.getProductsProteins());
        final List<Product> dairy = Arrays.asList(DataProduct.getProductsDairy());
        final List<Product> oils = Arrays.asList(DataProduct.getProductsOils());

        // Translator
        final TranslatorCollection translator = new TranslatorCollection();
        translator.setRawPrint(new HashSet<String>());
        translator.getRawPrint().addAll(Arrays.asList("uuid", "basicInfo.description", "prices"));
        translator.setResourceBundleMap(new HashMap<>());
        translator.getResourceBundleMap().put("basicInfo.name", DataProduct.getResourceBundleProductsNames());
        final TranslatorHelper transHelper = new TranslatorHelper(translator);

        // Options
        final TableOptions options = new TableOptions();
        options.setAutosize(true);
        options.setInitPosRow(2);
        options.setInitPosCol(1);
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
                OutputStream os = Files.newOutputStream(Paths.get("./demo.xlsx"))) {

            tg.generateTable("Fruits", options, fruits, titles, fields, transHelper);
            tg.generateTable("Vegetables", options, vegetables, titles, fields, transHelper);
            tg.generateTable("Proteins", options, proteins, titles, fields, transHelper);
            tg.generateTable("Dairy", options, dairy, titles, fields, transHelper);
            tg.generateTable("Oils", options, oils, titles, fields, transHelper);

            tg.write(os);
        }
    }

}