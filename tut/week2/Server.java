import java.io.*;
import java.net.*;

class Server {
    public static void main(String args[]) throws Exception {
        // args should be port
        ServerSocket ssock = new ServerSocket(Integer.parseInt(args[0]));
        // ^ Think: How does ServerSocket do the `socket`, `bind`, and `listen`
        // operations?

        while (true) {
            try {
                Socket csock = ssock.accept();
                System.out.println("Accepted connection: " + csock);
                BufferedReader reader = new BufferedReader(new InputStreamReader(csock.getInputStream()));
                PrintWriter writer = new PrintWriter(csock.getOutputStream(), true);

                String line = reader.readLine();
                System.out.println("Read data: " + line);
                writer.println(line.toUpperCase());
                // writer.flush();
                // writer.close();
                csock.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
