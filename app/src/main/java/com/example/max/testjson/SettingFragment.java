package com.example.max.testjson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import static com.example.max.testjson.TestJson.getUser;
import static com.example.max.testjson.TestJson.wv;


public class SettingFragment extends Fragment implements View.OnClickListener{

    public static Handler handler;
    private EditText preferEmail;
    private RadioGroup radioGroup;
    private int reminderDaysCache = TestJson.alertDay;

    private Button submit;
    private Button logout;

    private ImageView headshot;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    private int uploadHead = 0;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {

    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        preferEmail = (EditText)view.findViewById(R.id.email_input);
        radioGroup = (RadioGroup)view.findViewById(R.id.reminder_days);

        submit = (Button)view.findViewById(R.id.submit_setting);
        logout = (Button)view.findViewById(R.id.logout);

        headshot = (ImageView) view.findViewById(R.id.my_image);
        String imageURI = TestJson.getUser().getHeadshotUrl();
        Log.i("imageUri is------------",imageURI);

        handler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 6:
                        Log.i("ui handler", "set prefered email and headshot");
                        String email = inputMessage.getData().getString("preferedEmail");
                        String pictureUrl = inputMessage.getData().getString("headshotUrl");
                        preferEmail.setText(email);
                        Picasso.with(getActivity().getApplicationContext()).load(pictureUrl).fit().into(headshot);
                        break;
                }
            }
        };
        Picasso.with(getActivity().getApplicationContext()).load(imageURI).fit().into(headshot);


        preferEmail.setText(TestJson.getUser().getAlertEmail());
        radioGroup.check(R.id.sevendays);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.oneday:
                        reminderDaysCache = 1;
                        break;
                    case R.id.threedays:
                        reminderDaysCache = 3;
                        break;
                    case R.id.sevendays:
                        reminderDaysCache = 7;
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TestJson.alertDay = reminderDaysCache;
                String email = preferEmail.getText().toString();

                if(isEmail(email))
                {
                    TestJson.getUser().setAlertEmail(email);
                    try {
                        TestJson.getUser().updateAlertEmail(email);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                    alertBuilder.setTitle("Okay");
                    alertBuilder.setMessage("Personal setting updated");
                    alertBuilder.create().show();
                }
                else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                    alertBuilder.setTitle("Error");
                    alertBuilder.setMessage("Please enter a valid email address");
                    alertBuilder.create().show();
                }
                if(uploadHead == 1){
                    uploadMultipart();
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            Log.d("clear wv", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                            CookieManager.getInstance().removeAllCookies(null);
                            CookieManager.getInstance().flush();
                        } else
                        {
                            Log.d("clear wv", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(getContext());
                            cookieSyncMngr.startSync();
                            CookieManager cookieManager=CookieManager.getInstance();
                            cookieManager.removeAllCookie();
                            cookieManager.removeSessionCookie();
                            cookieSyncMngr.stopSync();
                            cookieSyncMngr.sync();
                        }
                        TestJson.resetWebView();
                        TestJson.setUser(null);
                        Intent intent = new Intent(getActivity(), SplashActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        headshot.setOnClickListener(this);

        return view;
    }

    public boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        uploadHead = 1;
        showFileChooser();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
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

                headshot.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
  * This is the method responsible for image upload
  * We need the full image path and the name for the image in this method
  * */
    public void uploadMultipart() {

        Log.i("UPLOAD", "UPLOAD MESSAGE");

        //getting name for the image
        String name = TestJson.getUser().getCardID();

        //getting the actual path of the image
        String path = getPath(filePath);

        Toast.makeText(getActivity().getApplicationContext(),path,Toast.LENGTH_SHORT).show();

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, BackgroundTask.UPLOAD_Headshot_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
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

}
