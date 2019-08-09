package com.spg.fast.sql.parser.source;

public class SourceParserFactory {

    private SourceParserFactory() {}

    public static SourceParser get(String fileName) throws ParserInstantiationException {
        if (fileName.endsWith(".json")) {
            return new JSONSourceParser(fileName);
        } else if (fileName.endsWith(".txt")) {
            return new TXTSourceParser(fileName);
        } else {
            throw new ParserInstantiationException("Parser not found for this file extension: " + fileName);
        }
    }
}
