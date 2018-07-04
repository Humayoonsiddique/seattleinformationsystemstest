package app.seattleinformationsystemstest.com.seattleinformationsystemstest.Details;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.seattleinformationsystemstest.com.seattleinformationsystemstest.R;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.adapters.UserFollowerListAdapter;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.constants.getUserList;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUser;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView userName,userEmail;
    private RecyclerView recyclerView;
    private String username;
    private DemoUser _getSingleUser;
    private List<DemoUserDetails> userFollowerList;
    private Activity mActivity;
    private ProgressBar progress;
    private UserFollowerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mActivity = this;

        setStatusBarColor();

        getBundleValue();

        loadGui();

    }

    public void getBundleValue() {
        Intent intent= getIntent();
        if (intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        getSingleUserData();
        }

    }

    public void setStatusBarColor() {
        Window window = mActivity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_gray_color));
        }
    }

    public void getSingleUserData() {

        Call<DemoUser> _getSingleUserCall = getUserList.getService().getSingleUser(username);

        _getSingleUserCall.enqueue(new Callback<DemoUser>() {
            @Override
            public void onResponse(Call<DemoUser> call, Response<DemoUser> response) {

                _getSingleUser = response.body();

                uiUpdate();

                getFollowers();

            }
            @Override
            public void onFailure(Call<DemoUser> call, Throwable t) {
                Toast.makeText(mActivity,"Internet Issue",Toast.LENGTH_LONG).show();
//                mAdapter.notifyDataSetChanged();


            }
        });

    }
    public void getFollowers() {

        final Call<List<DemoUserDetails>> _userFollowersList = getUserList.getService().getUserFollowers(username);

        _userFollowersList.enqueue(new Callback<List<DemoUserDetails>>() {
            @Override
            public void onResponse(Call<List<DemoUserDetails>> call, Response<List<DemoUserDetails>> response) {

                userFollowerList = response.body();

                setAdpater();

            }
            @Override
            public void onFailure(Call<List<DemoUserDetails>> call, Throwable t) {
                Toast.makeText(mActivity,"Internet Issue",Toast.LENGTH_LONG).show();
//                mAdapter.notifyDataSetChanged();


            }
        });




    }


    public void setAdpater() {

        progress.setVisibility(View.GONE);

        mAdapter = new UserFollowerListAdapter(getApplicationContext(),userFollowerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    public void uiUpdate() {
        userName.setText(_getSingleUser.getLogin());
        userEmail.setText(_getSingleUser.getEmail());
        setUserProfileImage(_getSingleUser.getAvatarUrl(),profileImage);
    }

    public void setUserProfileImage(String url,ImageView imageView) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    public void loadGui() {
        profileImage = (ImageView) findViewById(R.id.profile_image);
        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        recyclerView = (RecyclerView) findViewById(R.id.list_followers);
        progress = (ProgressBar) findViewById(R.id.progress);

    }
}
