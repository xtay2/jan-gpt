package main.java.app.records;

import main.java.app.util.ANSI;

/**
 * @author Dennis Woithe
 */
public enum Role {

    USER, ASSISTANT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String alias(boolean colored) {
        return switch (this) {
            case USER -> colored ? ANSI.colorize("Prompt", ANSI.Color.BLUE) : "Prompt";
            case ASSISTANT -> colored ? ANSI.colorize("Jan-GPT", ANSI.Color.RED) : "Jan-GPT";
        };
    }
}