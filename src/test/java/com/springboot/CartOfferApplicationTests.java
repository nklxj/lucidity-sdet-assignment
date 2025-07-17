package com.springboot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.*;
import com.testUtils.APIClient;
import com.testUtils.TestCaseDataPOJO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CartOfferApplicationTests {

    private static final APIClient apiClient = new APIClient();
    private static final Logger log = LoggerFactory.getLogger(CartOfferApplicationTests.class);

    public static Stream<TestCaseDataPOJO> testCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = CartOfferApplicationTests.class
                .getClassLoader()
                .getResourceAsStream("data/test-cases.json");

        Map<String, TestCaseDataPOJO> map = mapper.readValue(is, new TypeReference<>() {
        });

        return map.entrySet().stream().map(entry -> {
            TestCaseDataPOJO data = entry.getValue();
            data.setTestCaseName(entry.getKey());
            return data;
        });
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Test Cart Calculation Offers")
    void testCartCalculation(TestCaseDataPOJO testCaseData) throws Exception {
        String testCaseName = testCaseData.getTestCaseName();
        log.info("Executing {}", testCaseName);
        List<String> segments = Objects.isNull(testCaseData.getMockSegment()) ? new ArrayList<>() : List.of(testCaseData.getMockSegment());
        OfferRequest offerRequest = OfferRequest.builder()
                .restaurant_id(testCaseData.getRestaurantId())
                .offer_type(testCaseData.getOfferType())
                .offer_value(testCaseData.getOfferValue())
                .customer_segment(segments)
                .build();
        ApiResponse result = apiClient.addOffer(offerRequest);
        assertEquals("success", result.getResponse_msg());

        int restaurantId = (testCaseName.contains("NotAppliedToOtherRestaurants")) ? 2 : testCaseData.getRestaurantId();

        ApplyOfferRequest applyOfferRequest = ApplyOfferRequest.builder()
                .cart_value(testCaseData.getCartValue())
                .restaurant_id(restaurantId)
                .user_id(testCaseData.getUserId())
                .build();
        ApplyOfferResponse actualResponse = apiClient.applyOffer(applyOfferRequest);
        if (Objects.isNull(actualResponse))
            assertEquals(testCaseData.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        else
            assertEquals(testCaseData.getExpectedCartValue(), actualResponse.getCart_value());

    }
}
