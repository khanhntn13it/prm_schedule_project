package ntnk.sample.scheduleproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class FileCompressor {
    //max width and height values of the compressed image is taken as 612x816
    private int maxWidth = 612;
    private int maxHeight = 816;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private String cacheDirectoryPath;
    File storageDir ;
//    private String externalDirectoryPath;

    public FileCompressor(Context context) {
        cacheDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Thư mục gốc của SD Card  ==> /storage/emulated/0/xxx
//        externalDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/schedule_images";
    }

    public FileCompressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

        public File compressToFileInCache(FileDescriptor imageFile, String fileName) throws IOException {
        String fullPath = cacheDirectoryPath + File.separator + fileName;
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality, fullPath);
    }


    public File compressToFileInExternalStorage(FileDescriptor imageFile, String fileName) throws IOException {
        String fullPath = storageDir + File.separator + "taskImages"+ File.separator + fileName;
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality, fullPath);
    }
}
