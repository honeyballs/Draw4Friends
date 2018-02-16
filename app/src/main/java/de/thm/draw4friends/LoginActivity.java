package de.thm.draw4friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yannick Bals on 16.02.2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_MODE = "LOGIN";
    private static final String REGISTER_MODE = "REGISTER";
    private String mode = LOGIN_MODE;

    private TextView noAccountView;
    private EditText mailEdit;
    private EditText pwEdit;
    private LinearLayout registerLayout;
    private EditText pwConfirmEdit;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
        this.registerLayout = findViewById(R.id.registerLayout);
        this.noAccountView = findViewById(R.id.noAccountView);
        this.noAccountView.setOnClickListener(new NoAccountListener());
        this.mailEdit = findViewById(R.id.loginEmailEditText);
        this.pwEdit = findViewById(R.id.loginPwEditText);
        this.pwConfirmEdit = findViewById(R.id.loginPwRepeatEditText);
        this.loginButton = findViewById(R.id.loginButton);

    }


    class NoAccountListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            registerLayout.setVisibility(View.VISIBLE);
            loginButton.setText(R.string.register);
            noAccountView.setVisibility(View.GONE);
        }
    }
}
