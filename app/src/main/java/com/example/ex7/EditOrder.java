package com.example.ex7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

public class EditOrder extends AppCompatActivity {
    private FirestoreOrder order=null;
    ListenerRegistration listener=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_order);
        EditText name_right = findViewById(R.id.costumer_name_right);
        SeekBar pickles_right = findViewById(R.id.pickles_right);
        EditText comment_right = findViewById(R.id.comment_right);
        CheckBox hummus_right = findViewById(R.id.hummus_right);
        CheckBox tahini_right = findViewById(R.id.tahini_right);
        Button button_save = findViewById(R.id.button_save);
        Button button_delete = findViewById(R.id.button_delete);
        TextView pickles_num = findViewById(R.id.pickles_num);

        FirebaseFirestore firestore = OrderApp.getInstance().getFireBase();
        String id = OrderApp.getInstance().loadFromSp();
        firestore.collection("orders").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        order = documentSnapshot.toObject(FirestoreOrder.class);
                        name_right.setText(order.getCostumer_name());
                        int pickles = order.getPickles();
                        pickles_right.setProgress(pickles);
                        comment_right.setText(order.getComment());
                        hummus_right.setChecked(order.getHummus());
                        tahini_right.setChecked(order.getTahini());
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditOrder.this, "something went wrong, try again later",
                        Toast.LENGTH_SHORT).show();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("orders").document(id).delete();
                OrderApp.getInstance().clearSp();
                Intent new_order = new Intent(EditOrder.this,NewOrderActivity.class);
                EditOrder.this.startActivity(new_order);
                finish();
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setTahini(tahini_right.isChecked());
                order.setPickles(pickles_right.getProgress());
                order.setComment(comment_right.getText().toString());
                order.setHummus(hummus_right.isChecked());
                order.setCostumer_name(name_right.getText().toString());

                firestore.collection("orders").document(id).set(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {

                            }
                        });
            }
        });

        listener = firestore.collection("orders").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Toast.makeText(EditOrder.this, "something went wrong, try again later",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (!value.exists()){
                            Toast.makeText(EditOrder.this, "no such order, maybe it's deleted?",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            order = value.toObject(FirestoreOrder.class);
                            if (order.getStatus().equals("in-progress")){
                                createMakingOrderActivity();
                            }
                        }
                    }
                });

        pickles_right.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener!=null){
            listener.remove();
        }
    }
    private void createNewOrderActivity(){
        Intent new_order = new Intent(EditOrder.this,NewOrderActivity.class);
        EditOrder.this.startActivity(new_order);
        finish();
    }
    private void createEditOrderActivity(){
        Intent edit_order = new Intent(EditOrder.this,EditOrder.class);
        EditOrder.this.startActivity(edit_order);
        finish();
    }
    private void createMakingOrderActivity(){
        Intent making_order = new Intent(EditOrder.this,MakingOrder.class);
        EditOrder.this.startActivity(making_order);
        finish();
    }
    private void createReadyOrderActivity(){
        Intent ready_order = new Intent(EditOrder.this,ReadyOrder.class);
        EditOrder.this.startActivity(ready_order);
        finish();
    }
}
