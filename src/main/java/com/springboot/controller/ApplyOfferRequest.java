package com.springboot.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplyOfferRequest {
    private int cart_value;
    private int restaurant_id;
    private int user_id;
}
