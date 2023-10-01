package com.example.shopandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.shopandroid.R;
import com.example.shopandroid.services.session.CartItemsSessionManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.shopandroid.utilities.Validation.validEmail;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton imgBtnAlreadySignUp;

    private TextInputLayout tvFirstNameContainer;
    private TextInputEditText tvFirstName;

    private TextInputLayout tvLastNameContainer;
    private TextInputEditText tvLastName;


    private TextInputLayout tvUsernameContainer;
    private TextInputEditText tvUsername;

    private TextInputLayout tvPasswordContainer;
    private TextInputEditText tvPassword;

    private TextInputLayout tvConfirmPasswordContainer;
    private TextInputEditText tvConfirmPassword;

    private Button btnSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO remove any carts
        var cartSession =new CartItemsSessionManagement(getApplicationContext(),true);


        setContentView(R.layout.activity_sign_up);
        init();
        validations();
        events();
    }



    private void init(){
        imgBtnAlreadySignUp = findViewById(R.id.imgBtnAlreadySignUp);


        tvFirstNameContainer = findViewById(R.id.tvFirstNameSignUpContainer);
        tvFirstName = findViewById(R.id.tvFirstNameSignUp);

        tvLastNameContainer = findViewById(R.id.tvLastNameSignUpContainer);
        tvLastName = findViewById(R.id.tvLastNameSignUp);

        tvUsernameContainer = findViewById(R.id.tvUsernameSignUpContainer);
        tvUsername = findViewById(R.id.tvUsernameSignUp);

        tvPasswordContainer = findViewById(R.id.tvPasswordSignUpContainer);
        tvPassword = findViewById(R.id.tvPasswordSignUp);

        tvConfirmPasswordContainer = findViewById(R.id.tvConfirmPasswordSignUpContainer);
        tvConfirmPassword = findViewById(R.id.tvConfirmPasswordSignUp);


        btnSignUp = findViewById(R.id.btnSignUp);

    }
    private void events() {
        imgBtnAlreadySignUp.setOnClickListener(e->{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


    }
    private void validations() {
        tvUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence == null || charSequence.toString().equals("")){
                    tvUsernameContainer.setError(null);
                    tvUsernameContainer.setHelperText(null);
                }
                else if(!validEmail(charSequence.toString())){

                    tvUsernameContainer.setError(getString(R.string.invalid_email));
                }
                else
                {
                    tvUsernameContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tvConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(tvPassword.getText().toString())){
                    tvConfirmPasswordContainer.setError(getString(R.string.password_does_not_match));
                }else{
                    tvConfirmPasswordContainer.setError(null);
                    tvUsernameContainer.setHelperText(null);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}