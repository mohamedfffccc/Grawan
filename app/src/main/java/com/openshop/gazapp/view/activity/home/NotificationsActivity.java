package com.openshop.gazapp.view.activity.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.NotificationAdapter;
import com.openshop.gazapp.data.model.NotificationModel;
import com.openshop.gazapp.data.model.notification.Notification;
import com.openshop.gazapp.data.model.notification.NotificationData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;

public class NotificationsActivity extends AppCompatActivity {

    @BindView(R.id.activity_notification_btn_back)
    ImageView activityNotificationBtnBack;
    @BindView(R.id.activity_notification_rv_notifications)
    RecyclerView activityNotificationRvNotifications;
    ArrayList<NotificationData> data;
    NotificationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        linearLayoutManager=new LinearLayoutManager(this);
        activityNotificationRvNotifications.setLayoutManager(linearLayoutManager);
        disaplyNotifications();
    }

    private void disaplyNotifications() {
        data=new ArrayList<>();
        showProgressDialog(this , this.getString(R.string.wait));
        getClient().getNotificationList(LoadData(this , API_TOKEN)).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                dismissProgressDialog();
                Toast.makeText(NotificationsActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try{
                    if (response.body().getStatus()==1) {
                        data.addAll(response.body().getData().getNotifications());
                        adapter=new NotificationAdapter(data,NotificationsActivity.this);
                        activityNotificationRvNotifications.setAdapter(adapter);
                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.activity_notification_btn_back)
    public void onViewClicked() {
        super.onBackPressed();
    }
}
