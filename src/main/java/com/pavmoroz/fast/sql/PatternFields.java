package com.pavmoroz.fast.sql;

import com.pavmoroz.fast.sql.model.DataField;
import com.pavmoroz.fast.sql.model.ValueGroup;

public enum PatternFields {
    INSTITUTE_to_LANGUAGE(DataField.newBuilder()
            .setName("INSTITUTE")
            .setValueGroup(ValueGroup.newBuilder()
                    .setValue("'ALL'")
                    .setDataField(DataField.newBuilder()
                            .setName("LANGUAGE")
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'DE'")
                                    .build())
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'EN'")
                                    .build())
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'TR'")
                                    .build())
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'SR'")
                                    .build())
                            .build())
                    .build())
            .setValueGroup(ValueGroup.newBuilder()
                    .setValue("'EASYBANK'")
                    .setDataField(DataField.newBuilder()
                            .setName("LANGUAGE")
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'DE'")
                                    .build())
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue("'EN'")
                                    .build())
                            .build())
                    .build())
            .build());

    private DataField dataField;

    PatternFields(DataField dataField) {
        this.dataField = dataField;
    }

    public DataField getDataField() {
        return dataField;
    }
}
