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
        broadcastMessage.setResourceId("d7fd3629-57d9-44cd-ab06-05eb495e4e1a");
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
