package com.firstapp.cryptoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {

    //create obj of database referance class to access firebase
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://cryptoc-befcb-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText Fullname=findViewById(R.id.fullname);
        final EditText email=findViewById(R.id.email);
        final EditText phone=findViewById(R.id.phone);
        final EditText password=findViewById(R.id.password);
        final EditText confirmpassword=findViewById(R.id.conpassword);

        final Button registerBtn=findViewById(R.id.RegisterBtn);
        final TextView loginNowBtn=findViewById(R.id.LoginNowBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //get data from editText into String variables
                final String fullnameTxt=Fullname.getText().toString();
                final String emailTxt=email.getText().toString();
                final String phoneTxt=phone.getText().toString();
                final String passwordTxt= password.getText().toString();
                final String confirmpasswordTxt=confirmpassword.getText().toString();

                //check if user fill all the fields before sending data to firebase

                if(fullnameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty() || confirmpasswordTxt.isEmpty())
                {
                    Toast.makeText(Register.this, "Please fill all datails", Toast.LENGTH_SHORT).show();
                }
                // if password are matching each other
                else if(!passwordTxt.equals(confirmpasswordTxt))
                {
                    Toast.makeText(Register.this, "Password not matching", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check the num before registered

                            if(snapshot.hasChild(phoneTxt))
                            {
                                Toast.makeText(Register.this, "Phone number Already Registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //sending data to firebase
                                databaseReference.child("users").child(phoneTxt).child("Fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(phoneTxt).child("Email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("Password").setValue(passwordTxt);

                                Toast.makeText(Register.this, "User Register Successfully.", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }




            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}