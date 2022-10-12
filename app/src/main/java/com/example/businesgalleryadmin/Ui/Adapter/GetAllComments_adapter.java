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

import com.example.businesgalleryadmin.LocalDB.LocalSession;
import com.example.businesgalleryadmin.Model.DeleteCommentResponse;
import com.example.businesgalleryadmin.Model.DeleteWorkResponse;
import com.example.businesgalleryadmin.Model.LoveandCommentsModel2;
import com.example.businesgalleryadmin.Network.ApiClient;
import com.example.businesgalleryadmin.Network.RequestInterface;
import com.example.businesgalleryadmin.R;
import com.example.businesgalleryadmin.Utility.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllComments_adapter extends RecyclerView.Adapter<GetAllComments_adapter.ViewHolder>  {
    private Context context;
    private List<LoveandCommentsModel2> list;


    public GetAllComments_adapter(List<LoveandCommentsModel2> commentsModels, Context context) {
        this.context = context;
        this.list=commentsModels;
    }

    @NonNull
    @Override
    public GetAllComments_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_show_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllComments_adapter.ViewHolder holder, int position) {
        holder.tv_comment.setText(list.get(position).getComment());

        if(!list.get(position).getUser_id().matches(LocalSession.getId())) {
            holder.tv_delete_comment.setVisibility(View.INVISIBLE);
        }
        if(list.get(position).getUser_id().matches(LocalSession.getId())) {
            holder.tv_delete_comment.setVisibility(View.VISIBLE);
        }

        holder.tv_delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_delete_comment, null);
                TextView item_delete_massage = view1.findViewById(R.id.item_delete_massage);
                Button confirm_btn = view1.findViewById(R.id.confirm_btn);
                Button cancle_btn = view1.findViewById(R.id.cancle_btn);

                item_delete_massage.setText(context.getResources().getString(R.string.deletecomment_massage));
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
                    Call<DeleteCommentResponse> call = requestInterface.DeleteComment(list.get(position).getId(), "Bearer " + LocalSession.getToken());
                    call.enqueue(new Callback<DeleteCommentResponse>() {
                        @Override
                        public void onResponse(Call<DeleteCommentResponse> call, Response<DeleteCommentResponse> response) {
                            if (response.isSuccessful())
                            {
                                if (!response.body().isError())
                                {
                                    Toast.makeText(context, context.getResources().getString(R.string.delete_comment_successfully) + "", Toast.LENGTH_SHORT).show();
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
                        public void onFailure(Call<DeleteCommentResponse> call, Throwable t) {
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

        TextView tv_comment,tv_delete_comment;
        public ViewHolder(@NonNull View itemView) {
             super(itemView);
             tv_comment = itemView.findViewById(R.id.comment);
             tv_delete_comment = itemView.findViewById(R.id.delete_comment);
        }
    }
}
