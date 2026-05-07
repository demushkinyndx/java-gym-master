package ru.yandex.practicum.gym;

public enum Age {
    CHILD("Дети"),
    ADULT("Взрослые");

    private final String title;

    Age(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
