package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class NewOrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //find all texts and buttons
        Button save = findViewById(R.id.button);
        EditText name = findViewById(R.id.costumer_name_right);
        SeekBar pickles = findViewById(R.id.pickles_right);
        EditText comment = findViewById(R.id.comment_right);
        CheckBox tahini = findViewById(R.id.tahini_right);
        CheckBox hummus = findViewById(R.id.hummus_right);

        TextView pickles_num = findViewById(R.id.pickles_num);
        FirebaseFirestore firestore = OrderApp.getInstance().getFireBase();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreOrder new_order = new FirestoreOrder();
                new_order.setCostumer_name(name.getText().toString());
                new_order.setComment(comment.getText().toString());
                new_order.setHummus(hummus.isChecked());
                new_order.setPickles(pickles.getProgress());
                String new_id = UUID.randomUUID().toString();
                new_order.setId(new_id);
                new_order.setTahini(tahini.isChecked());
                new_order.setStatus("waiting");
                firestore.collection("orders").document(new_id).set(new_order);
                OrderApp.getInstance().saveInSp(new_id);
                Intent edit_order = new Intent(NewOrderActivity.this, EditOrder.class);
                NewOrderActivity.this.startActivity(edit_order);
                finish();
            }
        });

        pickles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                pickles_num.setText("" + progress);
                pickles_num.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                //textView.setY(100); just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }
}
