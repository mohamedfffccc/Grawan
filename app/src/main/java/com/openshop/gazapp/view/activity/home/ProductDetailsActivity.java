package com.openshop.gazapp.view.activity.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.local.room.OrderItem;
import com.openshop.gazapp.data.local.room.RoomDao;
import com.openshop.gazapp.data.model.products.ProductData;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.openshop.gazapp.data.local.room.RoomManger.getInistance;

public class ProductDetailsActivity extends AppCompatActivity {
    public ProductData product;
    public String product_name;
    public String product_price;
    public String product_image;
    @BindView(R.id.activity_product_details_btn_add_item)
    ImageView additem;
    @BindView(R.id.activity_product_details_btn_remove_item)
    ImageView removeitem;
    @BindView(R.id.activity_product_details_btn_back)
    ImageView detailsbtnback;
    @BindView(R.id.activity_product_details__item_count)
    TextView tvcount;
    @BindView(R.id.activity_product_details_tv_product_name)
    TextView tvname;
    @BindView(R.id.activity_product_details_iv_product_image)
    ImageView ivproductimage;
    @BindView(R.id.activity_product_details_tv_product_describtion)
    TextView tvproductdescribtion;
    @BindView(R.id.activity_product_details_tv_product_price)
    TextView tvProductPrice;
    int count = 1;
    //TODO room
    private RoomDao roomDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        product = (ProductData) getIntent().getSerializableExtra("product");
        product_name = product.getName();
        product_image = product.getImage();
        product_price = product.getPrice();
        tvname.setText(product_name);
        tvcount.setText(String.valueOf(count));
        Glide.with(this).load(product_image).into(ivproductimage);
        tvproductdescribtion.setText(product.getDescription());
        tvProductPrice.setText(product_price + " ريال  ");
        tvcount.setText(String.valueOf(count));

    }

    @OnClick({R.id.activity_product_details_btn_add_item,R.id.activity_product_details_iv_cart , R.id.activity_product_details_iv_search, R.id.activity_product_details_btn_add_to_cart,
            R.id.activity_product_details_btn_remove_item, R.id.activity_product_details_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_product_details_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_product_details_btn_add_item:
                count = count + 1;
                tvcount.setText(String.valueOf(count));
                break;


            case R.id.activity_product_details_btn_remove_item:
                if (count > 1) {
                    count = count - 1;
                    tvcount.setText(String.valueOf(count));


                }
                break;
            case R.id.activity_product_details_btn_add_to_cart:
                AddToCart();
                break;
            case R.id.activity_product_details_iv_search:
                startActivity(new Intent(this , SaarchActivity.class));

                break;
            case R.id.activity_product_details_iv_cart:
                startActivity(new Intent(this , CartActivity.class));

                break;
        }
    }

    //Add To Cart
    private void AddToCart() {

        roomDao = getInistance(ProductDetailsActivity.this).roomDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                OrderItem item = new OrderItem(product.getId(), product.getCategoryId(), product.getName()
                        , product.getImage(), Double.parseDouble(product.getPrice()), Integer.parseInt(tvcount.getText().toString()));
                roomDao.addItem(item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProductDetailsActivity.this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    }
}
