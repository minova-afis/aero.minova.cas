package aero.minova.cas.setup.xml.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true, value = { "identity" })
public class Table {
    private final static XmlMapper XML_MAPPER = new XmlMapper();
    private boolean identity = false;
    private String name;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Column> column = new ArrayList<>();
    private PrimaryKey primarykey;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<UniqueKey> uniquekey = new ArrayList<>();
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ForeignKey> foreignkey = new ArrayList<>();
    private Values values;

    public static Table parse(Path file) {
        try {
            return parse(Files.readString(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Table parse(String document) {
        try {
            return XML_MAPPER.readValue(document, Table.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Table parse(InputStream is) {
        try {
            final Table result = XML_MAPPER.readValue(is, Table.class);
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Column addNewColumn(){
        final Column column = new Column();
        this.column.add(column);
        return column;
    }
}