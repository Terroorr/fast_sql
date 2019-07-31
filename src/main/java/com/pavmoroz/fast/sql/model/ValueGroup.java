package com.pavmoroz.fast.sql.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ValueGroup {

    private String value;
    private Set<DataField> dataFields;

    private ValueGroup() {

    }

    public String getValue() {
        return value;
    }

    public Set<DataField> getDataFields() {
        return dataFields;
    }

    public static Builder newBuilder() {
        return new ValueGroup().new Builder();
    }

    public void addDataField(DataField dataField) {
        if (dataFields == null){
            dataFields = new HashSet<>();
            dataFields.add(dataField);
            return;
        }
        Optional<DataField> dataFieldOptional = dataFields.stream()
                .filter(o -> o.getName().equals(dataField.getName())).findFirst();
        if (dataFieldOptional.isPresent()){
            dataFieldOptional.get().addValueGroups(dataField.getValueGroups());
        } else {
            dataFields.add(dataField);
        }
    }

    public void addDataFields(Set<DataField> dataFields) {
        dataFields.forEach(this::addDataField);
    }

    public class Builder {

        private Builder() {

        }

        public Builder setValue(String name) {
            ValueGroup.this.value = name;

            return this;
        }

        public Builder setDataFields(Set<DataField> dataFields) {
            if (ValueGroup.this.dataFields == null){
                ValueGroup.this.dataFields = dataFields;
            } else {
                ValueGroup.this.dataFields.addAll(dataFields);
            }

            return this;
        }

        public Builder setDataField(DataField dataField) {
            if (dataField == null){
                return this;
            }
            if (ValueGroup.this.dataFields == null){
                ValueGroup.this.dataFields = new HashSet<>();
            }
            ValueGroup.this.dataFields.add(dataField);

            return this;
        }

        public ValueGroup build() {
            return ValueGroup.this;
        }
    }
}
