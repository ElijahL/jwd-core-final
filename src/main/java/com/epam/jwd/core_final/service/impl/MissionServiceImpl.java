package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;

import javax.xml.ws.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum MissionServiceImpl implements MissionService {
    INSTANCE;

    @Override
    public List<FlightMission> findAllMissions() {
        return (List<FlightMission>) NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class);
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class).stream()
                .filter(((FlightMissionCriteria) criteria)::matches)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class).stream()
                .filter(((FlightMissionCriteria) criteria)::matches)
                .findFirst();
    }

    @Override
    public FlightMission updateMissionDetails(FlightMissionCriteria findByThis, FlightMission updateLikeThat) throws ServiceException {
        Optional<FlightMission> optionalFlightMission = findMissionByCriteria(findByThis);
        FlightMission flightMission;
        if(optionalFlightMission.isPresent()){
            flightMission = optionalFlightMission.get();
            flightMission.setName(updateLikeThat.getName());
            flightMission.setStartDate(updateLikeThat.getStartDate());
            flightMission.setEndDate(updateLikeThat.getEndDate());
            flightMission.setDistance(updateLikeThat.getDistance());
            flightMission.setAssignedSpaceShift(updateLikeThat.getAssignedSpaceShift());
            flightMission.setAssignedCrew(updateLikeThat.getAssignedCrew());
            flightMission.setMissionResult(updateLikeThat.getMissionResult());
        } else {
            throw new ServiceException("Cannot find mission with given parameters.");
        }
        return flightMission;
    }

    @Override
    public FlightMission createMission(String name,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       Long distance,
                                       Spaceship assignedSpaceShift,
                                       List<CrewMember> assignedCrew,
                                       MissionResult missionResult) {
        FlightMission flightMission = FlightMissionFactory.INSTANCE.create(name,
                startDate,
                endDate,
                distance,
                assignedSpaceShift,
                assignedCrew,
                missionResult);
        NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class).add(flightMission);
        return flightMission;
    }
}
