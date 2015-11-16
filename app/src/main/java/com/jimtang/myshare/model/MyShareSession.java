package com.jimtang.myshare.model;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by tangz on 11/10/2015.
 */
public class MyShareSession {

    private final String sessionName;
    private final List<Share> shares;

    public MyShareSession(String sessionName, List<Share> shares) {
        this.sessionName = sessionName;
        this.shares = shares;
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
}
