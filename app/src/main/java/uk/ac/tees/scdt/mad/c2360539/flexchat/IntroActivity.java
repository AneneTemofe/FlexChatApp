package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private  Button button;
    private  Button buttonTwo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpActivity();
            }
        });

        buttonTwo = (Button) findViewById(R.id.button2);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });
    }

    public void goToSignUpActivity(){
        Intent intent = new Intent(IntroActivity.this,signup_activity.class);
        startActivity(intent);
    }

    public void goToLoginActivity(){
        Intent intent = new Intent(IntroActivity.this,login_activity.class);
        startActivity(intent);
    }


}