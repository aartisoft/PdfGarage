package sebpo.pdfgarage.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFUtils {


    static String FOLDER_NAME = "/PDFGarageoutput";

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

            /** U can you public  / private directory : both works according to their name crieteria*/
            // File folder= new File(Environment.getExternalStorageDirectory() + FOLDER_NAME);
            File folder = new File(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+
                    FileUtils.getAppPath(activity));

            if (!folder.exists()) {
                folder.mkdirs();
            }

            //File outputFile = new File(Environment.getExternalStorageDirectory(), "temp_img.jpg");

            File outputFile = new File(folder.getAbsolutePath()
                    , "temp_img.jpg");

            FileOutputStream outputStream = new FileOutputStream(outputFile);

            cbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Create JPG
     */
    public static void createPNG(int your_page_num, Activity activity, File file) {

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

            new File(Environment.getExternalStorageDirectory() + FOLDER_NAME).
                    mkdirs();
            File outputFile = new File(Environment.getExternalStorageDirectory() +
                    FOLDER_NAME, "temp_img.jpg");
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            cbitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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

    public static String getPath(Uri uri, Activity activity) {
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

    public static String extractText(String path) {
        String parsedText = "";
        try {

            PdfReader reader = new PdfReader(path);
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n"; //Extracting the content from the different pages
            }
            System.out.println(parsedText);
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
        return parsedText;
    }

    public static void createPdf(String dest, Context mContext) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /**
             * Creating Document
             */
            Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Android School");
            document.addCreator("Shihab Uddin");

            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.
                    createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("Order Details", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk("Order No:", mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            Font mOrderIdValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderIdValueChunk = new Chunk("#123123", mOrderIdValueFont);
            Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
            document.add(mOrderIdValueParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderDateFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderDateChunk = new Chunk("Order Date:", mOrderDateFont);
            Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
            document.add(mOrderDateParagraph);

            Font mOrderDateValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk = new Chunk("06/07/2017", mOrderDateValueFont);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderAcNameFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderAcNameChunk = new Chunk("Account Name:", mOrderAcNameFont);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            document.add(mOrderAcNameParagraph);

            Font mOrderAcNameValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderAcNameValueChunk = new Chunk("Pratik Butani", mOrderAcNameValueFont);
            Paragraph mOrderAcNameValueParagraph = new Paragraph(mOrderAcNameValueChunk);
            document.add(mOrderAcNameValueParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

            //sebpo.pdfgarage.utility.
            //sebpo.pdfgarage.utility.FileUtils.openFile(mContext, new File(dest));

        } catch (IOException | DocumentException ie) {
            LogMe.e("createPdf: Error :", ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {

            LogMe.e("createPdf: Error :", ae.getLocalizedMessage());

        }
    }

}
