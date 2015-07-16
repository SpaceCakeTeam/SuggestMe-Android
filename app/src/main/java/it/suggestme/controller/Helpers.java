package it.suggestme.controller;

public class Helpers {

    private static Helpers mInstance;

    private Helpers() {
        if (mInstance == null)
            mInstance = new Helpers();
    }
}
