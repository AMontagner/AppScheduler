package oop.applicationscheduler;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.MenuItem.OnMenuItemClickListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AddSchedule extends Activity implements OnClickListener {

    // Declare Variables
    private long rowID;
    public int selection;
    private EditText title_edit;
    private EditText note_edit;
    private static final String TITLE = "title";
    private static final String NOTE = "note";

    private ImageButton ib;
    private Calendar cal;
    private int hour;
    private int min;
    private EditText et;

    String[] Application = {"Email", "Youtube", "Facebook", "Instagram", "Twitter",
            "Music Player","Camera","Wifi (On)",
            "Wifi (Off)", "Bluetooth (On)", "Bluetooth (Off)",
            };



    final static private long ONE_SECOND = 1000;
    final static private long TWENTY_SECONDS = ONE_SECOND * 5;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;


    DatabaseConnector dbConnector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);


        title_edit = (EditText) findViewById(R.id.titleEdit);
        note_edit = (EditText) findViewById(R.id.noteEdit);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rowID = extras.getLong(MainActivity.ROW_ID);

            dbConnector = new DatabaseConnector(AddSchedule.this);


            dbConnector.open();
            String data = dbConnector.GetTitle(rowID);
            String description = dbConnector.GetDescription(rowID);

            Context context = getApplicationContext();

            int duration = Toast.LENGTH_SHORT;



            dbConnector.close();
            title_edit.setText(data);
            note_edit.setText(description);

        }





        ib = (ImageButton) findViewById(R.id.Button);
        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        et = (EditText) findViewById(R.id.editText);
        ib.setOnClickListener(this);

        Spinner mySpinner = (Spinner)findViewById(R.id.spinner);
        mySpinner.setAdapter(new MyCustomAdapter(AddSchedule.this, R.layout.row, Application));
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                selection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });



            setup();


    }



    private void setup() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                //Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();

                if(selection == 0)
                {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("vnd.android.cursor.item/email");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"ApplicationScheduler@gmail.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Daily Reminder!");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Reminder to feed the cats!");
                    //startActivity(Intent.createChooser(emailIntent, "Select Email Application"));
                    startActivity(emailIntent);
                }

                if(selection == 1)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/")));

                if(selection==2) {
                    Intent intent = new Intent("android.intent.category.LAUNCHER");
                    String facebookPackageName = "com.facebook.katana";
                    String facebookClassName = "com.facebook.katana.LoginActivity";
                    intent.setClassName(facebookPackageName, facebookClassName);
                    startActivity(intent);

                }

                if(selection == 3) {

                    Uri uri = Uri.parse("http://instagram.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }

                if(selection == 4) {

                    Uri uri = Uri.parse("http://twitter.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                if(selection == 5) {

                    Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                    startActivity(intent);
                }


                if(selection == 6 ) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1337);
                }

                if(selection == 7 ) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                }

                if(selection == 8 ) {
                    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(false);
                }

                if(selection == 9)
                {

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled() == false) {
                        mBluetoothAdapter.enable();
                    }
                }

                if(selection == 10)
                {

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }




            }
        };



        registerReceiver(br, new IntentFilter("com.authorwjf.wakeywakey") );
        pi = PendingIntent.getBroadcast( this, 0, new Intent("com.authorwjf.wakeywakey"),
                0 );
        am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }



    @Override
    protected void onDestroy() {
        am.cancel(pi);
        unregisterReceiver(br);
        super.onDestroy();
    }









    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.weekofday);
            label.setText(Application[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.icon);

            if (Application[position]=="Email"){
                icon.setImageResource(R.drawable.email);
            }

            if (Application[position]=="Youtube"){
                icon.setImageResource(R.drawable.youtube);
            }
            if (Application[position]=="Camera"){
                icon.setImageResource(R.drawable.camera);
            }
            if (Application[position]=="Facebook"){
                icon.setImageResource(R.drawable.facebook);
            }
            if (Application[position]=="Instagram"){
                icon.setImageResource(R.drawable.instagram);
            }
            if (Application[position]=="Twitter"){
                icon.setImageResource(R.drawable.twitter);
            }

            if (Application[position]=="Music Player"){
                icon.setImageResource(R.drawable.music);
            }

            if (Application[position]=="Wifi (On)"){
                icon.setImageResource(R.drawable.wifi);
            }

            if (Application[position]=="Wifi (Off)"){
                icon.setImageResource(R.drawable.wifioff);
            }

            if (Application[position]=="Bluetooth (On)"){
                icon.setImageResource(R.drawable.blueon);
            }
            if (Application[position]=="Bluetooth (Off)"){
                icon.setImageResource(R.drawable.blueoff);
            }



            return row;
        }
    }





    public void onCheckBoxClicked(View v) {

        CheckBox demo = (CheckBox) findViewById(R.id.demo);



        boolean checked = ((CheckBox) v).isChecked();


        switch (v.getId()) { //get the id of clicked CheckBox
            case R.id.demo:
                if (checked) {
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                            TWENTY_SECONDS, pi);


                }

        }
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.Button)
        {
            showDialog(0);
        }


    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new TimePickerDialog(this, timePickerListener, hour, min, false);
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            int hour;
            String am_pm;
            if (hourOfDay > 12) {
                hour = hourOfDay - 12;
                am_pm = "PM";
            } else {
                hour = hourOfDay;
                am_pm = "AM";
            }
            et.setText(hour + " : " + minute + " " + am_pm);
        }
    };


    // Create an ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save")
                .setOnMenuItemClickListener(this.SaveButtonClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Delete")
                .setOnMenuItemClickListener(this.DeleteButtonClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }


    // Capture delete menu item click
    OnMenuItemClickListener DeleteButtonClickListener = new OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {

            // Calls DeleteNote() Function
            DeleteNote();

            return false;

        }
    };

    private void DeleteNote() {


        AlertDialog.Builder alert = new AlertDialog.Builder(AddSchedule.this);
        alert.setTitle("Delete Schedule");
        alert.setMessage("Delete this schedule?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                final DatabaseConnector dbConnector = new DatabaseConnector(
                        AddSchedule.this);

                AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
                    @Override
                    protected Object doInBackground(Long... params) {
                        // Passes the Row ID to DeleteNote function in
                        // DatabaseConnector.java
                        dbConnector.DeleteNote(params[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        // Close this activity
                        finish();
                    }
                };
                // Execute the deleteTask AsyncTask above
                deleteTask.execute(new Long[]{rowID});
            }
        });

        // Do nothing on No button click
        alert.setNegativeButton("No", null).show();
    }


    OnMenuItemClickListener SaveButtonClickListener = new OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {




            if (title_edit.getText().length() != 0) {
                AsyncTask<Object, Object, Object> saveNoteAsyncTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        saveNote();

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {



                        finish();

                    }
                };


                // Execute the saveNoteAsyncTask AsyncTask above
                saveNoteAsyncTask.execute((Object[]) null);




            }

            else {
                // Display a simple alert dialog that forces user to put in a title
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        AddSchedule.this);
                alert.setTitle("Alarm Title is needed");
                alert.setMessage("Please enter alarm name");
                alert.setPositiveButton("Okay", null);
                alert.show();
            }





            return false;

        }
    };
    // saveNote() function
    private void saveNote() {


        DatabaseConnector dbConnector = new DatabaseConnector(this);

        if (getIntent().getExtras() == null) {

            dbConnector.InsertNote(title_edit.getText().toString(), note_edit
                    .getText().toString());
        } else {

            dbConnector.UpdateNote(rowID, title_edit.getText().toString(),
                    note_edit.getText().toString());
        }
    }




}