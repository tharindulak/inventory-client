package iit.cw.client;

import iit.cw.CheckQuantityRequest;
import iit.cw.CheckQuantityResponse;
import iit.cw.CheckQuantityServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class CheckBalanceServiceClient {
    private ManagedChannel channel = null;
    CheckQuantityServiceGrpc.CheckQuantityServiceBlockingStub clientStub = null;
    String host = null;
    int port = -1;

    public CheckBalanceServiceClient (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initializeConnection () {
        System.out.println("Initializing Connecting to server at " + host + ":" +
                port);
        channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        clientStub = CheckQuantityServiceGrpc.newBlockingStub(channel);
    }
    public void closeConnection() {
        channel.shutdown();
    }

    public void processUserRequests() throws InterruptedException {
        while (true) {
            Scanner userInput = new Scanner(System.in);
            System.out.println("\nEnter Item ID to check the quantity :");
            String itemId = userInput.nextLine().trim();
            System.out.println("Requesting server to check the quantity for " + itemId.toString());
            CheckQuantityRequest request = CheckQuantityRequest
                    .newBuilder()
                    .setItemId(itemId)
                    .build();
            CheckQuantityResponse response = clientStub.checkQuantity(request);
            System.out.printf("The quantity for the item " + itemId.toString() +  " is " + response.getQuantity());
            Thread.sleep(1000);
        }
    }
}
