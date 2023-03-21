package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.regex.Pattern;

public class signup_activity extends AppCompatActivity {


    //View Binding
    //private ActivityRegisterBinding binding;

    private EditText edtUsername, edtPassword, edtEmail, edtconfirmPassword;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    private TextView textView8 ;

    boolean isSigningUp ;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        firebaseAuth = FirebaseAuth.getInstance();



        textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        edtEmail = findViewById(R.id.signup_Email);
        edtPassword = findViewById(R.id.signup_Password);
        edtconfirmPassword = findViewById(R.id.signup_confirmPassword);
        edtUsername = findViewById(R.id.signup_Username);
        btnSubmit = findViewById(R.id.signup_btnSubmit);


        // handle click, register
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validDate();
            }
        });



//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
//                    if (isSigningUp && edtUsername.getText().toString().isEmpty()){
//                        Toast.makeText(signup_activity.this, "Invalid input", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//                if (isSigningUp){
//                    handleSignUp();
//                }
//            }
//        });
    }

    private String name = "", email = "", password ="";
    private void validDate() {
        /*Before creating account, lets do some data validation*/
        name = edtUsername.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        String cPassword = edtconfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText( this, "Enter your username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText( this, "Enter your email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText( this, "Enter your Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(cPassword)){
            Toast.makeText( this, "Enter your Confirm Password", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(cPassword)){
            Toast.makeText( this, "Password Entered does not match", Toast.LENGTH_SHORT).show();
        }else {
            CreateAccount();
        }

    }

    private void CreateAccount() {

        // firebase Auth Create
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {
                        UpdateUserInfo();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText( signup_activity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void UpdateUserInfo() {

        //time stamp
        long time = System.currentTimeMillis();

        String uid = firebaseAuth.getUid();

        //set data to add in the Database
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", time);

        // data base reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "user");
        ref.child(uid).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText( signup_activity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signup_activity.this, dashboard_activity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( signup_activity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void goToLoginActivity(){
        Intent intent = new Intent(signup_activity.this,login_activity.class);
        startActivity(intent);
    }

    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(signup_activity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(signup_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}