package com.github.decioamador.jdocsgen.test.table;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.github.decioamador.jdocsgen.table.TableGeneratorJ;
import com.github.decioamador.jdocsgen.test.Model1;

public class TableGeneratorJTest {

	public static void main(String[] args) throws Exception {
		
		File file = new File("xmlTest.jrxml");
		
		final List<String> fields = Arrays.asList(
				"guid","date","id","name");
//				,"Model2.Guid","Model2.Label",
//				"Model2.Model3.Guid","Model2.Model3.Number");
		
		TableGeneratorJ.xmlGenerate(file,Model1.class,fields);
	}

}