package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.strategy.BasicReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class MissionsFileReader implements BasicReader {
    @Override
    public Collection<FlightMission> read(String rootDir, String fileName) throws FileNotFoundException,
            InvalidArgsException,
            DuplicateObjectException {
        Scanner reader = new Scanner(new File("src/main/resources"
                + "/" + rootDir
                + "/" + fileName));
        return parseMissionsData(reader);
    }

    private Collection<FlightMission> parseMissionsData(Scanner reader) throws DuplicateObjectException{
        Collection<FlightMission> flightMissions = new ArrayList<>();
        while(reader.hasNextLine()){
            String nextLine = reader.nextLine();
            if(nextLine.charAt(0) != '#'){
                String[] args = nextLine.split("[;]");
                flightMissions.add(FlightMissionFactory.INSTANCE.create(args[0],
                        LocalDateTime.parse(args[1], DateTimeFormatter.ofPattern(NassaContext.getInstance().getFormat())),
                        LocalDateTime.parse(args[2], DateTimeFormatter.ofPattern(NassaContext.getInstance().getFormat())),
                        Long.parseLong(args[3])));
            }
        }
        reader.close();
        return flightMissions;
    }
}
