package currency.converter.demo.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Component
public class UnixEpochDateTypeAdapter extends TypeAdapter<Date> {

    // adapter для корректной работы GSON с Unix time (дата полученная через внешнее api в этом формате)
    @Override
    public Date read(JsonReader in)
            throws IOException {
        return Date.from(Instant.ofEpochSecond(in.nextLong()));
    }

    @Override
    public void write(JsonWriter out, Date value)
            throws IOException {
        out.value(value.getTime());
    }
}
