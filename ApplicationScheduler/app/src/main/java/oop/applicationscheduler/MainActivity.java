package oop.applicationscheduler;


import android.os.Bundle;

import android.os.AsyncTask;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

    // Declare Variables
    public static final String ROW_ID = "row_id";
    private static final String TITLE = "title";
    private ListView noteListView;
    private CursorAdapter noteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteListView = getListView();

        noteListView.setOnItemClickListener(viewNoteListener);

        String[] from = new String[] { TITLE };
        int[] to = new int[] { R.id.ViewTitleNotes };

        noteAdapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.item_list_view, null, from, to);

        setListAdapter(noteAdapter);
    }


    OnItemClickListener viewNoteListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {



            Intent viewnote = new Intent(MainActivity.this, AddSchedule.class);


            viewnote.putExtra(ROW_ID, arg3);
            startActivity(viewnote);



        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Execute GetNotes Asynctask on return to MainActivity
        new GetNotes().execute((Object[]) null);
    }

    @Override
    protected void onStop() {
        Cursor cursor = noteAdapter.getCursor();

        // Deactivates the Cursor
        if (cursor != null)
            cursor.deactivate();

        noteAdapter.changeCursor(null);
        super.onStop();
    }

    // Create an Actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        menu.add("Add Schedule")
                .setOnMenuItemClickListener(this.SaveButtonClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);



        return super.onCreateOptionsMenu(menu);
    }

    // Capture menu item click
    OnMenuItemClickListener SaveButtonClickListener = new OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {


            Intent addnote = new Intent(MainActivity.this, AddSchedule.class);

            startActivity(addnote);

            return false;

        }
    };


    private class GetNotes extends AsyncTask<Object, Object, Cursor> {
        DatabaseConnector dbConnector = new DatabaseConnector(MainActivity.this);

        @Override
        protected Cursor doInBackground(Object... params) {

            dbConnector.open();

            return dbConnector.ListAllNotes();
        }

        @Override
        protected void onPostExecute(Cursor result) {
            noteAdapter.changeCursor(result);

            dbConnector.close();
        }
    }
}
