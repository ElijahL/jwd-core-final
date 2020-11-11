package com.epam.jwd.core_final.domain;

import java.text.MessageFormat;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    // todo
    private static Long crewMemberIdCounter = 0L;
    private Role role;
    private Rank rank;
    private Boolean isReadyForNextMissions = true;

    public CrewMember(Role role, String name, Rank rank) {
        this.setName(name);
        this.setId(crewMemberIdCounter++);
        this.role = role;
        this.rank = rank;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(boolean isReadyForNextMissions){
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    public String toString(){
        return MessageFormat.format("[ ID: {0}; Name: {1}; Role: {2}; Rank: {3}, Ready for Next Mission: {4} ]",
                this.getId(),
                this.getName(),
                this.getRole(),
                this.getRank(),
                this.getReadyForNextMissions());
    }

    public boolean equals(CrewMember crewMember) {
        return this.getName().equals(crewMember.getName())
                && this.getRole().equals(crewMember.getRole())
                && this.getRank().equals(crewMember.getRank())
                && this.getReadyForNextMissions().equals(crewMember.getReadyForNextMissions());
    }
}
