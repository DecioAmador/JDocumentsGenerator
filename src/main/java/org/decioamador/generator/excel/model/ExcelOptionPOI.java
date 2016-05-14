package org.decioamador.generator.excel.model;

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
	 * <b>Expected type:</b> Point <br /> 
	 * <b>Meaning:</b> The initial position of the table <br /> 
	 * &nbsp;&nbsp;&nbsp;&nbsp;E.g: (2,1) - (third row, second column) <br /> 
	 * <b>Default:</b> (0,0) - (first row, first column)
	 */
	INICIAL_POSITION,
	
	/**
	 * <b>Expected type:</b> Boolean <br /> 
	 * <b>Meaning:</b> If columns will be auto size <br /> 
	 * <b>Default:</b> false
	 */
	AUTOSIZE_COLUMNS
}