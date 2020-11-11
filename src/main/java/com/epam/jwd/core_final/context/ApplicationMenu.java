package com.epam.jwd.core_final.context;

import java.io.IOException;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default void printAvailableOptions() {}

    default void handleUserInput() throws IOException {}
}
