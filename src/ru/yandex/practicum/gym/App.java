package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.exception.TrainingException;
import ru.yandex.practicum.gym.utils.InputHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class App {
    private final Timetable timetable;

    public App() {
        timetable = new Timetable();
    }


    public void run() {
        while (true) {
            System.out.println("1 - Добавление новой тренировки в расписание");
            System.out.println("2 - Вывести все тренировки за конкретный день недели");
            System.out.println("3 - Вывести все тренировки за конкретный день недели в конкретное время");
            System.out.println("4 - Вывести количество тренировок у каждого тренера");
            System.out.println("0 - Завершить программу");
            int command = InputHandler.getInt("Введите команду:");
            switch (command) {
                case 1:
                    addTrainingSession();
                    break;
                case 2:
                    getTrainingSessionsForDay();
                    break;
                case 3:
                    getTrainingSessionsForDayAndTime();
                    break;
                case 4:
                    displayCouchesStats();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный ввод!");
                    break;
            }
        }
    }

    private void displayCouchesStats() {
        for (CounterOfTrainings stats : timetable.getCountByCoaches()) {
            System.out.println(stats);
        }
    }

    private void getTrainingSessionsForDayAndTime() {
        DayOfWeek dayOfWeek = readDayOfWeekInput();
        TimeOfDay timeOfDay = readTimeOfDayInput();
        ArrayList<TrainingSession> trainingSessions = timetable.getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);
        if (trainingSessions == null) {
            System.out.println("Вы выбранное время тренировок нет");
            return;
        }
        for (TrainingSession trainingSession : trainingSessions) {
            displayTrainingSession(trainingSession);
        }
    }

    private void displayTrainingSession(TrainingSession trainingSession) {
        System.out.println(
                "["
                        + trainingSession.getGroup().getAge().getTitle()
                        + "] " + trainingSession.getGroup().getTitle()
                        + ", продолжительность "
                        + trainingSession.getGroup().getDuration()
                        + " минут, тренер "
                        + trainingSession.getCoach().toString()
        );
    }

    private DayOfWeek readDayOfWeekInput() {
        int dayNumber = InputHandler.getIntInRange("Введите номер дня недели (1-7 - понедельник-воскресенье):", 1, 7);
        return DayOfWeek.values()[dayNumber - 1];
    }

    private TimeOfDay readTimeOfDayInput() {
        int hours = InputHandler.getIntInRange("Введите часы:", 0, 23);
        int minutes = InputHandler.getIntInRange("Введите минуты:", 0, 59);
        return new TimeOfDay(hours, minutes);
    }

    private Coach readCoachInput() {
        String surname = InputHandler.getString("Введите фамилию:");
        String name = InputHandler.getString("Введите имя:");
        String middleName = InputHandler.getString("Введите отчество:");
        return new Coach(surname, name, middleName);
    }

    private void getTrainingSessionsForDay() {
        DayOfWeek dayOfWeek = readDayOfWeekInput();

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> dailyTrainings = timetable.getTrainingSessionsForDay(dayOfWeek);
        if (dailyTrainings == null) {
            System.out.println("В выбранный день тренировок не запланировано");
            return;
        }
        for (Map.Entry<TimeOfDay, ArrayList<TrainingSession>> entrySet : dailyTrainings.entrySet()) {
            System.out.println(entrySet.getKey().toString());
            for (TrainingSession trainingSession : entrySet.getValue()) {
                displayTrainingSession(trainingSession);
            }
        }
    }

    private void addTrainingSession() {
        System.out.println("Добавление новой тренировки.");
        DayOfWeek dayOfWeek = readDayOfWeekInput();
        TimeOfDay timeOfDay = readTimeOfDayInput();
        System.out.println();

        int groupNumber = InputHandler.getIntInRange("Введите тип группы (1 - дети, 2 - взрослые)", 1, 2);
        Age age = Age.values()[groupNumber - 1];

        String groupTitle = InputHandler.getString("Введите название группы: ");
        int duration = InputHandler.getIntInRange("Введите продолжительность занятия в минутах (15 - 180): ", 15, 180);

        Group group = new Group(groupTitle, age, duration);

        System.out.println("Введите данные тренера (ФИО)");
        Coach coach = readCoachInput();

        TrainingSession trainingSession = new TrainingSession(group, coach, dayOfWeek, timeOfDay);
        try {
            timetable.addNewTrainingSession(trainingSession);
        } catch (TrainingException e) {
            System.out.println("Запись не добавлена." + e.getMessage());
        }
    }
}
