package com.pavmoroz.fast.sql.model;

import java.util.ArrayList;

public class Row {
    private String operator;
    private String tableName;
    private ArrayList<Field> fields = new ArrayList<>();

    public Row(String operator, String tableName) {
        this.operator = operator;
        this.tableName = tableName;
    }

    public Row(Row baseRow) {
        this.operator = baseRow.getOperator();
        this.tableName = baseRow.getTableName();
        for (Field field : baseRow.getFields()){
            fields.add(new Field(field));
        }
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public String getFieldValue(String name) {
        if(name.equals("tableName")){
            return getTableName();
        }
        for (Field field : this.getFields()){
            if(field.getName().equals(name)) return field.getValue();
        }
        return null;
    }

    public void addField(String name, String value) {
        if (containsField(name)){
            setFieldValue(name, value);
        } else {
            this.fields.add(new Field(name, value));
        }
    }

    public void setFieldValue(String name, String value) {
        if(name.equals("tableName")){
            setTableName(value);
            return;
        }
        for (Field field : this.getFields()){
            if(field.getName().equals(name))
                field.setValue(value);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean containsField(String fieldName) {
        for (Field field : this.getFields()){
            if(fieldName.equals(field.getName())){
                return true;
            }
        }
        return false;
    }
}
