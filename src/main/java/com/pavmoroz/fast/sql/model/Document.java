package com.pavmoroz.fast.sql.model;

import com.pavmoroz.fast.sql.Const;

import java.util.ArrayList;
import java.util.HashSet;

public class Document {
    private ArrayList<Row> rows = new ArrayList<>();

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public void formatDocument() {
        filterRows();
        setCommentRows();
    }

    private void setCommentRows() {
        ArrayList<Row> newRows = new ArrayList<Row>();

        Row startDocumentRow = new Row(Const.COMMENT, "");
        startDocumentRow.addField(Const.COMMENT, Const.StartDocument);
        newRows.add(startDocumentRow);

        String currentTableName = "";
        for (Row row : getRows()){
            if(Const.DELETE.equals(row.getOperator())){
                Row newRow;
                if(currentTableName.equals(row.getTableName())){
                    newRow = new Row(Const.COMMENT, row.getTableName());
                    newRow.addField(Const.COMMENT, Const.OneTableDistance);
                } else {
                    newRow = new Row(Const.COMMENT, row.getTableName());
                    newRow.addField(Const.COMMENT, String.format(Const.DifferentTableDistance, row.getTableName()));
                    currentTableName = row.getTableName();
                }
                newRows.add(newRow);
            }
            newRows.add(row);
        }

        Row endDocumentRow = new Row(Const.COMMENT, "");
        endDocumentRow.addField(Const.COMMENT, Const.EndDocument);
        newRows.add(endDocumentRow);

        setRows(newRows);
    }

    private ArrayList<Row> addAllOperationsRows(ArrayList<Row> rows) {
        ArrayList<Row> newRows = new ArrayList<Row>();
        for (Row deleteRow : rows){
            newRows.add(deleteRow);
            for (Row row : getRows()){
                if (!deleteRow.getTableName().equals(row.getTableName())
                        || !equalsFields(deleteRow, row)
                        || Const.DELETE.equals(row.getOperator())){
                    continue;
                }
                newRows.add(row);
            }
        }
        return newRows;
    }

    private boolean equalsFields(Row deleteRow, Row row) {
        for (Field field : deleteRow.getFields()){
            if(!field.getValue().equals(row.getFieldValue(field.getName()))){
                return false;
            }
        }
        return true;
    }
    private void filterRows(){
        ArrayList<Row> deleteRows = new ArrayList<Row>();
        for (Row row : getRows()){
            if (Const.DELETE.equals(row.getOperator())){
                deleteRows.add(row);
            }
        }
        ArrayList<Row> filteredRows = new ArrayList<Row>();
        HashSet<String> uniqueFieldValue = new HashSet<String>();
        for (Row row : deleteRows){
            uniqueFieldValue.add(row.getFieldValue("tableName"));
        }
        for (String field : uniqueFieldValue){
            ArrayList<Row> newRows = new ArrayList<Row>();
            for (Row row : deleteRows){
                if (field.equals(row.getFieldValue("tableName"))){
                    newRows.add(row);
                }
            }
            filteredRows.addAll(newRows);
        }
        setRows(addAllOperationsRows(filteredRows));
    }
}
