package com.spg.fast.sql;

import com.spg.fast.sql.model.SourceField;
import com.spg.fast.sql.model.ValueGroup;

public enum PatternFields {
    INSTITUTE_to_LANGUAGE(SourceField.newBuilder()
            .setName("INSTITUTE")
            .setValueGroup(ValueGroup.newBuilder()
                    .setValue("'ALL'")
                    .setDataField(SourceField.newBuilder()
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
                    .setDataField(SourceField.newBuilder()
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

    private SourceField sourceField;

    PatternFields(SourceField sourceField) {
        this.sourceField = sourceField;
    }

    public SourceField getSourceField() {
        return sourceField;
    }
}
