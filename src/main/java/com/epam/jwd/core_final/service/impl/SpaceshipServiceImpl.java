package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum SpaceshipServiceImpl implements SpaceshipService {
    INSTANCE;

    @Override
    public List<Spaceship> findAllSpaceships() {
        return (List<Spaceship>) NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class);
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class).stream()
                .filter(((SpaceshipCriteria) criteria)::matches)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        return NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class).stream()
                .filter(e -> ((SpaceshipCriteria) criteria).matches(e))
                .findFirst();
    }

    public Spaceship findSpaceship(Spaceship spaceship) throws ServiceException {
        Optional<Spaceship> optionalSpaceship = NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class).stream()
                .filter(spaceship::equals)
                .findFirst();
        if(optionalSpaceship.isPresent()){
            return optionalSpaceship.get();
        } else {
            throw new ServiceException("Cant find a CrewMember with given parameters.");
        }
    }

    // todo: And for the rest implementations too
    @Override
    public Spaceship updateSpaceshipDetails(SpaceshipCriteria findByThis, Spaceship updateLikeThat) {
        Optional<Spaceship> optionalSpaceship = findSpaceshipByCriteria(findByThis);
        Spaceship spaceship;
        if(optionalSpaceship.isPresent()){
            spaceship = optionalSpaceship.get();
            spaceship.setName(updateLikeThat.getName());
            spaceship.setFlightDistance(updateLikeThat.getFlightDistance());
            spaceship.setCrew(updateLikeThat.getCrew());
            spaceship.setReadyForNextMissions(updateLikeThat.getReadyForNextMissions());
        } else {
            throw new ServiceException("Cannot find mission with given parameters.");
        }
        return spaceship;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship) throws RuntimeException {
        findSpaceship(spaceship).setReadyForNextMissions(false);
    }

    @Override
    public Spaceship createSpaceship(String name, Long flightDistance, Map<Role, Short> crew) throws RuntimeException, DuplicateObjectException {
        Spaceship spaceship = SpaceshipFactory.INSTANCE.create(name, flightDistance, crew);
        NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class).add(spaceship);
        return spaceship;
    }
}
