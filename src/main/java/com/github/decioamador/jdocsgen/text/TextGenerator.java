package com.github.decioamador.jdocsgen.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorUtils;
import com.github.decioamador.jdocsgen.utils.FieldResolution;

/**
 * This class has the purpose of generating text based document
 */
public class TextGenerator implements AutoCloseable {

    private final XWPFDocument document;

    /**
     * Creates a {@linkplain TextGenerator} by using a specified instance of a
     * {@link XWPFDocument}
     *
     * @param document
     *            High(ish) level representation for working with .docx files
     */
    public TextGenerator(final XWPFDocument document) {
        this.document = Objects.requireNonNull(document);
    }

    /**
     * Create a new {@linkplain XWPFParagraph} for the wrapped
     * {@linkplain XWPFDocument}. Generates a paragraph for that the object will be
     * represented in each line is a field and the labels will be put behind the
     * values of the fields. Return the paragraph created. <br>
     * The fields and labels should have the same order.
     *
     * @param obj
     *            Objects that will represent a paragraph in the text
     * @param options
     *            Options to generate this text
     * @param labels
     *            TAn array that each element is the label for a line
     * @param fields
     *            An array that each element is an EL path for a field
     * @param translator
     *            Gives the user an opportunity to change the value of a field by
     *            another (internationalization, format currency, format dates)
     * @return A {@linkplain XWPFParagraph} that contains the information of the
     *         object
     */
    public XWPFParagraph generateParagraph(final Object obj, final TextOptions options, final String[] labels,
            final String[] fields, final Translator translator) {

        final XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run;

        Object o;
        String field;
        String translated;
        final String sep = options.getSeperatorAgg();
        final boolean agg = options.isAggregate();

        if (obj != null) {
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];

                // Resolve field
                if (agg) {
                    o = FieldResolution.resolveFieldAggregation(obj, field);
                } else {
                    o = FieldResolution.resolveField(obj, field);
                }

                // Put label
                run = paragraph.createRun();
                run.setText(labels[i]);

                // Put a string between the label and the field
                run = paragraph.createRun();
                run.setText(options.getBetweenLabelAndField());

                // Put field representation
                if (o != null) {
                    translated = TranslatorUtils.translateObject(agg, sep, translator, o, field);

                    if (translated != null) {
                        run = paragraph.createRun();
                        run.setText(translated);
                    }
                }
                run.addBreak();
            }
        }

        return paragraph;
    }

    /**
     * Create a new {@linkplain XWPFTable} for the wrapped
     * {@linkplain XWPFDocument}. Generates a table that the objects will be
     * represented in each line and the titles will be put in the line above. Return
     * the table created.
     *
     * @param objs
     *            Objects that will represent the lines of the table
     * @param options
     *            Options to generate this table
     * @param titles
     *            An array that each element is the title for a column
     * @param fields
     *            An array that each element is an EL path for a field
     * @param translator
     *            Gives the user an opportunity to change the value of a field by
     *            another (internationalization, format currency, format dates)
     * @return A {@linkplain XWPFTable} that contains the information in a table
     */
    public XWPFTable generateTable(final Collection<?> objs, final TextOptions options, final String[] titles,
            final String[] fields, final Translator translator) {

        final boolean agg = options.isAggregate();
        final String sep = options.getSeperatorAgg();
        String translated;
        String field;
        int rowNum = 0;
        XWPFTableCell cell;
        Object o;

        final int countNonNullObjs = (int) objs.stream().filter(Objects::nonNull).count();
        final XWPFTable table = document.createTable(countNonNullObjs + 1, titles.length);

        // Put titles
        XWPFTableRow row = table.getRow(rowNum++);
        for (int i = 0; i < titles.length; i++) {
            cell = row.getCell(i);
            cell.setText(titles[i]);
        }

        // Put fields
        for (final Object obj : objs) {
            if (obj != null) {
                row = table.getRow(rowNum++);

                for (int i = 0; i < fields.length; i++) {
                    field = fields[i];
                    cell = row.getCell(i);

                    // Resolve field
                    if (agg) {
                        o = FieldResolution.resolveFieldAggregation(obj, field);
                    } else {
                        o = FieldResolution.resolveField(obj, field);
                    }

                    // Put field representation
                    if (o != null) {
                        translated = TranslatorUtils.translateObject(agg, sep, translator, o, field);

                        if (translated != null) {
                            cell.setText(translated);
                        }
                    }
                }
            }
        }

        return table;
    }

    /**
     * Writes the generated document on the stream
     *
     * @return ByteArrayInputStream it was the content of the file generated
     * @throws IOException
     *             If an I/O error occurs. In particular, an IOException may be
     *             thrown if the output stream has been closed.
     */
    public ByteArrayInputStream write() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        document.write(bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * Writes the generated document on the stream, writing on the existing one
     *
     * @param outputStream
     *            OutputStream that will have the content
     * @throws IOException
     *             If an I/O error occurs. In particular, an IOException may be
     *             thrown if the output stream has been closed.
     */
    public void write(final OutputStream outputStream) throws IOException {
        document.write(outputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        document.close();
    }

    /**
     * XWPFDocument that is being used by the {@link TextGenerator}
     *
     * @return text base document being used
     */
    public XWPFDocument getDocument() {
        return document;
    }

}
