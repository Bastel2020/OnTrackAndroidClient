package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static android.content.ContentValues.TAG;

public class ServerRequester {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2/travelhelperbackend/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static Server service = retrofit.create(Server.class);
    private static boolean loginResult;

    public static void LoginUser(Context context, String email, String password) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("Email", email);
            postData.put("Password", password);

        } catch (
                JSONException e) {
            Toast.makeText(context, context.getText(R.string.internal_error), Toast.LENGTH_LONG);
        }
        Call<loginResult> call = service.loginUser(new loginRequest(email, password));
        call.enqueue(new Callback<loginResult>() {
            @Override
            public void onResponse(Call<loginResult> call, Response<loginResult> response) {
                if (response.isSuccessful()) {
                    DbContext db = new DbContext(context);
                    db.SaveToken(response.body().token);
                    Toast.makeText(context, DbContext.GetToken(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getText(R.string.login_wrongData), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<loginResult> call, Throwable t) {
            }
        });
        Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_LONG);
    }

    public static void RegisterUser(Context context, String email, String password, String username)
    {
        JSONObject postData = new JSONObject();
        try {
            postData.put("Email", email);
            postData.put("Password", password);
            postData.put("Username", username);
        } catch (
                JSONException e) {
            Toast.makeText(context, context.getText(R.string.internal_error), Toast.LENGTH_LONG);
        }
        Call<loginResult> call = service.RegisterUser(new registerRequest(email, password, username));
        call.enqueue(new Callback<loginResult>() {
            @Override
            public void onResponse(Call<loginResult> call, Response<loginResult> response) {
                if (response.isSuccessful()) {
                    DbContext db = new DbContext(context);
                    db.SaveToken(response.body().token);
                    Toast.makeText(context, DbContext.GetToken(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getText(R.string.login_wrongData), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<loginResult> call, Throwable t) {
            }
        });
        Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_LONG);
    }

    public static boolean IsValidToken(Context context) {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TokenCheck> call = service.CheckToken("Bearer " + token);
        call.enqueue(new Callback<TokenCheck>() {
            @Override
            public final void onResponse(Call<TokenCheck> call, Response<TokenCheck> response) {
                if (response.isSuccessful()) {
                    ServerRequester.loginResult = true;
                    Log.e(TAG, "Token check success");
                }
                else
                {
                    ServerRequester.loginResult = false;
                    Log.e(TAG, "Token check fail! " + response.code());
                }
            }

            @Override
            public final void onFailure(Call<TokenCheck> call, Throwable t) {
                ServerRequester.loginResult = false;
                Log.e(TAG, "Token check response error! " + t.getMessage());
            }
        });
        return ServerRequester.loginResult;
    }

    public static void GetCityPlaces(Context context, int cityId)
    {
        Call<List<PlaceCategoryShortInfo>> call = service.GetCityPlaces(cityId);
        call.enqueue(new Callback<List<PlaceCategoryShortInfo>>() {
            @Override
            public void onResponse(Call<List<PlaceCategoryShortInfo>> call, Response<List<PlaceCategoryShortInfo>> response) {
                if (response.isSuccessful())
                {
                    CityFragment.UpdateEntities(response.body());
                }
                else
                {
                    Log.e(TAG, "Can't get city places on Id: " + cityId + ". Error code: " + response.code());
                    Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<PlaceCategoryShortInfo>> call, Throwable t) {
                Log.e(TAG, "Can't get city places on Id: " + cityId + ". Error while send request: " + t.getMessage());
                Toast.makeText(context, context.getText(R.string.internal_error ), Toast.LENGTH_LONG);
            }
        });
    }

    public static void GetCategoryPlaces(Context context, int cityId, int categoryId)
    {
        Call<PlaceShortInfo[]> call = service.GetPlacesByCategory(cityId, categoryId);
        call.enqueue(new Callback<PlaceShortInfo[]>() {
            @Override
            public void onResponse(Call<PlaceShortInfo[]> call, Response<PlaceShortInfo[]> response) {
                if (response.isSuccessful())
                {
                    PlaceCategoryFragment.UpdateEntities(response.body());
                }
                else
                {
                    Log.e(TAG, "Can't get category places on Id: " + cityId + ". Error code: " + response.code());
                    Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<PlaceShortInfo[]> call, Throwable t) {
                Log.e(TAG, "Can't get category places on Id: " + cityId + ". Error while send request: " + t.getMessage());
                Toast.makeText(context, context.getText(R.string.internal_error ), Toast.LENGTH_LONG);
            }
        });
    }

    public static class loginResult
    {
        @SerializedName("access_token")
        public String token;
        @SerializedName("username")
        public String username;
        @SerializedName("lifetime")
        public int lifetime;
    }

    public static class UserInfo
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Username")
        public String Username;
        @SerializedName("Email")
        public String Email;
        @SerializedName("FirstName")
        public String FirstName;
        @SerializedName("SecondName")
        public String SecondName;
        @SerializedName("UserTrips")
        public TripShortInfo[] UserTrips;
    }

    public static class TripShortInfo
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
    }

    public static class PlaceCategoryShortInfo
    {
        @SerializedName("category")
        public String CategoryName;
        @SerializedName("places")
        public PlaceShortInfo[] Places;
    }

    public static class PlaceShortInfo
    {
        @SerializedName("id")
        public int Id;
        @SerializedName("name")
        public String Name;
        @SerializedName("textLocation")
        public String TextLocation;
        @SerializedName("mainPhotoUrl")
        public String MainPhotoUrl;
    }

    public static class loginRequest
    {
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;

        loginRequest(String mail, String psrd) {
            email = mail;
            password = psrd;
        }
    }

    public static class registerRequest
    {
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;
        @SerializedName("username")
        public String username;

        registerRequest(String email, String password, String username) {
            this.email = email;
            this.password = password;
            this.username = username;
        }
    }

    public static class TokenCheck
    {
        @SerializedName("result")
        public String RequestResult;
    }

    public interface Server {
        @POST("auth/signin")
        Call<loginResult> loginUser(@Body loginRequest data);
        @POST("auth/register")
        Call<loginResult> RegisterUser(@Body registerRequest data);
        @GET("auth/check")
        Call<TokenCheck> CheckToken(@Header("Authorization") String token);
        @GET("cities/{id}/placesShort")
        Call<List<PlaceCategoryShortInfo>> GetCityPlaces(@Path("id") int id);
        @GET("cities/{id}/placesByCategory/{categoryId}")
        Call<PlaceShortInfo[]> GetPlacesByCategory(@Path("id") int id, @Path("categoryId") int categoryId);
    }
}

//final class AuthorizationRequestInterceptor implements Interceptor {
//    private String token;
//
//    public AuthorizationRequestInterceptor(String token) {
//        this.token = token;
//    }
//
//    @Override public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
//        Request originalRequest = chain.request();
//        if (originalRequest.body() == null || originalRequest.header("Authorization") != null) {
//            return chain.proceed(originalRequest);
//        }
//
//        okhttp3.Request authorizedRequest = originalRequest.newBuilder()
//                .header("Authorization", token)
//                .method(originalRequest.method(), originalRequest.body())
//                .build();
//        return chain.proceed(authorizedRequest);
//    }
//}