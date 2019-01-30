package com.example.claimtypedashboard1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FormActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView mImageView;
    String mCurrentPhotoPath;

    // Client object to hold the current clients information
    Client currentClient = new Client();

    // Timer variable to hold the timers based on User activity
    Timer exitTimer = new Timer();

    // EditText Fields to be populated with information from images, and client information
    public EditText firstNameField;
    public EditText lastNameField;
    public EditText clientIdField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Get extra from intent(Client object) from LoginActivity
        Intent clientIntent = getIntent();
        currentClient = (Client) clientIntent.getSerializableExtra("Client");

        // Creates a toolbar object to handle toolbar event listeners(i.e. back button)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        // Icon onClick listener, add the client object to the intent
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(FormActivity.this, MainActivity.class);
                intentMain.putExtra("Client", currentClient);
                startActivity(intentMain);
            }
        });

        // Initialize the ImageView and title TextView
        TextView claimTypeTitle = findViewById(R.id.claimtypeForm);
        ImageView claimTypeImage = findViewById(R.id.claimImageForm);

        // Get the claimTypeTitle from the incoming Intent and set it to the TextView
        claimTypeTitle.setText(getIntent().getStringExtra("title"));

        // Use Glide to load the image into the ImageView
        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
                .into(claimTypeImage);

        // FAB for camera access
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        // Create the spinner.
        Spinner spinner = findViewById(R.id.province_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        // Initialize the EditViews
        firstNameField = findViewById(R.id.firstName_text);
        lastNameField = findViewById(R.id.lastName_text);
        clientIdField = findViewById(R.id.id_text);

        // Variables to hold the client object values that will populate the text fields
        String fnUser = currentClient.getFirstName();
        String lnUser = currentClient.getLastName();
        int idUser = currentClient.getId();
        String strIdUser = Integer.toString(idUser);

        // Populate Client Information form First Name, Last Name, ID # fields
        // based on username and password which accessed the app.
        firstNameField.setText(fnUser);
        lastNameField.setText(lnUser);
        clientIdField.setText(strIdUser);

        // Provides parent activity call from FormActivity to MainActivity
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     *
     * @param menu Menu to inflate.
     * @return Returns true if the menu inflated.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        // This comment suppresses the Android Studio warning about simplifying
        // the return statements.
        // noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent intent = new Intent(FormActivity.this, ContactActivity.class);
                intent.putExtra("Client", currentClient);
                startActivity(intent);
                return true;
            case R.id.action_help:
                Intent intentHelp = new Intent(FormActivity.this, HelpActivity.class);
                intentHelp.putExtra("Client", currentClient);
                startActivity(intentHelp);
                return true;
            case R.id.action_logout:

                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                    finishAffinity();
                }
                else
                    finish();

                Intent intentLogout = new Intent(FormActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    // For Camera image capture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Display a toast for when a province is picked in the spinner
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    // When an item in the province Spinner has been picked, display it in a toast.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Radio button clicked method. Check to see which button has been checked.
    public void onRadioButtonClicked(View view) {
    }

    // Timer for user accidentally hitting the "Home" button, then getting back into app.
    // User has 10 seconds to continue current session after exiting.
    @Override
    public void onStop() {

        super.onStop();

        // Start the timer when the app is exited by the user pressing the home button,
        // and then send user back to login screen if the user takes longer than 10 seconds
        exitTimer.schedule(new TimerTask() {
            @Override
            public void run() {

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
        }, 10000);
    }

    // Timer for onPause when there is inactivity on the app/ activity for more than 10 mins
    @Override
    public void onPause() {

        super.onPause();
    }
}
