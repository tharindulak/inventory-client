package iit.cw.client;

import iit.cw.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class OrderItemServiceClient {
    private ManagedChannel channel = null;
    OrderItemServiceGrpc.OrderItemServiceBlockingStub clientStub = null;
    String host = null;
    int port = -1;

    public OrderItemServiceClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initializeConnection () {
        System.out.println("Initializing Connecting to server at " + host + ":" +
                port);
        channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        clientStub = OrderItemServiceGrpc.newBlockingStub(channel);
    }
    public void closeConnection() {
        channel.shutdown();
    }

    public void processUserRequests() throws InterruptedException {
        while (true) {
            Scanner userInput = new Scanner(System.in);
            System.out.println("\nEnter Item ID, quantity to be ordered :");
            String[] input = userInput.nextLine().trim().split(",");
            String itemId = input[0];
            double quantity = Double.parseDouble(input[1]);
            System.out.println("Requesting server to set the item quantity to " + quantity + " for item " + itemId.toString());
            OrderItemRequest request = OrderItemRequest
                    .newBuilder()
                    .setItemId(itemId)
                    .setQuantity(quantity)
                    .setIsSentByPrimary(false)
                    .build();
            OrderItemResponse response = clientStub.orderItem(request);
            System.out.printf("Transaction Status is" + (response.getStatus() ? " Successful" : " Failed"));
            Thread.sleep(1000);
        }
    }
}
