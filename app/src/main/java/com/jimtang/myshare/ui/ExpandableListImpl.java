package com.jimtang.myshare.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.jimtang.myshare.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by tangz on 9/12/2015.
 */
public class ExpandableListImpl extends BaseExpandableListAdapter {

    private Context context;
    private List<String> headerItems;
    private ListMultimap<String, String> subItems;

    public ExpandableListImpl(Context context, Collection<String> headerItems, ListMultimap<String, String> subItems) {
        this.context = context;
        this.headerItems = Lists.newArrayList(headerItems);
        this.subItems = subItems;
    }

    @Override
    public int getGroupCount() {
        return headerItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupItem = headerItems.get(groupPosition);
        return subItems.get(groupItem).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String groupItem = headerItems.get(groupPosition);
        return subItems.get(groupItem).get(childPosition);
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
        return true;
    }

    private View getConvertView(int resource) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, null);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = getConvertView(R.layout.shares);
        }

        TextView listHeader = (TextView) convertView.findViewById(R.id.shares);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = getConvertView(R.layout.share_details);
        }

        TextView listChildren = (TextView) convertView.findViewById(R.id.share_details);
        listChildren.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
