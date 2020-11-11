package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.util.Map;
import java.util.Optional;

// do the same for other entities
public enum CrewMemberFactory implements EntityFactory<CrewMember> {
    INSTANCE;

    @Override
    public CrewMember create(Object... args) throws InvalidArgsException, DuplicateObjectException {
        if(checkArgs(args)) {
            Optional<CrewMember> foundCrewMember = CrewServiceImpl.INSTANCE
                    .findCrewMemberByCriteria(new CrewMemberCriteria().setName((String) args[1]));
            if(foundCrewMember.isPresent()){
                throw new DuplicateObjectException("Name {" + foundCrewMember.get().getName() + "} is already in the context");
            } else {
                return new CrewMember((Role) args[0], (String) args[1], (Rank) args[2]);
            }
        } else {
            throw new InvalidArgsException("Invalid arguments when trying to create CrewMember");
        }
    }

    private boolean checkArgs(Object... args){
        return args[0] instanceof Role
                && args[1] instanceof String
                && args[2] instanceof Rank
                && !CrewServiceImpl.INSTANCE
                .findCrewMemberByCriteria(new CrewMemberCriteria().setName((String) args[1]))
                .isPresent();
    }
}
