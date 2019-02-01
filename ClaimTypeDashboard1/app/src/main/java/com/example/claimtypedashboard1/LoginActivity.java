package com.example.claimtypedashboard1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class LoginActivity extends AppCompatActivity {

    // Member variables to hold the list of clients to check for valid usernames and passwords.
    // A Client object to hold the correct Client information based on the valid
    // username and password.
    private ArrayList<Client> clients = new ArrayList<>();
    private Client currentClient;

    // Timer variable to hold the timers based on User activity
    public static Timer sessionTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*// Get text field ID's so as to empty the text fields every time you access the activity
        EditText usernameText = findViewById(R.id.username_text);
        EditText passwordText = findViewById(R.id.password_text);
        usernameText.setText("");
        passwordText.setText("");*/
    }

    public void loginCheck(View view) {

        // Get the input text from the username and password fields
        // to check against the client information
        EditText checkUser = findViewById(R.id.username_text);
        EditText checkPassword = findViewById(R.id.password_text);
        String user = checkUser.getText().toString();
        String password = checkPassword.getText().toString();

        // Get the clients information from the string arrays to populate an
        // ArrayList of Client objects
        String[] firstNameArray = getResources().getStringArray(R.array.firstnames);
        String[] lastNameArray = getResources().getStringArray(R.array.lastnames);
        String[] usernameArray = getResources().getStringArray(R.array.usernames);
        String[] passwordsArray = getResources().getStringArray(R.array.passwords);
        String[] idNumArray = getResources().getStringArray(R.array.idNumbers);

        // Clear the clients data to make sure there will be no duplicates
        clients.clear();

        try {
            // Populate the ArrayList of clients to check for a valid username and password
            for (int k = 0; k < usernameArray.length; k++) {
                clients.add(new Client(firstNameArray[k], lastNameArray[k],
                        usernameArray[k], passwordsArray[k], Integer.parseInt(idNumArray[k])));
            }
        }
        catch (NumberFormatException e)
        {
            e.getMessage();
        }

        try {

            for(int i = 0; i < clients.size(); i++)
            {
                // Check the entered username against a valid username
                if(user.equals(clients.get(i).getUserName()))
                {
                    if(password.equals(clients.get(i).getUserPassword()))
                    {
                        // Get the current client information from the ArrayList to be used
                        // to populate the claim type form fields
                        currentClient = new Client(clients.get(i).getFirstName(),
                                clients.get(i).getLastName(), clients.get(i).getUserName(),
                                clients.get(i).getUserPassword(), clients.get(i).getId());

                        // Username & Password is valid, start MainActivity page and attach
                        // the client object with the user information.
                        Intent intentLogin = new Intent(this, MainActivity.class);
                        intentLogin.putExtra("Client",currentClient);
                        startActivity(intentLogin);
                        break;
                    }
                    else // Have to change so alert box doesn't pop up every sign in
                    {
                        // Build Alert dialog box to display if password is invalid
                        AlertDialog.Builder userAlert =
                                new AlertDialog.Builder(LoginActivity.this);

                        // Set the dialog title and message.
                        userAlert.setTitle("Invalid Password");
                        userAlert.setMessage("Invalid Password. Please try again.");

                        // Add dialog button
                        userAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(R.id.password_text).requestFocus();
                            }
                        });

                        userAlert.show();
                        break;
                    }
                }
                else if (i == clients.size() - 1 )
                {
                    // Build Alert dialog box to display if username is invalid
                    AlertDialog.Builder userAlert =
                            new AlertDialog.Builder(LoginActivity.this);

                    // Set the dialog title and message.
                    userAlert.setTitle("Invalid Username");
                    userAlert.setMessage("Invalid Username. Please try again.");

                    // Add dialog button
                    userAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            findViewById(R.id.username_text).requestFocus();
                        }
                    });

                    userAlert.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Exit application when on login screen and back button is pressed
    public void onBackPressed() {

        // Check the version of the API running the app. If the version is
        // equal to or greater than API 21 then exit the app and remove all
        // the activities form the stack for the default back button
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
            finishAffinity();
        }
        else
            finish();
    }
}
