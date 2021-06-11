package ru.tikskit.service;

class PlayerInfo {
    private final String name;
    private final String surname;

    public PlayerInfo(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, surname);
    }
}
