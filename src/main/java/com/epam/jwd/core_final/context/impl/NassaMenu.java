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
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.LoggerFactory;

public enum NassaMenu implements ApplicationMenu {
    INSTANCE;

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.getInstance();
    }

    public void startMenu() throws IOException{
        System.out.println("###  Welcome to Nassa!  ###\n" +
                        "# Please, make yourself at home. :)\n" +
                        "# To use our services conveniently, please follow the instructions.");
        handleUserInput();
    }

    @Override
    public void printAvailableOptions() {
        if(LocalDateTime.now().getMinute() - NassaContext.getInstance().getInitializationTime().getMinute()
                >= NassaContext.getInstance().getRefreshRate()){
            NassaContext.getInstance().init(NassaContext.getInstance().getProperties());
        }
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "0. Exit\n" +
                "1. Create new mission\n" +
                "2. Print all crew members\n" +
                "3. Print all spaceships\n" +
                "4. Print all missions\n" +
                "5. Find Crew Member by Name\n" +
                "6. Find Spaceship by Name\n" +
                "7. Create JSON of mission and put in a file\n" +
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
                break;

            case 1:
                createNewMission();
                break;

            case 2:
                printList(CrewServiceImpl.INSTANCE.findAllCrewMembers());
                break;

            case 3:
                printList(SpaceshipServiceImpl.INSTANCE.findAllSpaceships());
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

    private void createNewMission(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name: ");
        String name = scanner.nextLine();

        DateTimeFormatter formatter = NassaContext.getInstance().getFormatter();
        System.out.println("Start Date: (Format: " + formatter.toString() + ")");
        String dateTime = scanner.nextLine();
        LocalDateTime startDate = LocalDateTime.parse(dateTime, formatter);

        System.out.println("End Date: (Format: " + formatter.toString() + ")");
        dateTime = scanner.nextLine();
        LocalDateTime endDate = LocalDateTime.parse(dateTime, formatter);

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
        String name = (new Scanner(System.in)).nextLine().split("[\n]")[0];
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
        String name = (new Scanner(System.in)).nextLine().split("[\n]")[0];
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
        Optional<Spaceship> optionalSpaceship =  SpaceshipServiceImpl.INSTANCE
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
        for(FlightMission flightMission: MissionServiceImpl.INSTANCE.findAllMissions()){
            objectMapper.writeValue(new File(MessageFormat
                            .format("src/main/resources/output/mission{0}.json", flightMission.getId())),
                    flightMission);
            System.out.println(objectMapper.writeValueAsString(flightMission));
            Logger logger = NassaContext.getInstance().getLogger();
            logger.info("Util JSON is created");
        }
    }

    private void reinitialization(){

    }
}
