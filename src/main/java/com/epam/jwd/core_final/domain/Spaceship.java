package com.epam.jwd.core_final.domain;

import java.text.MessageFormat;
import java.util.Map;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class Spaceship extends AbstractBaseEntity {
    //todo
    private static Long spaceshipIdCounter = 0L;
    private Map<Role, Short> crew;
    private Long flightDistance;
    private Boolean isReadyForNextMissions = true;

    public Spaceship(String name, Long flightDistance, Map<Role, Short> crew) {
        this.setName(name);
        this.setId(spaceshipIdCounter++);
        this.crew = crew;
        this.flightDistance = flightDistance;
    }

    public Map<Role, Short> getCrew() {
        return crew;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(Boolean readiness) {
        isReadyForNextMissions = readiness;
    }

    public String toString(){
        return MessageFormat.format("[ ID: {0}; Name: {1}; Crew: {2}; FlightDistance: {3}; ReadyForNextMission: {4} ]",
                this.getId(), this.getName(),
                this.getCrew().toString(), this.getFlightDistance(),
                this.getReadyForNextMissions());
    }

    public boolean equals(Spaceship spaceship){
        return spaceship.getName().equals(this.getName())
                && spaceship.getCrew().equals(this.crew)
                && spaceship.getFlightDistance().equals(this.flightDistance)
                && spaceship.getReadyForNextMissions().equals((this.isReadyForNextMissions));
    }
}
