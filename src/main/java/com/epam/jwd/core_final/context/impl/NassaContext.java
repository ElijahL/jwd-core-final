package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.strategy.Reader;
import com.epam.jwd.core_final.strategy.impl.CrewFileReader;
import com.epam.jwd.core_final.strategy.impl.MissionsFileReader;
import com.epam.jwd.core_final.strategy.impl.SpaceshipsFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// todo
public class NassaContext implements ApplicationContext {
    private static NassaContext instance;

    // no getters/setters for them
    private Collection<CrewMember> crewMembers = new ArrayList<>();
    private Collection<Spaceship> spaceships = new ArrayList<>();
    private Collection<FlightMission> flightMissions = new ArrayList<>();
    private DateTimeFormatter formatter;
    private static Logger logger;

    public static NassaContext getInstance(){
        if(instance == null){
            instance = new NassaContext();
        }
        return instance;
    }

    public DateTimeFormatter getFormatter(){
        return formatter;
    }

    public Logger getLogger(){
        return logger;
    }

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) throws UnknownEntityException {
        if(tClass.equals(CrewMember.class)){
            return (Collection<T>) crewMembers;
        } else if(tClass.equals(Spaceship.class)){
            return (Collection<T>) spaceships;
        } else if(tClass.equals(FlightMission.class)){
            return (Collection<T>) flightMissions;
        } else {
            throw new UnknownEntityException(tClass.toString());
        }
    }

    /**
     * You have to read input files, populate collections
     * @throws InvalidStateException
     */
    @Override
    public void init(ApplicationProperties properties) throws InvalidStateException {
        formatter = DateTimeFormatter.ofPattern(properties.getDateTimeFormat());
        logger = Logger.getLogger(NassaContext.class.getName());
        FileHandler handler;
        try {
            handler = new FileHandler("src/main/resources/myLogs.log");
        } catch (IOException ex){
            throw new InvalidArgsException(ex.getMessage());
        }
        logger.addHandler(handler);
        handler.setFormatter(new SimpleFormatter());

        Reader reader = new Reader();
        try {
            reader.setReader(new CrewFileReader());
            crewMembers = reader.read(properties.getInputRootDir(), properties.getCrewFileName());

            reader.setReader(new SpaceshipsFileReader());
            spaceships = reader.read(properties.getInputRootDir(), properties.getSpaceshipsFileName());

            reader.setReader(new MissionsFileReader());
            flightMissions = reader.read(properties.getInputRootDir(), properties.getMissionsFileName());
        } catch (DuplicateObjectException | FileNotFoundException | InvalidArgsException ex){
            throw new InvalidStateException(ex.getMessage());
        }
    }
}
