package iit.cw.client;

import nameService.NameServiceClient;

import java.io.IOException;

public class MainClass {
    private static final String NAME_SERVICE_ADDRESS = "http://localhost:2379";
    private static String host;
    private static int port;

    public static void main(String[] args) throws InterruptedException {
        try {
            fetchServerDetails();
            String operation = args[0];

            if (args.length != 1) {
                System.out.println("Usage Quantity manager ServiceClient <host> <port> <s(et)|o(rder)|c(heck)");
                System.exit(1);
            }

            if ("s".equals(operation)) {
                SetBalanceServiceClient client = new SetBalanceServiceClient(host, port);
                client.initializeConnection();
                client.processUserRequests();
                client.closeConnection();
            } else if ("o".equals(operation)) {
                OrderItemServiceClient client = new OrderItemServiceClient(host, port);
                client.initializeConnection();
                client.processUserRequests();
                client.closeConnection();
            } else {
                CheckBalanceServiceClient client = new CheckBalanceServiceClient(host, port);
                client.initializeConnection();
                client.processUserRequests();
                client.closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fetchServerDetails() throws IOException, InterruptedException {
        NameServiceClient client = new NameServiceClient(NAME_SERVICE_ADDRESS);
        NameServiceClient.ServiceDetails serviceDetails = client.findService("InventoryService");
        host = serviceDetails.getIPAddress();
        port = serviceDetails.getPort();
    }
}
