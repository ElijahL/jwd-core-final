package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;

public enum MissionResult {
    CANCELLED(0L),
    FAILED(1L),
    PLANNED(2L),
    IN_PROGRESS(3L),
    COMPLETED(4L);

    private final Long id;

    MissionResult(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static MissionResult resolveMissionResultById(int id) throws UnknownEntityException {
        switch (id){
            case 0:
                return CANCELLED;
            case 1:
                return FAILED;
            case 2:
                return PLANNED;
            case 3:
                return IN_PROGRESS;
            case 4:
                return COMPLETED;
            default:
                throw new UnknownEntityException("Unknown Entity", new Object[]{id});
        }
    }
}
