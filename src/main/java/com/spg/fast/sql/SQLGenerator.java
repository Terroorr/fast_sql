package com.spg.fast.sql;

import com.spg.fast.sql.model.SourceField;
import com.spg.fast.sql.model.Source;
import com.spg.fast.sql.model.SQL;
import com.spg.fast.sql.model.SQLRow;
import com.spg.fast.sql.parser.source.SourceParser;
import com.spg.fast.sql.parser.template.TemplateParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SQLGenerator {
    private static final Logger LOG = Logger.getLogger(SQLGenerator.class);

    private SourceParser sourceParser;
    private TemplateParser templateParser;

    public SQLGenerator(SourceParser sourceParser, TemplateParser templateParser) {
        this.sourceParser = sourceParser;
        this.templateParser = templateParser;
    }

    public void execute() {
        Source source = sourceParser.execute();
        SQL sql = templateParser.execute(source.getTemplateFileName());

        sql.setSqlRows(buildSqlRows(sql.getSqlRows(), source.getSourceFields()));
        sql.setSqlRows(addPatternRows(sql.getSqlRows()));
        formatDocument(sql);
        saveDocument(sql, source);
    }

    private void formatDocument(SQL sql) {
        sql.formatDocument();
    }

    private void saveDocument(SQL sql, Source source) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(Const.StartCommentsDocument,
                source.getSQLFileSettings().getAuthor(), source.getSQLFileSettings().getRelease(),
                source.getSQLFileSettings().getTicketType(), source.getSQLFileSettings().getTicketNumber(),
                source.getSQLFileSettings().getDescription(), source.getSQLFileSettings().getDb()));

        sql.getSqlRows().forEach(SQLRow -> {
            sb.append("\n");
            if (Const.DELETE.equals(SQLRow.getOperator())) {
                StringBuilder param = new StringBuilder();
                SQLRow.getSQLFields().forEach(field -> {
                    if (param.length() > 0) {
                        param.append(" AND ");
                    }
                    param.append(field.getName())
                            .append(" = ")
                            .append(field.getValue());
                });
                sb.append(String.format(Const.DeletePattern, SQLRow.getTableName(), param));
            }
            if (Const.INSERT.equals(SQLRow.getOperator())) {
                StringBuilder fields = new StringBuilder();
                StringBuilder values = new StringBuilder();
                SQLRow.getSQLFields().forEach(field -> {
                    if (fields.length() > 0) {
                        fields.append(", ");
                    }
                    if (values.length() > 0) {
                        values.append(", ");
                    }
                    fields.append(field.getName());
                    values.append(field.getValue());
                });
                sb.append(String.format(Const.InsertPattern, SQLRow.getTableName(), fields, values));
            }
            if (Const.COMMENT.equals(SQLRow.getOperator())) {
                sb.append(SQLRow.getFieldValue(Const.COMMENT));
            }
        });

        System.out.println(sb);

        File f = new File(String.format(Const.OutFileNamePattern,
                source.getSQLFileSettings().getProject(), source.getSQLFileSettings().getDb().toLowerCase(),
                source.getSQLFileSettings().getTicketType(), source.getSQLFileSettings().getTicketNumber(),
                source.getSQLFileSettings().getDescriptionInFileName(),
                source.getSQLFileSettings().getRelease().replace(".", "")));
        try {
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException ex) {
            LOG.info(ex.getMessage(), ex);
        }
    }

    private List<SQLRow> buildSqlRows(List<SQLRow> SQLRows, Set<SourceField> sourceFields) {
        for (SourceField sourceField : sourceFields) {
            List<SQLRow> newSQLRows = new ArrayList<>();
            for (SQLRow SQLRow : SQLRows) {
                newSQLRows.addAll(buildRowsByField(SQLRow, sourceField));
            }
            SQLRows = newSQLRows;
        }
        return SQLRows;
    }

    private List<SQLRow> buildRowsByField(SQLRow sqlRow, SourceField sourceField) {
        List<SQLRow> newSQLRows = new ArrayList<>();
        if (!sqlRow.containsField(sourceField.getName())) {
            newSQLRows.add(sqlRow);
            return newSQLRows;
        }
        sourceField.getValueGroups().forEach(valueGroup -> {
            SQLRow newSQLRow = new SQLRow(sqlRow);
            newSQLRow.addField(sourceField.getName(), valueGroup.getValue());
            if (CollectionUtils.isEmpty(valueGroup.getSourceFields())) {
                newSQLRows.add(newSQLRow);
            } else {
                List<SQLRow> currentSQLRows = new ArrayList<>();
                currentSQLRows.add(newSQLRow);
                newSQLRows.addAll(buildSqlRows(currentSQLRows, valueGroup.getSourceFields()));
            }
        });
        return newSQLRows;
    }

    private List<SQLRow> addPatternRows(List<SQLRow> SQLRows) {
        for (PatternFields s : PatternFields.values()) {
            List<SQLRow> newSQLRows = new ArrayList<>();
            for (SQLRow SQLRow : SQLRows) {
                newSQLRows.addAll(addPatternRows(SQLRow, s.getSourceField()));
            }
            SQLRows = newSQLRows;
        }
        return SQLRows;
    }

    private List<SQLRow> addPatternRows(SQLRow sqlRow, SourceField sourceField) {
        List<SQLRow> newSQLRows = new ArrayList<>();
        if (!sqlRow.containsField(sourceField.getName())) {
            newSQLRows.add(sqlRow);
            return newSQLRows;
        }
        sourceField.getValueGroups().forEach(valueGroup -> {
            if (sqlRow.getFieldValue(sourceField.getName()).equals(valueGroup.getValue())) {
                if (CollectionUtils.isNotEmpty(valueGroup.getSourceFields())) {
                    valueGroup.getSourceFields().forEach(dependentField -> {
                        if (sqlRow.containsField(dependentField.getName()) && sqlRow.getFieldValue(dependentField.getName()) == null) {
                            dependentField.getValueGroups().forEach(dependentVG -> {
                                SQLRow newSQLRow = new SQLRow(sqlRow);
                                newSQLRow.setFieldValue(dependentField.getName(), dependentVG.getValue());
                                newSQLRows.add(newSQLRow);
                            });
                        }
                    });
                }
            }
        });
        if (CollectionUtils.isEmpty(newSQLRows)) {
            newSQLRows.add(sqlRow);
        }
        return newSQLRows;
    }
}
