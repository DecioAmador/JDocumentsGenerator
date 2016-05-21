package org.jmsoffice.generator.excel.model;

/**
 * Options of the ExcelGeneratorPOI
 * @author D&eacute;cio Amador
 */
public enum ExcelOptionPOI {

	/**
	 * <b>Expected type:</b> String <br /> 
	 * <b>Meaning:</b> Format of the cells that have dates <br /> 
	 * <b>Default:</b> yyyy-MM-dd
	 */
	DATE_FORMAT,
	
	/**
	 * <b>Expected type:</b> Integer <br /> 
	 * <b>Meaning:</b> The initial row of the table <br /> 
	 * &nbsp;&nbsp;&nbsp;&nbsp;E.g: 1 -> second column <br /> 
	 * <b>Default:</b> 0 -> first row
	 */
	INICIAL_POSITION_ROW,
	
	/**
	 * <b>Expected type:</b> Integer <br /> 
	 * <b>Meaning:</b> The initial column of the table  <br /> 
	 * &nbsp;&nbsp;&nbsp;&nbsp;E.g: 2 -> third row <br /> 
	 * <b>Default:</b> 0 -> first column
	 */
	INICIAL_POSITION_COLUMN,
	
	/**
	 * <b>Expected type:</b> Boolean <br /> 
	 * <b>Meaning:</b> If columns will be auto size <br /> 
	 * <b>Default:</b> false
	 */
	AUTOSIZE_COLUMNS
}