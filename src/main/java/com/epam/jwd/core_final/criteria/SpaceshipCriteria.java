package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

import java.util.HashMap;
import java.util.Map;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {
    private String name;
    private Map<Role, Short> crew;
    private Long flightDistance;
    private Boolean isReadyForNextMission;

    private Map<String, Boolean> fieldsExistence = new HashMap<String, Boolean>() {{
        put("name", false);
        put("crew", false);
        put("flightDistance", false);
        put("isReadyForNextMission", false);
    }};
    private Map<String, Boolean> fieldsMatches = new HashMap<>(fieldsExistence);

    public SpaceshipCriteria setName(String name){
        this.name = name;
        fieldsExistence.replace("name", true);
        return this;
    }

    public SpaceshipCriteria setCrew(Map<Role, Short> crew) {
        this.crew = crew;
        fieldsExistence.replace("crew", true);
        return this;
    }

    public SpaceshipCriteria setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
        fieldsExistence.replace("flightDistance", true);
        return this;
    }

    public SpaceshipCriteria setIsReadyForNextMission(Boolean readiness){
        this.isReadyForNextMission = readiness;
        fieldsExistence.replace("isReadyForNextMission", true);
        return this;
    }

    public boolean matches(Spaceship spaceship) {
        fieldsMatches.replace("name", spaceship.getName().equals((this.name)));
        fieldsMatches.replace("crew", spaceship.getCrew().equals(this.crew));
        fieldsMatches.replace("flightDistance", spaceship.getFlightDistance().equals(this.flightDistance));
        fieldsMatches.replace("isReadyForNextMission", spaceship.getReadyForNextMissions().equals(this.isReadyForNextMission));

        for (Map.Entry<String, Boolean> entry: fieldsExistence.entrySet()){
            if(entry.getValue()){
                if(!fieldsMatches.get(entry.getKey())){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Spaceship build(){
        return new Spaceship(name, flightDistance, crew);
    }
}
