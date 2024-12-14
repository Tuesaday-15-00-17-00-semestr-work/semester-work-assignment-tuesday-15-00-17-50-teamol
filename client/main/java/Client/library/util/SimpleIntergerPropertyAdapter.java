package Client.library.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public class SimpleIntergerPropertyAdapter extends TypeAdapter<SimpleIntegerProperty> {

    @Override
    public void write(JsonWriter out, SimpleIntegerProperty value) throws IOException {
        // Serialize the integer value held by the SimpleIntegerProperty
        out.value(value.get());
    }

    @Override
    public SimpleIntegerProperty read(JsonReader in) throws IOException {
        // Deserialize the JSON number into a SimpleIntegerProperty
        return new SimpleIntegerProperty(in.nextInt());
    }
}