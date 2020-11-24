package flickster.shopping.design.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import flickster.shopping.design.R;

public class SellerRegistrationActivity extends AppCompatActivity {
private Button sellerLoginBegin;
private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;

private Button registerButton;
private ProgressDialog loadingBar;

private FirebaseAuth mAuth;
@Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

    sellerLoginBegin= findViewById(R.id.seller_already_have_account_btn);
    nameInput=findViewById(R.id.seller_name);
    phoneInput=findViewById(R.id.seller_phone);
    emailInput=findViewById(R.id.seller_email);
    passwordInput=findViewById(R.id.seller_password);
    addressInput=findViewById(R.id.seller_shop_address);
    registerButton=findViewById(R.id.seller_register_btn);
mAuth= FirebaseAuth.getInstance();
loadingBar = new ProgressDialog(this);
    registerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerSeller();
        }
    });


sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
        startActivity(intent);
    }
});





    }

    private void registerSeller() {

    String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();


        if(!name.equals("") && !phone.equals("") && !password.equals("") && !address.equals("") ){

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loadingBar.setTitle("Creating Seller Account");
                        loadingBar.setMessage("Please wait, while we are checking the credentials.");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        final DatabaseReference roorRef;
                        roorRef= FirebaseAuth.getInstance().getReference();
                        String sid = mAuth.getCurrentUser().getUid();
                        HashMap<String,Object>  sellerMap = new HashMap<>();
                        sellerMap.put("sid",sid);
                        sellerMap.put("phone",phone);
                        sellerMap.put("email",email);
                        sellerMap.put("address",address);
                        sellerMap.put("name",name);

                        roorRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SellerRegistrationActivity.this, "You are registered successfully.", Toast.LENGTH_SHORT).show();


                            }
                        });


                    }
                }
            });

        }else{
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        }
    }


}