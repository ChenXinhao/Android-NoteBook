package com.example.blacko.notebook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Note_Mian extends AppCompatActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT   = 1;
    private static final int DELETE_ID = 2;
    private Note_DataBase data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__mian);
        data = new Note_DataBase(this);
        data.open();
        refresh();

        ListView listView = (ListView) findViewById(R.id.noteslist);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                editNote(arg3);
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });
    }

    private void refresh() {
        Cursor notesCursor = data.fetchAllNotes();
        startManagingCursor(notesCursor);

        SimpleCursorAdapter notes = new SimpleCursorAdapter(
                this,
                R.layout.note_item1,
                notesCursor,
                new String[]
                {data.KEY_TITLE},
                new int[]{R.id.text1}
        );
        ListView listView = (ListView) findViewById(R.id.noteslist);
        listView.setAdapter(notes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        refresh();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                data.deleteNote(info.id);
                refresh();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, Note_editor_2.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void editNote(long id) {
        Intent i = new Intent(this, Note_editor_2.class);
        i.putExtra(Note_DataBase.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


}
