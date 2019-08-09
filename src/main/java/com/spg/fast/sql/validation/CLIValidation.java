package com.spg.fast.sql.validation;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class CLIValidation implements Validation {
    private Validation validation;

    public CLIValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public void execute(List<String> args) throws ValidationException {
        if (CollectionUtils.isNotEmpty(args)) {
            validation.execute(args);
        } else {
            throw new ValidationException("Specify the path to the data file!");
        }
    }
}
