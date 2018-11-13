package sebpo.pdfgarage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFUtils {

    /**
     * Create JPG
     */
    public static void createJPG(int your_page_num, Activity activity, File file) {

        PdfiumCore pdfiumCore = new PdfiumCore(activity);
        int pageNum = your_page_num;

        try {

            com.shockwave.pdfium.PdfDocument pdf = pdfiumCore.newDocument(
                    ParcelFileDescriptor.open(file,
                            ParcelFileDescriptor.MODE_READ_WRITE)
            );

            pdfiumCore.openPage(pdf, pageNum);

            int width = pdfiumCore.getPageWidth(pdf, pageNum);
            int height = pdfiumCore.getPageHeight(pdf, pageNum);

            Bitmap cbitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdf, cbitmap, your_page_num, 0, 0, width, height);

            pdfiumCore.closeDocument(pdf);

            new File(Environment.getExternalStorageDirectory() + "/PDF Reader").
                    mkdirs();
            File outputFile = new File(Environment.getExternalStorageDirectory() +
                    "/PDF Reader", "temp_img.jpg");
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            cbitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * CREATE A FILE
     */
    private void createAFile(Activity activity) {
        String filename = "myfile";
        String fileContents = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getPath(Uri uri,Activity activity) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri,
                projection, null, null, null);
        if (cursor != null) {
            //HERE YOU WILL GET A NULL POINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }
}
