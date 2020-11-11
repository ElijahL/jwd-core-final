package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.strategy.BasicReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class CrewFileReader implements BasicReader {
    public Collection<CrewMember> read(String rootDir, String fileName) throws FileNotFoundException, InvalidArgsException, DuplicateObjectException {
        Collection<CrewMember> crewMembersData;
        Scanner reader = new Scanner(new File("src/main/resources"
                + "/" + rootDir
                + "/" + fileName));
        crewMembersData = parseCrewData(reader);
        return crewMembersData;
    }

    private Collection<CrewMember> parseCrewData(Scanner reader) throws InvalidArgsException, DuplicateObjectException {
        Collection<CrewMember> crewMembers = new ArrayList<>();
        while(reader.hasNextLine()){
            String nextLine = reader.nextLine();
            if(nextLine.charAt(0) != '#'){
                String[] crewMemberDatas = nextLine.split("[;]");
                for (String crewMemberData: crewMemberDatas){
                    String[] arguments = crewMemberData.split("[,]");
                    crewMembers.add(CrewMemberFactory.INSTANCE.create(Role.resolveRoleById(Integer.parseInt(arguments[0])),
                            arguments[1],
                            Rank.resolveRankById(Integer.parseInt(arguments[2]))));
                }
            }
        }
        reader.close();
        return crewMembers;
    }
}
