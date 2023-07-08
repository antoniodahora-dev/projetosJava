package co.a3tecnology.retrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.sql.DriverManager.println;

public class MenuActivity extends AppCompatActivity {

    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        name = findViewById(R.id.userName);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String passedUserName = intent.getStringExtra("email");
            name.setText("Olá "+passedUserName);

        }
    }

}
