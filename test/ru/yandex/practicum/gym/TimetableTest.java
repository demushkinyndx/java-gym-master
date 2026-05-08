package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.exception.CoachIsBusyException;
import ru.yandex.practicum.gym.exception.GroupAgeComparsionException;
import ru.yandex.practicum.gym.exception.TrainingException;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() throws TrainingException {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));

    }


    @Test
    void testGetTrainingSessionsForDayMultipleSessions() throws TrainingException {
        Timetable timetable = getTimetable();

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Assertions.assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size());

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> trainingSessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals("13:00", trainingSessions.firstKey().toString());
        Assertions.assertEquals("20:00", trainingSessions.lastKey().toString());

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    private static Timetable getTimetable() throws TrainingException {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        return timetable;
    }


    @Test
    void testGetTrainingSessionsForDayAndTime() throws TrainingException {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
    }


    @Test
    void testAddTrainingSessions() throws TrainingException {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для детей", Age.ADULT, 60);
        Coach coach = new Coach("Цзю", "Константин", "Борисович");

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));

        //добавляется существующая группа, но для другого возраста
/*        Assertions.assertThrows(GroupAgeComparsionException.class, () -> {
            timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        });*/

        //пытаемся добавить тренера, но он в это время уже занят
        Assertions.assertThrows(CoachIsBusyException.class, () -> {
            timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        });
    }


    @Test
    void testCouchesStats() throws TrainingException {
        Timetable timetable = getTimetable();
        Coach coach1 = new Coach("Цзю", "Константин", "Борисович");
        Coach coach2 = new Coach("Валуев", "Николай", "Сергеевич");
        Group groupAdult = new Group("Бокс для всех", Age.ADULT, 60);
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach2, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> coachesList = new ArrayList<>(timetable.getCountByCoaches());
        CounterOfTrainings lastEntry = coachesList.getLast();
        CounterOfTrainings firstEntry = coachesList.getFirst();


        Assertions.assertEquals("Васильев Николай Сергеевич", firstEntry.getCoach().toString());
        Assertions.assertEquals("Валуев Николай Сергеевич", lastEntry.getCoach().toString());

    }
}
