package com.spg.fast.sql.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Source {
    private String templateFileName;
    private SQLFileSettings sqlFileSettings;
    private Set<SourceField> sourceFields = new HashSet<>();

    private Source() {
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public SQLFileSettings getSQLFileSettings() {
        return sqlFileSettings;
    }

    public Set<SourceField> getSourceFields() {
        return sourceFields;
    }

    public static Builder builder() {
        return new Source().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setTemplateFileName(String templateFileName) {
            Source.this.templateFileName = templateFileName;

            return this;
        }

        public Builder setSqlFileSettings(SQLFileSettings sqlFileSettings) {
            Source.this.sqlFileSettings = sqlFileSettings;

            return this;
        }

        public Builder setDataFields(Set<SourceField> sourceFields) {
            Source.this.sourceFields.addAll(sourceFields);

            return this;
        }

        public Builder setSourceField(SourceField sourceField) {
            Optional<SourceField> sourceFieldOptional = Source.this.sourceFields.stream()
                    .filter(o -> o.getName().equals(sourceField.getName())).findFirst();
            if (sourceFieldOptional.isPresent()) {
                sourceFieldOptional.get().addValueGroups(sourceField.getValueGroups());
            } else {
                Source.this.sourceFields.add(sourceField);
            }

            return this;
        }

        public Source build() {
            if (CollectionUtils.isEmpty(Source.this.sourceFields) || Source.this.sqlFileSettings == null) {
                throw new RuntimeException("No data found to create SQL.");
            }
            if (Source.this.templateFileName == null) {
                throw new RuntimeException("The path to the table template was not found.");
            }
            return Source.this;
        }
    }
}
