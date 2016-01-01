package com.example.admin.oneclickwash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;

/**
 * Created by appslure on 14-12-2015.
 */
public class MonthlyPriceAdapter extends  RecyclerView.Adapter<MonthlyPriceAdapter.MonthlyPriceHolder> {

    Context mContext;

    public MonthlyPriceAdapter(Context context){
        mContext=context;
    }

    @Override
    public MonthlyPriceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.monthly_pricing_raw, parent, false);

        return new MonthlyPriceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MonthlyPriceHolder holder, int position) {
        holder.tvPackage.setText("₹"+Constant.pricingPackage[position]);
        holder.tvPickup.setText(Constant.pricingPickup[position]);
        holder.tvOriginal.setText("₹"+Constant.pricingOriginal[position]);
        holder.tvClothes.setText(Constant.pricingCloth[position]);
        holder.tvUsers.setText(Constant.pricingUser[position]);
    }

    @Override
    public int getItemCount() {
        return Constant.pricingUser.length;
    }

    public static class MonthlyPriceHolder extends RecyclerView.ViewHolder {

        TextView tvUsers,tvClothes,tvPickup,tvPackage,tvOriginal;

        public MonthlyPriceHolder(View gridView) {
            super(gridView);
            tvUsers= (TextView) gridView.findViewById(R.id.monthly_users);
            tvClothes= (TextView) gridView.findViewById(R.id.monthly_clothes);
            tvOriginal= (TextView) gridView.findViewById(R.id.monthly_original);
            tvPickup= (TextView) gridView.findViewById(R.id.monthly_pickup);
            tvPackage= (TextView) gridView.findViewById(R.id.monthly_package);


        }
    }


}
