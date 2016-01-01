package com.example.admin.oneclickwash.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oneclickwash.BL.BookingListBL;
import com.example.admin.oneclickwash.BookingDetail;
import com.example.admin.oneclickwash.BookingList;
import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;
import com.example.admin.oneclickwash.RescheduleDrop;
import com.example.admin.oneclickwash.ReschedulePick;
import com.example.admin.oneclickwash.ScheduleDrop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by appslure on 15-12-2015.
 */
public class BookingListAdapter extends  RecyclerView.Adapter<BookingListAdapter.BookingListHolder> implements View.OnClickListener {
    Context mContext;
    BookingListBL objBookingListBL;
    public String result;
    int xx,yy;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormatTime = new SimpleDateFormat("K:mm a");
    Calendar cal;

    Activity activity;

    public  BookingListAdapter(Context context,String userID,int xxx,int yyy,Activity mActivity)
    {
        activity=mActivity;
        xx=xxx;
        yy=yyy;
        mContext=context;
        objBookingListBL=new BookingListBL();
        result=objBookingListBL.getBookingList(userID);

    }

    @Override
    public BookingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.booking_raw_list, parent, false);

        return new BookingListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingListHolder holder, int position) {

        if(!Constant.statusComplete.equalsIgnoreCase(Constant.bookingStatus[position])) {
            if (!Constant.statusCancel.equalsIgnoreCase(Constant.bookingStatus[position])) {
                if (!Constant.statusPickupFailed.equalsIgnoreCase(Constant.bookingStatus[position])) {
                    holder.tvBookingID.setTextColor(mContext.getResources().getColor(R.color.sky));
                }
                else {
                    holder.tvBookingID.setTextColor(mContext.getResources().getColor(R.color.textColor));
                }
            }
            else {
                holder.tvBookingID.setTextColor(mContext.getResources().getColor(R.color.textColor));
            }
        }
        else {
            holder.tvBookingID.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }


        holder.tvBookingID.setText("Booking ID: OCW"+Constant.bookingID[position]);

        holder.tvPickDate.setText(Constant.bookingPickdate[position]);
        holder.tvPickupSlot.setText(Constant.bookingPickTime[position]);

        if(Constant.bookingDryclean[position].equalsIgnoreCase("N"))
            holder.tvstatus.setText("Service Type: "+Constant.ServiceNormal);
        else
            holder.tvstatus.setText("Service Type: "+Constant.ServiceSpecial);

        String dd[]=Constant.bookingdate[position].split(" ");
        try {
            String onlydate=sdf.parse(dd[0])+"";
            onlydate=onlydate.substring(0,10);
            holder.tvBookingDate.setText("Booked on "+onlydate);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(Constant.statusPickup.equalsIgnoreCase(Constant.bookingStatus[position])){
            try {
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                String date = dateFormat.format(cal.getTime());
                Date d1 = dateFormat.parse(date);
                Date d2 = dateFormat.parse(Constant.bookingPickdate[position]);
                if(d2.compareTo(d1)==0){
                    Log.d("Ubder True condition",Constant.bookingPickdate[position]);
                    String time = dateFormatTime.format(cal.getTime());
                    Log.d("CURRENT TIME",time);
                    Date timed1 = dateFormatTime.parse(time);
                    String currentTime[]=Constant.bookingPickTime[position].split("-");
                    Date timed2 = dateFormatTime.parse(currentTime[0].trim());

                    long diffMs = timed2.getTime()- timed1.getTime() ;
                    long diffSec = diffMs / 1000;
                    long min = diffSec / 60;
                    long sec = diffSec % 60;
                    Log.d("Minutes",min+"");
                    Log.d("Window",Constant.bookingWindow[position]*60+"");

                    if(min>(Constant.bookingWindow[position]*60)){
                        holder.llReschedule.setVisibility(View.VISIBLE);
                        if(!Constant.bookingDryclean[position].equalsIgnoreCase("N")){
                            holder.btnReschedule.setVisibility(View.GONE);
                        }
                    }
                }
                else {
                    Log.d("Ubder False condition", Constant.bookingPickdate[position]);
                    holder.llReschedule.setVisibility(View.VISIBLE);
                    if(!Constant.bookingDryclean[position].equalsIgnoreCase("N")){
                        holder.btnReschedule.setVisibility(View.GONE);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            holder.llReschedule.setVisibility(View.GONE);
        }

        if(Constant.statusReady.equalsIgnoreCase(Constant.bookingStatus[position])){
            holder.llScheduleDrop.setVisibility(View.VISIBLE);
            holder.btnScheduleDrop.setVisibility(View.VISIBLE);
            /*if(Constant.bookingDryclean[position].equalsIgnoreCase("Y")){

                holder.llScheduleDrop.setVisibility(View.GONE);
                holder.btnScheduleDrop.setVisibility(View.GONE);
            }
            else {
                holder.llScheduleDrop.setVisibility(View.VISIBLE);
                holder.btnScheduleDrop.setVisibility(View.VISIBLE);
            }*/

        }
        else {
            holder.llScheduleDrop.setVisibility(View.GONE);
            holder.btnScheduleDrop.setVisibility(View.GONE);
        }

        if(Constant.statusDelivery.equalsIgnoreCase(Constant.bookingStatus[position])){

            try {
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                String date = dateFormat.format(cal.getTime());
                Date d1 = dateFormat.parse(date);
                Date d2 = dateFormat.parse(Constant.bookingDropdate[position]);
                if(d2.compareTo(d1)==0){
                    Log.d("Ubder True condition",Constant.bookingDropdate[position]);
                    String time = dateFormatTime.format(cal.getTime());
                    Log.d("CURRENT TIME",time);
                    Date timed1 = dateFormatTime.parse(time);
                    String currentTime[]=Constant.bookingDropTime[position].split("-");
                    Date timed2 = dateFormatTime.parse(currentTime[0].trim());

                    long diffMs = timed2.getTime()- timed1.getTime() ;
                    long diffSec = diffMs / 1000;
                    long min = diffSec / 60;
                    long sec = diffSec % 60;
                    Log.d("Minutes",min+"");
                    Log.d("Window",Constant.bookingDropWindow[position]*60+"");

                    if(min>(Constant.bookingDropWindow[position]*60)){
                         holder.llRescheduleDrop.setVisibility(View.VISIBLE);
                         holder.btnRescheduleDrop.setVisibility(View.VISIBLE);
                    }
                }
                else {
                     holder.llRescheduleDrop.setVisibility(View.VISIBLE);
                     holder.btnRescheduleDrop.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }
        else {
            holder.llRescheduleDrop.setVisibility(View.GONE);
            holder.btnRescheduleDrop.setVisibility(View.GONE);
        }


        holder.btnDetail.setOnClickListener(this);
        holder.btnDetail.setTag(position);

        holder.btnScheduleDrop.setOnClickListener(this);
        holder.btnScheduleDrop.setTag(position);

        holder.btnCancel.setOnClickListener(this);
        holder.btnCancel.setTag(position);

        holder.btnReschedule.setOnClickListener(this);
        holder.btnReschedule.setTag(position);

        holder.btnRescheduleDrop.setOnClickListener(this);
        holder.btnRescheduleDrop.setTag(position);
    }

    @Override
    public int getItemCount() {
        int count=0;
        if(Constant.bookingID==null)
            count=0;
        else
            count=Constant.bookingID.length;
        return count;
    }

    @Override
    public void onClick(View v) {
        int pos=(int) v.getTag();
        switch (v.getId()){
            case R.id.booking_list_detail:  // Navigate to booking detail page
                Intent intent=new Intent(mContext, BookingDetail.class);
                intent.putExtra("BookingID", Constant.bookingID[pos]);
                intent.putExtra("Window",Constant.bookingWindow[pos]);
                intent.putExtra("WindowDrop",Constant.bookingDropWindow[pos]);
                intent.putExtra("Type",Constant.bookingDryclean[pos]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                mContext.startActivity(intent);
                break;
            case R.id.booking_list_cancel:
                try {
                    showDialogCancel(activity,Constant.bookingID[pos],Constant.bookingDryclean[pos]);
                    //String result = new CancelPickup().execute(,).get();

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.booking_list_reschedue:  // Navigate to Pick-up Reschedule page
                Intent intentReschedue=new Intent(mContext, ReschedulePick.class);
                intentReschedue.putExtra("ID", Constant.bookingID[pos]);
                intentReschedue.putExtra("Date",Constant.bookingPickdate[pos]);
                intentReschedue.putExtra("Time",Constant.bookingPickTime[pos]);
                intentReschedue.putExtra("Type",Constant.bookingType[pos]);
                intentReschedue.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                mContext.startActivity(intentReschedue);
                break;
            case R.id.booking_list_schedule_drop: // Navigate to Schedule Drop page
                Intent intentDrop=new Intent(mContext, ScheduleDrop.class);
                intentDrop.putExtra("ID", Constant.bookingID[pos]);
                intentDrop.putExtra("Type",Constant.bookingType[pos]);
                intentDrop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                mContext.startActivity(intentDrop);
                break;
            case R.id.booking_list_reschedule_drop:
                Intent intentReschedueDrop=new Intent(mContext, RescheduleDrop.class);
                intentReschedueDrop.putExtra("ID", Constant.bookingID[pos]);
                intentReschedueDrop.putExtra("Date",Constant.bookingDropdate[pos]);
                intentReschedueDrop.putExtra("Time",Constant.bookingDropTime[pos]);
                intentReschedueDrop.putExtra("Type",Constant.bookingType[pos]);
                intentReschedueDrop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                mContext.startActivity(intentReschedueDrop);
                break;
        }
    }

    public static class BookingListHolder extends RecyclerView.ViewHolder {

        TextView tvBookingDate,tvPickDate,tvPickupSlot,tvstatus;
        Button tvBookingID,btnCancel,btnReschedule,btnScheduleDrop,btnDetail,btnRescheduleDrop;
        RelativeLayout llReschedule,llScheduleDrop,llRescheduleDrop;

        public BookingListHolder(View gridView) {
            super(gridView);
            tvBookingID= (Button) gridView.findViewById(R.id.booking_list_id);
            tvBookingDate= (TextView) gridView.findViewById(R.id.booking_list_date);
            tvPickDate= (TextView) gridView.findViewById(R.id.booking_list_pick_date);
            tvPickupSlot= (TextView) gridView.findViewById(R.id.booking_list_pick_time);
            tvstatus= (TextView) gridView.findViewById(R.id.booking_list_status);
            btnReschedule= (Button) gridView.findViewById(R.id.booking_list_reschedue);
            btnScheduleDrop= (Button) gridView.findViewById(R.id.booking_list_schedule_drop);
            btnCancel= (Button) gridView.findViewById(R.id.booking_list_cancel);
            llReschedule= (RelativeLayout) gridView.findViewById(R.id.layout_reschedule);
            llScheduleDrop= (RelativeLayout) gridView.findViewById(R.id.layout_dropoff);
            btnDetail= (Button) gridView.findViewById(R.id.booking_list_detail);
            btnRescheduleDrop= (Button) gridView.findViewById(R.id.booking_list_reschedule_drop);
            llRescheduleDrop= (RelativeLayout) gridView.findViewById(R.id.layout_drop_reschedule);

        }
    }

    private class CancelPickup extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            String result=objBookingListBL.cancelBooking(params[0],params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void showDialogCancel(Context context,final String bookingId,final String orderType){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvTitle;
        Button btnClosePopup,btnsave;
        RadioGroup rgPayment;
        final RadioButton rbYes,rbNo;
        final EditText etClothes;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_payment_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        rgPayment= (RadioGroup) dialog.findViewById(R.id.group_payment);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);
        rbYes= (RadioButton) dialog.findViewById(R.id.payment_cash);
        rbNo= (RadioButton) dialog.findViewById(R.id.payment_online);
        etClothes= (EditText) dialog.findViewById(R.id.popup_clothes);

        tvTitle.setText(mContext.getResources().getString(R.string._cancel_title));
        //tvMsg.setText(getResources().getString(R.string._referral_message));
        btnClosePopup.setText(mContext.getResources().getString(R.string._cancel_cancel));
        btnsave.setText(mContext.getResources().getString(R.string._cancel_save));
        rbYes.setText(mContext.getResources().getString(R.string._cancel_yes));
        rbNo.setText(mContext.getResources().getString(R.string._cancel_no));

        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbYes.isChecked()) {
                    try {
                        String result = new CancelPickup().execute(bookingId, orderType).get();
                        if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(result)) {
                            if (orderType.equalsIgnoreCase("Y"))
                                Toast.makeText(mContext, "Only Dry Clean Order Cancelled", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(mContext, "Pickup Cancelled", Toast.LENGTH_LONG).show();

                            try {
                                ((BookingList) mContext).finish();
                            }catch (Exception e){

                            }


                            Intent intentCancel = new Intent(mContext, BookingList.class);
                            intentCancel.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intentCancel);


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dialog.dismiss();
                    }
                    }
                    else if (rbNo.isChecked()) {
                        dialog.dismiss();
                    }


                }
            }

            );


        dialog.show();
        }
    }
