package by.sergei.testbanktmapp.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class LocalDateGsonAdapter: TypeAdapter<LocalDate>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    override fun write(jsonWriter: JsonWriter, value: LocalDate) {
        jsonWriter.value(formatter.format(value))
    }

    override fun read(jsonReader: JsonReader): LocalDate {
       return LocalDate.parse(jsonReader.nextString(), formatter)
    }
}