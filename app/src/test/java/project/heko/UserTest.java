package project.heko;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import project.heko.models.User;

public class UserTest {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "info";

    @Test
    public void mapper_test() {
        DocumentReference docRef = db.collection("users").document("S6DaEj3pxlhRIdgWABZsFk3T7FF3");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user = User.user_mapper(document);
                    assertEquals(user.getImgUrl(), "https://firebasestorage.googleapis.com/v0/b/heko-a6b88.appspot.com/o/S6DaEj3pxlhRIdgWABZsFk3T7FF3.jpg?alt=media&token=a50c3407-8d3c-4216-90b8-4f554f576859");
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

}
