package com.pavmoroz.fast.sql.parser;

import com.pavmoroz.fast.sql.model.DataField;
import com.pavmoroz.fast.sql.model.DataInput;
import com.pavmoroz.fast.sql.model.SqlFileSettings;
import com.pavmoroz.fast.sql.model.ValueGroup;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InputDataJSONParser implements Parser {

    private static final Logger log = Logger.getLogger(InputDataJSONParser.class);
    private JSONParser parser = new JSONParser();

    @Override
    public DataInput parse(String fileName) {
        try {
            JSONObject inputDataJSONObject = (JSONObject) parser.parse(new FileReader(fileName));
            return DataInput.newBuilder()
                    .setTablesTemplateFileName(getTablesTemplateFileName(inputDataJSONObject))
                    .setSqlFileSettings(getSqlFileSettings(inputDataJSONObject))
                    .setDataFields(getDataFields(inputDataJSONObject))
                    .build();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        } catch (ParseException | IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Set<DataField> getDataFields(JSONObject inputDataJSONObject) {
        if (inputDataJSONObject.containsKey("Data")){
            JSONObject dataJSONObject = (JSONObject) inputDataJSONObject.get("Data");
            return getFields((Map<String, Object>) dataJSONObject);
        } else {
            log.info("Data was not found.");
            return null;
        }
    }

    private Set<DataField> getFields(Map<String, Object> dataJSONObject) {
        return dataJSONObject.entrySet().stream()
                .map(this::getField)
                .collect(Collectors.toSet());
    }

    private DataField getField(Map.Entry<String, Object> dataFieldJSONObject) {
        DataField.Builder builder = DataField.newBuilder()
                .setName(dataFieldJSONObject.getKey());
        if (dataFieldJSONObject.getValue() instanceof String){
            builder.setValueGroup(ValueGroup.newBuilder()
                    .setValue(dataFieldJSONObject.getValue().toString())
                    .build());
        }
        if (dataFieldJSONObject.getValue() instanceof JSONArray){
            builder.setValueGroups(((List<String>) dataFieldJSONObject.getValue()).stream()
                    .map(value -> ValueGroup.newBuilder()
                            .setValue(value)
                            .build())
                    .collect(Collectors.toSet()));
        }
        if (dataFieldJSONObject.getValue() instanceof JSONObject){
            builder.setValueGroups(((Map<String, Object>) dataFieldJSONObject.getValue()).entrySet().stream()
                    .map(entry -> ValueGroup.newBuilder()
                            .setValue(entry.getKey())
                            .setDataFields(getFields(((Map<String, Object>) entry.getValue())))
                            .build())
                    .collect(Collectors.toSet()));
        }
        return builder.build();
    }

    private SqlFileSettings getSqlFileSettings(JSONObject inputDataJSONObject) {
        if (inputDataJSONObject.containsKey("SqlFileSettings")){
            JSONObject sqlFileSettingsJSONObject = (JSONObject) inputDataJSONObject.get("SqlFileSettings");
            return SqlFileSettings.newBuilder()
                    .setAuthor((sqlFileSettingsJSONObject).getOrDefault("Author", "").toString())
                    .setRelease(sqlFileSettingsJSONObject.getOrDefault("Release", "").toString())
                    .setTicketType(sqlFileSettingsJSONObject.getOrDefault("TicketType", "").toString())
                    .setTicketNumber(sqlFileSettingsJSONObject.getOrDefault("TicketNumber", "").toString())
                    .setDescription(sqlFileSettingsJSONObject.getOrDefault("Description", "").toString())
                    .setProject(sqlFileSettingsJSONObject.getOrDefault("Project", "").toString())
                    .setDb(sqlFileSettingsJSONObject.getOrDefault("Db", "").toString())
                    .setDescriptionInFileName(sqlFileSettingsJSONObject.getOrDefault("DescriptionInFileName", "").toString())
                    .build();
        } else {
            log.info("SQL file settings was not found.");
            return null;
        }
    }

    private String getTablesTemplateFileName(JSONObject inputDataJSONObject) {
        if (inputDataJSONObject.containsKey("TablesTemplateFileName")){
            return inputDataJSONObject.get("TablesTemplateFileName").toString();
        } else {
            throw new RuntimeException("The path to the table template was not found.");
        }
    }
}
