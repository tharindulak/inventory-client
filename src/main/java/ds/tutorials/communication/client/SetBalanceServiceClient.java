package ds.tutorials.communication.client;

import ds.tutorial.communication.grpc.generated.SetQuantityRequest;
import ds.tutorial.communication.grpc.generated.SetQuantityResponse;
import ds.tutorial.communication.grpc.generated.SetQuantityServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class SetBalanceServiceClient {
    private ManagedChannel channel = null;
    SetQuantityServiceGrpc.SetQuantityServiceBlockingStub clientStub = null;
    String host = null;
    int port = -1;

    public SetBalanceServiceClient (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initializeConnection () {
        System.out.println("Initializing Connecting to server at " + host + ":" +
                port);
        channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        clientStub = SetQuantityServiceGrpc.newBlockingStub(channel);
    }
    public void closeConnection() {
        channel.shutdown();
    }

    public void processUserRequests() throws InterruptedException {
        while (true) {
            Scanner userInput = new Scanner(System.in);
            System.out.println("\nEnter Item ID, quantity to set the quantity :");
            String[] input = userInput.nextLine().trim().split(",");
            String itemId = input[0];
            double quantity = Double.parseDouble(input[1]);
            System.out.println("Requesting server to set the item quantity to " + quantity + " for " + itemId.toString());
            SetQuantityRequest request = SetQuantityRequest
                    .newBuilder()
                    .setItemId(itemId)
                    .setQuantity(quantity)
                    .setIsSentByPrimary(false)
                    .build();
            SetQuantityResponse response = clientStub.setQuantity(request);
            System.out.printf("Transaction Status " + (response.getStatus() ? " Successful" : " Failed"));
            Thread.sleep(1000);
        }
    }
}
