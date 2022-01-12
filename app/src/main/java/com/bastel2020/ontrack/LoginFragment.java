package com.bastel2020.ontrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
            }
        });
        return  view;
    }

    public static void OnLogged(Context context, ServerRequester.loginResult response)
    {
        DbContext db = new DbContext(context);
        db.SaveToken(response.token);

        AppCompatActivity activity = (AppCompatActivity)context;
        FragmentManager fm = activity.getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; i++)
        {
            fm.popBackStack();
        }
        ServerRequester.IsValidToken(context);

        Helpers.loadFragment(new MainFragment(), activity.getSupportFragmentManager());

        Toast.makeText(context, "Успешный вход", Toast.LENGTH_LONG).show();
    }

    public static void OnLoginError(Context context)
    {
        Toast.makeText(context, context.getText(R.string.login_wrongData), Toast.LENGTH_LONG).show();
    }

//    public class loginResult
//    {
//        @SerializedName("access_token")
//        public String token;
//        @SerializedName("username")
//        public String username;
//        @SerializedName("lifetime")
//        public int lifetime;
//    }

//    public class loginRequest
//    {
//        @SerializedName("email")
//        public String email;
//        @SerializedName("password")
//        public String password;
//
//        loginRequest(String mail, String psrd) {
//            email = mail;
//            password = psrd;
//        }
//    }
//
//    public interface Server {
//        @POST("auth/signin")
//        Call<loginResult> loginUser(@Body loginRequest data);
//    }
}