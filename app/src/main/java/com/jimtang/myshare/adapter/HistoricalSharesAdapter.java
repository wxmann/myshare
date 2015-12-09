package com.jimtang.myshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.MyShareSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by tangz on 12/5/2015.
 */
public class HistoricalSharesAdapter extends ArrayAdapter<MyShareSession> {

    public HistoricalSharesAdapter(Context context, int resource, List<MyShareSession> sessions) {
        super(context, resource, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.display_historical_shares, null);
        }

        MyShareSession session = getItem(position);

        TextView title = (TextView) newView.findViewById(R.id.sessionTitle);
        title.setText(session.getSessionName());

        TextView time = (TextView) newView.findViewById(R.id.sessionTime);
        time.setText(dateString(session));

        TextView people = (TextView) newView.findViewById(R.id.sessionPeople);
        people.setText(peopleString(session));

        return newView;
    }

    private String dateString(MyShareSession session) {
        Date date = session.getTimestamp();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }

    private String peopleString(MyShareSession session) {
        Collection<String> people = session.getPeople();
        List<String> orderedPeopleList = Lists.newArrayList(people);
        Collections.sort(orderedPeopleList);

        int pplCt = orderedPeopleList.size();
        if (pplCt > 2) {
            return "Involves: " + Joiner.on(", ").join(orderedPeopleList.get(0), orderedPeopleList.get(1),
                    String.format("and %s more...", pplCt - 2));
        } else {
            return "Involves: " + Joiner.on(", ").join(orderedPeopleList);
        }
    }
}
