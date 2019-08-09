package com.spg.fast.sql.model;

import com.spg.fast.sql.Const;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SQL {
    private List<SQLRow> sqlRows = new ArrayList<>();

    public List<SQLRow> getSqlRows() {
        return sqlRows;
    }

    public void setSqlRows(List<SQLRow> sqlRows) {
        this.sqlRows = sqlRows;
    }

    public void formatDocument() {
        filterRows();
        addCommentRows();
    }

    private void addCommentRows() {
        List<SQLRow> newSQLRows = new ArrayList<>();

        SQLRow startDocumentSQLRow = new SQLRow(Const.COMMENT, "");
        startDocumentSQLRow.addField(Const.COMMENT, Const.START_DOCUMENT);
        newSQLRows.add(startDocumentSQLRow);

        String currentTableName = "";
        for (SQLRow sqlRow : getSqlRows()) {
            if (Const.DELETE.equals(sqlRow.getOperator())) {
                SQLRow tableDistance;
                if (currentTableName.equals(sqlRow.getTableName())) {
                    tableDistance = new SQLRow(Const.COMMENT, sqlRow.getTableName());
                    tableDistance.addField(Const.COMMENT, Const.ONE_TABLE_DISTANCE);
                } else {
                    tableDistance = new SQLRow(Const.COMMENT, sqlRow.getTableName());
                    tableDistance.addField(Const.COMMENT, String.format(Const.DIFFERENT_TABLE_DISTANCE, sqlRow.getTableName()));
                    currentTableName = sqlRow.getTableName();
                }
                newSQLRows.add(tableDistance);
            }
            newSQLRows.add(sqlRow);
        }

        SQLRow endDocumentSQLRow = new SQLRow(Const.COMMENT, "");
        endDocumentSQLRow.addField(Const.COMMENT, Const.END_DOCUMENT);
        newSQLRows.add(endDocumentSQLRow);

        setSqlRows(newSQLRows);
    }

    private void filterRows() {
        List<SQLRow> filteredSQLRows = getSqlRows().stream()
                .filter(sqlRow -> Const.DELETE.equals(sqlRow.getOperator()))
                .sorted(Comparator
                        .comparing(SQLRow::getTableName).reversed())
                .collect(Collectors.toList());
        setSqlRows(addAllOperationsRows(filteredSQLRows));
    }

    private List<SQLRow> addAllOperationsRows(List<SQLRow> sqlRows) {
        List<SQLRow> newSQLRows = new ArrayList<>();
        sqlRows.forEach(deleteSQLRow -> {
            newSQLRows.add(deleteSQLRow);
            getSqlRows().stream()
                    .filter(sqlRow -> deleteSQLRow.getTableName().equals(sqlRow.getTableName())
                            && !Const.DELETE.equals(sqlRow.getOperator())
                            && equalsFields(deleteSQLRow, sqlRow))
                    .forEachOrdered(newSQLRows::add);
        });
        return newSQLRows;
    }

    private boolean equalsFields(SQLRow deleteSQLRow, SQLRow SQLRow) {
        return deleteSQLRow.getSQLFields().stream()
                .allMatch(documentField -> documentField.getValue()
                        .equals(SQLRow.getFieldValue(documentField.getName())));
    }
}
