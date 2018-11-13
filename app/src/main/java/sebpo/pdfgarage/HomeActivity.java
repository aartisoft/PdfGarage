package sebpo.pdfgarage;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HomeActivity extends AppCompatActivity {


    private static final int PICKFILE_RESULT_CODE = 2121;
    Button button_select, button_open_file_explorer, button_collect_text;
    private int your_page_num = 1;
    Activity activity;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;

        button_select = findViewById(R.id.button_select);
        button_open_file_explorer = findViewById(R.id.button_open_file_explorer);
        button_collect_text = findViewById(R.id.button_collect_text);


        textview = findViewById(R.id.fileName);
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFileExlorer();
//                CopyReadAssets();
                openPDF();
            }
        });

        button_open_file_explorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFileExlorer();
                openFileWithLibrary();
            }
        });

        button_collect_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PDFUtils.createJPG(your_page_num, activity,
                        filePath.getAbsoluteFile());

            }
        });
        // createAFile();
    }

    public void openFileExlorer() {
        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        //fileintent.setType("gagt/sdf");
        //fileintent.setType("file/*");
        fileintent.setType("*/*");
        try {
            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
        }

    }

    public void openFileWithLibrary() {

        new ChooserDialog().with(this)
//                .withStartFile(path)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        Toast.makeText(activity, "FILE: " + path, Toast.LENGTH_SHORT).show();
                        filePath = pathFile;

                    }
                })
                .build()
                .show();

    }

    private void CopyReadAssets() {


        File directory = this.getFilesDir();
        File file = new File(directory, "/sample.pdf");

//        File file = new File(getFilesDir(), "sample.pdf");
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
//        File file = new File(getFilesDir(), "sample.pdf");
        try {
            in = assetManager.open("sample.pdf");
            out = openFileOutput(file.getName(), Context.MODE_PRIVATE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            // =====
            Uri photoURI = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(
//                Uri.parse("file://" + getFilesDir() + "/abc.pdf"),
//                "application/pdf");
            intent.setDataAndType(
                    photoURI,
                    "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String filePathString;
    File filePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    filePathString = data.getData().getPath();
                    Uri uri = data.getData();
                    textview.setText(filePathString);
                    filePath = new File(uri.getPath());
                    showToast(uri.toString());
                }
        }
    }

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    void openPDF() {
        startActivity(new Intent(this, OpenPdfActivity.class));
    }

}

