package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class ReadyOrder extends AppCompatActivity {
    ListenerRegistration listener=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_order);
        Button button = findViewById(R.id.button_ready);

        FirebaseFirestore firestore = OrderApp.getInstance().getFireBase();
        String id = OrderApp.getInstance().loadFromSp();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("orders").document(id).delete();
                OrderApp.getInstance().clearSp();
                Intent new_order = new Intent(ReadyOrder.this,NewOrderActivity.class);
                ReadyOrder.this.startActivity(new_order);
                finish();
            }
        });
        listener = firestore.collection("orders").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Toast.makeText(ReadyOrder.this, "something went wrong, try again later",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (!value.exists()){
                            Toast.makeText(ReadyOrder.this, "order does not exist",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirestoreOrder order = value.toObject(FirestoreOrder.class);
                            if (order.getStatus().equals("done")){
                                firestore.collection("orders").document(id).delete();
                                OrderApp.getInstance().clearSp();
                                createNewOrderActivity();
                            }
                        }
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
        Intent new_order = new Intent(ReadyOrder.this,NewOrderActivity.class);
        ReadyOrder.this.startActivity(new_order);
        finish();
    }
}
