package hr.foi.air.buuterknige;

import android.app.Activity;

public class CurrentActivity {
    private static Activity mActivity;

    public static Activity getActivity()
    {
        return mActivity;
    }

    public static void setActivity(Activity activity)
    {
        mActivity = activity;
    }
}
