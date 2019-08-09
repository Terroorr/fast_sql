package com.spg.fast.sql;

import com.spg.fast.sql.parser.source.ParserInstantiationException;
import com.spg.fast.sql.parser.source.SourceParser;
import com.spg.fast.sql.parser.source.SourceParserFactory;
import com.spg.fast.sql.parser.template.TemplateParser;
import com.spg.fast.sql.validation.CLIValidation;
import com.spg.fast.sql.validation.SourceFileValidation;
import com.spg.fast.sql.validation.ValidationException;

import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        List<String> params = Arrays.asList(args);

        try {
            CLIValidation validation = new CLIValidation(new SourceFileValidation());
            validation.execute(params);

            SourceParser sourceParser = SourceParserFactory.get(params.get(0));
            TemplateParser templateParser = new TemplateParser();

            SQLGenerator sqlGenerator = new SQLGenerator(sourceParser, templateParser);
            sqlGenerator.execute();
        } catch (ValidationException | ParserInstantiationException e) {
            e.printStackTrace();
        }
    }
}
