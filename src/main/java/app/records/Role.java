package main.java.app.records;

/**
 * @author Dennis Woithe
 */
public enum Role {

    USER, ASSISTANT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}