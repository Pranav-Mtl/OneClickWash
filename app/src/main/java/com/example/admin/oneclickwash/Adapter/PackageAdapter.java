package com.example.admin.oneclickwash.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.oneclickwash.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by appslure on 08-12-2015.
 */
public class PackageAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<Integer>> _listImgChild;

    TextView txtListChild;


    ImageView imgIndicator;

    public PackageAdapter(Context context, List<String> listDataHeader,
                         HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;


    }


    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
       return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_parent, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        TextView lblListHeaderDestination=(TextView) convertView
                .findViewById(R.id.lblListHeaderDestination);
        LinearLayout ll= (LinearLayout) convertView.findViewById(R.id.parent_main);
        imgIndicator= (ImageView) convertView.findViewById(R.id.expandableIcon);

        if(groupPosition != -1)
        {
            int imageResourceId = isExpanded ? R.drawable.ic_minus : R.drawable.ic_plus;
            imgIndicator.setImageResource(imageResourceId);
            imgIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            imgIndicator.setVisibility(View.INVISIBLE);

        }

        if(isExpanded){

            ll.setBackgroundColor(Color.parseColor("#19c2e0"));
        }
        else {
            ll.setBackgroundColor(Color.parseColor("#ffffff"));

        }


        String strExpand[]=headerTitle.split(",");

        lblListHeader.setText(strExpand[0]);
        lblListHeaderDestination.setText("₹ "+strExpand[1]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_child, null);
        }

        txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        String text = "<font color=#f55d2b>"+"● "+"</font>";
        System.out.println(text);

        Html.fromHtml(text);

        txtListChild.setText(Html.fromHtml(text+childText));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
