package de.thm.draw4friends.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Home.HomeActivity;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Server.AccountService;
import de.thm.draw4friends.Server.ServiceFacade;

/**
 * Created by Yannick Bals on 16.02.2018.
 */

public class LoginActivity extends AppCompatActivity implements LoginCommunicator {

    private static final String LOGIN_MODE = "LOGIN";
    private static final String REGISTER_MODE = "REGISTER";
    private String mode = LOGIN_MODE;

    private TextView headline;
    private TextView noAccountView;
    private EditText userEdit;
    private EditText pwEdit;
    private LinearLayout registerLayout;
    private EditText pwConfirmEdit;
    private Button loginButton;

    private AccountService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new AccountService(this);

        //Try to login with the token
        service.loginWithToken();

        setContentView(R.layout.login_layout);
        this.headline = findViewById(R.id.loginHeadline);
        this.registerLayout = findViewById(R.id.registerLayout);
        this.noAccountView = findViewById(R.id.noAccountView);
        this.noAccountView.setOnClickListener(new NoAccountListener());
        this.userEdit = findViewById(R.id.loginEmailEditText);
        this.pwEdit = findViewById(R.id.loginPwEditText);
        this.pwConfirmEdit = findViewById(R.id.loginPwRepeatEditText);
        this.loginButton = findViewById(R.id.loginButton);
        this.loginButton.setOnClickListener(new LoginButtonListener());

    }

    private void startHomeActivity(User user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(getString(R.string.user_obj), user);
        startActivity(intent);
        finish();
    }

    @Override
    public void setData(Object obj) {
        try {
            User user = (User) obj;
            if (user != null) {
                startHomeActivity(user);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    class NoAccountListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            headline.setText(R.string.register);
            registerLayout.setVisibility(View.VISIBLE);
            loginButton.setText(R.string.register);
            noAccountView.setVisibility(View.GONE);
            mode = REGISTER_MODE;
        }
    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mode.equals(LOGIN_MODE)) {
                service.loginUser(userEdit.getText().toString(), pwEdit.getText().toString());
            } else if (mode.equals(REGISTER_MODE)) {
                service.registerUser(userEdit.getText().toString(), pwEdit.getText().toString(), pwConfirmEdit.getText().toString());
            }
        }
    }

}
