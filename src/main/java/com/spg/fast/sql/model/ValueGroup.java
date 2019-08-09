package com.spg.fast.sql.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ValueGroup {

    private String value;
    private Set<SourceField> sourceFields = new HashSet<>();

    private ValueGroup() {

    }

    public String getValue() {
        return value;
    }

    public Set<SourceField> getSourceFields() {
        return sourceFields;
    }

    public static Builder newBuilder() {
        return new ValueGroup().new Builder();
    }

    public void addSourceField(SourceField sourceField) {
        Optional<SourceField> dataFieldOptional = sourceFields.stream()
                .filter(o -> o.getName().equals(sourceField.getName())).findFirst();
        if (dataFieldOptional.isPresent()){
            dataFieldOptional.get().addValueGroups(sourceField.getValueGroups());
        } else {
            sourceFields.add(sourceField);
        }
    }

    public void addSourceFields(Set<SourceField> sourceFields) {
        sourceFields.forEach(this::addSourceField);
    }

    public class Builder {

        private Builder() {

        }

        public Builder setValue(String name) {
            ValueGroup.this.value = name;

            return this;
        }

        public Builder setDataFields(Set<SourceField> sourceFields) {
            ValueGroup.this.sourceFields.addAll(sourceFields);

            return this;
        }

        public Builder setDataField(SourceField sourceField) {
            ValueGroup.this.sourceFields.add(sourceField);

            return this;
        }

        public ValueGroup build() {
            return ValueGroup.this;
        }
    }
}
