package co.a3tecnology.testapp.aulaDesignPatterns;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<String> names = new ArrayList<>();
    private Database() {
        names.add("Antonio");
        names.add("Da Hora");
        names.add("Neto");
    }

    private static Database INSTANCE;

    public static Database getInstance() {

        if (INSTANCE == null) {
            return new Database();
        }

            return INSTANCE;

    }

    public List<String> getNames() {
        return names;
    }
}
