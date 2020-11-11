package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

public enum FlightMissionFactory implements EntityFactory<FlightMission>{
    INSTANCE;

    @Override
    public FlightMission create(Object... args) throws InvalidArgsException {
        if(checkArgs(args)){
            Spaceship spaceship = assignSpaceship();
            List<CrewMember> crewMembers = assignCrewMembers(spaceship);
            return new FlightMission((String) args[0],
                    (LocalDateTime) args[1],
                    (LocalDateTime) args[2],
                    (Long) args[3],
                    spaceship,
                    crewMembers,
                    getRandomMissionResult());
        } else {
            throw new InvalidArgsException("Invalid arguments when trying create FlightMission.");
        }
    }

    private Spaceship assignSpaceship(){
        Optional<Spaceship> optionalSpaceship =  SpaceshipServiceImpl.INSTANCE
                .findSpaceshipByCriteria(new SpaceshipCriteria().setIsReadyForNextMission(true));
        SpaceshipServiceImpl.INSTANCE.assignSpaceshipOnMission(optionalSpaceship.get());
        return optionalSpaceship.get();
    }

    private List<CrewMember> assignCrewMembers(Spaceship spaceship){
        List<CrewMember> crewMembers = new ArrayList<>();
        for(Map.Entry<Role, Short> entry: spaceship.getCrew().entrySet()){
            for(int i = 0; i < entry.getValue(); ++i){
                Optional<CrewMember> optionalCrewMember = CrewServiceImpl.INSTANCE.findCrewMemberByCriteria(new CrewMemberCriteria()
                        .setRole(entry.getKey())
                        .setIsReadyForNextMission(true));
                CrewServiceImpl.INSTANCE.assignCrewMemberOnMission(optionalCrewMember.get());
                crewMembers.add(optionalCrewMember.get());
            }
        }
        return crewMembers;
    }

    private boolean checkArgs(Object... args){
        return args[0] instanceof String
                && args[1] instanceof LocalDateTime
                && args[2] instanceof LocalDateTime
                && args[3] instanceof Long;
    }

    private MissionResult getRandomMissionResult(){
        List<MissionResult> values =
                (List<MissionResult>) Collections.unmodifiableList(Arrays.asList(MissionResult.values()));
        return values.get(new Random().nextInt(values.size()));
    }
}
