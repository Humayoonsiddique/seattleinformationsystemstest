package app.seattleinformationsystemstest.com.seattleinformationsystemstest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.seattleinformationsystemstest.com.seattleinformationsystemstest.R;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUserDetails;

public class UserFollowerListAdapter extends RecyclerView.Adapter<UserFollowerListAdapter.MyViewHolder> {

    public List<DemoUserDetails> userFollowerList;
    private static Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView profileImage;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.user_name);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);


        }


    }


    public UserFollowerListAdapter(Context context, List<DemoUserDetails> userFollowerLists) {
        mContext = context;
        this.userFollowerList = userFollowerLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserFollowerListAdapter.MyViewHolder holder, int position) {

        DemoUserDetails userFollower = userFollowerList.get(position);
        holder.userName.setText(userFollower.getLogin());
        setUserProfileImage(userFollower.getAvatarUrl(), holder.profileImage);


    }


    public void setUserProfileImage(String url, ImageView imageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return userFollowerList.size();
    }



}