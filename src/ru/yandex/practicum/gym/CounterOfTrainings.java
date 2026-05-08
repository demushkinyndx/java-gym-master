package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private final Coach coach;
    private int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Coach getCoach() {
        return coach;
    }

    @Override
    public String toString() {
        return coach + " : " + count;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return count - o.getCount();
    }

    public void incrementCount() {
        count++;
    }
}