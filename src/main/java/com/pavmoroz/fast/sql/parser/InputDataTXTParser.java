package com.pavmoroz.fast.sql.parser;

import com.pavmoroz.fast.sql.model.DataField;
import com.pavmoroz.fast.sql.model.DataInput;
import com.pavmoroz.fast.sql.model.SqlFileSettings;
import com.pavmoroz.fast.sql.model.ValueGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class InputDataTXTParser implements Parser{

    @Override
    public DataInput parse(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            return parseInputData(scanner);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        }
    }

    private DataInput parseInputData(Scanner scanner) {
        DataInput.Builder inputDataBuilder = DataInput.newBuilder();

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
                    inputDataBuilder.setTablesTemplateFileName(tempString);
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
                    inputDataBuilder.setDataField(getOtherFields(fieldNames, tempString));
                    break;
            }
        }
        return inputDataBuilder.build();
    }

    private DataField getOtherFields(List<String> fieldNames, String tempString) {
        String[] values = tempString.split(":");
        DataField tempDataField = null;
        for (int i = values.length - 1; i >= 0 ; i--){
            tempDataField = DataField.newBuilder()
                    .setName(fieldNames.get(i))
                    .setValueGroup(ValueGroup.newBuilder()
                            .setValue(values[i])
                            .setDataField(tempDataField)
                            .build())
                    .build();
        }
        return tempDataField;
    }

    private Set<DataField> getUniqueFields(List<String> fieldNames, String tempString) {
        Set<DataField> dataFields = new HashSet<>();
        String[] values = tempString.split(":");
        for (int i = 0; i < values.length; i++){
            dataFields.add(DataField.newBuilder()
                            .setName(fieldNames.get(i))
                            .setValueGroup(ValueGroup.newBuilder()
                                    .setValue(values[i])
                                    .build())
                            .build());
        }
        return dataFields;
    }

    private List<String> getFieldNames(String tempString) {
        String[] values = tempString.split(":");
        return Arrays.asList(values);
    }

    private SqlFileSettings getSqlFileSettings(String tempString) {
        SqlFileSettings.Builder builder = SqlFileSettings.newBuilder();
        String[] values = tempString.split(":");
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
