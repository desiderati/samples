/*
 * Copyright (c) 2025 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package br.tech.desiderati.sample.atmosphere;// Import classes:

import io.openapi.client.ApiClient;
import io.openapi.client.ApiException;
import io.openapi.client.Configuration;
import io.openapi.client.api.BroadcastControllerApi;
import io.openapi.client.api.model.BroadcastMessageObject;

public class SampleAtmosphereClient {

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:9090/sample-atmosphere");

        BroadcastControllerApi apiInstance = new BroadcastControllerApi(defaultClient);
        BroadcastMessageObject broadcastMessage = new BroadcastMessageObject();

        // Replace it with a valid client ID!
        broadcastMessage.setResourceId("28179642-c9d7-4c08-903f-905c3c49c976");
        broadcastMessage.setPayload("Hello World!");

        try {
            apiInstance.broadcast(broadcastMessage);
        } catch (ApiException e) {
            System.out.println("Exception when calling BroadcastControllerApi#broadcast");
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
            System.out.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
