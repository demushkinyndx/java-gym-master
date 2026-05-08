package ru.yandex.practicum.gym.exception;

public class CoachIsBusyException extends TrainingException {
    public CoachIsBusyException() {
        super("Тренер уже занят в выбранное время");
    }
}
