package com.spg.fast.sql.parser.source;

import com.spg.fast.sql.model.SQLFileSettings;
import com.spg.fast.sql.model.Source;
import com.spg.fast.sql.model.SourceField;
import com.spg.fast.sql.model.ValueGroup;
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

public class JSONSourceParser implements SourceParser {
    private static final Logger log = Logger.getLogger(JSONSourceParser.class);

    private JSONParser parser = new JSONParser();
    private String fileName;

    public JSONSourceParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Source execute() {
        try {
            JSONObject sourceAsJSON = (JSONObject) parser.parse(new FileReader(fileName));
            return Source.builder()
                    .setTemplateFileName(templateFile(sourceAsJSON))
                    .setSqlFileSettings(fileSettings(sourceAsJSON))
                    .setDataFields(getDataFields(sourceAsJSON))
                    .build();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        } catch (ParseException | IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Set<SourceField> getDataFields(JSONObject inputDataJSONObject) {
        if (inputDataJSONObject.containsKey("DATA")) {
            JSONObject dataJSONObject = (JSONObject) inputDataJSONObject.get("DATA");
            return getFields((Map<String, Object>) dataJSONObject);
        } else {
            log.info("Data was not found.");
            return null;
        }
    }

    private Set<SourceField> getFields(Map<String, Object> dataJSONObject) {
        return dataJSONObject.entrySet().stream()
                .map(this::getField)
                .collect(Collectors.toSet());
    }

    private SourceField getField(Map.Entry<String, Object> dataFieldJSONObject) {
        SourceField.Builder builder = SourceField.newBuilder()
                .setName(dataFieldJSONObject.getKey());
        if (dataFieldJSONObject.getValue() instanceof String) {
            builder.setValueGroup(ValueGroup.newBuilder()
                    .setValue(dataFieldJSONObject.getValue().toString())
                    .build());
        }
        if (dataFieldJSONObject.getValue() instanceof JSONArray) {
            builder.setValueGroups(((List<String>) dataFieldJSONObject.getValue()).stream()
                    .map(value -> ValueGroup.newBuilder()
                            .setValue(value)
                            .build())
                    .collect(Collectors.toSet()));
        }
        if (dataFieldJSONObject.getValue() instanceof JSONObject) {
            builder.setValueGroups(((Map<String, Object>) dataFieldJSONObject.getValue()).entrySet().stream()
                    .map(entry -> ValueGroup.newBuilder()
                            .setValue(entry.getKey())
                            .setDataFields(getFields(((Map<String, Object>) entry.getValue())))
                            .build())
                    .collect(Collectors.toSet()));
        }
        return builder.build();
    }

    //VALIDATION!!!!
    private SQLFileSettings fileSettings(JSONObject sourceAsJSON) {
        JSONObject fileSettingsAsJSON = (JSONObject) sourceAsJSON.get("FILE_SETTINGS");
        return SQLFileSettings.newBuilder()
                .setAuthor((fileSettingsAsJSON).getOrDefault("AUTHOR", "").toString())
                .setRelease(fileSettingsAsJSON.getOrDefault("RELEASE", "").toString())
                .setTicketType(fileSettingsAsJSON.getOrDefault("TICKET_TYPE", "").toString())
                .setTicketNumber(fileSettingsAsJSON.getOrDefault("TICKET_NUMBER", "").toString())
                .setDescription(fileSettingsAsJSON.getOrDefault("DESCRIPTION", "").toString())
                .setProject(fileSettingsAsJSON.getOrDefault("PROJECT", "").toString())
                .setDb(fileSettingsAsJSON.getOrDefault("DB", "").toString())
                .setDescriptionInFileName(fileSettingsAsJSON.getOrDefault("DESCRIPTION_IN_FILE_NAME", "").toString())
                .build();
    }

    private String templateFile(JSONObject sourceAsJSON) {
        return sourceAsJSON.getOrDefault("TEMPLATE_FILE", "").toString();
    }
}
