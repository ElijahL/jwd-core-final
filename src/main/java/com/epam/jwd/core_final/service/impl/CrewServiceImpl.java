package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CrewServiceImpl implements CrewService {
    INSTANCE;

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return (List<CrewMember>) NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class);
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<CrewMember> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class).stream()
                .filter(criteria::matches)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<CrewMember> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class).stream()
                .filter(e -> ((CrewMemberCriteria) criteria).matches(e))
                .findFirst();
    }

    public CrewMember findCrewMember(CrewMember crewMember) throws ServiceException {
        Optional<CrewMember> optionalCrewMember = NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class).stream()
                .filter(crewMember::equals)
                .findFirst();
        if(optionalCrewMember.isPresent()){
            return optionalCrewMember.get();
        } else {
            throw new ServiceException("Cant find a CrewMember with given parameters.");
        }
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMemberCriteria findByThis, CrewMember updateLikeThat) throws ServiceException {
        Optional<CrewMember> optionalCrewMember = findCrewMemberByCriteria(findByThis);
        CrewMember crewMember;
        if(optionalCrewMember.isPresent()){
            crewMember = optionalCrewMember.get();
            crewMember.setName(updateLikeThat.getName());
            crewMember.setRank(updateLikeThat.getRank());
            crewMember.setRole(updateLikeThat.getRole());
            crewMember.setReadyForNextMissions(updateLikeThat.getReadyForNextMissions());
        } else {
            throw new ServiceException("Cant find a CrewMember with given parameters.");
        }
        return crewMember;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws ServiceException {
        findCrewMember(crewMember).setReadyForNextMissions(false);
    }

    @Override
    public CrewMember createCrewMember(Role role, String name, Rank rank) throws ServiceException, DuplicateObjectException {
        CrewMember crewMember = CrewMemberFactory.INSTANCE.create(role, name, rank);
        NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class).add(crewMember);
        return crewMember;
    }
}
