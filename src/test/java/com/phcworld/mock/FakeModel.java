package com.phcworld.mock;

import org.springframework.core.Conventions;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FakeModel implements Model {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public Model addAttribute(String attributeName, Object attributeValue) {
        map.put(attributeName, attributeValue);
        return this;
    }

    @Override
    public Model addAttribute(Object attributeValue) {
        return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
    }

    @Override
    public Model addAllAttributes(Collection<?> attributeValues) {
        return this;
    }

    @Override
    public Model addAllAttributes(Map<String, ?> attributes) {
        return this;
    }

    @Override
    public Model mergeAttributes(Map<String, ?> attributes) {
        return this;
    }

    @Override
    public boolean containsAttribute(String attributeName) {
        Object object = map.get(attributeName);
        return Objects.nonNull(object);
    }

    @Override
    public Object getAttribute(String attributeName) {
        return map.get(attributeName);
    }

    @Override
    public Map<String, Object> asMap() {
        return map;
    }
}
