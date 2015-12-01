package com.jimtang.myshare.model;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tangz on 11/10/2015.
 */
public class MyShareSession {

    private final String sessionName;
    private final List<Share> shares;
    private final Date timestamp;

    public MyShareSession(String sessionName, List<Share> shares, Date timestamp) {
        this.sessionName = sessionName;
        this.shares = shares;
        this.timestamp = timestamp;
    }

    public String getSessionName() {
        return sessionName;
    }

    public List<Share> getShares() {
        return Collections.unmodifiableList(shares);
    }

    public Collection<String> getPeople() {
        Collection<String> ppl = Sets.newHashSet();
        for (Share share: shares) {
            ppl.add(share.getPersonName());
        }
        return ppl;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
