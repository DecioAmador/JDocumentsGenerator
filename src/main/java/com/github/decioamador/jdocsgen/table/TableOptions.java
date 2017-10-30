package com.github.decioamador.jdocsgen.table;

import java.util.Collection;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Options of the {@link TableGenerator}
 */
public class TableOptions {

    /**
     * <b>Expected type:</b> {@link Integer} <br>
     * <b>Meaning:</b> The initial row of the table. E.g: 1 - second row <br>
     * <b>Default:</b> 0 - first row
     */
    private int initPosRow = 0;

    /**
     * <b>Expected type:</b> {@link Integer} <br>
     * <b>Meaning:</b> The initial column of the table. E.g: 2 - third column <br>
     * <b>Default:</b> 0 - first column
     */
    private int initPosCol = 0;

    /**
     * <b>Expected type:</b> {@link Boolean} <br>
     * <b>Meaning:</b> If columns will be auto size <br>
     * <b>Default:</b> false <b>Note:</b> This operation might be slow on large
     * tables
     */
    private boolean autosize = false;

    /**
     * <b>Expected type:</b> {@link CellStyle} <br>
     * <b>Meaning:</b> Cell style applied to titles <br>
     * <b>Default:</b> null
     */
    private CellStyle titlesStyle;

    /**
     * <b>Expected type:</b> {@link CellStyle} <br>
     * <b>Meaning:</b> Cell style applied to fields <br>
     * <b>Default:</b> null
     */
    private CellStyle fieldsStyle;

    /**
     * <b>Expected type:</b> {@link boolean} <br>
     * <b>Meaning:</b> Aggregate values in case they pass through several
     * {@link Collection} or Arrays <br>
     * <b>Default:</b> true
     */
    private boolean aggregate = true;

    /**
     * <b>Expected type:</b> {@link String} <br>
     * <b>Meaning:</b> The result of aggregating will be separated by it<br>
     * <b>Default:</b> ', '
     */
    private String seperatorAgg = ", ";

    /**
     * @return the initial position of the row
     * @see TableOptions#initPosRow
     */
    public int getInitPosRow() {
	return initPosRow;
    }

    /**
     * @param initPosRow
     *            the initial position of the row
     * @see TableOptions#initPosRow
     */
    public void setInitPosRow(final int initPosRow) {
	this.initPosRow = initPosRow;
    }

    /**
     * @return the initial position of the column
     * @see TableOptions#initPosCol
     */
    public int getInitPosCol() {
	return initPosCol;
    }

    /**
     * @param initPosCol
     *            the initial position of the column
     * @see TableOptions#initPosCol
     */
    public void setInitPosCol(final int initPosCol) {
	this.initPosCol = initPosCol;
    }

    /**
     * @return if the columns will be auto size
     * @see TableOptions#autosize
     */
    public boolean isAutosize() {
	return autosize;
    }

    /**
     * @param autosize
     *            if the columns will be autosize
     * @see TableOptions#autosize
     */
    public void setAutosize(final boolean autosize) {
	this.autosize = autosize;
    }

    /**
     * @return the cell style being used on the titles
     * @see TableOptions#titlesStyle
     */
    public CellStyle getTitlesStyle() {
	return titlesStyle;
    }

    /**
     * @param titlesStyle
     *            the cell style being used on the titles
     * @see TableOptions#titlesStyle
     */
    public void setTitlesStyle(final CellStyle titlesStyle) {
	this.titlesStyle = titlesStyle;
    }

    /**
     * @return the cell style being used on the rest of cells
     * @see TableOptions#fieldsStyle
     */
    public CellStyle getFieldsStyle() {
	return fieldsStyle;
    }

    /**
     * @param fieldsStyle
     *            the cell style being used on the rest of the cells
     * @see TableOptions#fieldsStyle
     */
    public void setFieldsStyle(final CellStyle fieldsStyle) {
	this.fieldsStyle = fieldsStyle;
    }

    /**
     * @param aggregate
     *            wherever if aggregates values
     * @see TableOptions#aggregate
     */
    public void setAggregate(final boolean aggregate) {
	this.aggregate = aggregate;
    }

    /**
     * @return wherever if aggregates values
     * @see TableOptions#aggregate
     */
    public boolean isAggregate() {
	return aggregate;
    }

    /**
     * @return The result of aggregating will be separated by it
     * @see TableOptions#seperatorAgg
     */
    public String getSeperatorAgg() {
	return seperatorAgg;
    }

    /**
     * @param seperatorAgg
     *            The result of aggregating will be separated by it
     * @see TableOptions#seperatorAgg
     */
    public void setSeperatorAgg(final String seperatorAgg) {
	this.seperatorAgg = seperatorAgg;
    }

}