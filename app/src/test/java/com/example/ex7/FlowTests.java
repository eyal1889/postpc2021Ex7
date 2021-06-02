package com.example.ex7;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class FlowTests extends TestCase {
  @Test
  public void when_activityIsLaunchingAndNoOrder_then_NoOrderId_and_NewOrderActivityStarts(){
    // create a MainActivity and let it think it's currently displayed on the screen
      String id= OrderApp.getInstance().loadFromSp();
      MainActivity activityA = Robolectric.buildActivity(MainActivity.class).setup().get();
      assertEquals(id,"nope");
// now we need a Shadow (spooky!) to verify the next activity is started
      ShadowActivity shadowOfA = Shadows.shadowOf(activityA);

// with the shadow it is easy if ActivityB was launched
    assertEquals(shadowOfA.getNextStartedActivity().getComponent().getClassName(),NewOrderActivity.class.getName());
  }

  @Test
  public void when_activityIsLaunchingAndOrderInWaiting_then_IdExists_and_EditActivityStarts(){
    //create a new order and insert it into the firebase:
    FirestoreOrder order = new FirestoreOrder();
    order.setCostumer_name("hh");
    order.setStatus("waiting");
    FirebaseFirestore fireBase = OrderApp.getInstance().getFireBase();
    fireBase.collection("orders").document("0").set(order);
    OrderApp.getInstance().saveInSp("0");
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity activityA = Robolectric.buildActivity(MainActivity.class).setup().get();
    String id= OrderApp.getInstance().loadFromSp();
    assertEquals(id,"0");

// now we need a Shadow (spooky!) to verify the next activity is started
    ShadowActivity shadowOfA = Shadows.shadowOf(activityA);

// with the shadow it is easy if ActivityB was launched
    assertEquals(shadowOfA.getNextStartedActivity().getComponent().getClassName(),EditOrder.class.getName());
    //clean:
    fireBase.collection("orders").document("0").delete();
    OrderApp.getInstance().clearSp();
  }
  @Test
  public void when_activityIsLaunchingAndOrderInWaiting_then_IdExists_and_MakingOrderStarts(){
    //create a new order and insert it into the firebase:
    FirestoreOrder order = new FirestoreOrder();
    order.setCostumer_name("hh");
    order.setStatus("in-progress");
    FirebaseFirestore fireBase = OrderApp.getInstance().getFireBase();
    fireBase.collection("orders").document("0").set(order);
    OrderApp.getInstance().saveInSp("0");
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity activityA = Robolectric.buildActivity(MainActivity.class).setup().get();
    String id= OrderApp.getInstance().loadFromSp();
    assertEquals(id,"0");

// now we need a Shadow (spooky!) to verify the next activity is started
    ShadowActivity shadowOfA = Shadows.shadowOf(activityA);

// with the shadow it is easy if ActivityB was launched
    assertEquals(shadowOfA.getNextStartedActivity().getComponent().getClassName(),MakingOrder.class.getName());
    //clean:
    fireBase.collection("orders").document("0").delete();
    OrderApp.getInstance().clearSp();
  }
}