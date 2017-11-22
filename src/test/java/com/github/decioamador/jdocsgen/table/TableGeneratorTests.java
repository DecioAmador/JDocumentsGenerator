package com.github.decioamador.jdocsgen.table;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.temp.SXSSFWorkbookWithCustomZipEntrySource;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.decioamador.jdocsgen.model.DataProduct;
import com.github.decioamador.jdocsgen.model.product.BasicInfo;
import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

public class TableGeneratorTests {

    static Stream<Workbook> getWorkbooks() {
        final Builder<Workbook> builder = Stream.builder();

        Workbook wb = new HSSFWorkbook();
        builder.add(wb);

        wb = new XSSFWorkbook();
        builder.add(wb);

        wb = new SXSSFWorkbook();
        builder.add(wb);

        wb = new SXSSFWorkbookWithCustomZipEntrySource();
        builder.add(wb);

        return builder.build();
    }

    static Stream<Arguments> generateTableArguments() {
        final Builder<Arguments> builder = Stream.builder();

        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);
            final String sheetName = "sheet";
            final TableOptions options = new TableOptions();
            options.setSeperatorAgg("; ");

            final Collection<?> objs1 = Arrays.asList(DataProduct.getProductsFruits());
            final String[] titles = new String[] { "Reference", "Name", "Description", "Prices" };
            final String[] fields = new String[] { "uuid", "basicInfo.name", "basicInfo.description", "prices" };

            final TranslatorCollection transCol = new TranslatorCollection();
            transCol.setRawPrint(new HashSet<>());
            Collections.addAll(transCol.getRawPrint(), fields);
            final Translator translator = new TranslatorHelper(transCol);

            final int[][] cellsToExclude = new int[1][2];
            cellsToExclude[0][0] = 4;
            cellsToExclude[0][1] = 3;

            builder.add(Arguments.of(wb, tg, sheetName, options, objs1, titles, fields, translator, cellsToExclude));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("generateTableArguments")
    public void generateTableNoAgg(final Workbook wb, final TableGenerator tg, final String sheetName,
            final TableOptions options, final Collection<?> objs, final String[] titles, final String[] fields,
            final Translator translator, final int[][] cellsToExclude) throws Exception {

        try {
            options.setAggregate(false);
            final Sheet sheet = tg.generateTable(sheetName, options, objs, titles, fields, translator);
            assertNotNull(tg.getWorkbook());

            Row row;
            Cell cell;
            String value;
            final long countNonNull = objs.stream().filter((final Object o) -> o != null).count();
            for (int i = 1; i < countNonNull + 1; i++) {
                row = sheet.getRow(i);
                assertNotNull(row);
                for (int j = 0; j < titles.length; j++) {
                    cell = row.getCell(j);
                    assertNotNull(cell);

                    value = cell.getStringCellValue();
                    assertNotNull(value);
                    if (!exclude(cellsToExclude, options.getInitPosRow(), options.getInitPosCol(), i, j)) {
                        assertFalse(value.trim().isEmpty());
                    }
                }
            }
        } finally {
            tg.close();
        }
    }

    @ParameterizedTest
    @MethodSource("generateTableArguments")
    public void generateTableStyleAndAgg(final Workbook wb, final TableGenerator tg, final String sheetName,
            final TableOptions options, final Collection<?> objs, final String[] titles, final String[] fields,
            final Translator translator, final int[][] cellsToExclude) throws Exception {

        try {
            // Style for fields
            final CellStyle styleFields = wb.createCellStyle();
            styleFields.setBorderBottom(BorderStyle.DOUBLE);
            styleFields.setBorderTop(BorderStyle.DASH_DOT_DOT);
            styleFields.setBorderLeft(BorderStyle.MEDIUM_DASH_DOT_DOT);
            styleFields.setBorderRight(BorderStyle.SLANTED_DASH_DOT);
            options.setFieldsStyle(styleFields);

            // Style for titles
            final CellStyle styleTitles = wb.createCellStyle();
            styleTitles.cloneStyleFrom(styleFields);
            styleTitles.setRightBorderColor(IndexedColors.BLUE_GREY.getIndex());
            styleTitles.setLeftBorderColor(IndexedColors.BRIGHT_GREEN.getIndex());
            styleTitles.setTopBorderColor(IndexedColors.VIOLET.getIndex());
            styleTitles.setBottomBorderColor(IndexedColors.BROWN.getIndex());
            options.setTitlesStyle(styleTitles);

            options.setInitPosRow(5);
            options.setInitPosCol(5);
            options.setPrevailTitlesStyle(true);
            options.setAggregate(true);

            final Sheet sheet = tg.generateTable(sheetName, options, objs, titles, fields, translator);
            assertNotNull(tg.getWorkbook());

            Row row;
            Cell cell;
            String value;
            final int rowNum = options.getInitPosRow();
            final int colNum = options.getInitPosCol();
            final long countNonNull = objs.stream().filter((final Object o) -> o != null).count();
            for (int i = rowNum; i < rowNum + countNonNull + 1; i++) {
                row = sheet.getRow(i);
                assertNotNull(row);
                for (int j = colNum; j < colNum + titles.length; j++) {
                    cell = row.getCell(j);
                    assertNotNull(cell);
                    if (i == rowNum) {
                        assertBorder(styleTitles, cell.getCellStyle());
                        assertBorderColor(styleTitles, cell.getCellStyle());

                    } else if (i == rowNum + 1) {
                        assertEquals(styleTitles.getBottomBorderColor(), cell.getCellStyle().getTopBorderColor());
                        assertEquals(styleTitles.getBorderBottomEnum(), cell.getCellStyle().getBorderTopEnum());

                    } else {
                        assertBorder(styleFields, cell.getCellStyle());
                    }

                    value = cell.getStringCellValue();
                    assertNotNull(value);
                    if (!exclude(cellsToExclude, options.getInitPosRow(), options.getInitPosCol(), i, j)) {
                        assertFalse(value.trim().isEmpty());
                    }
                }
            }
        } finally {
            tg.close();
        }
    }

    @ParameterizedTest
    @MethodSource("generateTableArguments")
    public void generateTableStyleFields(final Workbook wb, final TableGenerator tg, final String sheetName,
            final TableOptions options, final Collection<?> objs, final String[] titles, final String[] fields,
            final Translator translator, final int[][] cellsToExclude) throws Exception {

        try {
            final CellStyle style = wb.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
            style.setBorderBottom(BorderStyle.DASH_DOT_DOT);
            style.setBorderTop(BorderStyle.DOTTED);
            style.setBorderLeft(BorderStyle.DOUBLE);
            style.setBorderRight(BorderStyle.THIN);
            options.setFieldsStyle(style);

            options.setInitPosRow(3);
            options.setInitPosCol(6);
            options.setPrevailTitlesStyle(false);

            final Sheet sheet = tg.generateTable(sheetName, options, objs, titles, fields, translator);
            assertNotNull(tg.getWorkbook());

            Row row;
            Cell cell;
            String value;
            final int rowNum = options.getInitPosRow();
            final int colNum = options.getInitPosCol();
            final long countNonNull = objs.stream().filter((final Object o) -> o != null).count();
            for (int i = rowNum + 1; i < rowNum + countNonNull + 1; i++) {
                row = sheet.getRow(i);
                assertNotNull(row);
                for (int j = colNum; j < colNum + titles.length; j++) {
                    cell = row.getCell(j);
                    assertNotNull(cell);
                    assertEquals(style.getVerticalAlignmentEnum(), cell.getCellStyle().getVerticalAlignmentEnum());
                    assertBorder(style, cell.getCellStyle());

                    value = cell.getStringCellValue();
                    assertNotNull(value);
                    if (!exclude(cellsToExclude, options.getInitPosRow(), options.getInitPosCol(), i, j)) {
                        assertFalse(value.trim().isEmpty());
                    }
                }
            }
        } finally {
            tg.close();
        }
    }

    @ParameterizedTest
    @MethodSource("getWorkbooks")
    public void generateTableWithNulls(final Workbook wb) throws Exception {

        final String sheetName = "sheetName";
        final TableOptions options = new TableOptions();
        final String[] titles = new String[] { "Name" };

        try (final TableGenerator tg = new TableGenerator(wb)) {

            final List<BasicInfo> objs1 = new ArrayList<>();
            assertTrue(objs1.add(null));
            assertTrue(objs1.size() == 1);
            assertTrue(objs1.add(new BasicInfo()));
            assertTrue(objs1.add(new BasicInfo("Apple")));
            assertTrue(objs1.add(new BasicInfo("bla bla bla")));
            final String[] fields = new String[] { "name" };

            // English to Zulu
            final String zuluApple = "i-apula";
            final TranslatorCollection translator = new TranslatorCollection();
            translator.setFieldsToMap(new HashSet<>());
            translator.getFieldsToMap().add("name");
            translator.setMap(new HashMap<>());
            translator.getMap().put("Apple", zuluApple);
            final TranslatorHelper transHelper = new TranslatorHelper(translator);

            // Should have only one cell fill
            final Sheet sheet = tg.generateTable(sheetName, options, objs1, titles, fields, transHelper);
            Row row = sheet.getRow(1);
            Cell cell = row.getCell(0);
            assertTrue(cell.getStringCellValue().trim().isEmpty());

            row = sheet.getRow(2);
            cell = row.getCell(0);
            assertEquals(zuluApple, cell.getStringCellValue());

            row = sheet.getRow(3);
            cell = row.getCell(0);
            assertTrue(cell.getStringCellValue().trim().isEmpty());
        }
    }

    static Stream<Arguments> prevailTitlesStyleArguments() {
        final Builder<Arguments> builder = Stream.builder();

        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            final TableOptions options = new TableOptions();

            // Style for fields
            final CellStyle styleFields = wb.createCellStyle();
            final BorderStyle border = BorderStyle.THIN;
            styleFields.setBorderBottom(border);
            styleFields.setBorderTop(border);
            styleFields.setBorderLeft(border);
            styleFields.setBorderRight(border);
            options.setFieldsStyle(styleFields);

            // Style for titles
            final CellStyle styleTitles = wb.createCellStyle();
            styleTitles.cloneStyleFrom(styleFields);
            styleTitles.setAlignment(HorizontalAlignment.CENTER);
            final short color = IndexedColors.AQUA.getIndex();
            styleTitles.setRightBorderColor(color);
            styleTitles.setLeftBorderColor(color);
            styleTitles.setTopBorderColor(color);
            styleTitles.setBottomBorderColor(color);
            options.setTitlesStyle(styleTitles);

            builder.add(Arguments.of(tg, options));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("prevailTitlesStyleArguments")
    public void prevailBottomBorderTitles(final TableGenerator tg, final TableOptions options) throws Exception {

        try {
            final Method prevailTitlesStyle = TableGenerator.class.getDeclaredMethod("prevailTitlesStyle",
                    TableOptions.class);
            prevailTitlesStyle.setAccessible(true);

            final CellStyle result = (CellStyle) prevailTitlesStyle.invoke(tg, options);

            assertEquals(options.getTitlesStyle().getBottomBorderColor(), result.getTopBorderColor());
            assertEquals(options.getTitlesStyle().getBorderBottomEnum(), result.getBorderTopEnum());
        } finally {
            tg.close();
        }
    }

    static Stream<Arguments> generateTitlesArguments() {
        final Builder<Arguments> builder = Stream.builder();
        final String[] titles = new String[] { "Kingdom", "Specie", "Weight", "Transport" };

        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            // Style
            final CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.MEDIUM);

            // Options
            final TableOptions options = new TableOptions();
            options.setTitlesStyle(style);
            options.setInitPosCol(2);
            options.setInitPosRow(4);

            final Sheet sheet = wb.createSheet("Titles Tests");
            final Row row = sheet.createRow(options.getInitPosRow());

            builder.add(Arguments.of(tg, row, options, titles));
        });

        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            // Style
            final CellStyle style = null;

            // Options
            final TableOptions options = new TableOptions();
            options.setTitlesStyle(style);
            options.setInitPosCol(2);
            options.setInitPosRow(4);

            final Sheet sheet = wb.createSheet("Titles Tests");
            final Row row = sheet.createRow(options.getInitPosRow());

            builder.add(Arguments.of(tg, row, options, titles));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("generateTitlesArguments")
    public void generateTitles(final TableGenerator tg, final Row row, final TableOptions options,
            final String[] titles) throws Exception {
        try {
            final Method generateTitles = TableGenerator.class.getDeclaredMethod("generateTitles", Row.class,
                    TableOptions.class, String[].class);
            generateTitles.setAccessible(true);

            generateTitles.invoke(tg, row, options, titles);

            assertNotNull(tg.getWorkbook());
            assertEquals(options.getInitPosRow(), row.getRowNum());
            assertEquals(options.getInitPosCol(), row.getFirstCellNum());
            assertEquals(row.getFirstCellNum() + titles.length, row.getLastCellNum());

            Cell cell;
            int i = 0;
            final Iterator<Cell> it = row.cellIterator();
            while (it.hasNext()) {
                cell = it.next();
                assertEquals(titles[i++], cell.getStringCellValue());
                if (options.getTitlesStyle() != null) {
                    assertEquals(options.getTitlesStyle(), cell.getCellStyle());
                }
            }
        } finally {
            tg.close();
        }
    }

    static Stream<Arguments> autoSizeColumnsArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Columns with width lesser than default
        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            final TableOptions options = new TableOptions();
            options.setAutoSize(true);

            final int size = 5;
            final Sheet sheet = wb.createSheet();
            final Row row = sheet.createRow(options.getInitPosRow());
            final int col = options.getInitPosCol();

            Cell cell;
            for (int i = col; i < col + size; i++) {
                cell = row.createCell(i);
                cell.setCellValue(String.valueOf(i));
            }

            builder.add(Arguments.of(tg, sheet, options, size, true));
        });

        // Columns with width greater than default
        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            final TableOptions options = new TableOptions();
            options.setAutoSize(true);

            final int size = 7;
            final Sheet sheet = wb.createSheet();
            final Row row = sheet.createRow(options.getInitPosRow());
            final int col = options.getInitPosCol();

            Cell cell;
            char[] chars;
            for (int i = col; i < col + size; i++) {
                cell = row.createCell(i);
                chars = new char[250];
                Arrays.fill(chars, 'z');
                cell.setCellValue(String.valueOf(chars));
            }

            builder.add(Arguments.of(tg, sheet, options, size, false));
        });

        // Auto size is false
        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            final TableOptions options = new TableOptions();
            options.setAutoSize(false);

            final int size = 20;
            final Sheet sheet = wb.createSheet();

            builder.add(Arguments.of(tg, sheet, options, size, false));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("autoSizeColumnsArguments")
    public void autoSizeColumns(final TableGenerator tg, final Sheet sheet, final TableOptions options,
            final int titlesSize, final boolean lesser) throws Exception {

        try {
            final Method autoSize = TableGenerator.class.getDeclaredMethod("autoSizeColumns", Sheet.class,
                    TableOptions.class, int.class);
            autoSize.setAccessible(true);

            final int[] original = getOrderedWidths(sheet, options.getInitPosCol(), titlesSize);

            // All the columns have the same size
            int init = original[0];
            for (int i = 0; i < original.length; i++) {
                assertEquals(init, original[i]);
            }

            autoSize.invoke(tg, sheet, options, titlesSize);
            final int[] result = getOrderedWidths(sheet, options.getInitPosCol(), titlesSize);
            assertNotNull(tg.getWorkbook());

            if (options.isAutoSize()) {

                // The arrays have the same size
                assertEquals(original.length, result.length);

                // All columns have the same size
                init = result[0];
                for (int i = 0; i < result.length; i++) {
                    assertEquals(init, result[i]);
                }

                // The result size is lesser or greater than the original
                for (int i = 0; i < original.length; i++) {
                    if (lesser) {
                        assertTrue(result[i] < original[i]);
                    } else {
                        assertTrue(result[i] > original[i]);
                    }
                }
            } else {
                assertArrayEquals(original, result);
            }
        } finally {
            tg.close();
        }

    }

    /**
     * Get widths
     *
     * @param sheet
     *            Object sheet
     * @param initCol
     *            initial position of a column
     * @param size
     *            size of the columns
     * @return An array with the width of the columns
     */
    private int[] getOrderedWidths(final Sheet sheet, final int initCol, final int size) {
        int j = 0;
        final int[] widths = new int[size];
        final int col = initCol;

        for (int i = col; i < col + size; i++) {
            widths[j++] = sheet.getColumnWidth(i);
        }
        return widths;
    }

    static Stream<Arguments> writeArguments() {
        final Builder<Arguments> builder = Stream.builder();

        getWorkbooks().forEach((final Workbook wb) -> {
            final TableGenerator tg = new TableGenerator(wb);

            final int size = 5;
            final Sheet sheet = wb.createSheet();
            final Row row = sheet.createRow(0);
            final int col = 0;

            Cell cell;
            for (int i = col; i < col + size; i++) {
                cell = row.createCell(i);
                cell.setCellValue(String.valueOf(i));
            }

            builder.add(Arguments.of(tg));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("writeArguments")
    public void writeInputStream(final TableGenerator tg) throws Exception {
        InputStream bais = null;
        assertNotNull(tg.getWorkbook());
        try {
            bais = tg.write();
            assertTrue(bais.available() > 50);
        } finally {
            bais.close();
            tg.close();
        }
    }

    @ParameterizedTest
    @MethodSource("writeArguments")
    public void writeOutputStream(final TableGenerator tg) throws Exception {
        OutputStream os = null;
        assertNotNull(tg.getWorkbook());
        final Path p = Paths
                .get(String.format(".%cwriteTest%s.xlsx", File.separatorChar, UUID.randomUUID().toString()));
        try {
            os = Files.newOutputStream(p);
            assertNotNull(tg.getWorkbook());
            assertTrue(Files.size(p) == 0L);
            tg.write(os);
            assertTrue(Files.size(p) > 50L);
        } finally {
            if (os != null) {
                os.close();
            }
            Files.delete(p);
            tg.close();

        }
    }

    private void assertBorder(final CellStyle a, final CellStyle b) {
        assertEquals(a.getBorderLeftEnum(), b.getBorderLeftEnum());
        assertEquals(a.getBorderRightEnum(), b.getBorderRightEnum());
        assertEquals(a.getBorderTopEnum(), b.getBorderTopEnum());
        assertEquals(a.getBorderBottomEnum(), b.getBorderBottomEnum());

    }

    private void assertBorderColor(final CellStyle a, final CellStyle b) {
        assertEquals(a.getLeftBorderColor(), b.getLeftBorderColor());
        assertEquals(a.getRightBorderColor(), b.getRightBorderColor());
        assertEquals(a.getTopBorderColor(), b.getTopBorderColor());
        assertEquals(a.getBottomBorderColor(), b.getBottomBorderColor());
    }

    private boolean exclude(final int[][] cellsToExclude, final int initX, final int initY, final int x, final int y) {
        boolean result = false;
        for (int i = 0; i < cellsToExclude.length; i++) {
            final int[] cells = cellsToExclude[i];
            if (cells[0] + initX == x && cells[1] + initY == y) {
                result = true;
                break;
            }
        }
        return result;
    }

}
