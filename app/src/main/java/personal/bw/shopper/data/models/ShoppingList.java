package personal.bw.shopper.data.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable(tableName = "shoppingList")

public class ShoppingList implements Serializable {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public final static String ID_FIELD_NAME = "id";
    public final static String NAME_FIELD_NAME = "name";
    public final static String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private Long id;

    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;

    @DatabaseField(columnName = DATE_FIELD_NAME)
    private Date date;

    ShoppingList() {
    }

    public ShoppingList(String name) {
        this.name = name;
        this.date = new Date();
    }

    public ShoppingList(Long id, String name) {
        this(name);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getFormattedDate() {
        return DATE_FORMAT.format(date);
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
