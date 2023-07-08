package co.a3tecnology.enghaw;

import android.app.Application;

import com.squareup.otto.Bus;

public class DiscoApp extends Application {

    //evento de click através do OTTO
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();
    }

    public Bus getBus() {
        return bus;
    }
}
