package com.phcworld.mock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class FakeHttpSession implements HttpSession {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return map.get(name);
    }

    @Override
    public Object getValue(String name) {
        return map.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        map.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        map.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        map.remove(name);
    }

    @Override
    public void removeValue(String name) {
        map.remove(name);
    }

    @Override
    public void invalidate() {
        map = new HashMap<>();
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
