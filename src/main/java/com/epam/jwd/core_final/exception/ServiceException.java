package com.epam.jwd.core_final.exception;

import org.omg.SendingContext.RunTime;

public class ServiceException extends RuntimeException {
    public ServiceException(String errorMessage) {
        super(errorMessage);
    }
}
