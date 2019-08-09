package com.spg.fast.sql.validation;

import java.util.List;

public interface Validation {

    void execute(List<String> args) throws ValidationException;
}
