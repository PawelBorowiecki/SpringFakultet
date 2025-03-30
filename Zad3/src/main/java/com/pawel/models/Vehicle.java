package com.pawel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private String id, category, brand, model, plate;
    private int year;


    @Builder.Default
    private Map<String, Object> attributes = Map.of();

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value){
        attributes.put(key, value);
    }

    public void removeAttribute(String key){
        attributes.remove(key);
    }
}
