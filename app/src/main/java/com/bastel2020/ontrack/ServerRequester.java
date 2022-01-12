package com.bastel2020.ontrack;

import android.content.Context;
import android.util.Log;
import android.view.View;
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
import retrofit2.http.Query;

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
                    LoginFragment.OnLogged(context, response.body());
                } else {
                    LoginFragment.OnLoginError(context);
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
                    Toast.makeText(context, "Аккаунт успешно создан", Toast.LENGTH_LONG).show();
                    RegisterFragment.OnAccountCreated(context);
                } else {
                    Toast.makeText(context, context.getText(R.string.login_wrongData), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<loginResult> call, Throwable t) {
                Toast.makeText(context, context.getText(R.string.login_wrongData), Toast.LENGTH_LONG);
            }
        });
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
                    Log.i(TAG, "Token check success");
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

    public static void GetPlaceInfo(View v, int placeId)
    {
        Call<PlaceInfo> call = service.GetPlaceInfo(placeId);
        call.enqueue(new Callback<PlaceInfo>() {
            @Override
            public void onResponse(Call<PlaceInfo> call, Response<PlaceInfo> response) {
                if (response.isSuccessful())
                {
                    PlaceFragment.UpdateEntities(v, response.body());
                }
                else
                {
                    Log.e(TAG, "Can't get place info on Id: " + placeId + ". Error code: " + response.code());
                    Toast.makeText(v.getContext(), v.getContext().getText(R.string.login_error), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<PlaceInfo> call, Throwable t) {
                Log.e(TAG, "Can't get place info on Id: " + placeId + ". Error while send request: " + t.getMessage());
                Toast.makeText(v.getContext(), v.getContext().getText(R.string.internal_error ), Toast.LENGTH_LONG);
            }
        });
    }

    public static void ChangeFavoriteState(Context context, int placeId)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<Boolean> call = service.ChangeFavoritesState("Bearer " + token, placeId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful() || (Boolean) response.body() == false)
                {
                    Log.e(TAG, "can't change favorite state on Server! Status code: " + response.code());
                }
                else
                {
                    Log.i(TAG, "favorites state changed");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "can't change favorite state on Server! Error while making request. " + t.getMessage());
            }
        });
    }

    public static void SyncFavorites(Context context)
    {
        Log.i(TAG, "favorites sync is started");
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<List<PlaceInfo>> call = service.GetFavoritesFromServer("Bearer " + token);
        call.enqueue(new Callback<List<PlaceInfo>>() {
            @Override
            public void onResponse(Call<List<PlaceInfo>> call, Response<List<PlaceInfo>> response) {
                if(response.isSuccessful())
                {
                    FavoritesLogic.SyncFavorites(response.body());
                    Log.i(TAG, "favorites are synchronized");
                }
                else { Log.e(TAG, "Sync favorites failed! Status code: " + response.code()); }
            }

            @Override
            public void onFailure(Call<List<PlaceInfo>> call, Throwable t) {
                Log.e(TAG, "Sync favorites failed! Error: " + t.getMessage());
            }
        });
    }

    public static void LoadProfileInfo(Context context)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<UserInfo> call = service.GetProfileInfo("Bearer " + token);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful())
                {
                    profileFragment.UpdateEntities(context, response.body());
                    Log.i(TAG, "Get profile info success");
                }
                else { Log.e(TAG, "Get profile info failed! Status code: " + response.code()); }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.e(TAG, "Get profile info failed! Error: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void CreateNewAction(Context context, CreateAction data)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.CreateAction(data, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "Create new action success");
                }
                else
                {
                    Log.e(TAG, "Can't create new Action. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't create new Action. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void GetTripInfo(Context context, int tripId, boolean fromTripMembers)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.GetTripInfo(tripId, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    if(response.code() == 200) {
                        if(fromTripMembers)
                            TripMembersFragment.UpdateEntities(context, response.body());
                        else
                            tripsFragment.UpdateEntities(context, response.body());
                        Log.i(TAG, "Get trip Info success");
                    }
                    if(response.code() == 204)
                    {
                        tripsFragment.ShowNoTripsFragment(context);
                    }
                }
                else
                {
                    Log.e(TAG, "can't get trip info! Status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "can't get trip info! Error while making request. " + t.getMessage());
            }
        });
    }

    public static void GetAction(Context context, long actionId)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripAction> call = service.GetActionInfo(actionId, "Bearer " + token);
        call.enqueue(new Callback<TripAction>() {
            @Override
            public void onResponse(Call<TripAction> call, Response<TripAction> response) {
                if (response.isSuccessful())
                {
                    EditActionFragment.UpdateEntities(context, response.body());
                    Log.i(TAG, "Get action Info success");
                }
                else
                {
                    Log.e(TAG, "can't get action info! Status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "can't get trip info! Error while making request. " + t.getMessage());
            }
        });
    }

    public static void EditAction(Context context, EditAction data)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.EditAction(data, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "Create new action success");
                }
                else
                {
                    Log.e(TAG, "Can't create new Action. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't create new Action. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void CreateTrip(Context context, CreateTrip data)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.CreateTrip(data, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "Create new trip success");
                    CreateTripFragment.OnTripCreated(response.body(), context);
                }
                else
                {
                    Log.e(TAG, "Can't create new trip. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't create new trip. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void JoinToTripByCode(Context context, String code)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.JoinToTripByCode(code, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "Join to trip success");
                    CreateTripFragment.OnTripCreated(response.body(), context);
                }
                else
                {
                    Log.e(TAG, "Can't join to trip. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't join to trip. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void GenerateInviteCode(Context context, int tripId)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.GenerateInviteCode(tripId, "Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "generate trip code success");
                    TripMembersFragment.UpdateEntities(context, response.body());
                }
                else
                {
                    Log.e(TAG, "Can't generate trip code. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't generate trip code. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
            }
        });
    }

    public static void ChangeUserRole(Context context, ChangeUserRole data)
    {
        DbContext db = new DbContext(context);
        String token = db.GetToken();
        Call<TripInfo> call = service.ChangeUserRole(data,"Bearer " + token);
        call.enqueue(new Callback<TripInfo>() {
            @Override
            public void onResponse(Call<TripInfo> call, Response<TripInfo> response) {
                if (response.isSuccessful())
                {
                    Log.i(TAG, "change user role success");
                    TripMembersFragment.UpdateEntities(context, response.body());
                }
                else
                {
                    Log.e(TAG, "Can't change user role. Status code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TripInfo> call, Throwable t) {
                Log.e(TAG, "Can't change user role. Error while sending request: " + t.getMessage() + " " + t.getCause().getMessage());
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

    public static class TripShortInfo
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("DestinationId")
        public int DestinationId;
        @SerializedName("DestinationName")
        public String DestinationName;
        @SerializedName("TripStart")
        public String TripStart;
        @SerializedName("TripEnd")
        public String tripEnd;
        @SerializedName("ActionsCount")
        public int placesCount;
        @SerializedName("FirstTwoUsers")
        public UserInTripShortInfo[] firstUsers;
        @SerializedName("AdditionalUserCount")
        public int AdditionalUserCount;
    }

    public static class TripInfo
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("DestinationId")
        public int DestinationId;
        @SerializedName("DestinationName")
        public String DestinationName;
        @SerializedName("InviteCode")
        public String InviteCode;
        @SerializedName("TripStart")
        public String TripStart;
        @SerializedName("TripEnd")
        public String TripEnd;
        @SerializedName("Users")
        public UserInTripInfo[] users;
        @SerializedName("TripDays")
        public TripDayInfo[] TripDays;
        @SerializedName("Polls")
        public Poll[] Polls;
        @SerializedName("RequiedRoleForInviteCode")
        public int RequiedRoleForInviteCode;
    }

    public static class TripDayInfo
    {
        @SerializedName("TripDayId")
        public int Id;
        @SerializedName("Date")
        public String Date;
        @SerializedName("DayOfWeek")
        public String DayOfWeek;
        @SerializedName("Actions")
        public TripAction[] Actions;
    }

    public static class TripAction
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("Description")
        public String Description;
        @SerializedName("Location")
        public String Location;
        @SerializedName("Files")
        public String Files;
        @SerializedName("TimeOfAction")
        public String TimeOfAction;

        public TripAction()
        {
            Name = "";
        }
    }

    public static class Poll
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Name")
        public String Name;
        @SerializedName("Description")
        public String Description;
        @SerializedName("Variants")
        public PollVariant[] PollVariants;
    }

    public class PollVariant
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Answer")
        public String Answer;
    }

    public static class UserInTripShortInfo
    {
        @SerializedName("Id")
        public int Id;
        @SerializedName("Username")
        public String Username;
        @SerializedName("AvatarPath")
        public String AvatarPath;
        @SerializedName("Role")
        public int Role;
    }
    public static class UserInTripInfo
    {
        @SerializedName("UserId")
        public int Id;
        @SerializedName("Username")
        public String Username;
        @SerializedName("AvatarPath")
        public String AvatarPath;
        @SerializedName("Role")
        public int Role;
        @SerializedName("CurrentUser")
        public boolean isCurrentUser;
    }

    public static class PlaceInfo
    {
        @SerializedName("id")
        public int Id;
        @SerializedName("name")
        public String Name;
        @SerializedName("description")
        public String Description;
        @SerializedName("placeSite")
        public String PlaceSite;
        @SerializedName("enterCost")
        public String EnterCost;
        @SerializedName("textLocation")
        public String TextLocation;
        @SerializedName("mainPhotoUrl")
        public String MainPhotoUrl;
        @SerializedName("photosUrl")
        public String[] PhotosUrls;
        @SerializedName("latitude")
        public double Latitude;
        @SerializedName("longitude")
        public double Longitude;
        @SerializedName("workingHours")
        public String WorkingHours;
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

    public static class CreateAction {
        @SerializedName("TripDayId")
        public int tripDayId;
        @SerializedName("Name")
        public String name;
        @SerializedName("Description")
        public String description;
        @SerializedName("timeOfAction")
        public String actionTime;
//        @SerializedName("Location")
//        public String location;

        CreateAction(int parentId, String name, String description, String actionTime) {
            this.tripDayId = parentId;
            this.name = name;
            this.description = description;
            this.actionTime = actionTime;
        }
    }

        public static class EditAction {
            @SerializedName("ActionId")
            public int actionId;
            @SerializedName("Name")
            public String name;
            @SerializedName("Description")
            public String description;
            @SerializedName("TimeOfAction")
            public String actionTime;
//        @SerializedName("Location")
//        public String location;

            EditAction(int parentId, String name, String description, String actionTime) {
                this.actionId = parentId;
                this.name = name;
                this.description = description;
                this.actionTime = actionTime;
            }
        }

    public static class CreateTrip {
        @SerializedName("tripDestanation")
        public int tripDestination;
        @SerializedName("name")
        public String name;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;

        CreateTrip(int tripDestination, String name, String startDate, String endDate) {
            this.tripDestination = tripDestination;
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    public static class ChangeUserRole {
        @SerializedName("tripId")
        public int tripId;
        @SerializedName("userToChangeId")
        public int userToChangeId;
        @SerializedName("newRoleId")
        public int newRoleId;

        ChangeUserRole(int tripId, int userToChangeId, int newRoleId) {
            this.tripId = tripId;
            this.userToChangeId = userToChangeId;
            this.newRoleId = newRoleId;
        }
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
        @GET("cities/places/{placeId}")
        Call<PlaceInfo> GetPlaceInfo(@Path("placeId") int placeId);
        @GET("user/Favorites/AddOrRemove")
        Call<Boolean> ChangeFavoritesState(@Header("Authorization") String token, @Query("placeId") int placeId);
        @GET("user/Favorites")
        Call<List<PlaceInfo>> GetFavoritesFromServer(@Header("Authorization") String token);
        @GET("user")
        Call<UserInfo> GetProfileInfo(@Header("Authorization") String token);
        @GET("trips/GetAction/{id}")
        Call<TripAction> GetActionInfo(@Path("id") long id, @Header("Authorization") String token);
        @GET("trips/{id}")
        Call<TripInfo> GetTripInfo(@Path("id") int id, @Header("Authorization") String token);
        @POST("trips/addAction")
        Call<TripInfo> CreateAction(@Body CreateAction data, @Header("Authorization") String token);
        @POST("trips/EditAction")
        Call<TripInfo> EditAction(@Body EditAction data, @Header("Authorization") String token);
        @POST("trips/Create")
        Call<TripInfo> CreateTrip(@Body CreateTrip data, @Header("Authorization") String token);
        @POST("trips/Join")
        Call<TripInfo> JoinToTripByCode(@Body String code, @Header("Authorization") String token);
        @GET("trips/{id}/GenerateInvite")
        Call<TripInfo> GenerateInviteCode(@Path("id") int tripId, @Header("Authorization") String token);
        @POST("trips/ChangeRole")
        Call<TripInfo> ChangeUserRole(@Body ChangeUserRole data, @Header("Authorization") String token);
    }
}
