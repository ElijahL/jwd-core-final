package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.util.Map;
import java.util.Optional;

public enum SpaceshipFactory implements EntityFactory<Spaceship> {
    INSTANCE;

    @Override
    public Spaceship create(Object... args) throws InvalidArgsException, DuplicateObjectException{
        if(checkArgs(args)) {
            Optional<Spaceship> foundSpaceship = SpaceshipServiceImpl.INSTANCE
                    .findSpaceshipByCriteria((new SpaceshipCriteria()).setName((String) args[0]));
            if(foundSpaceship.isPresent()){
                throw new DuplicateObjectException("Name {" + foundSpaceship.get().getName() + "} is already in Context");
            } else {
                return new Spaceship((String) args[0], (Long) args[1], (Map<Role, Short>) args[2]);
            }
        } else {
            throw new InvalidArgsException("Invalid arguments when trying to create Spaceship");
        }
    }

    private boolean checkArgs(Object... args){
        boolean isCorrect = args[0] instanceof String
                && args[1] instanceof Long
                && args[2] instanceof Map<?, ?>;
        if(isCorrect){
            Map.Entry<?, ?> entry = ((Map<?, ?>) args[2]).entrySet().iterator().next();
            return entry.getKey() instanceof Role && entry.getValue() instanceof Short;
        } else {
            return false;
        }
    }
}
