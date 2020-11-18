package com.epam.jwd.core_final;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.exception.InvalidStateException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static Logger logger;

    public static void main(String[] args) {
        try {
            Application.start();
            logger = NassaContext.getInstance().getLogger();
        } catch (InvalidStateException ex){
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
}