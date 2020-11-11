package com.epam.jwd.core_final.strategy;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.exception.DuplicateObjectException;
import com.epam.jwd.core_final.exception.InvalidArgsException;

import java.io.FileNotFoundException;
import java.util.Collection;

public class Reader {
    private BasicReader basicReader;

    public Reader(){ }

    public void setReader(BasicReader basicReader){
        this.basicReader = basicReader;
    }

    public <T extends BaseEntity> Collection<T> read(String rootDir, String fileName) throws FileNotFoundException, InvalidArgsException, DuplicateObjectException {
        return basicReader.read(rootDir, fileName);
    }
}
