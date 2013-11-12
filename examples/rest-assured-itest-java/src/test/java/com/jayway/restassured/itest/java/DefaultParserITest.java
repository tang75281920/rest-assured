/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jayway.restassured.itest.java;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseBuilder;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.itest.java.support.WithJetty;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.parsing.Parser.JSON;
import static org.hamcrest.Matchers.equalTo;

public class DefaultParserITest extends WithJetty {

    @Test
    public void usingStaticDefaultParserParsersAnyContentWhenResponseContentTypeIsDefined() throws Exception {
        RestAssured.defaultParser = JSON;
        try {
            expect().body("message", equalTo("It works")).when().get("/customMimeTypeJsonCompatible");
        } finally {
            RestAssured.reset();
        }
    }

    @Test
    public void usingDefaultParserParsersAnyContentWhenResponseContentTypeIsDefined() throws Exception {
        expect().defaultParser(JSON).and().body("message", equalTo("It works")).when().get("/customMimeTypeJsonCompatible");
    }

    @Test
    public void usingStaticDefaultParserWhenResponseContentTypeIsUnDefined() throws Exception {
        RestAssured.defaultParser = JSON;
        try {
            expect().body("message", equalTo("It works")).when().get("/noContentTypeJsonCompatible");
        } finally {
            RestAssured.reset();
        }
    }

    @Test
    public void usingDefaultParserWhenResponseContentTypeIsEmpty() throws Exception {
        expect().defaultParser(JSON).and().body("message", equalTo("It works")).when().get("/noContentTypeJsonCompatible");
    }

    @Test
    public void usingStaticallyConfiguredDefaultParserWhenResponseContentTypeIsEmpty() throws Exception {
        RestAssured.defaultParser = JSON;
        try {
            expect().body("message", equalTo("It works")).when().get("/noContentTypeJsonCompatible");
        } finally {
            RestAssured.reset();
        }
    }

    @Test
    public void usingDefaultParserWhenResponseContentTypeIsUnDefined() throws Exception {
        expect().defaultParser(JSON).and().body("message", equalTo("It works")).when().get("/noContentTypeJsonCompatible");
    }

    class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "{ \"message\" : \"It works\" }";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
