package Client.library.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class SimpleStringPropertyAdapter extends TypeAdapter<SimpleStringProperty> {

    @Override
    public void write(JsonWriter out, SimpleStringProperty value) throws IOException {
        // Serialize the string value held by SimpleStringProperty
        out.value(value.get());
    }

    @Override
    public SimpleStringProperty read(JsonReader in) throws IOException {
        // Deserialize the incoming JSON string into a SimpleStringProperty object
        return new SimpleStringProperty(in.nextString());
    }
}