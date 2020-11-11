package com.epam.jwd.core_final.strategy;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;

import java.io.FileNotFoundException;
import java.util.Collection;

public interface BasicReader {
    public <T extends BaseEntity> Collection<T> read(String rootDir, String fileName) throws FileNotFoundException, InvalidArgsException, DuplicateObjectException;
}
