JDocumentsGenerator
=======
The objective is to generate documents.
-----------

One of the best ways to see how it works is to look at the demonstrations below.

### Table based documents
- [TableGenerator](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/main/java/com/github/decioamador/jdocsgen/table/TableGenerator.java)
- [TableOptions](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/main/java/com/github/decioamador/jdocsgen/table/TableOptions.java)

[Demonstration](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/test/java/com/github/decioamador/jdocsgen/table/TableGeneratorDemo.java)

---

### Text based documents
- [TextGenerator](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/main/java/com/github/decioamador/jdocsgen/text/TextGenerator.java)
- [TextOptions](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/main/java/com/github/decioamador/jdocsgen/text/TextOptions.java)

[Demonstration](https://github.com/DecioAmador/JDocumentsGenerator/blob/master/src/test/java/com/github/decioamador/jdocsgen/text/TextGeneratorDemo.java)

---

### Versions:

#### 2.1.1
 - Aggregate is true by default
 - TranslatorHelper now uses distinct

#### 2.1.0
 - Support of collections and arrays in the field resolution (doesn't support primitive arrays)

#### 2.0.0
 - Insert text based documents
 - Significant changes of the existing code in the table based document
   