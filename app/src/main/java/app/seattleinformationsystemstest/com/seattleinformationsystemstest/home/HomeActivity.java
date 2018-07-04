package app.seattleinformationsystemstest.com.seattleinformationsystemstest.home;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.seattleinformationsystemstest.com.seattleinformationsystemstest.Details.DetailActivity;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.adapters.UserListAdapter;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.constants.getUserList;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.R;
import app.seattleinformationsystemstest.com.seattleinformationsystemstest.models.DemoUserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements UserListAdapter.UserListAdapterListener {
    Activity mActivity;
    private List<DemoUserDetails> getUserLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserListAdapter mAdapter;
    private ProgressBar progress;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActivity = this;

        setStatusBarColor();


        loadGui();

        getAllUserData();

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

    public void loadGui() {

        toolbarSettings();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    private void toolbarSettings() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(ResourcesCompat.getFont(mActivity, R.font.romantic_beach));

        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    private void getAllUserData() {

        Call<List<DemoUserDetails>> _getUserList = getUserList.getService().getAllUserList();

        _getUserList.enqueue(new Callback<List<DemoUserDetails>>() {
            @Override
            public void onResponse(Call<List<DemoUserDetails>> call, Response<List<DemoUserDetails>> response) {

                getUserLists = response.body();

                setAdpater();
            }

            @Override
            public void onFailure(Call<List<DemoUserDetails>> call, Throwable t) {
                Toast.makeText(mActivity, "Internet Issue", Toast.LENGTH_LONG).show();
//                mAdapter.notifyDataSetChanged();


            }
        });
    }

    public void setAdpater() {

        progress.setVisibility(View.GONE);

        mAdapter = new UserListAdapter(getApplicationContext(), getUserLists, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onUserListSelected(DemoUserDetails getUserList) {
//        Toast.makeText(mActivity.getApplicationContext(), "Selected: " + getUserList.getLogin(), Toast.LENGTH_LONG).show();

        Intent DetailScreenActivityIntent = new Intent(mActivity, DetailActivity.class);
        DetailScreenActivityIntent.putExtra("username", getUserList.getLogin());
        startActivity(DetailScreenActivityIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        searchBarSettings(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void searchBarSettings(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                checkIfEmpty();
                return false;
            }

            private void checkIfEmpty() {
                if (mAdapter.getUserFilteredLists.isEmpty()) {

                    displayAlertUserNotFound();
                }
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                if (mAdapter.getUserFilteredLists.isEmpty()) {

                    displayAlertUserNotFound();
                }

                return false;
            }


        });
    }

    public void displayAlertUserNotFound() {
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        if (!alertDialog.isShowing()) {
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("User Not Found");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

}
