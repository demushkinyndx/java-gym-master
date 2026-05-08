package ru.yandex.practicum.gym;

/**
 * @param hours   часы (от 0 до 23)
 * @param minutes минуты (от 0 до 59)
 */
public record TimeOfDay(int hours, int minutes) implements Comparable<TimeOfDay> {

    @Override
    public String toString() {
        return hours + ":" + String.format("%02d", minutes);
    }

    @Override
    public int compareTo(TimeOfDay o) {
        if (hours != o.hours) return hours - o.hours;
        return minutes - o.minutes;
    }

}
