package co.a3tecnology.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

   @POST("sessions")
   Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

//   @GET("users/{name}")
//   Call<LoginResponse> getUser(@Path("name") String name);
//   @GET("sessions")
//   Call<ResponseBody> getUsers(@Body LoginRequest loginRequest);
}
