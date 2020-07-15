package com.openshop.gazapp.view.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.ProductAdapter;
import com.openshop.gazapp.adapter.ProductSearchAdapter;
import com.openshop.gazapp.data.model.products.ProductData;
import com.openshop.gazapp.data.model.products.Products;

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
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadStringList;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.saveStringList;

public class SaarchActivity extends AppCompatActivity {

    @BindView(R.id.activity_search_ed_keyword)
    EditText activitySearchEdKeyword;
    @BindView(R.id.activity_search_btn_search)
    ImageView activitySearchBtnSearch;
    @BindView(R.id.activity_search_btn_cancel)
    TextView activitySearchBtnCancel;
    @BindView(R.id.activity_search_rel_title)
    RelativeLayout activitySearchRelTitle;
    @BindView(R.id.activity_search_tv_last_search)
    TextView activitySearchTvLastSearch;
    @BindView(R.id.activity_search_lv_last_keywords)
    ListView activitySearchLvLastKeywords;
    @BindView(R.id.activity_search_rv_results)
    RecyclerView activitySearchRvResults;
    ArrayList<String> last_searching = new ArrayList<>();
    ArrayAdapter<String> search_adapter;
    //TODO search results
    ArrayList<ProductData> p_list;
    ProductSearchAdapter adapter;
    GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saarch);
        ButterKnife.bind(this);
        setLast();
        gridLayoutManager = new GridLayoutManager(this , 2);
        activitySearchRvResults.setLayoutManager(gridLayoutManager);
        activitySearchLvLastKeywords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activitySearchEdKeyword.setText(LoadStringList(SaarchActivity.this, "last_search").get(position));
                search(LoadStringList(SaarchActivity.this, "last_search").get(position));
            }
        });

    }

    @OnClick({R.id.activity_search_btn_search, R.id.activity_search_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_search_btn_search:
                last_searching.add(activitySearchEdKeyword.getText().toString());
                saveStringList(SaarchActivity.this, "last_search", last_searching);
                Log.d("TAG saved" , LoadStringList(SaarchActivity.this, "last_search")+"");
                search(activitySearchEdKeyword.getText().toString());

                break;
            case R.id.activity_search_btn_cancel:
                super.onBackPressed();
                break;
        }
    }
    //TODO set last searching
    public void  setLast()
    {
        try {
            if (LoadStringList(SaarchActivity.this, "last_search")!=null) {
                last_searching= LoadStringList(SaarchActivity.this, "last_search");
                search_adapter=new ArrayAdapter<String>(this , R.layout.item_text , R.id.text ,
                        last_searching   );
                activitySearchLvLastKeywords.setAdapter(search_adapter);
            }




        }
        catch (Exception e)
        {

        }
    }
    //TODO search
    public void search(String keyword)
    {
        p_list=new ArrayList<>();
        showProgressDialog(SaarchActivity.this , SaarchActivity.this.getString(R.string.wait));
        getClient().search(LoadData(SaarchActivity.this , API_TOKEN) , keyword , 1)
                .enqueue(new Callback<Products>() {
                    @Override
                    public void onResponse(Call<Products> call, Response<Products> response) {
                        p_list.clear();
                        dismissProgressDialog();
                        activitySearchLvLastKeywords.setVisibility(View.GONE);
                        activitySearchTvLastSearch.setVisibility(View.GONE);
                        activitySearchRvResults.setVisibility(View.VISIBLE);
                        Toast.makeText(SaarchActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        try {
                            if (response.body().getStatus()==1) {
                                p_list.addAll(response.body().getData().getData());

                                adapter=new ProductSearchAdapter(p_list,SaarchActivity.this , SaarchActivity.this);
                                activitySearchRvResults.setAdapter(adapter);
                            }

                        }
                        catch (Exception e)
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<Products> call, Throwable t) {

                    }
                });
    }

}
