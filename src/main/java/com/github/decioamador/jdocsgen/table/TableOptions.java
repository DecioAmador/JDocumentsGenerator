package com.github.decioamador.jdocsgen.table;

import java.util.Collection;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Options of the {@link TableGenerator}
 */
public class TableOptions {

    private int initPosRow = 0;
    private int initPosCol = 0;

    private boolean autoSize = false;
    private boolean prevailTitlesStyle = true;

    private boolean aggregate = false;
    private String seperatorAgg = ", ";

    private CellStyle titlesStyle;
    private CellStyle fieldsStyle;

    /**
     * <b>Meaning:</b> The initial row of the table. E.g: 1 - second row <br>
     * <b>Default:</b> {@literal 0} - first row
     *
     * @return The position of the first row
     */
    public int getInitPosRow() {
        return initPosRow;
    }

    /**
     * <b>Meaning:</b> The initial row of the table. E.g: 1 - second row <br>
     * <b>Default:</b> {@literal 0} - first row
     *
     * @param initPosRow
     *            The position of the first row
     */
    public void setInitPosRow(final int initPosRow) {
        this.initPosRow = initPosRow;
    }

    /**
     * <b>Meaning:</b> The initial column of the table. E.g: 2 - third column <br>
     * <b>Default:</b> {@literal 0} - first column
     *
     * @return The position of the first column
     */
    public int getInitPosCol() {
        return initPosCol;
    }

    /**
     * <b>Meaning:</b> The initial column of the table. E.g: 2 - third column <br>
     * <b>Default:</b> {@literal 0} - first column
     *
     * @param initPosCol
     *            The position of the first column
     */
    public void setInitPosCol(final int initPosCol) {
        this.initPosCol = initPosCol;
    }

    /**
     * <b>Meaning:</b> If columns will be auto size <br>
     * <b>Default:</b> {@literal false} <br>
     * <b>Note:</b> This operation might be slow on large tables
     *
     * @return Returns true if is pretended that columns are auto size and false
     *         otherwise
     */
    public boolean isAutoSize() {
        return autoSize;
    }

    /**
     * <b>Meaning:</b> If columns will be auto size <br>
     * <b>Default:</b> {@literal false} <br>
     * <b>Note:</b> This operation might be slow on large tables
     *
     * @param autoSize
     *            Returns true if is pretended that columns are auto size and false
     *            otherwise
     */
    public void setAutoSize(final boolean autoSize) {
        this.autoSize = autoSize;
    }

    /**
     * <b>Meaning:</b> Cell style applied to titles <br>
     * <b>Default:</b> {@literal null}
     *
     * @return The cell style used on titles
     */
    public CellStyle getTitlesStyle() {
        return titlesStyle;
    }

    /**
     * <b>Meaning:</b> Cell style applied to titles <br>
     * <b>Default:</b> {@literal null}
     *
     * @param titlesStyle
     *            The cell style used on titles
     */
    public void setTitlesStyle(final CellStyle titlesStyle) {
        this.titlesStyle = titlesStyle;
    }

    /**
     * <b>Meaning:</b> Cell style applied to fields <br>
     * <b>Default:</b> {@literal null}
     *
     * @return The cell style being used on fields
     */
    public CellStyle getFieldsStyle() {
        return fieldsStyle;
    }

    /**
     * <b>Meaning:</b> Cell style applied to fields <br>
     * <b>Default:</b> {@literal null}
     *
     * @param fieldsStyle
     *            The cell style being used on fields
     */
    public void setFieldsStyle(final CellStyle fieldsStyle) {
        this.fieldsStyle = fieldsStyle;
    }

    /**
     * <b>Meaning:</b> Aggregate values in case they pass through several
     * {@link Collection} or Arrays <br>
     * <b>Default:</b> {@literal false}
     *
     * @param aggregate
     *            Set to true if you want to aggregate the values when finds a
     *            {@linkplain Collection} or an array or set to false if you don't
     *            that need
     */
    public void setAggregate(final boolean aggregate) {
        this.aggregate = aggregate;
    }

    /**
     * <b>Meaning:</b> Aggregate values in case they pass through several
     * {@link Collection} or Arrays <br>
     * <b>Default:</b> {@literal false}
     *
     * @return Return true if you want to aggregate the values when finds a
     *         {@linkplain Collection} or an array or false otherwise
     */
    public boolean isAggregate() {
        return aggregate;
    }

    /**
     * <b>Meaning:</b> The result of aggregating will be separated by it<br>
     * <b>Default:</b> {@literal ", "}
     *
     * @return The string that will separate each element of a group of elements
     */
    public String getSeperatorAgg() {
        return seperatorAgg;
    }

    /**
     * <b>Meaning:</b> The result of aggregating will be separated by it<br>
     * <b>Default:</b> {@literal ", "}
     *
     * @param seperatorAgg
     *            The string that will separate each element of a group of elements
     */
    public void setSeperatorAgg(final String seperatorAgg) {
        this.seperatorAgg = seperatorAgg;
    }

    /**
     * <b>Meaning:</b> The bottom border of the titles will have the style of the
     * titles instead of the top border of the fields<br>
     * <b>Default:</b> {@literal true}<br>
     * <b>Note:</b> Most templates do that
     *
     * @return Return true if you want the style of titles to prevail or false
     *         otherwise
     */
    public boolean isPrevailTitlesStyle() {
        return prevailTitlesStyle;
    }

    /**
     * <b>Meaning:</b> The bottom border of the titles will have the style of the
     * titles instead of the top border of the fields<br>
     * <b>Default:</b> {@literal true}<br>
     * <b>Note:</b> Most templates do that
     *
     * @param prevailTitlesStyle
     *            Set true if you want the style of titles to prevail or false
     *            otherwise
     */
    public void setPrevailTitlesStyle(final boolean prevailTitlesStyle) {
        this.prevailTitlesStyle = prevailTitlesStyle;
    }

}
