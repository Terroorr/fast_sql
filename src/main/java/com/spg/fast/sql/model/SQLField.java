package com.spg.fast.sql.model;

public class SQLField {
    private String name;
    private String value;

    public SQLField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SQLField(SQLField SQLField) {
        this(SQLField.getName(), SQLField.getValue());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
