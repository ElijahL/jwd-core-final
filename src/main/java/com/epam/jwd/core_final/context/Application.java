package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.context.impl.NassaMenu;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.IOException;
import java.util.function.Supplier;

public interface Application {

    static ApplicationMenu start() throws InvalidStateException {
        final Supplier<ApplicationContext> applicationContextSupplier = NassaContext::getInstance; // todo
        final NassaContext nassaContext = NassaContext.getInstance();

        PropertyReaderUtil.loadProperties();
        ApplicationProperties properties = PropertyReaderUtil.loadApplicationProperties();

        nassaContext.init(properties);

        try {
            NassaMenu.INSTANCE.startMenu();
        } catch (IOException ex) {
            throw new InvalidStateException(ex.getMessage());
        }

        return applicationContextSupplier::get;
    }
}
