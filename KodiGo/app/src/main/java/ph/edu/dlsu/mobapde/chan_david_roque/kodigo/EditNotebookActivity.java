package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_ID;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.KEY_NOTEBOOK_POSITION;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_NOTEBOOK;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.REQUEST_ADD_COLOR_TITLE;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_COLOR;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_DELETED;
import static ph.edu.dlsu.mobapde.chan_david_roque.kodigo.KeysCodes.RESULT_NOTEBOOK_EDITED;

public class EditNotebookActivity extends AppCompatActivity {

    EditText notebookName;
    Button updateButton;
    Button deleteButton;
    ImageView titleColor;
    ImageView notebookColor;
    RelativeLayout notebookIcon;
    Notebook notebook;
    long notebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);

        final DatabaseOpenHelper dbhelper;

        dbhelper = new DatabaseOpenHelper(getApplicationContext());
        notebookID = (long) getIntent().getExtras().get(KEY_NOTEBOOK_ID);
        notebook = dbhelper.queryNotebookByID(notebookID);
        getSupportActionBar().setTitle(notebook.getTitle());

        notebookName = (EditText) findViewById(R.id.notebookName);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        notebookColor = (ImageView) findViewById(R.id.notebookColor);
        titleColor = (ImageView) findViewById(R.id.titleColor);
        notebookIcon = (RelativeLayout) findViewById(R.id.notebookIcon);

        notebookName.setText(notebook.getTitle());
        notebookName.setTextColor(notebook.getTitleColor());
        titleColor.setBackgroundColor(notebook.getTitleColor());
        notebookIcon.setBackgroundColor(notebook.getNotebookColor());
        notebookColor.setBackgroundColor(notebook.getNotebookColor());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                notebook.setTitle(notebookName.getText().toString());
                ColorDrawable color = (ColorDrawable) titleColor.getBackground();
                notebook.setTitleColor(color.getColor());
                color = (ColorDrawable) notebookColor.getBackground();
                notebook.setNotebookColor(color.getColor());

                dbhelper.updateNotebook(notebook);
                result.putExtra(KEY_NOTEBOOK_ID, notebookID);
                setResult(RESULT_NOTEBOOK_EDITED, result);
                Log.i("EdiNotebookActivity", "Notebook Edited");
                finish();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();

                dbhelper.deleteNotebook(notebookID);
                result.putExtra(KEY_NOTEBOOK_POSITION, notebook.getNotebookNumber());
                setResult(RESULT_NOTEBOOK_DELETED, result);
                Log.i("DeleteNotebookActivity", "Notebook deleted");
                finish();
            }
        });

        notebookColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_NOTEBOOK);
            }
        });

        titleColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                colorPicker(REQUEST_ADD_COLOR_TITLE);
            }
        });

    }

    public void colorPicker(int requestCode) {
        startActivityForResult(new Intent(getBaseContext(), ColorpickerActivity.class), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ADD_COLOR_TITLE == requestCode && resultCode == RESULT_COLOR){
            titleColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookName.setTextColor((Integer) data.getExtras().get(KEY_COLOR));;
        }
        else if(REQUEST_ADD_COLOR_NOTEBOOK == requestCode && resultCode == RESULT_COLOR){
            notebookColor.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
            notebookIcon.setBackgroundColor((Integer) data.getExtras().get(KEY_COLOR));
        }
    }
}
