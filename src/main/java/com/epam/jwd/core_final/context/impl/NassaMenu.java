package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public enum NassaMenu implements ApplicationMenu {
    INSTANCE;

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.getInstance();
    }

    public void startMenu() throws IOException{
        System.out.println(
                "# # # # #  Welcome to Nassa!  # # # # #\n" +
                "#  Please, make yourself at home. :)  #\n" +
                "#  To use our services conveniently,  #\n" +
                "#  please follow the instructions.    #\n" +
                "# # # # # # # # # # # # # # # # # # # #");
        handleUserInput();
    }

    @Override
    public void printAvailableOptions() {
        if(LocalDateTime.now().getMinute() - NassaContext.getInstance().getInitializationTime().getMinute()
                >= NassaContext.getInstance().getRefreshRate()){
            NassaContext.getInstance().init(NassaContext.getInstance().getProperties());
        }
        System.out.println(
                "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "0. Exit\n" +
                "1. Create new mission\n" +
                "2. Print all crew members\n" +
                "3. Print all spaceships\n" +
                "4. Print all missions\n" +
                "5. Find Crew Member by Name\n" +
                "6. Find Spaceship by Name\n" +
                "7. Print mission in JSON format\n" +
                "   to console and file\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

    @Override
    public void handleUserInput() throws IOException {
        Scanner scanner = new Scanner(System.in);

        int request = -1;
        while(request != 0){
            printAvailableOptions();
            request = scanner.nextInt();
            response(request);
        }
    }

    private void response(int request) throws IOException{
        switch (request){
            case 0:
                System.out.println(
                        "# # # # # # # # # # # # # # # #\n" +
                        "# Bye!  Hope to see you soon! #\n" +
                        "# # # # # # # # # # # # # # # #");
                break;
            case 1:
                createNewMission();
                break;
            case 2:
                printCrewMembers();
                break;
            case 3:
                printSpaceships();
                break;
            case 4:
                printList(MissionServiceImpl.INSTANCE.findAllMissions());
                break;
            case 5:
                findCrewMemberByName();
                break;
            case 6:
                findSpaceshipByName();
                break;
            case 7:
                createAndPrintJSON();
                break;
            default:
                System.out.println("It seems you made a mistake. Try one more time! Check given menu :)\n");
                break;
        }
    }

    private <T extends BaseEntity> void printList(List<T> list){
        for(T element: list){
            System.out.println(element);
        }
    }

    private void printCrewMembers(){
        String breakLine = "+-----+------------------------------+--------------------+----------------+-----------+";
        System.out.println(breakLine + "\n"
                + "| id  | Name                         | Role               | Rank           | Ready     |\n"
                + breakLine);
        for(CrewMember crewMember: CrewServiceImpl.INSTANCE.findAllCrewMembers()){
            System.out.format("| %-4d| %-29s| %-19s| %-15s| %-10s|\n",
                    crewMember.getId(),
                    crewMember.getName(),
                    crewMember.getRole().toString(),
                    crewMember.getRank().toString(),
                    crewMember.getReadyForNextMissions() ? "ready" : "not ready");
            System.out.println(breakLine);
        }
    }

    private void printSpaceships(){
        System.out.println(
                  "+-----+--------------------+-------------------------+-----------------+----------+\n"
                + "| id  | Name               | Crew                    | Flight Distance | Ready    |\n"
                + "+-----+--------------------+-------------------------+-----------------+----------+"
        );
        for(Spaceship spaceship: SpaceshipServiceImpl.INSTANCE.findAllSpaceships()){
            String firstLineFormat = "| %-4d| %-19s| %-19s: %-3d| %-16d| %-9s|\n";
            String nextLinesFormat = "| %-4s| %-19s| %-19s: %-3d| %-16s| %-9s|\n";
            System.out.format(firstLineFormat,
                    spaceship.getId(),
                    spaceship.getName(),
                    Role.MISSION_SPECIALIST.toString(), spaceship.getCrew().get(Role.MISSION_SPECIALIST),
                    spaceship.getFlightDistance(),
                    spaceship.getReadyForNextMissions() ? "ready" : "not ready");
            System.out.format(nextLinesFormat,
                    "", "",
                    Role.COMMANDER.toString(), spaceship.getCrew().get(Role.COMMANDER),
                    "", "");
            System.out.format(nextLinesFormat,
                    "", "",
                    Role.FLIGHT_ENGINEER.toString(), spaceship.getCrew().get(Role.COMMANDER),
                    "", "");
            System.out.format(nextLinesFormat,
                    "", "",
                    Role.PILOT.toString(), spaceship.getCrew().get(Role.COMMANDER),
                    "", "");
            System.out.println(
                    "+-----+--------------------+-------------------------+-----------------+----------+");
        }
    }

    private void printMissions(){
        String breakLine =
                  "+-----+--------------------+---------------------+---------------------+---------------+-------------------------+--------+";
        System.out.println(breakLine + "\n"
                + "| id  | Name               | Start Date          | End Date            | Distance      | Spaceship               | Result |\n"
                + breakLine);
        for(FlightMission mission: MissionServiceImpl.INSTANCE.findAllMissions()){
            System.out.format("| %-4d| %-19s| %-20s| %-20s| %-14d| %-24s| %-7s",
                    mission.getId(),
                    mission.getName(),
                    mission.getStartDate().toString(),
                    mission.getEndDate().toString(),
                    mission.getDistance(),
                    mission.getAssignedSpaceShift().getName(),
                    mission.getMissionResult().toString());
        }
    }

    private void createNewMission(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name: ");
        String name = scanner.nextLine();

        DateFormat format = new SimpleDateFormat(NassaContext.getInstance().getFormat());
        System.out.println("Start Date: (Format: " + format.toString() + ")");
        String dateTime = scanner.nextLine();
        LocalDateTime startDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format.toString()));

        System.out.println("End Date: (Format: " + format.toString() + ")");
        dateTime = scanner.nextLine();
        LocalDateTime endDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(format.toString()));

        System.out.println("Distance: (Long)");
        Long distance = scanner.nextLong();

        Spaceship spaceship = assignSpaceship();

        NassaContext.getInstance()
                .retrieveBaseEntityList(FlightMission.class)
                .add(FlightMissionFactory.INSTANCE.create(name, startDate, endDate,
                        distance, spaceship, assignCrewMembers(spaceship), MissionResult.PLANNED));
    }

    private CrewMember findCrewMemberByName(){
        System.out.println("Name:");
        String name = (new Scanner(System.in))
                .nextLine()
                .split("[\n]")[0];
        Optional<CrewMember> optionalCrewMember = CrewServiceImpl.INSTANCE
                .findCrewMemberByCriteria(new CrewMemberCriteria().setName(name));
        if(optionalCrewMember.isPresent()){
            System.out.println(optionalCrewMember.get());
            return optionalCrewMember.get();
        } else {
            System.out.println("There is no Crew Member with Name: " + name + "\n");
            return null;
        }
    }

    private Spaceship findSpaceshipByName(){
        System.out.println("Name:");
        String name = (new Scanner(System.in))
                .nextLine()
                .split("[\n]")[0];
        Optional<Spaceship> optionalSpaceship = SpaceshipServiceImpl.INSTANCE
                .findSpaceshipByCriteria((new SpaceshipCriteria()).setName(name));
        if(optionalSpaceship.isPresent()){
            System.out.println(optionalSpaceship.get());
            return optionalSpaceship.get();
        } else {
            System.out.println("There is no Spaceship with Name: " + name + "\n");
            return null;
        }
    }

    private Spaceship assignSpaceship(){
        Optional<Spaceship> optionalSpaceship = SpaceshipServiceImpl.INSTANCE
                .findSpaceshipByCriteria(new SpaceshipCriteria().setIsReadyForNextMission(true));
        SpaceshipServiceImpl.INSTANCE.assignSpaceshipOnMission(optionalSpaceship.get());
        return optionalSpaceship.get();
    }

    private List<CrewMember> assignCrewMembers(Spaceship spaceship){
        List<CrewMember> crewMembers = new ArrayList<>();
        for(Map.Entry<Role, Short> entry: spaceship.getCrew().entrySet()){
            for(int i = 0; i < entry.getValue(); ++i){
                Optional<CrewMember> optionalCrewMember = CrewServiceImpl.INSTANCE.findCrewMemberByCriteria(new CrewMemberCriteria()
                        .setRole(entry.getKey())
                        .setIsReadyForNextMission(true));
                CrewServiceImpl.INSTANCE.assignCrewMemberOnMission(optionalCrewMember.get());
                crewMembers.add(optionalCrewMember.get());
            }
        }
        return crewMembers;
    }

    private void createAndPrintJSON() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setDateFormat(new SimpleDateFormat(NassaContext.getInstance().getFormat()));
        for(FlightMission flightMission: MissionServiceImpl.INSTANCE.findAllMissions()){
            objectMapper.writeValue(new File(MessageFormat
                            .format("src/main/resources/output/mission{0}.json", flightMission.getId())),
                    flightMission);
            System.out.println(objectMapper.writeValueAsString(flightMission));
            Logger logger = NassaContext.getInstance().getLogger();
            logger.info("Util JSON is created");
        }
    }
}
