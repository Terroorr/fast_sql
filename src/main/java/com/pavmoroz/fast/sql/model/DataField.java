package com.pavmoroz.fast.sql.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DataField {

    private String name;
    private Set<ValueGroup> valueGroups;

    private DataField() {

    }

    public String getName() {
        return name;
    }

    public Set<ValueGroup> getValueGroups() {
        return valueGroups;
    }

    public void addValueGroup(ValueGroup valueGroup) {
        if (valueGroups == null){
            valueGroups = new HashSet<>();
            valueGroups.add(valueGroup);
            return;
        }
        Optional<ValueGroup> valueGroupOptional = valueGroups.stream()
                .filter(o -> o.getValue().equals(valueGroup.getValue())).findFirst();
        if (valueGroupOptional.isPresent()){
            valueGroupOptional.get().addDataFields(valueGroup.getDataFields());
        } else {
            valueGroups.add(valueGroup);
        }
    }

    public void addValueGroups(Set<ValueGroup> valueGroups) {
        valueGroups.forEach(this::addValueGroup);
    }

    public static Builder newBuilder() {
        return new DataField().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setName(String name) {
            DataField.this.name = name;

            return this;
        }

        public Builder setValueGroups(Set<ValueGroup> valueGroups) {
            if (DataField.this.valueGroups == null){
                DataField.this.valueGroups = valueGroups;
            } else {
                DataField.this.valueGroups.addAll(valueGroups);
            }

            return this;
        }

        public Builder setValueGroup(ValueGroup valueGroup) {
            if (DataField.this.valueGroups == null){
                DataField.this.valueGroups = new HashSet<>();
            }
            DataField.this.valueGroups.add(valueGroup);

            return this;
        }

        public DataField build() {
            return DataField.this;
        }
    }
}
