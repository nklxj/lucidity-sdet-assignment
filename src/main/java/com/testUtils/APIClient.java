package com.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.*;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.restassured.RestAssured.given;

public class APIClient {

    private static final Logger log = LoggerFactory.getLogger(APIClient.class);

    public ApiResponse addOffer(OfferRequest offerRequest) {
        return given()
                .baseUri("http://localhost:9001")
                .body(offerRequest)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/offer")

                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response().as(ApiResponse.class);
    }

    public ApplyOfferResponse applyOffer(ApplyOfferRequest applyOfferRequest) {
        Response response = given()
                .baseUri("http://localhost:9001")
                .body(applyOfferRequest)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/v1/cart/apply_offer");

        if (response.statusCode() == HttpStatus.OK.value()) {
            return response.as(ApplyOfferResponse.class);
        } else {
            return null;
        }
    }
}
