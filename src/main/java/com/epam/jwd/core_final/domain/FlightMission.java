package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Expected fields:
 * <p>
 * missions name {@link String}
 * start date {@link java.time.LocalDateTime}
 * end date {@link java.time.LocalDateTime}
 * distance {@link Long} - missions distance
 * assignedSpaceShift {@link Spaceship} - not defined by default
 * assignedCrew {@link java.util.List<CrewMember>} - list of missions members based on ship capacity - not defined by default
 * missionResult {@link MissionResult}
 */
public class FlightMission extends AbstractBaseEntity {
    // todo
    private static Long flightMissionIdCounter = 0L;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long distance;
    private Spaceship assignedSpaceShift;
    private List<CrewMember> assignedCrew;
    private MissionResult missionResult;

    public FlightMission(String name,
                         LocalDateTime startDate,
                         LocalDateTime endDate,
                         Long distance,
                         Spaceship assignedSpaceShift,
                         List<CrewMember> assignedCrew,
                         MissionResult missionResult) {
        this.setName(name);
        this.setId(flightMissionIdCounter++);
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.assignedSpaceShift = assignedSpaceShift;
        this.assignedCrew = assignedCrew;
        this.missionResult = missionResult;
    }

    public static void setFlightMissionIdCounter(Long flightMissionIdCounter) {
        FlightMission.flightMissionIdCounter = flightMissionIdCounter;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public void setAssignedSpaceShift(Spaceship assignedSpaceShift) {
        this.assignedSpaceShift = assignedSpaceShift;
    }

    public void setAssignedCrew(List<CrewMember> assignedCrew) {
        this.assignedCrew = assignedCrew;
    }

    public void setMissionResult(MissionResult missionResult) {
        this.missionResult = missionResult;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getDistance() {
        return distance;
    }

    public Spaceship getAssignedSpaceShift() {
        return assignedSpaceShift;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public String toString(){
        return MessageFormat.format("[ ID: {0}; Name: {1};" +
                " Start Date: {2}; End Date: {3};" +
                " Distance: {4}; AssignedSpaceShift: {5}; AssignedCrew: {6}; MissionResult: {7} ]",
                this.getId(), this.getName(),
                this.getStartDate(), this.getEndDate(),
                this.getDistance(), this.getAssignedSpaceShift(), this.getAssignedCrew(), this.getMissionResult());
    }

    public boolean equals(FlightMission flightMission){
        return flightMission.getName().equals(this.getName())
                && flightMission.getStartDate().equals(this.startDate)
                && flightMission.getEndDate().equals(this.endDate)
                && flightMission.getDistance().equals((this.distance))
                && flightMission.getAssignedSpaceShift().equals(this.assignedSpaceShift)
                && flightMission.getAssignedCrew().equals(this.assignedCrew)
                && flightMission.getMissionResult().equals(this.missionResult);
    }
}
