package com.example.user.android_assignment_20_2;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Second extends AppCompatActivity {

    EditText ev_diag_update_contactName, ev_diag_update_contactNumber;
    //String diag_contactName,diag_contactNumber;
    String edit_contactName,edit_contactNumber;
    MyAdapter myAdapter;
    int currentposition;
    // The ListView
    private ListView lstNames;
    private ArrayList<String> al_contactName,al_contactNumber;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_UPDATE_CONTACTS = 100;
    ContentResolver contentResolver;

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        contentResolver = getContentResolver();

        al_contactName = new ArrayList<>();
        al_contactNumber = new ArrayList<>();
        showContacts();
        myAdapter = new MyAdapter(getApplicationContext(),al_contactName,al_contactNumber);
        lstNames = (ListView) findViewById(R.id.lstNames);
        lstNames.setAdapter(myAdapter);
        registerForContextMenu(lstNames);
    }
    private void refresh() {
        Intent intent = new Intent(Second.this, Second.class);
        Second.this.startActivity(intent);
        finish();
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.setHeaderTitle("Select The Action");
        Log.e("SecondACTIVITY","ContextMenu is inflated");
    }
    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        currentposition = info.position;
        Log.e("MI CP : ", String.valueOf(currentposition));
        int selecteditemid = item.getItemId();

        switch (selecteditemid) {
            case R.id.subOptionUpdate:
                Toast.makeText(Second.this, "Update Selected " +al_contactName.get(currentposition), Toast.LENGTH_LONG).show();
                Log.e("SecondActivity", "Update Option Selected");
                updateItem();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void updateItem(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Second.this);
        LayoutInflater inflater = this.getLayoutInflater();
        alertDialog.setTitle("Update "+al_contactName.get(currentposition)+"...");
        alertDialog.setView(inflater.inflate(R.layout.update, null))
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog diag = (Dialog) dialog;
                        ev_diag_update_contactName = (EditText) diag.findViewById(R.id.diag_update_contact_name);
                        ev_diag_update_contactNumber = (EditText) diag.findViewById(R.id.diag_update_phone_number);
                        edit_contactName = ev_diag_update_contactName.getText().toString();
                        edit_contactNumber = ev_diag_update_contactNumber.getText().toString();

                        Log.e("updateItem ",edit_contactNumber);
                        editContacts();
                        refresh();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void displayAllContacts(){
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                            new String[]{id},null);
                    while (pCur.moveToNext()){
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        al_contactName.add(name);
                        al_contactNumber.add(phoneNo);
                        Log.e("DAC : ",name+", "+phoneNo+", "+id);
                    }
                }

            }
        }
    }
    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        displayAllContacts();
    }

    private void editContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_UPDATE_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        Log.e("EC : ",al_contactNumber.get(currentposition)+", "+edit_contactName+", "+edit_contactNumber);
        updateContact(al_contactNumber.get(currentposition),edit_contactName,edit_contactNumber);
    }
    private void updateContact(String number,String contactNumber, String firstName) {
        Helper.updateContact(contentResolver,number,contactNumber,firstName);
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSIONS_REQUEST_UPDATE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                updateContact(al_contactNumber.get(currentposition),edit_contactName,edit_contactNumber);
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
