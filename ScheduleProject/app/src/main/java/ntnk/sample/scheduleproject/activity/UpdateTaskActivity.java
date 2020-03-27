package ntnk.sample.scheduleproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntnk.sample.scheduleproject.BuildConfig;
import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;
import ntnk.sample.scheduleproject.sqlite.TaskDatabaseHelper;
import ntnk.sample.scheduleproject.utils.FileCompressor;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDate;

    private RadioButton radioButtonNotyet;
    private RadioButton radioButtonDoing;
    private RadioButton radioButtonDone;
    private RadioButton radioButtonPriority1;
    private RadioButton radioButtonPriority2;
    private RadioButton radioButtonPriority3;
    private RadioButton radioButtonPriority4;

    String dateStr;
    String timeStr;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    TaskDAO taskDAO;
    Task task;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;

    File mPhotoFile;
    FileCompressor mCompressor;
    @BindView(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_task);

        taskDAO = new TaskDAO(this);
        Intent intent = getIntent();
        //*****remember to fix default value*******************************
        int taskId = intent.getIntExtra("taskId", 1);
        if(taskId  < 0){
            setContentView(R.layout.activity_task_notfound);
            return;
        }else {
            task = taskDAO.getTaskById(taskId);
            if(task == null){
                setContentView(R.layout.activity_task_notfound);
                return;
            }
        }
        setContentView(R.layout.activity_update_task);

        assignUIComponent();


        dateStr = "";
        timeStr = "";
        setCurrentData();

        ButterKnife.bind(this);
        mCompressor = new FileCompressor(this);

    }

    private void assignUIComponent(){
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setKeyListener(null);
        editTextDescription = findViewById(R.id.editTextDes);

        radioButtonNotyet = findViewById(R.id.radioButtonNotyet);
        radioButtonDoing = findViewById(R.id.radioButtonDoing);
        radioButtonDone = findViewById(R.id.radioButtonDone);

        radioButtonPriority1 = findViewById(R.id.radioButtonUI1);
        radioButtonPriority2 = findViewById(R.id.radioButtonUI2);
        radioButtonPriority3 = findViewById(R.id.radioButtonUI3);
        radioButtonPriority4 = findViewById(R.id.radioButtonUI4);
    }

    private void setCurrentData(){
        editTextTitle.setText(task.getTitle());
        editTextDescription.setText(task.getDescription());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(task.getDate());
        editTextDate.setText(datetime);
        dateStr = datetime.substring(0,9);
        timeStr = datetime.substring(11);

        switch (task.getStatus()){
            case 1 :{
                radioButtonNotyet.setChecked(true);
                break;
            }
            case 2 :{
                radioButtonDoing.setChecked(true);
                break;
            }
            case 3 :{
                radioButtonDone.setChecked(true);
                break;
            }
        }

        switch (task.getUrgent_importance()){
            case 1:{
                radioButtonPriority1.setChecked(true);
                break;
            }
            case 2:{
                radioButtonPriority2.setChecked(true);
                break;
            }
            case 3:{
                radioButtonPriority3.setChecked(true);
                break;
            }
            case 4:{
                radioButtonPriority4.setChecked(true);
                break;
            }
        }
        if(task.getTaskImage() != null) {
            Glide.with(UpdateTaskActivity.this)
                    .load(task.getTaskImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.profile_pic_place_holder))
                    .into(imageViewProfilePic);
        }
    }

    public void datePickerAction(View view){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateStr = year +"-"+month + "-" + dayOfMonth;
                editTextDate.setText(dateStr+ " " + timeStr);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void timePickerAction(View view){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeStr = hourOfDay +":"+minute;
                if(dateStr.equals("")){
                    dateStr = calendar.get(Calendar.YEAR) + "-"
                            + (calendar.get(Calendar.MONTH)+1) + "-"
                            + calendar.get(Calendar.DATE);
                }
                editTextDate.setText(dateStr+ " " + timeStr);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void cameraBtnAction(View view){
        requestStoragePermission(true);
    }

    public void uploadBtnAction(View view){
        requestStoragePermission(false);
    }

    public void saveBtnAction(View view){
        Task task = new Task();
        String title = editTextTitle.getText().toString().trim();
        if(title.equals("")){
            Toast.makeText(this, "Title must not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        task.setTitle(title);

        String dateStr = editTextDate.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Date empty or invalid format", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        task.setDate(date);

        task.setDescription(editTextDescription.getText().toString().trim());

        if(radioButtonNotyet.isChecked()){
            task.setStatus(1);
        }else if(radioButtonDoing.isChecked()){
            task.setStatus(2);
        }else {
            task.setStatus(3);
        }

        if(radioButtonPriority1.isChecked()){
            task.setUrgent_importance(1);
        }else  if(radioButtonPriority2.isChecked()){
            task.setUrgent_importance(2);
        }else if(radioButtonPriority3.isChecked()){
            task.setUrgent_importance(3);
        }else{
            task.setUrgent_importance(4);
        }

        if(mPhotoFile != null){
            try {
                String path = mCompressor.saveToExternalStorage(mPhotoFile);
                task.setTaskImage(path);
                editTextTitle.setText("image path external saved: " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        taskDAO.update(task);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("update_task", task);
        setResult(100,returnIntent);
//        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(UpdateTaskActivity.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.profile_pic_place_holder))
                        .into(imageViewProfilePic);

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(UpdateTaskActivity.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.profile_pic_place_holder))
                        .into(imageViewProfilePic);
            }
        }
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        final boolean isCameraFinal = isCamera;
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCameraFinal) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                } )
                .onSameThread()
                .check();
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * navigating user to app settings
     */
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    /**
     * Create file with current timestamp name
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
