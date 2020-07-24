package com.collabcreations.hdwallpaper.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.collabcreations.hdwallpaper.Interface.OnUserFetchedListener;
import com.collabcreations.hdwallpaper.Modal.Common;
import com.collabcreations.hdwallpaper.Modal.ResponseCode;
import com.collabcreations.hdwallpaper.Modal.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.collabcreations.hdwallpaper.Modal.Common.saveUser;

public class GetUserByIdTask extends AsyncTask<Void, Void, Void> {
    private FirebaseUser currentUser;
    private String uId;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Common.USER);
    private OnUserFetchedListener onUserFetchedListener;
    private Context context;

    public GetUserByIdTask(Context context, String uId, OnUserFetchedListener onUserFetchedListener) {
        this.uId = uId;
        this.context = context;
        this.onUserFetchedListener = onUserFetchedListener;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        userRef.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (currentUser != null &&
                            currentUser.getUid().equals(uId) && user != null) {
                        saveUser(context, user);
                    }
                    if (onUserFetchedListener != null) {
                        onUserFetchedListener.onUserFetchComplete(true, user, ResponseCode.SUCCESS);
                    }
                } else {
                    if (currentUser != null &&
                            currentUser.getUid().equals(uId)) {
                        User user = new User();
                        user.setEmailAddress(currentUser.getEmail());
                        if (currentUser.getPhotoUrl() != null) {
                            user.setProfileImage(currentUser.getPhotoUrl().toString());
                        }
                        user.setUserName(currentUser.getDisplayName());
                        user.setuId(currentUser.getUid());
                        saveUser(context, user);
                        userRef.child(user.getuId())
                                .setValue(user);

                    }
                    if (onUserFetchedListener != null) {
                        onUserFetchedListener.onUserFetchComplete(false, null, ResponseCode.NO_USER_FOUND);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (onUserFetchedListener != null) {
                    onUserFetchedListener.onUserFetchComplete(false, null, ResponseCode.DATABASE_ERROR);
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
    }


}
