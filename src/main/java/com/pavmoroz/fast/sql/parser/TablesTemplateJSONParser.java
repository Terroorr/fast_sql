package com.pavmoroz.fast.sql.parser;

import com.pavmoroz.fast.sql.Const;
import com.pavmoroz.fast.sql.model.Document;
import com.pavmoroz.fast.sql.model.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TablesTemplateJSONParser implements Parser {

    private JSONParser parser = new JSONParser();

    @Override
    public Document parse(String fileName) {
        try {
            JSONObject tablesTemplate = (JSONObject) parser.parse(new FileReader(fileName));
            Document document = new Document();
            document.setRows(getDocumentRows(tablesTemplate));
            return document;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        } catch (ParseException | IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private ArrayList<Row> getDocumentRows(JSONObject tablesTemplate) {
        ArrayList<Row> rows = new ArrayList<>();
        for (Object table : (JSONArray) tablesTemplate.get("tables")){
            for (Object tableName : ((JSONObject) table).keySet()){
                Row insertRow = new Row(Const.INSERT, tableName.toString());
                for(Object field : (JSONArray) ((JSONObject) table).get(tableName)){
                    for (Object fieldName : ((JSONObject) field).keySet()){
                        if (fieldName.toString().equals(Const.FieldsForDelete)){
                            Row deleteRow = new Row(Const.DELETE, tableName.toString());
                            for (Object deleteField : (JSONArray) ((JSONObject) field).get(Const.FieldsForDelete)){
                                deleteRow.addField(deleteField.toString(), null);
                            }
                            rows.add(deleteRow);
                            continue;
                        }
                        insertRow.addField(fieldName.toString(),
                                ((JSONObject) field).get(fieldName) != null ?
                                        ((JSONObject) field).get(fieldName).toString() : null);
                    }
                }
                rows.add(insertRow);
            }
        }
        return rows;
    }
}
