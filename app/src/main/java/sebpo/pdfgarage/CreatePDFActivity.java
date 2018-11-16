package sebpo.pdfgarage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sebpo.pdfgarage.utility.ApplicationData;
import sebpo.pdfgarage.utility.FileUtils;
import sebpo.pdfgarage.utility.LogMe;

public class CreatePDFActivity extends AppCompatActivity {
    private static final String TAG = "CreatePDFActivity";
    Button button_create_pdf;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        button_create_pdf = findViewById(R.id.button_create_pdf);
        editText = findViewById(R.id.editText);

        button_create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().length() > 0) {
                    new CreateAsync().execute(editText.getText().toString());
                }

            }
        });
    }

    class CreateAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... value) {

            Log.d(TAG, "doInBackground: " + value[0]);
            PDFUtils.createPdf(FileUtils.
                    getAppPath(CreatePDFActivity.this) +
                    value[0], CreatePDFActivity.this);

            return value[0];
        }

        @Override
        protected void onPostExecute(String s) {
            ApplicationData.hideKeyboard(CreatePDFActivity.this);
            Toast.makeText(getApplicationContext(), s + " .pdf is created.", Toast.LENGTH_LONG).show();
        }
    }

}
