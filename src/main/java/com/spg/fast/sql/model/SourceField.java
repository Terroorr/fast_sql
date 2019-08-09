package com.spg.fast.sql.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SourceField {
    private String name;
    private Set<ValueGroup> valueGroups = new HashSet<>();

    private SourceField() {
    }

    public String getName() {
        return name;
    }

    public Set<ValueGroup> getValueGroups() {
        return valueGroups;
    }

    public void addValueGroup(ValueGroup valueGroup) {
        Optional<ValueGroup> valueGroupOptional = valueGroups.stream()
                .filter(o -> o.getValue().equals(valueGroup.getValue())).findFirst();
        if (valueGroupOptional.isPresent()) {
            valueGroupOptional.get().addSourceFields(valueGroup.getSourceFields());
        } else {
            valueGroups.add(valueGroup);
        }
    }

    public void addValueGroups(Set<ValueGroup> valueGroups) {
        valueGroups.forEach(this::addValueGroup);
    }

    public static Builder newBuilder() {
        return new SourceField().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setName(String name) {
            SourceField.this.name = name;

            return this;
        }

        public Builder setValueGroups(Set<ValueGroup> valueGroups) {
            SourceField.this.valueGroups.addAll(valueGroups);

            return this;
        }

        public Builder setValueGroup(ValueGroup valueGroup) {
            SourceField.this.valueGroups.add(valueGroup);

            return this;
        }

        public SourceField build() {
            if (SourceField.this.name == null) {
                SourceField.this.name = "";
            }
            return SourceField.this;
        }
    }
}
