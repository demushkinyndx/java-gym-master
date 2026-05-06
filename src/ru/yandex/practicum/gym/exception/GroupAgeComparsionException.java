package ru.yandex.practicum.gym.exception;

public class GroupAgeComparsionException extends TrainingException {
    public GroupAgeComparsionException() {
        super("Указанная группа уже создана и не соответствует выбранному возрасту");
    }
}
