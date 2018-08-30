package hr.foi.air.buuterknige;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AllUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    static View mView;
    public ItemClickListener itemClickListener;

    public AllUserHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    static TextView emailUser;
    public static void setEmail(String email) {
        emailUser = (TextView) mView.findViewById(R.id.phone_contact);
        emailUser.setText(email);
    }

    public void setUsername(String username) {
        TextView userName = (TextView) mView.findViewById(R.id.name_contact);
        userName.setText(username);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}
