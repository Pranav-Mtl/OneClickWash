package com.example.admin.oneclickwash;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.oneclickwash.Constant.Constant;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ConcurrentSkipListMap;


/**
 * Created by Admin on 11/26/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>  {



    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    private String name;        //String Resource for header View Name
    private String profilePic;        //int Resource for header view profile picture
    private String email;       //String Resource for header view email
    private String wallet;
    static  Context mContext;
    String imgUrl;




    public class ViewHolder extends RecyclerView.ViewHolder {

        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name,signUp;
        Button viewProfile;
        TextView email;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView =(TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView =(ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            } else {


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                // Creating Text View object from header.xml for email
               // Creating Image view object from header.xml for profile pic
               signUp=(TextView)itemView.findViewById(R.id.wallet);
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }

    public DrawerAdapter(String[] Titles, int[] Icons, String Name,String Wallet, Context context) {
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;

        wallet=Wallet;
        mContext=context;

    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons

        } else {


            holder.Name.setText(Constant.NAME);
            holder.signUp.setText("Wallet ₹"+Constant.WALLET);

        }

    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

