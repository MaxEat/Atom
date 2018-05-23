package com.example.max.testjson;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Admin_AddClassificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class Admin_AddClassificationFragment extends Fragment implements View.OnClickListener{
//
//    private EditText add_editText;
//
//
//
//    public Admin_AddClassificationFragment() {
//        // Required empty public constructor
//    }
//
//    public static Admin_AddClassificationFragment newInstance(String param1, String param2) {
//        Admin_AddClassificationFragment fragment = new Admin_AddClassificationFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_admin_add_classification, container, false);
//
//        add_editText = (EditText) view.findViewById(R.id.add_editTextName);
//
//        return view;
//    }

    /*
   * This is the method responsible for image upload
   * We need the full image path and the name for the image in this method
   * */
//    public void uploadMultipart() {
//
//        Log.i("UPLOAD", "UPLOAD MESSAGE");
//
//        //getting name for the image
//        String name = add_editText.getText().toString().trim();
//
//        //getting the actual path of the image
//        String path = getPath(filePath);
//
//        Toast.makeText(getActivity().getApplicationContext(),path,Toast.LENGTH_SHORT).show();
//
//        //Uploading code
//        try {
//            String uploadId = UUID.randomUUID().toString();
//
//            //Creating a multi part request
//            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, BackgroundTask.UPLOAD_URL)
//                    .addFileToUpload(path, "image") //Adding file
//                    .addParameter("name", name) //Adding text parameter to the request
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//        //getting name for the image
//        String name = add_editText.getText().toString().trim();
//
//        //getting the actual path of the image
//        String path = getPath(filePath);
//
//        Toast.makeText(getActivity().getApplicationContext(),path,Toast.LENGTH_SHORT).show();
//
//        //Uploading code
//        try {
//            String uploadId = UUID.randomUUID().toString();
//
//            //Creating a multi part request
//            new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, BackgroundTask.UPLOAD_URL)
//                    .addFileToUpload(path, "image") //Adding file
//                    .addParameter("name", name) //Adding text parameter to the request
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
 //   }

//}
