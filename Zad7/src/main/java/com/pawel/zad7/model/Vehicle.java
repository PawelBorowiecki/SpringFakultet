package com.pawel.zad7.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @Column(columnDefinition = "NUMERIC")
    private double price;
    private String category;
    private String brand;
    private String model;
    private int year;
    private String plate;
    @Column(columnDefinition = "BOOLEAN")
    private boolean isActive = true;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

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
