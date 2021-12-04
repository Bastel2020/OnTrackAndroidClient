package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Button loginButton;
    private EditText signInEmail, signInPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        signInEmail = view.findViewById(R.id.loginEmailField);
        signInPassword = view.findViewById(R.id.loginPasswordField);

        loginButton = view.findViewById(R.id.loginButton2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerRequester.LoginUser(view.getContext(), signInEmail.getText().toString(), signInPassword.getText().toString());
//                JSONObject postData = new JSONObject();
//                try {
//                    postData.put("Email", signInEmail.getText().toString());
//                    postData.put("Password", signInPassword.getText().toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(getString(R.string.server_base))
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                Server service = retrofit.create(Server.class);
//                Call<loginResult> call = service.loginUser(new loginRequest(signInEmail.getText().toString(), signInPassword.getText().toString()));
//                call.enqueue(new Callback<loginResult>()
//                {
//                    @Override
//                    public void onResponse(Call<loginResult> call, Response<loginResult> response)
//                    {
//                        if (response.isSuccessful())
//                        {
//                            DbContext db = new DbContext(v.getContext());
//                            db.SaveToken(response.body().token);
//                            Toast.makeText(v.getContext(), DbContext.GetToken(), Toast.LENGTH_LONG).show();
//                        }
//                        else
//                            {
//                            Toast.makeText(v.getContext(), getString(R.string.login_wrongData), Toast.LENGTH_LONG);
//                            }
//                        return;
//                    }
//
//                    @Override
//                    public void onFailure(Call<loginResult> call, Throwable t) { }
//                });
//                Toast.makeText(v.getContext(), getString(R.string.login_error), Toast.LENGTH_LONG);
            }
        });
        return  view;
    }

    public class loginResult
    {
        @SerializedName("access_token")
        public String token;
        @SerializedName("username")
        public String username;
        @SerializedName("lifetime")
        public int lifetime;
    }

    public class loginRequest
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

    public interface Server {
        @POST("auth/signin")
        Call<loginResult> loginUser(@Body loginRequest data);
    }
}