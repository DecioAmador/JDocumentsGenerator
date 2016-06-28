package com.github.decioamador.jdocsgen.text;

import java.io.Serializable;

/**
 * Options of the {@link TextGenerator}
 * <br>
 * <br>Copyright 2016 Décio Amador <br>
 * <br>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License. <br>
 * You may obtain a copy of the License at <br>
 * <br>
 *     http://www.apache.org/licenses/LICENSE-2.0 <br>
 * <br>
 * Unless required by applicable law or agreed to in writing, software <br>
 * distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License. <br>
 * @author Décio Amador
 */
public class TextOptions implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * <b>Meaning:</b> Puts a string between the label and the value of the field <br> 
	 * E.g: Guid: 9ca2e341-a4c5-485e-ba68-c90fafb13f27 <br>
	 * <b>Default:</b> ": " 
	 * @since 1.1.0.0
	 */
	private String betweenLabelAndField = ": ";

	// GET'S and SET'S
	public String getBetweenLabelAndField() {
		return betweenLabelAndField;
	}
	public void setBetweenLabelAndField(String betweenLabelAndField) {
		this.betweenLabelAndField = betweenLabelAndField;
	}
}
