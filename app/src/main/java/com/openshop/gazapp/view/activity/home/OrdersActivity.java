package com.openshop.gazapp.view.activity.home;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

import com.google.android.material.tabs.TabLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.view.fragment.order.CurrentOrder;
import com.openshop.gazapp.view.fragment.order.LastOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.openshop.gazapp.data.helper.HelperMethod.replaceFragment;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.activity_orders_btn_back)
    ImageView activityOrdersBtnBack;


    @BindView(R.id.activity_orders_tab_layout_orders)
    TabLayout activityOrdersTabLayoutOrders;
    private CurrentOrder fragment_current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders2);
        ButterKnife.bind(this);
        fragment_current = new CurrentOrder();
        replaceFragment(getSupportFragmentManager(), R.id.orders_container, fragment_current);

        activityOrdersTabLayoutOrders.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(getSupportFragmentManager(), R.id.orders_container, fragment_current);

                        break;
                    case 1:
                        replaceFragment(getSupportFragmentManager(), R.id.orders_container, new LastOrder());

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //TODO set default selection
        TabLayout.Tab tab = activityOrdersTabLayoutOrders.getTabAt(0);
        tab.select();
    }


    @OnClick(R.id.activity_orders_btn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
