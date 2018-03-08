package com.example.user.android_assignment_20_2;
//Package objects contain version information about the implementation and specification of a Java package.
import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //public is a method and fields can be accessed by the members of any class.
    //class is a collections of objects.
    //created MainActivity and extends with AppCompatActivity which is Parent class that implements the OnClickListner.

    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 100;
    // Request code for READ_CONTACTS. It can be any number > 0.
    ContentResolver contentResolver;
    Button buttonAdd, buttonViewAll;
    Intent intent;
    EditText ev_diag_contactName,ev_diag_contactNumber;
    String diag_contactName,diag_contactNumber;
    //declared the values

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    protected void onCreate(Bundle savedInstanceState) {
        //protected can be accessed by within the package and class and subclasses
        //The Void class is an uninstantiable placeholder class to hold a reference to the Class object
        //representing the Java keyword void.
        //The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity
        //Activities have the ability, under special circumstances, to restore themselves to a previous state using the data stored in this bundle.
        super.onCreate(savedInstanceState);
        //Android class works in same.You are extending the Activity class which have onCreate(Bundle bundle) method in which meaningful code is written
        //So to execute that code in our defined activity. You have to use super.onCreate(bundle)
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //main is the xml you have created under res->layout->main.xml
        //Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        //he other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        //the design

        contentResolver = getContentResolver();
        buttonAdd = (Button) findViewById(R.id.buttonADD);
        buttonViewAll = (Button) findViewById(R.id.buttonViewAll);
        //giving id to the buttons
        buttonAdd.setOnClickListener(this);
        buttonViewAll.setOnClickListener(this);
        //setting clickon listner to the butons
    }



    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onClick(View view) {
        switch(view.getId()){
            //giving the switch method whether it satisifies case 1 or case 2
            case R.id.buttonADD:
                //case 1 as buttonADD
                addItem();
                //adding a item
                Toast.makeText(MainActivity.this,"buttonADD Clicked",Toast.LENGTH_LONG).show();
                //giving toast msg that button is clicked
                break;
            case R.id.buttonViewAll:
                intent = new Intent(MainActivity.this,Second.class);
                //giving intent so that the activity may switch from main to second activity.
                startActivity(intent);
                //starting the activity.
                Toast.makeText(MainActivity.this,"buttonViewAll Clicked",Toast.LENGTH_LONG).show();
                //giving toast msg that button is clicked
                break;
            default:
                break;
        }
    }

    public void addItem(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialog.setView(inflater.inflate(R.layout.contacts, null))
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog diag = (Dialog) dialog;
                        ev_diag_contactName = (EditText) diag.findViewById(R.id.diag_contact_name);
                        ev_diag_contactNumber = (EditText) diag.findViewById(R.id.diag_phone_number);
                    //setting the dialog as save and getting contact name and phone number from the layout file
                        diag_contactName = ev_diag_contactName.getText().toString();
                        diag_contactNumber = ev_diag_contactNumber.getText().toString();
                        Log.e("addItem ",diag_contactName);
                        writeContacts();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void writeContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            insertContact(diag_contactName, diag_contactNumber);
        }
    }

    private void insertContact(String contactName,String contactNumber) {
        Helper.insertContact(contentResolver,contactName,contactNumber);
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                insertContact(diag_contactName, diag_contactNumber);
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
