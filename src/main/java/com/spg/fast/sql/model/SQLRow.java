package com.spg.fast.sql.model;

import java.util.ArrayList;

public class SQLRow {
    private String operator;
    private String tableName;
    private ArrayList<SQLField> SQLFields = new ArrayList<>();

    public SQLRow(String operator, String tableName) {
        this.operator = operator;
        this.tableName = tableName;
    }

    public SQLRow(SQLRow baseSQLRow) {
        this(baseSQLRow.getOperator(), baseSQLRow.getTableName());
        baseSQLRow.getSQLFields().forEach(SQLField -> SQLFields.add(new SQLField(SQLField)));
    }

    public String getOperator() {
        return operator;
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<SQLField> getSQLFields() {
        return SQLFields;
    }

    public void addField(String name, String value, FieldType type) {
        if (containsField(name)) {
            setFieldValue(name, value);
        } else {
            this.SQLFields.add(new SQLField(name, value, type));
        }
    }

    public String getFieldValue(String name) {
        return this.getSQLFields().stream()
                .filter(SQLField -> SQLField.getName().equals(name)).findFirst()
                .map(SQLField::getValue)
                .orElse(null);
    }

    public void setFieldValue(String name, String value) {
        this.getSQLFields().stream()
                .filter(SQLField -> SQLField.getName().equals(name))
                .forEachOrdered(SQLField -> SQLField.setValue(value));
    }

    public boolean containsField(String fieldName) {
        return this.getSQLFields().stream()
                .anyMatch(SQLField -> fieldName.equals(SQLField.getName()));
    }
}
