package com.example.businesgalleryadmin.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.Model.GetAllOrdersModel;
import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.GetAllOrdersModel;
import com.example.businesgalleryadmin.Model.StatusOrdersResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Ui.Activity.MainActivity;
import com.example.businesgalleryadmin.Utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllOrders_adapter extends RecyclerView.Adapter<AllOrders_adapter.ViewHolder> {
    private Context context;
    private List<GetAllOrdersModel> list;


    public AllOrders_adapter(List<GetAllOrdersModel> ordersModels, Context context) {
        this.context = context;
        this.list=ordersModels;

    }

    @NonNull
    @Override
    public AllOrders_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_allorder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrders_adapter.ViewHolder holder, int position) {
        holder.name_of_order.setText(list.get(position).getName_of_theorder());
        holder.phone.setText(list.get(position).getPhone());
        holder.image_title.setText(list.get(position).getName());
        holder.image_price.setText(list.get(position).getPrice());
        Picasso.with(context).load("http://192.168.144.199:8000/images/works/"+list.get(position).getPhoto()).into(holder.imageView);

        if(list.get(position).getStatus().equals("0")){
            holder.status_order.setText("معلقة");

        }
        if(list.get(position).getStatus().equals("1")){
            holder.status_order.setText("مقبولة");
            holder.btn_acceapt_order.setVisibility(View.GONE);
            holder.btn_cancle_order.setVisibility(View.GONE);
        }
        if(list.get(position).getStatus().equals("2")){
            holder.status_order.setText("مرفوضة");
            holder.btn_acceapt_order.setVisibility(View.GONE);
            holder.btn_cancle_order.setVisibility(View.GONE);
        }

        //BUTTON Acceapt
        holder.btn_acceapt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_delete_comment, null);
                TextView item_delete_massage = view1.findViewById(R.id.item_delete_massage);
                Button confirm_btn = view1.findViewById(R.id.confirm_btn);
                Button cancle_btn = view1.findViewById(R.id.cancle_btn);

                item_delete_massage.setText(context.getResources().getString(R.string.acceapt_order_massage));
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                dialog.getWindow().setBackgroundDrawable(insetDrawable);


                //confirm
                confirm_btn.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);

                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<StatusOrdersResponse> call = requestInterface.StatusOrder(list.get(position).getOrder_id(), "1","Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<StatusOrdersResponse>() {
                        @Override
                        public void onResponse(Call<StatusOrdersResponse> call, Response<StatusOrdersResponse> response) {
                            if (response.isSuccessful())
                            {
                                if (!response.body().isError())
                                {
                                    Toast.makeText(context, context.getResources().getString(R.string.acceapt_order_successfully) + "", Toast.LENGTH_SHORT).show();
                                       MainActivity.meowBottomNavigatio.show(3,false);
                                    loading.dismiss();
                                }
                                else
                                {
                                    Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                                    loading.dismiss();
                                }
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusOrdersResponse> call, Throwable t) {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                        }
                    });
                });
                cancle_btn.setOnClickListener(v -> dialog.dismiss());
                dialog.show();

            }
        });


        holder.btn_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_delete_comment, null);
                TextView item_delete_massage = view1.findViewById(R.id.item_delete_massage);
                Button confirm_btn = view1.findViewById(R.id.confirm_btn);
                Button cancle_btn = view1.findViewById(R.id.cancle_btn);

                item_delete_massage.setText(context.getResources().getString(R.string.cancle_order_massage));
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                dialog.getWindow().setBackgroundDrawable(insetDrawable);


                //confirm
                confirm_btn.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);

                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<StatusOrdersResponse> call = requestInterface.StatusOrder(list.get(position).getOrder_id(), "2","Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<StatusOrdersResponse>() {
                        @Override
                        public void onResponse(Call<StatusOrdersResponse> call, Response<StatusOrdersResponse> response) {
                            if (response.isSuccessful())
                            {
                                if (!response.body().isError())
                                {
                                    Toast.makeText(context, context.getResources().getString(R.string.cancle_order_successfully) + "", Toast.LENGTH_SHORT).show();
                                    MainActivity.meowBottomNavigatio.show(3,false);
                                    loading.dismiss();
                                }
                                else
                                {
                                    Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                                    loading.dismiss();
                                }
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusOrdersResponse> call, Throwable t) {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                        }
                    });
                });
                cancle_btn.setOnClickListener(v -> dialog.dismiss());
                dialog.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView image_title,image_price,name_of_order,phone,status_order;
        ImageView imageView;
        Button btn_acceapt_order,btn_cancle_order;

        public ViewHolder(@NonNull View itemView) {
             super(itemView);
            image_title = itemView.findViewById(R.id.image_title);
            image_price = itemView.findViewById(R.id.image_price);
            imageView = itemView.findViewById(R.id.image);
            status_order = itemView.findViewById(R.id.status);
            name_of_order = itemView.findViewById(R.id.name_of_user);
            phone = itemView.findViewById(R.id.phone);
            btn_acceapt_order = itemView.findViewById(R.id.btn_acceapt_order);
            btn_cancle_order = itemView.findViewById(R.id.btn_canle_order);
           ;
        }
    }
}
