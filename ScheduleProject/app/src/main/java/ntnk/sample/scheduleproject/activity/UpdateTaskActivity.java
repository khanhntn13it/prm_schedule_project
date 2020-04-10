package ntnk.sample.scheduleproject.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
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
import ntnk.sample.scheduleproject.broadcast.NotifiTaskChannel;
import ntnk.sample.scheduleproject.entity.Task;
import ntnk.sample.scheduleproject.sqlite.TaskDAO;
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
    int position;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    private static final String FORMAT_DATE = "yyyy-MM-dd HH:mm";

    File mPhotoFile;
    FileCompressor mCompressor;
    @BindView(R.id.imageViewProfilePic_u)
    ImageView imageViewProfilePic;

    NotifiTaskChannel notifiTaskChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskDAO = new TaskDAO(this);
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", -1);
        position = intent.getIntExtra("taskPosi", -1);
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
        
        dateStr = "";
        timeStr = "";
        assignUIComponent();
        setCurrentData();

        ButterKnife.bind(this);
        mCompressor = new FileCompressor(this);

        notifiTaskChannel = new NotifiTaskChannel(this);
        notifiTaskChannel.createNotificationChannel();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void assignUIComponent(){
        editTextTitle = findViewById(R.id.editTextTitle_u);
        editTextDate = findViewById(R.id.editTextDate_u);
        editTextDate.setKeyListener(null);
        editTextDescription = findViewById(R.id.editTextDes_u);

        radioButtonNotyet = findViewById(R.id.radioButtonNotyet_u);
        radioButtonDoing = findViewById(R.id.radioButtonDoing_u);
        radioButtonDone = findViewById(R.id.radioButtonDone_u);

        radioButtonPriority1 = findViewById(R.id.radioButtonUI1_u);
        radioButtonPriority2 = findViewById(R.id.radioButtonUI2_u);
        radioButtonPriority3 = findViewById(R.id.radioButtonUI3_u);
        radioButtonPriority4 = findViewById(R.id.radioButtonUI4_u);

        imageViewProfilePic = findViewById(R.id.imageViewProfilePic_u);
    }

    private void setCurrentData(){
        editTextTitle.setText(task.getTitle());
        editTextDescription.setText(task.getDescription());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        dateStr = dateFormat.format(task.getDate());
        timeStr = timeFormat.format(task.getDate());
        editTextDate.setText(dateStr + " " + timeStr);

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
            Uri uri = Uri.fromFile(new File(task.getTaskImage()));
            try {
                ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
                if(parcelFileDescriptor != null) {
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
                    Glide.with(UpdateTaskActivity.this)
                            .load(bitmap)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.profile_pic_place_holder))
                            .into(imageViewProfilePic);
                }
            }catch (FileNotFoundException e){

            }
        }
    }

    public void datePickerAction(View view){
        final Calendar calendar = Calendar.getInstance();
        int c_year = calendar.get(Calendar.YEAR);
        int c_month = calendar.get(Calendar.MONTH);
        int c_day = calendar.get(Calendar.DATE);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateStr = year +"-" + (month + 1) + "-" + dayOfMonth;
                editTextDate.setText(dateStr+ " " + timeStr);
            }
        }, c_year, c_month, c_day);
        datePickerDialog.show();
    }

    public void timePickerAction(View view){
        final Calendar calendar = Calendar.getInstance();
        int c_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int c_minute = calendar.get(Calendar.MINUTE);
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
        }, c_hour, c_minute, true);
        timePickerDialog.show();
    }

    public void backBtnAction(View view){
        finish();
    }

    public void cameraBtnAction(View view){
        requestStoragePermission(true);
    }

    public void uploadBtnAction(View view){
        requestStoragePermission(false);
    }

    public void saveBtnAction(View view){
        //get data
        String title = editTextTitle.getText().toString().trim();
        if(title.equals("")){
            Toast.makeText(this, "Title must not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        task.setTitle(title);

        String dateStr = editTextDate.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
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

        if (mPhotoFile != null) {
            try {
                Uri uri = Uri.fromFile(mPhotoFile);
                ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
                String fileName = mPhotoFile.getName();
                String path = mCompressor.compressToFileInExternalStorage(parcelFileDescriptor.getFileDescriptor(), fileName).getPath();
//                Toast.makeText(this, "image path external saved: " + path, Toast.LENGTH_LONG).show();
                task.setTaskImage(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //update
        taskDAO.update(task);
        //set notification
        notifiTaskChannel.setAlarm(task);

        //return result
        Intent returnIntent = new Intent();
        returnIntent.putExtra("update_task", task);
        returnIntent.putExtra("taskPosi", position);
        setResult(202, returnIntent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Glide.with(UpdateTaskActivity.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.profile_pic_place_holder))
                        .into(imageViewProfilePic);

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImageURI = data.getData();
                try {
                    ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(selectedImageURI, "r");
                    String fileName = "GALLERY_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                    mPhotoFile = mCompressor.compressToFileInCache(parcelFileDescriptor.getFileDescriptor(), fileName);
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
        String imageFileName = "CAMERA_" + Calendar.getInstance().getTimeInMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir );

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

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(UpdateTaskActivity.this, BoardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_task:
                            intent = new Intent(UpdateTaskActivity.this, TodayTaskActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.navigation_aboutus:
                            intent = new Intent(UpdateTaskActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            };
}
