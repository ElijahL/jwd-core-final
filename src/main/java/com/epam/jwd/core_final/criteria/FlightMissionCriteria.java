package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long distance;
    private Spaceship assignedSpaceShift;
    private List<CrewMember> assignedCrew;
    private MissionResult missionResult;

    private Map<String, Boolean> fieldsExistence = new HashMap<String, Boolean>() {{
        put("name", false);
        put("startDate", false);
        put("endDate", false);
        put("distance", false);
        put("assignedSpaceShift", false);
        put("assignedCrew", false);
        put("missionResult", false);
    }};
    private Map<String, Boolean> fieldsMatches = new HashMap<>(fieldsExistence);

    public FlightMissionCriteria setName(String name){
        this.name = name;
        fieldsExistence.replace("name", true);
        return this;
    }

    public FlightMissionCriteria setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        fieldsExistence.replace("startDate", true);
        return this;
    }

    public FlightMissionCriteria setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        fieldsExistence.replace("endDate", true);
        return this;
    }

    public FlightMissionCriteria setDistance(Long distance) {
        this.distance = distance;
        fieldsExistence.replace("distance", true);
        return this;
    }

    public FlightMissionCriteria setAssignedSpaceShift(Spaceship assignedSpaceShift) {
        this.assignedSpaceShift = assignedSpaceShift;
        fieldsExistence.replace("assignedSpaceShift", true);
        return this;
    }

    public FlightMissionCriteria setAssignedCrew(List<CrewMember> assignedCrew) {
        this.assignedCrew = assignedCrew;
        fieldsExistence.replace("assignedCrew", true);
        return this;
    }

    public FlightMissionCriteria setMissionResult(MissionResult missionResult) {
        this.missionResult = missionResult;
        fieldsExistence.replace("missionResult", true);
        return this;
    }

    public boolean matches(FlightMission flightMission) {
        fieldsMatches.replace("name", flightMission.getName().equals(this.name));
        fieldsMatches.replace("startDate", flightMission.getStartDate().equals(this.startDate));
        fieldsMatches.replace("endDate", flightMission.getEndDate().equals(this.endDate));
        fieldsMatches.replace("distance", flightMission.getDistance().equals(this.distance));
        fieldsMatches.replace("assignedSpaceShift", flightMission.getAssignedSpaceShift().equals(this.assignedSpaceShift));
        fieldsMatches.replace("assignedCrew", flightMission.getAssignedCrew().equals(this.assignedCrew));
        fieldsMatches.replace("missionResult", flightMission.getMissionResult().equals(this.missionResult));

        for(Map.Entry<String, Boolean> entry: fieldsExistence.entrySet()){
            if(entry.getValue()){
                if(!fieldsMatches.get(entry.getKey())){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public FlightMission build(){
        return new FlightMission(name, startDate, endDate, distance,
                assignedSpaceShift, assignedCrew, missionResult);
    }
}
