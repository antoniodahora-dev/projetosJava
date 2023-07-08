package co.a3tecnology.retrofit;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);

        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Necessário preencher", Toast.LENGTH_LONG).show();
                } else {

                    login();
//                    getUsers();
                }
            }
        });
    }

    private static String name;

    public void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Usuário oK", Toast.LENGTH_LONG).show();

                    LoginResponse loginResponse = response.body();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this,
                                    MenuActivity.class).putExtra("email", loginResponse.getEmail()));

//                            Toast.makeText(MainActivity.this, response.body().getName(),
//                                    Toast.LENGTH_LONG).show();
//                            name = response.body().getName();
                        }
                    }, 700);
                } else {
                    Toast.makeText(MainActivity.this, "falhou conexão",
                            Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "falhou conexão", Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void getUsers() {
//       Call<ResponseBody> call = ApiClient.getUserService().getUsers(name);
//
//       call.enqueue(new Callback<ResponseBody>() {
//           @Override
//           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//               try {
//                   Toast.makeText(MainActivity.this, response.body().string(),
//                           Toast.LENGTH_LONG).show();
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//
//           }
//
//           @Override
//           public void onFailure(Call<ResponseBody> call, Throwable t) {
//               Toast.makeText(MainActivity.this, "falhou token",
//                       Toast.LENGTH_LONG).show();
//           }
//       });
//
//
//    }
}