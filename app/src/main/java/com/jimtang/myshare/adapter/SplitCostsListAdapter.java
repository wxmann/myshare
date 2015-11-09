package com.jimtang.myshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;
import com.jimtang.myshare.model.Share;

import java.util.List;
import java.util.Map;

/**
 * Created by tangz on 10/18/2015.
 */
public class SplitCostsListAdapter extends BaseExpandableListAdapter {

    static final int PORTIONS_POSITION = 0;
    static final int CUMUL_COSTS_POSITION = 1;

    private Context context;
    private List<Share> shares;

    public SplitCostsListAdapter(Context context, List<Share> shares) {
        this.context = context;
        this.shares = shares;
    }

    @Override
    public int getGroupCount() {
        return shares.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // just the subtotal/tax/tip details and share details.
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shares.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Share share = shares.get(groupPosition);
        switch (childPosition) {
            case PORTIONS_POSITION:
                return share.getExpensePortions();
            case CUMUL_COSTS_POSITION:
                return share.getIndivCumulativeCost();
            default:
                throw new IllegalArgumentException("Invalid child position in shared costs list");
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
        Share share = (Share) getGroup(groupPosition);
        String headerTitle = share.getPersonName() +
                " owes: " + share.getIndivCumulativeCost().getTotal().toFormattedString();

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
        String textToDisplay;
        Object childObj = getChild(groupPosition, childPosition);
        switch (childPosition) {
            case PORTIONS_POSITION:
                textToDisplay = getPortionsDisplay((Map<Expense, MonetaryAmount>) childObj);
                break;
            case CUMUL_COSTS_POSITION:
                textToDisplay = getCumulativeDisplay((CumulativeCost) childObj);
                break;
            default:
                throw new IllegalArgumentException("Invalid group/child position");

        }
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_shares_person_details, null);
        }

        TextView detailsView = (TextView) convertView.findViewById(R.id.share_person_detail);
        detailsView.setText(textToDisplay);

        return convertView;
    }

    private String getPortionsDisplay(Map<Expense, MonetaryAmount> amountMap) {
        String title = "Item subtotals (w/o tax or tip):\n";
        if (amountMap.isEmpty()) {
            return title + "N/A";
        }
        List<String> portionsPieces = Lists.newArrayList(title);
        for (Map.Entry<Expense, MonetaryAmount> expenseEntry: amountMap.entrySet()) {
            StringBuilder builder = new StringBuilder();
            Expense expense = expenseEntry.getKey();
            MonetaryAmount amount = expenseEntry.getValue();
            builder.append(expense.getExpenseName());
            builder.append(": ");
            builder.append(amount.toFormattedString());
            portionsPieces.add(builder.toString());
        }
        return Joiner.on("\n").join(portionsPieces);
    }

    private String getCumulativeDisplay(CumulativeCost cost) {
        StringBuilder builder = new StringBuilder("Cumulative amounts:\n\n");
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
