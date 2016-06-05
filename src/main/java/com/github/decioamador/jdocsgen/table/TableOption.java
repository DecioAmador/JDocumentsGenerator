package com.github.decioamador.jdocsgen.table;

/**
 * Options of the {@link TableGenerator}
 */
public enum TableOption {

	/**
	 * <b>Expected type:</b> String <br> 
	 * <b>Meaning:</b> Format of the cells that have dates <br> 
	 * <b>Default:</b> yyyy-MM-dd
	 * @since 1.0.0.0
	 */
	DATE_FORMAT,
	
	/**
	 * <b>Expected type:</b> Integer <br> 
	 * <b>Meaning:</b> The initial row of the table. E.g: 1 - second column <br> 
	 * <b>Default:</b> 0 - first row
	 * @since 1.0.0.0
	 */
	INICIAL_POSITION_ROW,
	
	/**
	 * <b>Expected type:</b> Integer <br> 
	 * <b>Meaning:</b> The initial column of the table. E.g: 2 - third row <br> 
	 * <b>Default:</b> 0 - first column
	 * @since 1.0.0.0
	 */
	INICIAL_POSITION_COLUMN,
	
	/**
	 * <b>Expected type:</b> Boolean <br> 
	 * <b>Meaning:</b> If columns will be auto size <br> 
	 * <b>Default:</b> false
	 * @since 1.0.0.0
	 */
	AUTOSIZE_COLUMNS,
	
	/**
	 * <b>Expected type:</b> String <br> 
	 * <b>Meaning:</b> Puts a prefix when search on properties <br> 
	 * <b>Default:</b> It doesn't append anything
	 * @since 1.0.0.0
	 */
	PREFIX_PROPERTIES,
	
	/**
	 * <b>Expected type:</b> String <br> 
	 * <b>Meaning:</b> Puts a prefix when search on resource bundle <br> 
	 * <b>Default:</b> It doesn't append anything
	 * @since 1.0.0.0
	 */
	PREFIX_RESOURCE_BUNDLE
}