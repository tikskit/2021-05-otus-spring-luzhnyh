package ru.tikskit.service;

public class WrongProductValue extends IllegalArgumentException{
    public WrongProductValue(String s) {
        super(s);
    }
}
