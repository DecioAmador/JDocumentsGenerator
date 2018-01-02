package com.github.decioamador.jdocsgen.text;

import java.util.Collection;

/**
 * Options of the {@link TextGenerator}
 */
public class TextOptions {

    private String betweenLabelAndField = ": ";

    private boolean aggregate = false;
    private String seperatorAgg = ", ";

    /**
     * <b>Meaning:</b> Puts a string between the label and the value of the field
     * <br>
     * E.g: if you have the String {@literal ": "} and the label Uuid, you will
     * get<br>
     * Uuid: 9ca2e341-a4c5-485e-ba68-c90fafb13f27 <br>
     * <b>Default:</b> {@literal ": "} @ return The String between the label and the
     * field
     *
     * @return The String that will be put between the label and the field
     */
    public String getBetweenLabelAndField() {
        return betweenLabelAndField;
    }

    /**
     * <b>Meaning:</b> Puts a string between the label and the value of the field
     * <br>
     * E.g: if you have the String {@literal ": "} and the label Uuid, you will
     * get<br>
     * Uuid: 9ca2e341-a4c5-485e-ba68-c90fafb13f27 <br>
     * <b>Default:</b> {@literal ": "} @ return The String between the label and the
     * field
     *
     * @param betweenLabelAndField
     *            The String that will be put between the label and the field
     */
    public void setBetweenLabelAndField(final String betweenLabelAndField) {
        this.betweenLabelAndField = betweenLabelAndField;
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

}
