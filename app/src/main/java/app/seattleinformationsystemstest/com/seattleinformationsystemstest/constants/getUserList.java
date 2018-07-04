package app.seattleinformationsystemstest.com.seattleinformationsystemstest.constants;

import java.io.IOException;
import java.util.List;

import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUser;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUserDetails;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class getUserList {

    public final static String getAllUserListApi = "https://api.github.com/";

    public static GetService getService = null;


    public static GetService getService() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Content-Type", "application/json; charset=utf-8").build();
                return chain.proceed(request);
            }
        });

        if (getService==null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getAllUserListApi)
                    .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                    .build();
            getService = retrofit.create(GetService.class);

        }

        return getService;

    }

    public interface GetService {


        @GET("users")
        Call<List<DemoUserDetails>> getAllUserList();

        @GET("users/{login}")
        Call<DemoUser> getSingleUser(@Path("login") String user_name);

        @GET("users/{login}/followers")
        Call<List<DemoUserDetails>> getUserFollowers(@Path("login") String user_name);

    }
}
