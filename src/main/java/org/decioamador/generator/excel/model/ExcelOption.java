package org.decioamador.generator.excel.model;

/**
 * @author D&eacute;cio Amador
 */
public enum ExcelOption {

	/**
	 * Expected type: String
	 * Meaning: Format of the cells that have dates
	 * Default: yyyy-MM-dd hh:mm:ss
	 */
	DATE_FORMAT,
	
	/**
	 * Expected type: Integer
	 * Meaning: The initial position of the table (e.g. 0 -> (0,0))
	 * Default: 0
	 */
	INICIAL_POSITION,
	
	/**
	 * Expected type: Boolean
	 * Meaning: If columns will be auto size
	 * Default: false
	 */
	AUTOSIZE_COLUMNS
}
