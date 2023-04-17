package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_activity extends AppCompatActivity {


    private Button btnUpdate;
    private CircleImageView userProfileImage;
    private EditText updateUsername, updatePhoneNumber;
    private TextView forgotPass, updatedEmailProfile, updatedUserNameProfile;
    boolean imageControl = false;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri imageUri;
    String image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

           userProfileImage = findViewById(R.id.UserProfileImage);
           updatePhoneNumber = findViewById(R.id.updatePhoneNumber);
           updateUsername = findViewById(R.id.updateUsername);
           forgotPass = findViewById(R.id.forgotPass);
           btnUpdate = findViewById(R.id.btnUpdate);

           // textview name
           updatedUserNameProfile = findViewById(R.id.updatedUserNameProfile);
           updatedEmailProfile = findViewById(R.id.updatedEmailProfile);


        forgotPass.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(Profile_activity.this, forgotPassword_activity.class));
              }
          });



        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        getUserInfo();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

    }

    private void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && requestCode == RESULT_OK && data != null){
            imageUri = data.getData();
            //imageViewCircle.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(userProfileImage);
            imageControl = true;
        }
        else
        {
            imageControl = false;
        }
    }



    private void updateProfile()
    {
        String userName = updateUsername.getText().toString();
        String phone = updatePhoneNumber.getText().toString();

        reference.child("Users").child(firebaseUser.getUid()).child("userName").setValue(userName);
        reference.child("Users").child(firebaseUser.getUid()).child("phone").setValue(phone);

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
                            reference.child("users").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText( Profile_activity.this, "write to database is successful.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText( Profile_activity.this, "There is not successful.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            reference.child("Users").child(auth.getUid()).child("image").setValue(image);
        }
        Toast.makeText( Profile_activity.this, "Profile Updated successful.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Profile_activity.this, MainActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("phone",phone );
        startActivity(intent);
        finish();

    }

    private void getUserInfo(){
        reference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                updateUsername.setText(name);
                updatePhoneNumber.setText(phone);
                updatedUserNameProfile.setText(name);
                updatedEmailProfile.setText(email);

                if (image.equals("null")){
                    userProfileImage.setImageResource(R.drawable.profiledefault);
                }
                else {
                    Picasso.get().load(image).into(userProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}