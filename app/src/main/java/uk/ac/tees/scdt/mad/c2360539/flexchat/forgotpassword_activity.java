package uk.ac.tees.scdt.mad.c2360539.flexchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class forgotpassword_activity extends AppCompatActivity {

    private TextView textView8 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_activity);

        textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });
    }


    public void goToLoginActivity(){
        Intent intent = new Intent(forgotpassword_activity.this,login_activity.class);
        startActivity(intent);
    }

}