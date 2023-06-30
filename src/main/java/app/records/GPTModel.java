package main.java.app.records;

/**
 * @author Dennis Woithe
 */
public enum GPTModel {

    GPT_3_5("gpt-3.5-turbo"), GPT_4("gpt-4");

    public final String modelName;

    GPTModel(String modelName) {
        this.modelName = modelName;
    }
}