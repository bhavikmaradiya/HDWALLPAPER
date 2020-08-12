package com.collabcreations.hdwallpaper.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.collabcreations.hdwallpaper.Interface.UserFetchListener;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.ResponseCode;
import com.collabcreations.hdwallpaper.Modal.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.collabcreations.hdwallpaper.Modal.Common.firebaseUserToUser;
import static com.collabcreations.hdwallpaper.Modal.Common.saveUser;

public class GetUserByIdTask extends AsyncTask<Void, Void, Void> {
    private User currentUser;
    private String userId;
    private DatabaseReference userRef;
    private UserFetchListener userFetchListener;
    private Context context;

    public GetUserByIdTask(@NonNull Context context, @NonNull String userId, UserFetchListener userFetchListener) {
        this.userId = userId;
        this.context = context;
        this.userFetchListener = userFetchListener;
        this.currentUser = firebaseUserToUser();
        this.userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE);
    }


    @Override
    protected Void doInBackground(Void... voids) {
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (currentUser != null && currentUser.getuId().equals(userId) && user != null) {
                        saveUser(context, user);
                    }
                    if (userFetchListener != null) {
                        userFetchListener.onUserFetchComplete(true, user, ResponseCode.SUCCESS);
                    }
                } else {
                    if (currentUser != null && currentUser.getuId().equals(userId)) {
                        User user = firebaseUserToUser();
                        saveUser(context, user);
                        userRef.child(user.getuId())
                                .setValue(user);

                    }
                    if (userFetchListener != null) {
                        userFetchListener.onUserFetchComplete(false, null, ResponseCode.NO_USER_FOUND);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (userFetchListener != null) {
                    userFetchListener.onUserFetchComplete(false, null, ResponseCode.DATABASE_ERROR);
                }
            }
        });
        return null;
    }


}
