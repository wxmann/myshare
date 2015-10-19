package com.jimtang.myshare.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Cost;
import com.jimtang.myshare.model.MonetaryAmount;

import java.util.List;
import java.util.Map;

/**
 * Created by tangz on 10/18/2015.
 */
public class SplitCostsListAdapter extends BaseExpandableListAdapter {

    static final int SUBTOTAL_CHILD_POSITION = 0;
    static final int TAX_CHILD_POSITION = 1;
    static final int TIP_CHILD_POSITION = 2;

    private Context context;
    private List<String> names;
    private Map<String, Cost> nameShareMap;

    public SplitCostsListAdapter(Context context, List<String> names, Map<String, Cost> nameShareMap) {
        this.context = context;
        this.names = names;
        this.nameShareMap = nameShareMap;
    }

    @Override
    public int getGroupCount() {
        return names.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // Cost will always have three attributes: subtotal, tax, tip.
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return names.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Cost cost = nameShareMap.get(names.get(groupPosition));
        switch (childPosition) {
            case SUBTOTAL_CHILD_POSITION: return cost.getSubtotal();
            case TAX_CHILD_POSITION: return cost.getTax();
            case TIP_CHILD_POSITION: return cost.getTip();
            default: throw new IllegalArgumentException("Invalid child position.");
        }
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
        String name = (String) getGroup(groupPosition);
        Cost cost = nameShareMap.get(name);
        String headerTitle = name + ": " + cost.getTotal().toFormattedString();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_shares_person, null);
        }

        TextView headerTextView = (TextView) convertView.findViewById(R.id.share_person);
        headerTextView.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MonetaryAmount amt = (MonetaryAmount) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_shares_person_details, null);
        }

        TextView detailsView = (TextView) convertView.findViewById(R.id.share_person_detail);
        detailsView.setText(getDisplayText(childPosition, amt));

        return convertView;
    }

    private String getDisplayText(int childPosition, MonetaryAmount amt) {
        String amtTxt = amt.toFormattedString();
        switch(childPosition) {
            case SUBTOTAL_CHILD_POSITION: return "Subtotal: " + amtTxt;
            case TIP_CHILD_POSITION: return "Tip: " + amtTxt;
            case TAX_CHILD_POSITION: return "Tax: " + amtTxt;
            default:  throw new IllegalArgumentException("Invalid child position");
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
