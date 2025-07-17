package com.testUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCaseDataPOJO {
    private String testCaseName;
    private int cartValue;
    private int expectedCartValue;
    private int statusCode;
    private int userId;
    private int restaurantId;
    private String offerType;
    private int offerValue;
    private String mockSegment;
    private String expectedError;
}
