package Client.library.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleIntegerProperty;

public class GsonConfig {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SimpleIntegerProperty.class, new SimpleIntergerPropertyAdapter())
            .create();

    public static Gson getGsonInstance() {
        return gson;
    }
}
