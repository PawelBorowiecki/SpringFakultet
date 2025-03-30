package com.pawel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    private String id, vehicleId, userId, rentDateTime, returnDateTime;
}
