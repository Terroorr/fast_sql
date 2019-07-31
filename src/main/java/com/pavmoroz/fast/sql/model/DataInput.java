package com.pavmoroz.fast.sql.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DataInput {

    private String tablesTemplateFileName;
    private SqlFileSettings sqlFileSettings;
    private Set<DataField> dataFields;

    private DataInput() {

    }

    public String getTablesTemplateFileName() {
        return tablesTemplateFileName;
    }

    public SqlFileSettings getSqlFileSettings() {
        return sqlFileSettings;
    }

    public Set<DataField> getDataFields() {
        return dataFields;
    }

    public static Builder newBuilder() {
        return new DataInput().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setTablesTemplateFileName(String tablesTemplateFileName) {
            DataInput.this.tablesTemplateFileName = tablesTemplateFileName;

            return this;
        }

        public Builder setSqlFileSettings(SqlFileSettings sqlFileSettings) {
            DataInput.this.sqlFileSettings = sqlFileSettings;

            return this;
        }

        public Builder setDataFields(Set<DataField> dataFields) {
            if (DataInput.this.dataFields == null){
                DataInput.this.dataFields = dataFields;
            } else {
                DataInput.this.dataFields.addAll(dataFields);
            }

            return this;
        }

        public Builder setDataField(DataField dataField) {
            if (DataInput.this.dataFields == null){
                DataInput.this.dataFields = new HashSet<>();
                DataInput.this.dataFields.add(dataField);
                return this;
            }
            Optional<DataField> dataFieldOptional = DataInput.this.dataFields.stream()
                    .filter(o -> o.getName().equals(dataField.getName())).findFirst();
            if (dataFieldOptional.isPresent()){
                dataFieldOptional.get().addValueGroups(dataField.getValueGroups());
            } else {
                DataInput.this.dataFields.add(dataField);
            }

            return this;
        }

        public DataInput build() {
            if (CollectionUtils.isEmpty(DataInput.this.dataFields) && DataInput.this.sqlFileSettings == null){
                throw new RuntimeException("No data found to create SQL.");
            }
            if (DataInput.this.tablesTemplateFileName == null){
                throw new RuntimeException("The path to the table template was not found.");
            }
            return DataInput.this;
        }
    }
}
