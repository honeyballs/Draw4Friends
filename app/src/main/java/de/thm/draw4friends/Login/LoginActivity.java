package de.thm.draw4friends.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Yannick Bals on 16.02.2018.
 */

public class LoginActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Try to login with the token
        new LoginTokenTask().execute();

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
                new LoginTask().execute(userEdit.getText().toString(), pwEdit.getText().toString());
            } else if (mode.equals(REGISTER_MODE)) {
                new RegisterTask().execute(userEdit.getText().toString(), pwEdit.getText().toString(), pwConfirmEdit.getText().toString());
            }
        }
    }

    class RegisterTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            String username = strings[0];
            String pw = strings[1];
            String pwConfirm = strings[2];
            User user = null;
            if (pw.equals(pwConfirm)) {
                //Generate a token and save it to the shared preferences
                String token = TokenGenerator.generateToken();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.login_token), token);
                editor.apply();
                //Create the user object and store it in the database
                user = new User();
                user.setUsername(username);
                user.setPassword(pw);
                user.setToken(token);
                Database db = Database.getDatabaseInstance(LoginActivity.this);
                long success = db.userDAO().insertUser(user);
                //Load the user from the db to get access to the id
                if (success != 0) {
                    user = db.userDAO().findUser(username);
                } else {
                    user = null;
                }
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                startHomeActivity(user);
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.register_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    class LoginTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            String username = strings[0];
            String pw = strings[1];
            Database db = Database.getDatabaseInstance(LoginActivity.this);
            User user = db.userDAO().loginWithInfo(username, pw);
            if (user != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.login_token), user.getToken());
                editor.apply();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                startHomeActivity(user);
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    class LoginTokenTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            User user = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            String token = prefs.getString(getString(R.string.login_token), "");
            if (token != "") {
                Database db = Database.getDatabaseInstance(LoginActivity.this);
                user = db.userDAO().loginWithToken(token);
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                startHomeActivity(user);
            }
        }
    }
}
