package com.spg.fast.sql.model;

public class SQLField {
    private String name;
    private String value;
    private FieldType type;

    public SQLField(String name, String value, FieldType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public SQLField(SQLField SQLField) {
        this(SQLField.getName(), SQLField.getValue(), SQLField.getType());
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

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}
