package com.openshop.gazapp.view.activity.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.BuildConfig;
import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.CategoryAdapter;
import com.openshop.gazapp.adapter.SidemenuAdapter;
import com.openshop.gazapp.adapter.SlideAdapter;
import com.openshop.gazapp.data.helper.OnEndLess;
import com.openshop.gazapp.data.model.SideMenuModel;
import com.openshop.gazapp.data.model.advertisement.Advertisement;
import com.openshop.gazapp.data.model.advertisement.Datum;
import com.openshop.gazapp.data.model.category.Category;
import com.openshop.gazapp.data.model.category.CategoryData;
import com.openshop.gazapp.data.model.setting.Setting;
import com.openshop.gazapp.view.activity.auth.AuthActivity;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.shareDialoge;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.FIREBASE_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.clean;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;

public class HomeActivity extends AppCompatActivity implements SidemenuAdapter.OnSideMenuItemClickListener {

    @BindView(R.id.activity_home_iv_menu)
    ImageView activityHomeIvMenu;
    @BindView(R.id.activity_home_tv_title)
    TextView activityHomeTvTitle;
    @BindView(R.id.activity_home_iv_cart)
    ImageView activityHomeIvCart;
    @BindView(R.id.activity_home_rel_1)
    RelativeLayout activityHomeRel1;
    @BindView(R.id.activity_home_rv_categories)
    RecyclerView activityHomeRvCategories;
    @BindView(R.id.home_container)
    RelativeLayout homeContainer;
    @BindView(R.id.activity_home_iv_search)
    ImageView activityHomeIvSearch;
    @BindView(R.id.activity_home_vp_ads)
    ViewPager activityHomeVpAds;
    private AppBarConfiguration mAppBarConfiguration;
    private SlidingRootNav slidingRootNav;
    List<SideMenuModel> sideMenusModels = new ArrayList<SideMenuModel>();
    SidemenuAdapter adapter;
    private ImageView iv_profile;
    private TextView tv_name;
    private TextView tv_logout;
    //TODO categories
    List<CategoryData> categories;
    CategoryAdapter categoryAdapter;
    GridLayoutManager grid_categories;
    String token;
    private OnEndLess onEndLess;
    private int Maxpage;
    private Dialog dialog;
    //TODO auto slide
    private Timer timer;
    private int current_position = 0;
    private ArrayList<Datum> offer_data;
    SlideAdapter slideAdapter;
    @BindView(R.id.indicator)
    SpringDotsIndicator indicator;
    int currentPage = 0;
    Timer timer2;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home2);
        ButterKnife.bind(this);
        checkLogin();
        indicator.setVisibility(View.GONE);

        addDataToSlider();

        createSlideShow();

        //TODO setup side menu
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withGravity(SlideGravity.RIGHT)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        addDataToMenu();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_left_drawer_rv_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SidemenuAdapter adapter = new SidemenuAdapter(this, sideMenusModels, this);
        recyclerView.setAdapter(adapter);
        initViews();
        try {
            if (loadUserData(this , USER_DATA)!=null)
            {
                tv_name.setText(loadUserData(this, USER_DATA).getName());



            if (loadUserData(this, USER_DATA).getImage() != null) {

                Glide.with(this).load(loadUserData(this, USER_DATA).getImage()).into(iv_profile);
            }
            }
        } catch (Exception e) {

        }
        //TODO get user profile
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile_i);
            }
        });
        //TODO search
        activityHomeIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SaarchActivity.class));

            }
        });
        //TODO logout
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show logout dialoge
                showDialoge();
            }
        });
        //TODO cart
        activityHomeIvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));


            }
        });

        //TODO add categories
        grid_categories = new GridLayoutManager(this, 2);
        activityHomeRvCategories.setLayoutManager(grid_categories);
        token = LoadData(HomeActivity.this, API_TOKEN);
        addCategories(token, 1);
        categoryAdapter = new CategoryAdapter(categories, HomeActivity.this);
        activityHomeRvCategories.setAdapter(categoryAdapter);

        onEndLess = new OnEndLess(grid_categories, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        addCategories(token, current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };


//
    }
    //check login
public void checkLogin()
{
    try {
        if (LoadData(this , API_TOKEN)==null)
        {
            startActivity(new Intent(HomeActivity.this , AuthActivity.class));
        }

    }
    catch (Exception e)
    {

    }
}

    private void addCategories(String api_token, int page) {
        categories = new ArrayList<>();

        showProgressDialog(this, this.getString(R.string.wait));
        getClient().getCategories(api_token, page).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                dismissProgressDialog();
              //  Toast.makeText(HomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                try {
                    if (response.body().getStatus() == 1) {
                        categories.addAll(response.body().getData().getData());
                        categoryAdapter.notifyDataSetChanged();
                        Maxpage = response.body().getData().getLastPage();
//                        startBottomnimation(activityHomeRvCategories , HomeActivity.this);


                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });

    }

    private void initViews() {
        iv_profile = (ImageView) findViewById(R.id.menu_left_drawer_iv_profile);
        tv_name = (TextView) findViewById(R.id.menu_left_drawer_tv_name);
        tv_logout = (TextView) findViewById(R.id.menu_left_drawer_tv_logout);
    }

    private void addDataToMenu() {
        sideMenusModels.add(new SideMenuModel(R.drawable.home, getString(R.string.home), true));
        sideMenusModels.add(new SideMenuModel(R.drawable.orders, this.getString(R.string.mu_orders), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.cart, this.getString(R.string.cart), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.notification, this.getString(R.string.notifications), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.about, this.getString(R.string.about), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.rules, this.getString(R.string.rules), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.contactus, this.getString(R.string.contact_us), false));

        sideMenusModels.add(new SideMenuModel(R.drawable.share, this.getString(R.string.share), false));
        sideMenusModels.add(new SideMenuModel(R.drawable.ic_star_border_black_24dp,this.getString(R.string.rate), false));




    }

    @OnClick(R.id.activity_home_iv_menu)
    public void onViewClicked() {
        slidingRootNav.openMenu();
    }

    @Override
    public void onItemSelectListener(int index, Object obj) {
        switch (index) {
            case 0:
                slidingRootNav.closeMenu();
                break;
            case 1:
                //TODO my orders
                startActivity(new Intent(HomeActivity.this, OrdersActivity.class));

                break;
            case 2:
                //TODO my cart
                startActivity(new Intent(HomeActivity.this, CartActivity.class));

                break;
            case 3:
                //TODO my notifications
                startActivity(new Intent(HomeActivity.this, NotificationsActivity.class));


                break;
            case 4:
                //TODO about
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
            case 5:
                //TODO rules
                startActivity(new Intent(HomeActivity.this, RulesActivity.class));
                break;
            case 6:
                //TODO contact us
                startActivity(new Intent(HomeActivity.this, ContactusActivity.class));

                break;
            case 7:
                //TODO share app
                shareDialoge(this);
                break;
            case 8 :
                //TODO rate
                Intent val_i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
                startActivity(val_i);
                break;



        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //TODO logout
    private void showDialoge() {
        dialog = new Dialog(this, R.style.customDialogTheme);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.logout_dialog, null);
        dialog.setContentView(v);
        ImageView yes = (ImageView) v.findViewById(R.id.logout_dialog_btn_yes);
        ImageView no = (ImageView) v.findViewById(R.id.logout_dialog_btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFirebase();


            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }
    //Create Slide Show
    public void createSlideShow() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position == offer_data.size())
                    current_position = 0;
                activityHomeVpAds.setCurrentItem(current_position++, false);


            }

        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);

            }
        }, 250, 2500);

    }



    //TODO add slider
    public void addDataToSlider() {
        offer_data = new ArrayList<>();

        // showProgressDialog(getActivity(), "Please Wait...");
        getClient().getAds(LoadData(this , API_TOKEN)).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(Call<Advertisement> call, Response<Advertisement> response) {
                dismissProgressDialog();
            //    Toast.makeText(HomeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus()==1) {

                        offer_data.addAll(response.body().getData().getData());

                        slideAdapter = new SlideAdapter(HomeActivity.this, offer_data);
                        activityHomeVpAds.setAdapter(slideAdapter);
                        //TODO indicator
                        indicator.setVisibility(View.VISIBLE);
                        indicator.setViewPager(activityHomeVpAds);
                        indicator.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                    }
                }catch (Exception e){
                    dismissProgressDialog();
                }
            }
            @Override
            public void onFailure(Call<Advertisement> call, Throwable t) {
                dismissProgressDialog();

            }
        });


    }
    //TODO remove firebase
    public void removeFirebase()
    {
        getClient().removeToken(LoadData(HomeActivity.this , API_TOKEN) , LoadData(HomeActivity.this , FIREBASE_TOKEN))
                .enqueue(new Callback<Setting>() {
                    @Override
                    public void onResponse(Call<Setting> call, Response<Setting> response) {
                        try {
                            if (response.body().getStatus()==1)
                            {
                                Log.d("FIREBASE" , response.body().getMsg());

                                clean(HomeActivity.this);
                                dialog.dismiss();
                                finishAffinity();
                            }

                        }
                        catch (Exception e)
                        {
                            Log.d("FIREBASE" , e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<Setting> call, Throwable t) {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


}
