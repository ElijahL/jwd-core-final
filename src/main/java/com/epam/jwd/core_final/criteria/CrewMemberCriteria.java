package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private String name = null;
    private Role role = null;
    private Rank rank = null;
    private Boolean isReadyForNextMissions = null;

    private Map<String, Boolean> fieldsExistence = new HashMap<String, Boolean>(){{
        put("name", false);
        put("role", false);
        put("rank", false);
        put("readiness", true);
    }};
    private Map<String, Boolean> fieldsMatches = new HashMap<>(fieldsExistence);

    public CrewMemberCriteria setName(String name) {
        this.name = name;
        fieldsExistence.replace("name", true);
        return null;
    }

    public CrewMemberCriteria setRole(Role role) {
        this.role = role;
        fieldsExistence.replace("role", true);
        return this;
    }

    public CrewMemberCriteria setRank(Rank rank) {
        this.rank = rank;
        fieldsExistence.replace("rank", true);
        return this;
    }

    public CrewMemberCriteria setIsReadyForNextMission(Boolean readiness){
        this.isReadyForNextMissions = readiness;
        fieldsExistence.replace("readiness", true);
        return this;
    }

    public boolean matches(CrewMember crewMember) {
        fieldsMatches.replace("name", crewMember.getName().equals(this.name));
        fieldsMatches.replace("role", crewMember.getRole().equals(this.role));
        fieldsMatches.replace("rank", crewMember.getRank().equals(this.rank));
        fieldsMatches.replace("readiness", crewMember.getReadyForNextMissions() == this.isReadyForNextMissions);

        for(Map.Entry<String, Boolean> entry: fieldsExistence.entrySet()){
            if(entry.getValue()){
                if(!fieldsMatches.get(entry.getKey())){
                    return false;
                }
            }
        }

        return true;
    }

    public String toString(){
        return MessageFormat.format("[ Name: {0}, Role: {1}, Rank: {2}, Ready: {3} ]",
                this.name,
                this.role,
                this.rank,
                this.isReadyForNextMissions);
    }

    @Override
    public CrewMember build(){
        return new CrewMember(role, name, rank);
    }
}
