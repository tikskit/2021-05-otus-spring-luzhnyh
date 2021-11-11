package ru.tikskit.service;

public class InappropriateProductValue extends IllegalArgumentException{
    public InappropriateProductValue(String s) {
        super(s);
    }
}
