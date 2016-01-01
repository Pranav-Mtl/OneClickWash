package com.example.admin.oneclickwash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;

/**
 * Created by appslure on 14-12-2015.
 */
public class SubscriptionPackageAdapter extends  RecyclerView.Adapter<SubscriptionPackageAdapter.SubscriptionPackagHolder> {

    Context mContext;
    public int mSelectedItem = -1;



    CheckBox lastChecked;
    public SubscriptionPackageAdapter(Context context){
        mContext=context;
    }
    @Override
    public SubscriptionPackagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.subscription_package_raw, parent, false);

        return new SubscriptionPackagHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubscriptionPackagHolder holder,final int position) {
        holder.tvPackage.setText("₹"+Constant.pricingPackage[position]);
        holder.tvPickup.setText(Constant.pricingPickup[position]);

        holder.tvClothes.setText(Constant.pricingCloth[position]);
        holder.tvUsers.setText(Constant.pricingUser[position]);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;

                if (cb.isChecked()) {

                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        lastChecked = null;
                    }

                    mSelectedItem = position;
                    lastChecked = cb;

                } else {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        lastChecked = null;
                    }
                    mSelectedItem = -1;
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return Constant.pricingID.length;
    }

    public static class SubscriptionPackagHolder extends RecyclerView.ViewHolder {

        TextView tvUsers,tvClothes,tvPickup,tvPackage,tvOriginal;
        CheckBox checkBox;

        public SubscriptionPackagHolder(View gridView) {
            super(gridView);
            tvUsers= (TextView) gridView.findViewById(R.id.monthly_users);
            tvClothes= (TextView) gridView.findViewById(R.id.monthly_clothes);

            tvPickup= (TextView) gridView.findViewById(R.id.monthly_pickup);
            tvPackage= (TextView) gridView.findViewById(R.id.monthly_package);
            checkBox= (CheckBox) gridView.findViewById(R.id.monthly_checkbox);


        }
    }

}
