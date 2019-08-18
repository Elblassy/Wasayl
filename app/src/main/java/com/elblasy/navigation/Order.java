package com.elblasy.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class Order extends AppCompatActivity {

    EditText order, numberOrder;
    TextView cobon;
    int numberOfLines = 1;
    boolean addFirstLine = true;
    private DatabaseReference mdatabase;
    private StringBuilder details = new StringBuilder();
    private boolean active = true;
    boolean addLine = true;
    int[] lines;
    int[] checkLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        order = findViewById(R.id.editText1);
        numberOrder = findViewById(R.id.editText2);
        cobon = findViewById(R.id.text_cobon);
        lines = new int[10];
        checkLines = new int[10];

        order.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (order.getText() != null) {
                    details.append(numberOrder.getText().toString()).append(" ");
                    details.append(order.getText().toString()).append("\n");
                } else
                    Toast.makeText(Order.this, "Please write your order", Toast.LENGTH_LONG).show();
            }
        });

        order.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && addFirstLine) {
                    addFirstLine = false;
                    addLine();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button placeOrder = findViewById(R.id.place_order);
        placeOrder.setOnClickListener(view -> {

            for (int i = 0; i < numberOfLines - 1; i++) {

                EditText editText = findViewById(lines[i]);
                editText.setFocusable(false);

                editText.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        if (order.getText() != null) {
                            addLine = false;
                            details.append(editText.getText().toString()).append("\n");
                        } else
                            Toast.makeText(Order.this, "Please write your order", Toast.LENGTH_LONG).show();
                    }
                });

            }
            order.setFocusable(false);

        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void addLine() {

        int dpValue = 8; // margin in dips
        float d = Order.this.getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d); // margin in pixels

        //linearLayout
        LinearLayout parent = new LinearLayout(Order.this);
        LinearLayout layout = findViewById(R.id.layout);

        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);

        //add edittext
        EditText et = new EditText(this);
        EditText et2 = new EditText(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0.15f);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 2f);

        p.setMarginStart(margin);
        p2.setMarginStart(margin);

        //details editText
        et.setLayoutParams(p2);
        et.setHint("write what you want...");
        et.setId(numberOfLines);
        et.setHintTextColor(Color.DKGRAY);
        et.setTextColor(Color.WHITE);
        et.setBackgroundColor(Color.TRANSPARENT);
        et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        et.setMaxLines(1);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(1);

        //number editText
        et2.setLayoutParams(p);
        et2.setText("1");
        et2.setMaxLines(1);
        et2.setId(R.id.editText2 + numberOfLines);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);
        et2.setFilters(filterArray);
        et2.setHintTextColor(Color.LTGRAY);
        et2.setTextColor(Color.WHITE);
        et2.setBackgroundColor(Color.TRANSPARENT);
        et2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);

        parent.addView(et2);
        parent.addView(et);
        layout.addView(parent);

        lines[numberOfLines - 1] = et.getId();

        //System.out.println(et.getId());

        if (numberOfLines < 9)
            numberOfLines++;

        cobon.setText(String.valueOf(et.getId()));


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                System.out.println("before " + s);
//                System.out.println("before " + start);
//                System.out.println("before " + count);
//                System.out.println("before " + after);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println(start);
//                System.out.println(before);
//                System.out.println(s);

                if (s != null && count == 1)
                    addLine();


            }

            @Override
            public void afterTextChanged(Editable s) {
                addLine = false;
            }
        });
        addLine = true;

    }
}
