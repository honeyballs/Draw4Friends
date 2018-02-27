package de.thm.draw4friends.Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Login.LoginActivity;
import de.thm.draw4friends.Login.TokenGenerator;
import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class AccountService {

    private Communicator communicator;
    private Context context;

    public AccountService(Communicator communicator) {
        this.communicator = communicator;
        this.context = (AppCompatActivity) communicator;
    }

    public void registerUser(String username, String pw, String pwConfirm) {
        new RegisterTask().execute(username, pw, pwConfirm);
    }

    public void loginUser(String username, String pw) {
        new LoginTask().execute(username, pw);
    }

    public void loginWithToken() {
        new LoginTokenTask().execute();
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(context.getString(R.string.login_token), token);
                editor.apply();
                //Create the user object and store it in the database
                user = new User();
                user.setUsername(username);
                user.setPassword(pw);
                user.setToken(token);
                Database db = Database.getDatabaseInstance(context);
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
                communicator.setData(user);
            } else {
                Toast.makeText(context, context.getString(R.string.register_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    class LoginTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            String username = strings[0];
            String pw = strings[1];
            Database db = Database.getDatabaseInstance(context);
            User user = db.userDAO().loginWithInfo(username, pw);
            if (user != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(context.getString(R.string.login_token), user.getToken());
                editor.apply();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                communicator.setData(user);
            } else {
                Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    class LoginTokenTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            User user = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String token = prefs.getString(context.getString(R.string.login_token), "");
            if (token != "") {
                Database db = Database.getDatabaseInstance(context);
                user = db.userDAO().loginWithToken(token);
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                communicator.setData(user);
            }
        }
    }

}
