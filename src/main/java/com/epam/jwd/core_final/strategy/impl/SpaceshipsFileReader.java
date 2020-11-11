package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.strategy.BasicReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SpaceshipsFileReader implements BasicReader {
    public Collection<Spaceship> read(String rootDir, String fileName) throws FileNotFoundException, InvalidArgsException, DuplicateObjectException {
        Scanner reader = new Scanner(new File("src/main/resources"
                + "/" + rootDir
                + "/" + fileName));
        return parseSpaceshipsData(reader);
    }

    private Collection<Spaceship> parseSpaceshipsData(Scanner reader) throws InvalidArgsException, DuplicateObjectException {
        Collection<Spaceship> spaceships = new ArrayList<>();
        while(reader.hasNextLine()){
            String nextLine = reader.nextLine();
            if(nextLine.charAt(0) != '#'){
                String[] args = nextLine.split("[;]");
                spaceships.add(SpaceshipFactory.INSTANCE.create(args[0],
                        Long.parseLong(args[1]),
                        parseMap(args[2])));
            }
        }
        reader.close();
        return spaceships;
    }

    private Map<Role, Short> parseMap(String string){
        Map<Role, Short> map = new HashMap<>();
        for(String pair: string.substring(1, string.length() - 1).split("[,]")){
            String[] values = pair.split("[:]");
            map.put(Role.resolveRoleById(Integer.parseInt(values[0])), Short.parseShort(values[1]));
        }
        return map;
    }
}
