package hr.foi.air.buuterknige.Helper;

import android.support.v4.app.Fragment;

public interface NavigationItem {
    public String getItemName();
    public int getPosition();
    public void setPosition(int position);
    public Fragment getFragment();
}
