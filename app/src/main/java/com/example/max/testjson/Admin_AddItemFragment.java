package com.example.max.testjson;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.leakcanary.RefWatcher;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


public class Admin_AddItemFragment extends Fragment implements View.OnClickListener{

    Admin_AddItemFragment thisFragment;

    //Declaring views
    private ImageView imageView;
    private EditText editText;

    private Button QR;
    private Button BarCode;
    private Button TextRecognize;

    private Button addNewClassBtn;
    private LinearLayout addNewClassLayout;
    private EditText permissionDayText;

    private Button confirmBtn;

    private EditText ScannedCode_admin;
    private EditText locationText;

    //private EditText boughtTime;
    private static final String TAG = "Admin_AddItemFragment";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //qr variable
    private String itemTagAdmin;


    //classification spinner variable
    private String set;

    //permission spinner variable
    private String permission;

    private int buttonToggle = 1;




    //scan variable
    private String codeFormat,codeContent;
    private final String noResultErrorMsg = "No scan data received!";

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    public Admin_AddItemFragment() {
        // Required empty public constructor
    }

    public static Admin_AddItemFragment newInstance() {
        Admin_AddItemFragment fragment = new Admin_AddItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add_item, container, false);

        //Requesting storage permission
        //requestStoragePermission();

        //Initializing views
        imageView = (ImageView)view. findViewById(R.id.addImage);
        editText = (EditText) view.findViewById(R.id.editTextName);

        QR = (Button)view.findViewById(R.id.QR_scan_admin);
        BarCode = (Button)view.findViewById(R.id.Barcode_scan_admin);
        TextRecognize = (Button) view.findViewById(R.id.Text_scan_admin);

        mDisplayDate = (TextView) view.findViewById(R.id.tvDate);

        addNewClassBtn = (Button) view.findViewById(R.id.addNewClass_btn);
        addNewClassLayout = (LinearLayout) view.findViewById(R.id.hiddenLayout);
        addNewClassLayout.setVisibility(View.GONE);
        permissionDayText = (EditText) view.findViewById(R.id.permission_day_text);

        locationText = (EditText)view.findViewById(R.id.itemLocationText);

        ScannedCode_admin = (EditText)view.findViewById(R.id.scannedCode_admin) ;

        confirmBtn = (Button)view.findViewById(R.id.confirmButton);

        //Setting clicklistener
        imageView.setOnClickListener(this);

        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR(v);
            }
        });
        BarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBar(v);
            }
        });
        TextRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanText(v);
            }
        });

        addNewClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHidden(v);
            }
        });

        //select bought time
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(view);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDate(datePicker,year,month,day);
            }
        };



        //spinner select item classification
        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinnerClassification);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,TestJson.classificationArray);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        set = TestJson.classificationArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTagAdmin = ScannedCode_admin.getText().toString();
//                Log.i("itemTag", ScannedCode_admin.);
                try {
                    if(buttonToggle == 1){
                        uploadItemInfo(v);

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                        alertBuilder.setTitle("Okay");
                        alertBuilder.setMessage("New item added successfully");
                        alertBuilder.create().show();
                    }else if(buttonToggle == 0){
                        uploadMultipart();
                        uploadItemInfoNew(v);

                        uploadItemInfo(v);
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                        alertBuilder.setTitle("Okay");
                        alertBuilder.setMessage("New item & New classification added successfully");
                        alertBuilder.create().show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void scanQR(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan!!");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    public void scanBar(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this); // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        // integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void scanText(View view) {
        Intent scan = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        startActivityForResult(scan,1);
    }

    /*
   * This is the method responsible for image upload
   * We need the full image path and the name for the image in this method
   * */
    public void uploadMultipart() {

        Log.i("UPLOAD", "UPLOAD MESSAGE");

        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        Toast.makeText(getActivity().getApplicationContext(),path,Toast.LENGTH_SHORT).show();

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, BackgroundTask.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Log.i("choose", "choose file");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //upload image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap( getActivity().getApplicationContext().getContentResolver(), filePath);

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get tag info
        //retrieve scan result

        if(resultCode == getActivity().RESULT_FIRST_USER)
        {
            String answer = data.getStringExtra("result");
            answer = answer.replace("\n\n","\n");
            answer = answer.replace("\n",", ");
            ScannedCode_admin.setText(answer);

            Log.i("scan text", answer);
        }
        else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

            if (scanningResult != null) {
                codeContent = scanningResult.getContents();
                codeFormat = scanningResult.getFormatName();
                ScannedCode_admin.setText(codeContent);
                // send received data
                parentActivity.scanResultData(codeFormat, codeContent);


            } else {
                parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
            }
        }



    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor =  getActivity().getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity().getApplicationContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity().getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
            showFileChooser();
    }

    public void selectDate(View view){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showDate(DatePicker datePicker, int year, int month, int day){
        month = month + 1;
        Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

        String date = year + "-" + month + "-" +day ;
        mDisplayDate.setText(date);
    }

    public void uploadItemInfo(View view) throws IOException {
        String currentLocation = locationText.getText().toString().trim();
        String timestamp =  mDisplayDate.getText().toString().trim();

        TestJson.getUser().administratorAddItem(itemTagAdmin, currentLocation, timestamp, set);
    }

    public void uploadItemInfoNew(View view) throws IOException{
        String classification = editText.getText().toString().trim();
        String currentLocation = locationText.getText().toString().trim();
        String timestamp = mDisplayDate.getText().toString().trim();
        String permissionDay = permissionDayText.getText().toString();
        int permission = Integer.parseInt(permissionDay);

        TestJson.getUser().administratorAddItemNew(itemTagAdmin, currentLocation, timestamp, classification, permission);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public void setHidden(View v){
        if(buttonToggle == 0){
            buttonToggle = 1;
            addNewClassLayout.setVisibility(View.GONE);
        }else{
            buttonToggle = 0;
            addNewClassLayout.setVisibility(View.VISIBLE);
        }
    }


//    public void administratorAddItem(String itemTag) {
//
//        Log.i("state", "borrowing");
//        TestJson.getUser().administratorAddItem(itemTag, "");
//
//    }

}
