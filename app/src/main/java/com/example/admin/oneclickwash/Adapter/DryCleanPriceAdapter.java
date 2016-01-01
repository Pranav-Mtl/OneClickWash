package com.example.admin.oneclickwash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.oneclickwash.Constant.Constant;
import com.example.admin.oneclickwash.R;

/**
 * Created by appslure on 14-12-2015.
 */
public class DryCleanPriceAdapter extends  RecyclerView.Adapter<DryCleanPriceAdapter.DryCleanPriceHolder> {

   Context mContext;
    public DryCleanPriceAdapter(Context context){
        mContext=context;
    }

    @Override
    public DryCleanPriceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.dryclean_price_raw, parent, false);

        return new DryCleanPriceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DryCleanPriceHolder holder, int position) {

        holder.tvItems.setText(Constant.dryCleanItem[position]);
        holder.tvDiscount.setText("₹"+Constant.dryCleanPrice[position]);
        holder.tvOriginal.setText("₹"+Constant.dryCleanOriginal[position]);
    }

    @Override
    public int getItemCount() {
        return Constant.dryCleanID.length;
    }

    public static class DryCleanPriceHolder extends RecyclerView.ViewHolder {

        TextView tvItems,tvDiscount,tvOriginal;

        public DryCleanPriceHolder(View gridView) {
            super(gridView);
            tvItems= (TextView) gridView.findViewById(R.id.dryclean_item);
            tvDiscount= (TextView) gridView.findViewById(R.id.dryclean_discounted);
            tvOriginal= (TextView) gridView.findViewById(R.id.dryclean_original);


        }
    }
}
