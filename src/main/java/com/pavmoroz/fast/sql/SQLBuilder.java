package com.pavmoroz.fast.sql;

import com.pavmoroz.fast.sql.model.*;
import com.pavmoroz.fast.sql.parser.InputDataJSONParser;
import com.pavmoroz.fast.sql.parser.InputDataTXTParser;
import com.pavmoroz.fast.sql.parser.TablesTemplateJSONParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class SQLBuilder {

    private static final Logger log = Logger.getLogger(SQLBuilder.class);

    private DataInput dataInput;
    private Document document;

    public SQLBuilder() {
    }

    public void init(String fileName) {
        if (fileName.endsWith(".json")) {
            InputDataJSONParser inputDataJSONParser = new InputDataJSONParser();
            dataInput = inputDataJSONParser.parse(fileName);
        } else if (fileName.endsWith(".txt")) {
            InputDataTXTParser inputDataTXTParser = new InputDataTXTParser();
            dataInput = inputDataTXTParser.parse(fileName);
        } else {
            throw new RuntimeException("Wrong file format!");
        }

        TablesTemplateJSONParser tablesTemplateJSONParser = new TablesTemplateJSONParser();
        document = tablesTemplateJSONParser.parse(dataInput.getTablesTemplateFileName());
    }

    public void build() {
        document.setRows(buildSqlRows(document.getRows(), dataInput.getDataFields()));
        document.setRows(addPatternRows(document.getRows()));
        formatDocument();
        saveDocument();
    }

    private void formatDocument() {
        document.formatDocument();
    }

    private void saveDocument() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(Const.StartCommentsDocument,
                dataInput.getSqlFileSettings().getAuthor(), dataInput.getSqlFileSettings().getRelease(),
                dataInput.getSqlFileSettings().getTicketType(), dataInput.getSqlFileSettings().getTicketNumber(),
                dataInput.getSqlFileSettings().getDescription(), dataInput.getSqlFileSettings().getDb()));

        document.getRows().forEach(row -> {
            sb.append("\n");
            if (Const.DELETE.equals(row.getOperator())) {
                StringBuilder param = new StringBuilder();
                row.getFields().forEach(field -> {
                    if (param.length() > 0) {
                        param.append(" AND ");
                    }
                    param.append(field.getName())
                            .append(" = ")
                            .append(field.getValue());
                });
                sb.append(String.format(Const.DeletePattern, row.getTableName(), param));
            }
            if (Const.INSERT.equals(row.getOperator())) {
                StringBuilder fields = new StringBuilder();
                StringBuilder values = new StringBuilder();
                row.getFields().forEach(field -> {
                    if (fields.length() > 0) {
                        fields.append(", ");
                    }
                    if (values.length() > 0) {
                        values.append(", ");
                    }
                    fields.append(field.getName());
                    values.append(field.getValue());
                });
                sb.append(String.format(Const.InsertPattern, row.getTableName(), fields, values));
            }
            if (Const.COMMENT.equals(row.getOperator())) {
                sb.append(row.getFieldValue(Const.COMMENT));
            }
        });

        System.out.println(sb);

        File f = new File(String.format(Const.OutFileNamePattern,
                dataInput.getSqlFileSettings().getProject(), dataInput.getSqlFileSettings().getDb().toLowerCase(),
                dataInput.getSqlFileSettings().getTicketType(), dataInput.getSqlFileSettings().getTicketNumber(),
                dataInput.getSqlFileSettings().getDescriptionInFileName(),
                dataInput.getSqlFileSettings().getRelease().replace(".", "")));
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException ex) {
            log.info(ex.getMessage(), ex);
        }
    }

    private ArrayList<Row> buildSqlRows(ArrayList<Row> rows, Set<DataField> dataFields) {
        for (DataField dataField : dataFields) {
            ArrayList<Row> newRows = new ArrayList<>();
            for (Row row : rows) {
                newRows.addAll(buildRowsByField(row, dataField));
            }
            rows = newRows;
        }
        return rows;
    }

    private ArrayList<Row> buildRowsByField(Row row, DataField dataField) {
        ArrayList<Row> newRows = new ArrayList<>();
        if (!row.containsField(dataField.getName())){
            newRows.add(row);
            return newRows;
        }
        dataField.getValueGroups().forEach(valueGroup -> {
            Row newRow = new Row(row);
            newRow.addField(dataField.getName(), valueGroup.getValue());
            if (CollectionUtils.isEmpty(valueGroup.getDataFields())){
                newRows.add(newRow);
            } else {
                ArrayList<Row> currentRows = new ArrayList<>();
                currentRows.add(newRow);
                newRows.addAll(buildSqlRows(currentRows, valueGroup.getDataFields()));
            }
        });
        return newRows;
    }

    private ArrayList<Row> addPatternRows(ArrayList<Row> rows) {
        for (PatternFields s : PatternFields.values()){
            ArrayList<Row> newRows = new ArrayList<>();
            for (Row row : rows) {
                newRows.addAll(addPatternRows(row, s.getDataField()));
            }
            rows = newRows;
        }
        return rows;
    }

    private ArrayList<Row> addPatternRows(Row row, DataField dataField) {
        ArrayList<Row> newRows = new ArrayList<>();
        if (!row.containsField(dataField.getName())){
            newRows.add(row);
            return newRows;
        }
        dataField.getValueGroups().forEach(valueGroup -> {
            if (row.getFieldValue(dataField.getName()).equals(valueGroup.getValue())) {
                if (CollectionUtils.isNotEmpty(valueGroup.getDataFields())) {
                    valueGroup.getDataFields().forEach(dependentField -> {
                        if (row.containsField(dependentField.getName()) && row.getFieldValue(dependentField.getName()) == null) {
                            dependentField.getValueGroups().forEach(dependentVG -> {
                                Row newRow = new Row(row);
                                newRow.setFieldValue(dependentField.getName(), dependentVG.getValue());
                                newRows.add(newRow);
                            });
                        }
                    });
                }
            }
        });
        if (CollectionUtils.isEmpty(newRows)){
            newRows.add(row);
        }
        return newRows;
    }
}
