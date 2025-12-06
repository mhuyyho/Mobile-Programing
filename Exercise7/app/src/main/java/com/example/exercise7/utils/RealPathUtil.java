package com.example.exercise7.utils;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class RealPathUtil {

    @SuppressLint("NewApi")
    public static String getRealPath(Context context, Uri uri) {

        String realPath = "";

        // SDK >= 19 (Android 4.4 KitKat)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            realPath = getRealPathFromURI_API19(context, uri);
        }
        // SDK < 19
        else {
            realPath = getRealPathFromURI_BelowAPI19(context, uri);
        }
        return realPath;
    }

    // Dành cho Android < 19
    public static String getRealPathFromURI_BelowAPI19(Context context, Uri contentUri) {
        String filePath = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }

    // Android >= 19
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {

            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};

            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getRealPathFromURI_BelowAPI19(context, uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return filePath;
    }
}
