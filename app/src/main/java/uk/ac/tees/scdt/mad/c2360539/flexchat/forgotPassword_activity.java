package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword_activity extends AppCompatActivity {

    private Button btnSubmitForgotPassword;
    private EditText edtEmailForgotPassword;
    private TextView signUpButtonForgotPassword ;

    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_activity);

        btnSubmitForgotPassword = findViewById(R.id.btnSubmitForgotPassword);
        edtEmailForgotPassword = findViewById(R.id.edtEmailForgotPassword);

        btnSubmitForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPassword = edtEmailForgotPassword.getText().toString().trim();
                if(!emailPassword.equals(""))
                {
                    passwordReset(emailPassword);
                }
            }
        });

        auth = FirebaseAuth.getInstance();


        signUpButtonForgotPassword = findViewById(R.id.SignUpButtonForgotPassword);
        signUpButtonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(forgotPassword_activity.this,signup_activity.class));
            }
        });

    }

    public void passwordReset(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText( forgotPassword_activity.this, "Please check your email.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText( forgotPassword_activity.this, "There is a problem.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}