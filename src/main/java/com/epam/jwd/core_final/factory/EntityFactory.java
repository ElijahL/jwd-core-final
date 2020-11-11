package com.epam.jwd.core_final.factory;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;

public interface EntityFactory<T extends BaseEntity> {
    T create(Object... args) throws InvalidArgsException, DuplicateObjectException;
}
