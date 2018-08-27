package hr.foi.air.buuterknige.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.foi.air.buuterknige.R;
import hr.foi.air.buuterknige.User;

public class CustomDialog extends Dialog {

    private static final String TAG = "CustomDialog";
    View myMainView;
    public TextView mUsername;
    public TextView mEmail;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = (TextView) myMainView.findViewById(R.id.dialog_name_id);
        mEmail = (TextView) myMainView.findViewById(R.id.dialog_phone_id);
    }

    public void setmUsername(TextView mUsername) {
        this.mUsername = mUsername;
    }

    public void setmEmail(TextView mEmail) {
        this.mEmail = mEmail;
    }

}


