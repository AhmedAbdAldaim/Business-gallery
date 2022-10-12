package com.example.businesgalleryadmin.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businesgalleryadmin.Ui.Activity.Comments;
import com.example.businesgalleryadmin.LocalDB.SqlliteLove;
import com.example.businesgalleryadmin.Model.DeleteLoveResponse;
import com.example.businesgalleryadmin.Model.DeleteWorkResponse;
import com.example.businesgalleryadmin.Model.SendLikeResponse;
import com.example.businesgalleryadmin.Model.LoveandCommentsResponse;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.Ui.Activity.EditWork;
import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Model.GetAllWorksByUserIDModel2;
import com.example.businesgalleryadmin.Utility.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllWorksByUserID_adapter extends RecyclerView.Adapter<GetAllWorksByUserID_adapter.ViewHolder> implements Filterable {
    private Context context;
    private List<GetAllWorksByUserIDModel2> list;
    private List<GetAllWorksByUserIDModel2> filter;
    Bundle bundle;

    public GetAllWorksByUserID_adapter(List<GetAllWorksByUserIDModel2> getAllWorksByUserIDModel2, Context context) {
        this.context = context;
        this.list=getAllWorksByUserIDModel2;
        filter = new ArrayList<>(getAllWorksByUserIDModel2);
    }

    @NonNull
    @Override
    public GetAllWorksByUserID_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_allworks,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllWorksByUserID_adapter.ViewHolder holder, int position) {
        holder.username.setText(LocalSession.getName());
        if(LocalSession.getRole().equals("2")){
            holder.type.setText("مصمم");
        }else if(LocalSession.getRole().equals("3")){
            holder.type.setText("مصور فوتوغرافي");
        }else if(LocalSession.getRole().equals("4")){
            holder.type.setText("رسام");
        }
        holder.img_name.setText(list.get(position).getName());
        try {
            holder.img_details.setText(list.get(position).getDetails());
        }catch (StringIndexOutOfBoundsException e){
        }
        holder.img_price.setText(list.get(position).getPrice());
        Picasso.with(context).load("http://192.168.144.199:8000/images/works/"+list.get(position).getPhoto()).into(holder.image);



// <-----------------------Menue ----------------------------------------->
        holder.menue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             PopupMenu popupMenu = new PopupMenu(context,view);
             popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
        //<-----------------------Edit ------------------------------->
                case R.id.edit_work:
                    Intent intent = new Intent(context,EditWork.class);
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("img_name",list.get(position).getName());
                    intent.putExtra("img_details",list.get(position).getDetails());
                    intent.putExtra("img_price",list.get(position).getPrice());
                    intent.putExtra("image","http://192.168.144.199:8000/images/works/"+list.get(position).getPhoto());
                    context.startActivity(intent);
                    return true;
                    //<-----------------------Delete ------------------------------->
                case R.id.delete_work:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_logout_massage, null);
                    TextView item_delete_massage = view1.findViewById(R.id.item_delete_massage);
                    Button confirm_btn = view1.findViewById(R.id.confirm_btn);
                    Button cancle_btn = view1.findViewById(R.id.cancle_btn);
                    ImageView image_cancle = view1.findViewById(R.id.cancle);
                    item_delete_massage.setText(context.getResources().getString(R.string.item_delete_massage)+" "+ list.get(position).getName());
                    builder.setView(view1);
                    final AlertDialog dialog = builder.create();
                    InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                    dialog.getWindow().setBackgroundDrawable(insetDrawable);

                    //cancle
                    image_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

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
                    Call<DeleteWorkResponse> call = requestInterface.DeleteWorks(list.get(position).getId(), "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<DeleteWorkResponse>() {
                        @Override
                        public void onResponse(Call<DeleteWorkResponse> call, Response<DeleteWorkResponse> response) {
                            if (response.isSuccessful())
                            {
                                if (!response.body().isError())
                                {
                                    Toast.makeText(context, context.getResources().getString(R.string.delete_work_successfully) + "", Toast.LENGTH_SHORT).show();
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
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
                            public void onFailure(Call<DeleteWorkResponse> call, Throwable t) {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                            }
                        });
                    });
                        cancle_btn.setOnClickListener(v -> dialog.dismiss());
                        dialog.show();
                        return true;
                    }
                   return false;
                }
           });

                popupMenu.inflate(R.menu.menu);
                popupMenu.show();
            }
        });


    //<----------------------------Love ------------------------------------->
        holder.cursor = holder.sqlliteLove.show();

        //<-- check from sqllite -->
        while (holder.cursor.moveToNext()) {
            holder.value_love = String.valueOf(holder.cursor.getInt(holder.cursor.getColumnIndex("id")));
            if (holder.value_love.contentEquals(list.get(position).getId())) {
                holder.love.setVisibility(View.VISIBLE);
                holder.total_love_tv.setVisibility(View.VISIBLE);

            } else {
                holder.love_border.setVisibility(View.VISIBLE);
                holder.total_love_tv.setVisibility(View.GONE);
            }
        }
        //-- clilk--
        holder.love_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.is_add&& holder.love.getVisibility()==View.VISIBLE){

                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);

                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<DeleteLoveResponse> call = requestInterface.DeleteLove(holder.get_id_like, "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<DeleteLoveResponse>() {
                        @Override
                        public void onResponse(Call<DeleteLoveResponse> call, Response<DeleteLoveResponse> response) {
                            if (response.isSuccessful())
                            {
                                holder.total_love_tv.setVisibility(View.GONE);
                                holder.love.setVisibility(View.INVISIBLE);
                                holder.sqlliteLove.delete(list.get(position).getId());
                                holder.is_add = false;
                                loading.dismiss();
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteLoveResponse> call, Throwable t) {
                            loading.dismiss();
                           Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                        }
                    });


                }

                else if(holder.is_add){

                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);


                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<SendLikeResponse> call = requestInterface.SendLove(list.get(position).getId(), "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<SendLikeResponse>() {
                        @Override
                        public void onResponse(Call<SendLikeResponse> call, Response<SendLikeResponse> response) {
                            if (response.isSuccessful())
                            {
                                if(!response.body().isError()) {
                                    holder.total_love_tv.setVisibility(View.VISIBLE);
                                    holder.love.setVisibility(View.VISIBLE);
                                    holder.sqlliteLove.insert(list.get(position).getId());
                                    holder.get_id_like = response.body().getSendLikeModel().getId();
                                    holder.is_add = false;
                                    loading.dismiss();
                                }else{
                                    Toast.makeText(context, response.body().getMessage_ar()+"", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<SendLikeResponse> call, Throwable t) {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                        }
                    });

                }

                else if(holder.is_add==false){

                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);

                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<DeleteLoveResponse> call = requestInterface.DeleteLove(holder.get_id_like, "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<DeleteLoveResponse>() {
                        @Override
                        public void onResponse(Call<DeleteLoveResponse> call, Response<DeleteLoveResponse> response) {
                            if (response.isSuccessful())
                            {
                                holder.total_love_tv.setVisibility(View.GONE);
                                holder.love.setVisibility(View.INVISIBLE);
                                holder.sqlliteLove.delete(list.get(position).getId());
                                holder.is_add=true;
                                loading.dismiss();
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteLoveResponse> call, Throwable t) {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow)+t.getMessage(), context);

                        }
                    });

                }
            }
        });


        holder.total_love_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view1 = LayoutInflater.from(context).inflate(R.layout.custom_bottom_sheet_love,null);



                ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                loading.setContentView(R.layout.custom_progressbar);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);

                final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                Call<LoveandCommentsResponse> call = requestInterface.TotalLove(list.get(position).getId(), "Bearer " + LocalSession.getToken());
                call.enqueue(new Callback<LoveandCommentsResponse>() {
                    @Override
                    public void onResponse(Call<LoveandCommentsResponse> call, Response<LoveandCommentsResponse> response) {
                        if (response.isSuccessful())
                        {

                            TextView tota_love = view1.findViewById(R.id.total_love);
                            tota_love.setText(response.body().getTotalLoveModel().getLoveandCommentsModel().size()+"");
                            loading.dismiss();
                        }
                        else
                        {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoveandCommentsResponse> call, Throwable t) {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow)+t.getMessage(), context);

                    }
                });

                holder.bottomSheetDialog.setContentView(view1);
                holder.bottomSheetDialog.show();

            }
        });



     //<-----------------------------------Comments ------------------------------------------->
     holder.comments.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             context.startActivity(new Intent(context, Comments.class).putExtra("work_id",list.get(position).getId()));
         }
     });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    //<-- Search -->
    @Override
    public Filter getFilter() {
        return filterr;
    }

    public Filter filterr = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String key = charSequence.toString();
            List<GetAllWorksByUserIDModel2> list_getallworks = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {

             list_getallworks.addAll(filter);
            }
            else
            {
                for (GetAllWorksByUserIDModel2 item : list)
                {
                    if (item.getName().contains(key))
                    {
                        list_getallworks.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = list_getallworks;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends GetAllWorksByUserIDModel2>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder{
        Cursor cursor;
        Boolean is_add = true;
        String value_love,get_id_like;
        TextView username,type,img_name,img_price;
        com.borjabravo.readmoretextview.ReadMoreTextView img_details;
        ImageView image,menue,love,love_border,comments;
        SqlliteLove sqlliteLove;
        TextView total_love_tv;
        BottomSheetDialog bottomSheetDialog;

        public ViewHolder(@NonNull View itemView) {
             super(itemView);
            username = itemView.findViewById(R.id.username);
            type = itemView.findViewById(R.id.type);
            img_name = itemView.findViewById(R.id.image_title);
            img_details = itemView.findViewById(R.id.image_details);
            img_price = itemView.findViewById(R.id.image_price);
            image = itemView.findViewById(R.id.image);
            menue = itemView.findViewById(R.id.menue);
            love_border = itemView.findViewById(R.id.love_border);
            love = itemView.findViewById(R.id.love);
            sqlliteLove = new SqlliteLove(context);
            total_love_tv = itemView.findViewById(R.id.total_love);
            bottomSheetDialog = new BottomSheetDialog(context,R.style.bottomsheet);
            comments = itemView.findViewById(R.id.comments);
        }
    }
}
