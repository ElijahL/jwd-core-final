package com.epam.jwd.core_final.domain;

/**
 * Expected fields:
 * <p>
 * id {@link Long} - entity id
 * name {@link String} - entity name
 */
public abstract class AbstractBaseEntity implements BaseEntity {
    private Long id;
    private String name;

    @Override
    public Long getId() {
        // todo
        return id;
    }

    void setId(Long id){
        this.id = id;
    }

    @Override
    public String getName() {
        // todo
        return name;
    }

    void setName(String name){
        this.name = name;
    }
}
