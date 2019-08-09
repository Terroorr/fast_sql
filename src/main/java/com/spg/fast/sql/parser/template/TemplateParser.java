package com.spg.fast.sql.parser.template;

import com.spg.fast.sql.Const;
import com.spg.fast.sql.model.SQL;
import com.spg.fast.sql.model.SQLRow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TemplateParser {
    private JSONParser parser = new JSONParser();

    public SQL execute(String fileName) {
        try {
            JSONObject tablesTemplate = (JSONObject) parser.parse(new FileReader(fileName));
            SQL SQL = new SQL();
            SQL.setSqlRows(getDocumentRows(tablesTemplate));
            return SQL;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found: " + fileName);
        } catch (ParseException | IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private ArrayList<SQLRow> getDocumentRows(JSONObject tablesTemplate) {
        ArrayList<SQLRow> SQLRows = new ArrayList<>();

        for (Object table : (JSONArray) tablesTemplate.get("tables")){
            for (Object tableName : ((JSONObject) table).keySet()){
                SQLRow insertSQLRow = new SQLRow(Const.INSERT, tableName.toString());
                SQLRow deleteSQLRow = new SQLRow(Const.DELETE, tableName.toString());
                for(Object field : (JSONArray) ((JSONObject) table).get(tableName)){
                    boolean presenceInDelete = false;
                    String name = null;
                    String value = null;
                    for (Object fieldName : ((JSONObject) field).keySet()){
                        if (Const.PRESENCE_IN_DELETE.equals(fieldName)){
                            presenceInDelete = (boolean) ((JSONObject) field).get(fieldName);
                        }
                        else {
                            name = fieldName.toString();
                            value = ((JSONObject) field).get(fieldName) != null ?
                                    ((JSONObject) field).get(fieldName).toString() : null;
                        }
                    }
                    if (presenceInDelete){
                        deleteSQLRow.addField(name, value);
                    }
                    insertSQLRow.addField(name, value);
                }
                SQLRows.add(deleteSQLRow);
                SQLRows.add(insertSQLRow);
            }
        }
        return SQLRows;
    }

    private ArrayList<SQLRow> getDocumentRows2(JSONObject tablesTemplate) {
        ArrayList<SQLRow> SQLRows = new ArrayList<>();

        for (Object table : (JSONArray) tablesTemplate.get("tables")){
            for (Object tableName : ((JSONObject) table).keySet()){
                SQLRow insertSQLRow = new SQLRow(Const.INSERT, tableName.toString());
                SQLRow deleteSQLRow = new SQLRow(Const.DELETE, tableName.toString());
                for(Object field : (JSONArray) ((JSONObject) table).get(tableName)){
                    boolean presenceInDelete = false;
                    String name = null;
                    String value = null;
                    for (Object fieldName : ((JSONObject) field).keySet()){
                        if (Const.PRESENCE_IN_DELETE.equals(fieldName)){
                            presenceInDelete = (boolean) ((JSONObject) field).get(fieldName);
                        }
                        else {
                            name = fieldName.toString();
                            value = ((JSONObject) field).get(fieldName) != null ?
                                    ((JSONObject) field).get(fieldName).toString() : null;
                        }
                    }
                    if (presenceInDelete){
                        deleteSQLRow.addField(name, value);
                    }
                    insertSQLRow.addField(name, value);
                }
                SQLRows.add(deleteSQLRow);
                SQLRows.add(insertSQLRow);
            }
        }
        return SQLRows;
    }
}
