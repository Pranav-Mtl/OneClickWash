package com.example.admin.oneclickwash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.oneclickwash.BL.NotificationBL;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appslure on 28-12-2015.
 */
public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context mContext;
    NotificationBL objNotificationBL;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NotificationAdapter(Context context,String userID){
        mContext=context;
        objNotificationBL=new NotificationBL();
        objNotificationBL.getNotificationList(userID);
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.notification_raw, parent, false);

        return new NotificationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        holder.tvTitle.setText(Constant.notificationTitle[position]);
        holder.tvMessage.setText(Constant.notificationMessage[position]);

        try {
            Date dtArray = dateFormat.parse(Constant.notificationDate[position]);
            //  contactViewHolder.tvTime.setText(new SimpleDateFormat("yyyy-MM-dd K:mm a").format(dtArray));
            holder.tvDate.setText("Date: "+new SimpleDateFormat("dd-MMM-yyyy K:mm a").format(dtArray));
        }
        catch (Exception e){

        }
        //holder.tvDate.setText("Date: "+Constant.notificationDate[position]);
    }

    @Override
    public int getItemCount() {
        if(Constant.notificationTitle==null){
            return 0;
        }
        return Constant.notificationTitle.length;
    }

    public static class NotificationHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvMessage,tvDate;

        public NotificationHolder(View gridView) {
            super(gridView);
            tvTitle= (TextView) gridView.findViewById(R.id.notification_title);
            tvMessage= (TextView) gridView.findViewById(R.id.notification_message);
            tvDate= (TextView) gridView.findViewById(R.id.notification_date);


        }
    }
}
