package com.jimtang.myshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Cost;

import java.util.List;
import java.util.Map;

/**
 * Created by tangz on 10/18/2015.
 */
public class SplitCostsListAdapter extends BaseExpandableListAdapter {

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
        // just the subtotal/tax/tip details.
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return names.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return nameShareMap.get(names.get(groupPosition));
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
        String headerTitle = name + " owes : " + cost.getTotal().toFormattedString();

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
        Cost amt = (Cost) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_shares_person_details, null);
        }

        TextView detailsView = (TextView) convertView.findViewById(R.id.share_person_detail);
        detailsView.setText(getDisplayText(amt));

        return convertView;
    }

    private String getDisplayText(Cost cost) {
        StringBuilder builder = new StringBuilder();
        builder.append("Subtotal: ").append(cost.getSubtotal().toFormattedString()).append("\n");
        builder.append("Tax: ").append(cost.getTax().toFormattedString()).append("\n");
        builder.append("Tip: ").append(cost.getTip().toFormattedString());
        return builder.toString();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
