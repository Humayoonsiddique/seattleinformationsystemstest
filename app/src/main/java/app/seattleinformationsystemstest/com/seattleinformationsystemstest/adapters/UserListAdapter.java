package app.seattleinformationsystemstest.com.seattleinformationsystemstest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.seattleinformationsystemstest.com.seattleinformationsystemstest.R;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUserDetails;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> implements Filterable{

    public List<DemoUserDetails> getUserLists;
    public List<DemoUserDetails> getUserFilteredLists;
    public UserListAdapterListener listener;
    private static Context mContext;



    public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public ImageView profileImage;

    public MyViewHolder(View view) {
        super(view);
        userName = (TextView) view.findViewById(R.id.user_name);
        profileImage = (ImageView) view.findViewById(R.id.profile_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onUserListSelected(getUserFilteredLists.get(getAdapterPosition()));
            }
        });
    }


}


    public UserListAdapter(Context context, List<DemoUserDetails> _getUserLists, UserListAdapterListener listener ) {
        mContext = context;
        this.getUserLists = _getUserLists;
        this.listener = listener;
        this.getUserFilteredLists = getUserLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DemoUserDetails mgetUserList = getUserFilteredLists.get(position);
        holder.userName.setText(mgetUserList.getLogin());
        setUserProfileImage(mgetUserList.getAvatarUrl(),holder.profileImage);


    }



    public void setUserProfileImage(String url,ImageView imageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return getUserFilteredLists.size();
    }
    boolean isEmpty = false;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getUserFilteredLists = getUserLists;
                } else {
                    List<DemoUserDetails> filteredList = new ArrayList<>();
                    for (DemoUserDetails row : getUserLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLogin().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    getUserFilteredLists = filteredList;


                }
                FilterResults filterResults = new FilterResults();


//
                filterResults.values = getUserFilteredLists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getUserFilteredLists = (ArrayList<DemoUserDetails>) filterResults.values;
                notifyDataSetChanged();


            }
        };
    }

    public interface UserListAdapterListener {
        void onUserListSelected(DemoUserDetails getUserList);
    }


}