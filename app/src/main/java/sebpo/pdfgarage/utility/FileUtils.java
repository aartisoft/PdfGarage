package sebpo.pdfgarage.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import sebpo.pdfgarage.R;

import static sebpo.pdfgarage.utility.LogMe.LOGD;
import static sebpo.pdfgarage.utility.LogMe.LOGE;

/**
 * Created by k2 on 30/6/17.
 */

public class FileUtils {

    /** From
     *
     * https://developer.android.com/training/data-storage/files#java
     *
     * */

    /**

     After you request storage permissions and verify that storage is available,
    you can save two different types of files:

    Public files: Files that should be freely available to other apps and to the
    user. When the user uninstalls your app, these files should remain available to
    the user. For example, photos captured by your app or other downloaded files
    should be saved as public files.


    Private files: Files that rightfully belong to your app and will be deleted
    when the user uninstalls your app. Although these files are technically
    accessible by the user and other apps because they are on the external
    storage, they don't provide value to the user outside of your app.

    */

    private static final String TAG = FileUtils.class.getName();
    private static final String extensions[] = new String[]{"avi", "3gp", "mp4", "mp3", "jpeg", "jpg",
            "gif", "png",
            "pdf", "docx", "doc", "xls", "xlsx", "csv", "ppt", "pptx",
            "txt", "zip", "rar"};


    public static void openFile(Context context, File url) throws ActivityNotFoundException,
            IOException {
        // Create URI
        //Uri uri = Uri.fromFile(url);

        //TODO you want to use this method then create file provider in androidmanifest.xml with fileprovider name

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", url);

        String urlString = url.toString().toLowerCase();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        /**
         * Security
         */
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (urlString.toLowerCase().toLowerCase().contains(".doc")
                || urlString.toLowerCase().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (urlString.toLowerCase().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");

        } else if (urlString.toLowerCase().contains(".ppt")
                || urlString.toLowerCase().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (urlString.toLowerCase().contains(".xls")
                || urlString.toLowerCase().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (urlString.toLowerCase().contains(".zip")
                || urlString.toLowerCase().contains(".rar")) {
            // ZIP file
            intent.setDataAndType(uri, "application/trap");
        } else if (urlString.toLowerCase().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (urlString.toLowerCase().contains(".wav")
                || urlString.toLowerCase().contains(".mp3")) {
            // WAV/MP3 audio file
            intent.setDataAndType(uri, "audio/*");
        } else if (urlString.toLowerCase().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (urlString.toLowerCase().contains(".jpg")
                || urlString.toLowerCase().contains(".jpeg")
                || urlString.toLowerCase().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (urlString.toLowerCase().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (urlString.toLowerCase().contains(".3gp")
                || urlString.toLowerCase().contains(".mpg")
                || urlString.toLowerCase().contains(".mpeg")
                || urlString.toLowerCase().contains(".mpe")
                || urlString.toLowerCase().contains(".mp4")
                || urlString.toLowerCase().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            // if you want you can also define the intent type for any other file

            // additionally use else clause below, to manage other unknown extensions
            // in this case, Android will show all applications installed on the device
            // so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }

    /**
     * Get Path of App which contains Files
     *
     * @return path of root dir
     */
    public static String getAppPath(Context context) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    /***
     * Copy File
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(File src, File dst) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            String tempExt = FileUtils.getExtension(dst.getPath());

            if (tempExt.equals("jpeg") || tempExt.equals("jpg") || tempExt.equals("gif")
                    || tempExt.equals("png")) {
                if (out != null) {

                    Bitmap bit = BitmapFactory.decodeFile(src.getPath());
                    LOGD("Bitmap : " + bit);

                    if (bit.getWidth() > 700) {
                        if (bit.getHeight() > 700)
                            bit = Bitmap.createScaledBitmap(bit, 700, 700, true);
                        else
                            bit = Bitmap.createScaledBitmap(bit, 700, bit.getHeight(), true);
                    } else {
                        if (bit.getHeight() > 700)
                            bit = Bitmap.createScaledBitmap(bit, bit.getWidth(), 700, true);
                        else
                            bit = Bitmap.createScaledBitmap(bit, bit.getWidth(), bit.getHeight(), true);
                    }

                    bit.compress(Bitmap.CompressFormat.JPEG, 90, out);
                }
                LOGD("File Compressed...");
            } else {

                // Transfer bytes from in to out
                byte[] buf = new byte[1024 * 4];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOGE("Compressing ERror :  " + e.getLocalizedMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGE("Compressing ERror IOE : " + e.getLocalizedMessage());
        } catch (Exception e) {
            // TODO: handle exception
            LOGE("Compressing ERror Other: " + e.getLocalizedMessage());
        }
    }

    /***
     * Move File
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void move(File src, File dst) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            String tempExt = FileUtils.getExtension(dst.getPath());

            if (tempExt.equals("jpeg") || tempExt.equals("jpg") || tempExt.equals("gif")
                    || tempExt.equals("png")) {
                if (out != null) {

                    Bitmap bit = BitmapFactory.decodeFile(src.getPath());
                    LOGD("Bitmap : " + bit);

                    if (bit.getWidth() > 700 || bit.getHeight() > 700) {
                        bit = Bitmap.createScaledBitmap(bit, 700, 700, true);
                    }
                    bit.compress(Bitmap.CompressFormat.JPEG, 90, out);
                }
                LOGD("File Compressed...");
            } else {

                // Transfer bytes from in to out
                byte[] buf = new byte[1024 * 4];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

            in.close();
            out.close();

            /**
             * Delete File from Source folder...
             */
            if (src.delete())
                LOGD("File Successfully Copied...");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOGE("Compressing ERror :  " + e.getLocalizedMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGE("Compressing ERror IOE : " + e.getLocalizedMessage());
        } catch (Exception e) {
            // TODO: handle exception
            LOGE("Compressing ERror Other: " + e.getLocalizedMessage());
        }
    }

    /**
     * Is Valid Extension
     *
     * @param ext
     * @return
     */
    public static boolean isValidExtension(String ext) {
        return Arrays.asList(extensions).contains(ext);

    }

    /**
     * Return Extension of given path without dot(.)
     *
     * @param path
     * @return
     */
    public static String getExtension(String path) {
        return path.contains(".") ? path.substring(path.lastIndexOf(".") + 1).toLowerCase() : "";
    }


    public File getPrivateAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }


    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     *  If the system runs low on storage, it may delete your cache files
     *  without warning, so make sure you check for the
     *  existence of your cache files before reading them.
     *
     * */
    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }



}