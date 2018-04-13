package com.example.max.testjson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Borrowed_Item_Detail extends AppCompatActivity {

    private EditText type;
    private EditText borrowDate;
    private EditText returnDate;
    private EditText daysLeft;
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
        returnDate = findViewById(R.id.editText_return);
        daysLeft = findViewById(R.id.editText_leftdays);
        imageView = findViewById(R.id.item_detail_image);

        initDetails();
    }

    public void initDetails() {
        currentItem.getAllowableDays(TestJson.getUser());
        currentItem.setLeftDays();

        type.setText(currentItem.getClassification());
        borrowDate.setText(currentItem.getBorrowedTimeStamp());
        returnDate.setText(currentItem.getReturnDate());
        daysLeft.setText(currentItem.getLeftDays()+" days left");

        type.setEnabled(false);
        borrowDate.setEnabled(false);
        returnDate.setEnabled(false);
        daysLeft.setEnabled(false);

    }
}
