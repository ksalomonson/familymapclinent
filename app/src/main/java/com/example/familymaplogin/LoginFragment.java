package com.example.familymaplogin;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import Utilities.LoginObject;
import Utilities.RegisterObject;
import tasks.LoginTask;
import tasks.RegisterTask;


public class LoginFragment extends Fragment {


    private LoginObject loginRequest;
    private RegisterObject registerRequest;
    private EditText serverHostEditField;
    private EditText serverPortEditField;
    private EditText usernameEditField;
    private EditText passwordEditField;
    private EditText firstNameEditField;
    private EditText lastNameEditField;
    private EditText emailEditField;
    private RadioGroup genderRadioGroup;
    private Button signInButton;
    private Button registerButton;
    private static Context cameFrom;
    private String serverHost;
    private String serverPort;

    public LoginFragment() {

    }

    public static LoginFragment newInstance(Context in) {
        cameFrom = in;
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRequest = new LoginObject();
        registerRequest = new RegisterObject();
        registerRequest.setGender("m");
    }

    private void checkLoginFieldsForEmptyValues(){
        String serverHost = serverHostEditField.getText().toString();
        String serverPort = serverPortEditField.getText().toString();
        String username = usernameEditField.getText().toString();
        String password = passwordEditField.getText().toString();


        if(serverHost.equals("")|| serverPort.equals("") || username.equals("") || password.equals("")){
            signInButton.setEnabled(false);
        } else {
            signInButton.setEnabled(true);
        }
    }

    private void checkRegisterFieldsForEmptyValues(){
        String serverHost = serverHostEditField.getText().toString();
        String serverPort = serverPortEditField.getText().toString();
        String username = usernameEditField.getText().toString();
        String password = passwordEditField.getText().toString();
        String firstName = firstNameEditField.getText().toString();
        String lastName = lastNameEditField.getText().toString();
        String email = emailEditField.getText().toString();

        if(serverHost.equals("")|| serverPort.equals("") || username.equals("") || password.equals("") || firstName.equals("") || lastName.equals("") || email.equals("")){
            registerButton.setEnabled(false);
        } else {
            registerButton.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        signInButton = (Button) v.findViewById(R.id.signInbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInButtonClicked();
            }
        });

        registerButton = (Button) v.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        });


        serverHostEditField = (EditText) v.findViewById(R.id.serverHostEditText);
        serverHostEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                loginRequest.setHostName(s.toString());
                registerRequest.setHostName(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginFieldsForEmptyValues();
                checkRegisterFieldsForEmptyValues();
            }
        });

        serverPortEditField = (EditText) v.findViewById(R.id.serverPortEditText);
        serverPortEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank just like the field
            }


            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                loginRequest.setPortNumber(s.toString());
                registerRequest.setPortNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginFieldsForEmptyValues();
                checkRegisterFieldsForEmptyValues();
            }
        });


        usernameEditField = (EditText) v.findViewById(R.id.userNameEditText);
        usernameEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                loginRequest.setUsername(s.toString());
                registerRequest.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginFieldsForEmptyValues();
                checkRegisterFieldsForEmptyValues();
            }
        });

        passwordEditField = (EditText) v.findViewById(R.id.passwordEditText);
        passwordEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                loginRequest.setPassword(s.toString());
                registerRequest.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginFieldsForEmptyValues();
                checkRegisterFieldsForEmptyValues();
            }
        });

        checkLoginFieldsForEmptyValues();       //enable button
        //Register Request fields

        firstNameEditField = (EditText) v.findViewById(R.id.firstNameEditText);
        firstNameEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                registerRequest.setFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegisterFieldsForEmptyValues();
            }
        });

        lastNameEditField = (EditText) v.findViewById(R.id.lastNameEditText);
        lastNameEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                registerRequest.setLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegisterFieldsForEmptyValues();
            }
        });

        emailEditField = (EditText) v.findViewById(R.id.emailEditText);
        emailEditField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                registerRequest.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegisterFieldsForEmptyValues();
            }
        });

        genderRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.femaleRadioButton) {
                    registerRequest.setGender("f");
                }
                if (checkedId == R.id.maleRadioButton) {
                    registerRequest.setGender("m");
                }
            }
        });

        checkRegisterFieldsForEmptyValues();



        return v;
    }


    private void onSignInButtonClicked() {
        LoginTask signInTask = new LoginTask(this, cameFrom);
        signInTask.execute(loginRequest);
    }

    private void onRegisterButtonClicked(){
        RegisterTask registerTask = new RegisterTask(this, cameFrom);
        registerTask.execute(registerRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }


}


