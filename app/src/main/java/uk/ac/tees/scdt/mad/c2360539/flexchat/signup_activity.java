package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup_activity extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;


    //View Binding
    //private ActivityRegisterBinding binding;

    private EditText edtUsername, edtPassword, edtEmail, edtconfirmPassword;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    private TextView textView8 ;
    private CircleImageView imageViewCircle;
    boolean imageControl = false;

    boolean isSigningUp ;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        imageViewCircle = findViewById(R.id.imageViewCircle);
        edtEmail = findViewById(R.id.signup_Email);
        edtPassword = findViewById(R.id.signup_Password);
        edtconfirmPassword = findViewById(R.id.signup_confirmPassword);
        edtUsername = findViewById(R.id.signup_Username);
        btnSubmit = findViewById(R.id.signup_btnSubmit);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });


        imageViewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

//        // handle click, register
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                validDate();
//            }
//        });


        // New Signup with image upload
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtconfirmPassword.getText().toString().trim();
                
                
                if (!email.equals("") && !name.equals("") && !password.equals("") && !confirmPassword.equals("")){
                    signup(email,name,password,confirmPassword);
                }

            }
        });
    }

    private void signup(String email, String name, String password, String confirmPassword) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference.child("Users").child(firebaseAuth.getUid()).child("userName").setValue(name);
                    reference.child("Users").child(firebaseAuth.getUid()).child("email").setValue(email);
                    reference.child("Users").child(firebaseAuth.getUid()).child("phone").setValue("");

                    if (imageControl){
                        UUID randomID = UUID.randomUUID();
                        String imageName  = "images/"+randomID+"jpg";
                        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference myStorageRef  = firebaseStorage.getReference(imageName);
                                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        reference.child("users").child(firebaseAuth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText( signup_activity.this, "write to database is successful.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText( signup_activity.this, "There is not successful.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else{
                        reference.child("Users").child(firebaseAuth.getUid()).child("image").setValue("null");
                    }

                    Intent intent = new Intent(signup_activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText( signup_activity.this, "There is a problem.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    private void imageChooser(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//
//    }

    private void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1 && requestCode == RESULT_OK && data != null){
//            imageUri = data.getData();
//            //imageViewCircle.setImageURI(imageUri);
//            Picasso.get().load(imageUri).into(imageViewCircle);
//            imageControl = true;
//        }
//        else
//        {
//            Toast.makeText( signup_activity.this, "There is a problem with image", Toast.LENGTH_SHORT).show();
//            imageControl = false;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && requestCode == RESULT_OK && data != null){
            imageUri = data.getData();
            //imageViewCircle.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(imageViewCircle);
            imageControl = true;
        }
        else
        {
            Toast.makeText( signup_activity.this, "There is a problem with image", Toast.LENGTH_SHORT).show();
            imageControl = false;
        }
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
                    startActivity(new Intent(signup_activity.this, MainActivity.class));
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