package com.spg.fast.sql.validation;

import java.util.List;

public class SourceFileValidation implements Validation {
    @Override
    public void execute(List<String> args) throws ValidationException {
        String fileName = args.get(0);
        if (!fileName.endsWith(".json") && !fileName.endsWith(".txt")) {
            throw new ValidationException("Wrong source file extension: " + fileName);
        }
    }
}
