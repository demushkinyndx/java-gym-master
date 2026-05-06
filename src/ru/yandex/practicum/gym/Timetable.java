package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.exception.CoachIsBusyException;
import ru.yandex.practicum.gym.exception.GroupAgeComparsionException;
import ru.yandex.practicum.gym.exception.TrainingException;

import java.util.*;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) throws TrainingException {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> trainingsForDay = this.timetable.computeIfAbsent(dayOfWeek, k -> new TreeMap<>());
        this.timetable.put(dayOfWeek, trainingsForDay);

        ArrayList<TrainingSession> listOfTrainings = trainingsForDay.computeIfAbsent(time, k -> new ArrayList<>());

        this.checkScheduleParameters(trainingSession);

        listOfTrainings.add(trainingSession);
    }

    private void checkScheduleParameters(TrainingSession trainingSession) throws TrainingException {

        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> entrySet : this.timetable.entrySet()) {
            for (ArrayList<TrainingSession> tsArray : entrySet.getValue().values()) {
                if (tsArray != null) {
                    for (TrainingSession ts : tsArray) {
                        if (
                                entrySet.getKey().equals(trainingSession.getDayOfWeek())
                                && ts.getTimeOfDay().equals(trainingSession.getTimeOfDay())
                        ) {
                            //нужно проверить тренера
                            if (ts.getCoach().equals(trainingSession.getCoach())) {
                                throw new CoachIsBusyException();
                            }
                        }

                        if (
                                ts.getGroup().getTitle().equals(trainingSession.getGroup().getTitle())
                                && !ts.getGroup().getAge().equals(trainingSession.getGroup().getAge())
                        ) {
                            //проверка соответствия группы возрасту
                            throw new GroupAgeComparsionException();
                        }
                    }
                }
            }
        }
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return this.timetable.computeIfAbsent(dayOfWeek, k -> null);
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        if (!this.timetable.containsKey(dayOfWeek)) {
            //нет тренировок в этот день
            return null;
        }

        if (!this.timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            //в выбранное время нет тренировок
            return null;
        }
        return this.timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> counterMap = new HashMap<>();

        for (TreeMap<TimeOfDay, ArrayList<TrainingSession>> daySchedules : this.timetable.values()) {
            for (ArrayList<TrainingSession> sessions : daySchedules.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    CounterOfTrainings counter = counterMap.computeIfAbsent(coach, c -> new CounterOfTrainings(c, 0));
                    counter.incrementCount();
                }
            }
        }

        List<CounterOfTrainings> list = new ArrayList<>(counterMap.values());
        list.sort(Comparator.reverseOrder());
        return list;
    }
}
