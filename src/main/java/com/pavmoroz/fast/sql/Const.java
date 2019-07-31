package com.pavmoroz.fast.sql;

public class Const {
    public static final String DELETE = "delete";
    public static final String DeletePattern = "DELETE FROM %s WHERE %s;";
    public static final String INSERT = "insert";
    public static final String InsertPattern = "INSERT INTO %s (%s) VALUES (%s);";
    public static final String FieldsForDelete = "fields_for_delete";
    public static final String COMMENT = "comment";
    public static final String OneTableDistance = "";
    public static final String DifferentTableDistance = "\n" +
            "-- **************************************\n" +
            "-- * %-35s*\n" +
            "-- **************************************\n";
    public static final String StartDocument = "\n" +
            "REM Errorhandling off\n" +
            "WHENEVER SQLERROR CONTINUE\n" +
            "set define off;";
    public static final String EndDocument = "\n" +
            "REM Errorhandling ON\n" +
            "WHENEVER SQLERROR EXIT SQL.SQLCODE\n" +
            "\n" +
            "COMMIT;";
    public static final String StartCommentsDocument = "" +
            "-- =====================================================================================\n" +
            "-- Author: %s\n" +
            "-- Release: %s\n" +
            "-- %s: %s\n" +
            "-- Description: %s\n" +
            "-- DB: %s\n" +
            "-- =====================================================================================";

    public static final String OutFileNamePattern = "%s_%s_%s-%s_%s_%s.sql";
    public static final Object LANGUAGE = "LANGUAGE";
}
