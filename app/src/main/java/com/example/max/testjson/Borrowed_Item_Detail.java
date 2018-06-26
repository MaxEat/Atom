package com.example.max.testjson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class Borrowed_Item_Detail extends AppCompatActivity {

    private EditText type;
    private EditText borrowDate;
    private EditText returnDate;
    private EditText daysLeft;
    private EditText location;
    private TextView reminderDays;
    private ImageView imageView;

    private BorrowedItem currentItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed__item__detail);
        Intent intent = getIntent();
        currentItem = (BorrowedItem) intent.getExtras().getSerializable("item");

        type = findViewById(R.id.editText_Type);
        borrowDate = findViewById(R.id.editText_date);
        location = findViewById(R.id.editText_borrow_location);
        returnDate = findViewById(R.id.editText_return);
        daysLeft = findViewById(R.id.editText_leftdays);
        imageView = findViewById(R.id.item_detail_image);
        reminderDays = findViewById(R.id.textView3);

        initDetails();
    }

    public void initDetails() {
        currentItem.calculateAllowableDays();
        currentItem.setLeftDays();

        type.setText(currentItem.getClassification());
        borrowDate.setText(currentItem.getBorrowedTimeStamp());
        returnDate.setText(currentItem.getReturnDate());
        int days = Integer.parseInt(currentItem.getLeftDays());
        if(days<0){
            reminderDays.setText("Expired for: ");
            int expiredDays = 0-days;
            daysLeft.setText(expiredDays+" days");
        }
        else {
            reminderDays.setText("There are:");
            daysLeft.setText(currentItem.getLeftDays() + " days left");
        }
        location.setText(currentItem.getBorrowedLocation());
        type.setEnabled(false);
        borrowDate.setEnabled(false);
        returnDate.setEnabled(false);
        daysLeft.setEnabled(false);
        location.setEnabled(false);

        String pictureUrl = TestJson.pictureMap.get(currentItem.getClassification());
        Picasso.with(getApplicationContext()).load(pictureUrl).fit().into(imageView);
    }
}
