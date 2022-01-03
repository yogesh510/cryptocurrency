package com.firstapp.cryptoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://cryptoc-befcb-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById( R.id.password);
        final Button LoginBtn = findViewById(  R.id.LoginBtn);
        final TextView registerNowBtn = findViewById( R.id.registerNowBtn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phoneTxt=phone.getText().toString();
                final String passwordTxt=password.getText().toString();

                if(phoneTxt.isEmpty() || passwordTxt.isEmpty())
                {
                    Toast.makeText(Login.this,"please enter your Mobile or massword",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // check mobile num are exit in firebase db
                            if(snapshot.hasChild(phoneTxt)) {
                                //mob are exit in fdb
                                //now check password matching in fdb
                                final String getpasswordTxt = snapshot.child(phoneTxt).child("Password").getValue(String.class);

                                if (getpasswordTxt.equals(passwordTxt))
                                {
                                    Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();

                                    //open main activity after successfully login
                                    Intent intent = new Intent(Login.this, MainActivity.class);

                                    // start the activity connect to the specified class
                                    startActivity(intent);
                                    //startActivity(new Intent(Login.this,MainActivity.class));
                                    finish();

                                }

                                else
                                {
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // open register activity
                startActivity(new Intent(Login.this,Register.class));

            }
        });
    }
}