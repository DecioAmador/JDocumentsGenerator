package com.github.decioamador.jdocsgen.text;

/**
 * Options of the {@link TextGenerator}
 */
public class TextOptions {

    /**
     * <b>Expected type:</b> {@link String} <br>
     * <b>Meaning:</b> Puts a string between the label and the value of the field
     * <br>
     * E.g: Uuid: 9ca2e341-a4c5-485e-ba68-c90fafb13f27 <br>
     * <b>Default:</b> ": "
     */
    private String betweenLabelAndField = ": ";

    /**
     * @return the string between label and field
     * @see TextOptions#betweenLabelAndField
     */
    public String getBetweenLabelAndField() {
	return betweenLabelAndField;
    }

    /**
     * @param betweenLabelAndField
     *            the string between label and field
     * @see TextOptions#betweenLabelAndField
     */
    public void setBetweenLabelAndField(final String betweenLabelAndField) {
	this.betweenLabelAndField = betweenLabelAndField;
    }

}