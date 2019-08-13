package com.spg.fast.sql.parser.source;

import com.spg.fast.sql.model.SourceField;
import com.spg.fast.sql.model.Source;
import com.spg.fast.sql.model.SQLFileSettings;
import com.spg.fast.sql.model.ValueGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TXTSourceParser implements SourceParser {
    private String fileName;

    public TXTSourceParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Source execute() {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            return parseInputData(scanner);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        }
    }

    private Source parseInputData(Scanner scanner) {
        Source.Builder inputDataBuilder = Source.builder();

        List<String> fieldNames = new ArrayList<>();
        int numberLine = 0;
        while(scanner.hasNext()){
            String tempString = scanner.nextLine();
            if (tempString.startsWith("//")
                    || (tempString.replace(" ", "")).length() == 0){
                continue;
            } else numberLine++;
            switch (numberLine){
                case 1:
                    inputDataBuilder.setTemplateFileName(tempString);
                    break;
                case 2:
                    inputDataBuilder.setSqlFileSettings(getSqlFileSettings(tempString));
                    break;
                case 3:
                case 5:
                    fieldNames = getFieldNames(tempString);
                    break;
                case 4:
                    inputDataBuilder.setDataFields(getUniqueFields(fieldNames, tempString));
                    break;
                default:
                    inputDataBuilder.setSourceField(getOtherFields(fieldNames, tempString));
                    break;
            }
        }
        return inputDataBuilder.build();
    }

    private SourceField getOtherFields(List<String> fieldNames, String tempString) {
        String[] values = tempString.split("<  >");
        SourceField tempSourceField = null;
        for (int i = values.length - 1; i >= 0 ; i--){
            String[] params = values[i].split("::");
            Set<ValueGroup> valueGroupSet = new HashSet<>();
            for (String param : params) {
                ValueGroup.Builder builder = ValueGroup.newBuilder()
                        .setValue(param);
                if(tempSourceField != null){
                    builder.setDataField(tempSourceField);
                }
                valueGroupSet.add(builder.build());
            }
            tempSourceField = SourceField.newBuilder()
                    .setName(fieldNames.get(i))
                    .setValueGroups(valueGroupSet)
                    .build();
        }
        return tempSourceField;
    }

    private Set<SourceField> getUniqueFields(List<String> fieldNames, String tempString) {
        Set<SourceField> sourceFields = new HashSet<>();
        String[] values = tempString.split("<  >");
        for (int i = 0; i < values.length; i++){
            sourceFields.add(SourceField.newBuilder()
                            .setName(fieldNames.get(i))
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue(values[i])
                                    .build())
                            .build());
        }
        return sourceFields;
    }

    private List<String> getFieldNames(String tempString) {
        String[] values = tempString.split("<  >");
        return Arrays.asList(values);
    }

    private SQLFileSettings getSqlFileSettings(String tempString) {
        SQLFileSettings.Builder builder = SQLFileSettings.newBuilder();
        String[] values = tempString.split("<  >");
        builder.setAuthor(values[0]);
        builder.setRelease(values[1]);
        builder.setTicketType(values[2]);
        builder.setTicketNumber(values[3]);
        builder.setDescription(values[4]);
        builder.setProject(values[5]);
        builder.setDb(values[6]);
        builder.setDescriptionInFileName(values[7]);
        return builder.build();
    }
}
