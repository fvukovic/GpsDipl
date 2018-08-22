package hr.foi.air.buuterknige;

import com.google.firebase.auth.FirebaseAuth;

public class CurrentFirebaseAuth {
    private static FirebaseAuth firebaseAuth;

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static void setFirebaseAuth(FirebaseAuth fa)  {
        firebaseAuth = fa;
    }

}
