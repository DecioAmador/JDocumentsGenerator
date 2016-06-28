package com.github.decioamador.jdocsgen.table;

import java.io.Serializable;
import java.text.DateFormat;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Options of the {@link TableGenerator}
 * 
 * @author Décio Amador
 */
public class TableOptions implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * <b>Expected type:</b> Integer <br> 
	 * <b>Meaning:</b> The initial row of the table. E.g: 1 - second row <br> 
	 * <b>Default:</b> 0 - first row
	 * @since 1.0.0.0
	 */
	private int initPosRow = 0;
	
	/**
	 * <b>Expected type:</b> Integer <br> 
	 * <b>Meaning:</b> The initial column of the table. E.g: 2 - third column <br> 
	 * <b>Default:</b> 0 - first column
	 * @since 1.0.0.0
	 */
	private int initPosCol = 0;
	
	/**
	 * <b>Expected type:</b> Boolean <br> 
	 * <b>Meaning:</b> If columns will be auto size <br> 
	 * <b>Default:</b> false
	 * @since 1.0.0.0
	 */
	private boolean autosize = false;
	
	/**
	 * <b>Expected type:</b> org.apache.poi.ss.usermodel.CellStyle <br> 
	 * <b>Meaning:</b> Cell style applied to columns <br> 
	 * <b>Default:</b> none
	 * @since 1.1.0.0
	 */
	private CellStyle columnsStyle;
	
	/**
	 * <b>Expected type:</b> org.apache.poi.ss.usermodel.CellStyle <br> 
	 * <b>Meaning:</b> Cell style applied to fields <br> 
	 * <b>Default:</b> none
	 * @since 1.1.0.0
	 */
	private CellStyle fieldsStyle;
	
	public TableOptions(){
		super();
	}
	
	public TableOptions(int initPosRow, int initPosCol, boolean autosize, DateFormat dateFormat, CellStyle columnsStyle,
			CellStyle fieldsStyle) {
		super();
		this.initPosRow = initPosRow;
		this.initPosCol = initPosCol;
		this.autosize = autosize;
		this.columnsStyle = columnsStyle;
		this.fieldsStyle = fieldsStyle;
	}

	// GET'S and SET'S
	public int getInitPosRow() {
		return initPosRow;
	}
	public void setInitPosRow(int initPosRow) {
		this.initPosRow = initPosRow;
	}
	public int getInitPosCol() {
		return initPosCol;
	}
	public void setInitPosCol(int initPosCol) {
		this.initPosCol = initPosCol;
	}
	public boolean isAutosize() {
		return autosize;
	}
	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
	}
	public CellStyle getColumnsStyle() {
		return columnsStyle;
	}
	public void setColumnsStyle(CellStyle columnsStyle) {
		this.columnsStyle = columnsStyle;
	}
	public CellStyle getFieldsStyle() {
		return fieldsStyle;
	}
	public void setFieldsStyle(CellStyle fieldsStyle) {
		this.fieldsStyle = fieldsStyle;
	}
}