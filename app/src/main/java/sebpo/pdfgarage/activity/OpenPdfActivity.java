package sebpo.pdfgarage.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import sebpo.pdfgarage.R;

public class OpenPdfActivity extends AppCompatActivity {

    PDFView pdfView;
    Button btn_create_jpg;
    private int your_page_num = 1;
    OpenPdfActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("sample.pdf").load();

    }


    /*private File createFileFromInputStream() {


        //File file = createFileFromInputStream(inputStream);
        try {
            AssetManager am = getAssets();
            InputStream inputStream = am.open("sample.pdf");

            File f = new File("sample.pdf");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
            e.printStackTrace();
        }

        return null;
    }*/


}
