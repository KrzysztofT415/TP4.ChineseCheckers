package appClient;

import view.AppWindow;
import view.swing.SwingAppWindowPlaying;
import view.swing.SwingAppWindowWatching;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A client for a network multi-player Chinese Checkers game.
 * Window of application is made using Swing but is made to be easily prescribed using other GUI libraries.
 * After compiling user will be asked to enter ip of server to connect to.
 *
 * Communication between client and server is based on plain text, called as ChChP (Chinese Checkers Protocol)
 * The strings that are sent in ChChP are:
 *
 * ----------------
 * Client -> Server
 *
 * CLICK [x;y]
 * PASS
 * QUIT
 *
 * where:
 *  * in CLICK [x;y] are coordinates of clicked cell
 * ----------------
 * Server -> Client
 *
 * WELCOME [n] (1 <= n <= 6)
 * START [n;x1;y1;s1;x2;y2...]
 * SET [n;x1;y1;s1;x2;y2...]
 * CLEAR
 * MESSAGE [String]
 * LOST
 * WON [n] (1 <= n <= 6)
 * LEAVE
 * END
 *
 * where:
 * in SET and START [n;x1;y1;s1;x2;y2...]:
 *   <n> means number of changed cells
 *   x[i], y[i] are coordinates of changed cell
 *   s[i] is new state of this cell
 *   where 0 < [i] <= <n>
 * in WELCOME [n] is playerId of player that joined
 * in WON [n] is playerId of player that won game
 * in MESSAGE [String] is text to be displayed in message box on client
 * ----------------
 *
 * @author Martyna Dziamara, Krzysztof Talalaj
 *
 */
public class MainClient {

    private final ClientCommunicationService communicationService;
    private final Socket socket;
    private AppWindow appWindow;

    /**
     * Verifies arguments and then creates lobby for expected players.
     * @param socket server socket that client is connected to
     */
    public MainClient(Socket socket) {
        this.socket = socket;
        this.communicationService = new ClientCommunicationService(this, this.socket);
    }

    public void connectModule(CommunicationModule communicationModule) {
        this.communicationService.connectModule(communicationModule);
    }

    public void connectWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * Runs the client communication with server.
     */
    public void start() {
        try {
            communicationService.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { this.socket.close();
            } catch (IOException e) { e.printStackTrace(); }
            this.appWindow.close();
        }
    }

    /**
     * Runs the application. Verifies connection and creates new Client.
     */
    public static void main(String[] args) {

        int mode = JOptionPane.showOptionDialog(null, "Welcome to chinese Checkers!\nWhat do you want to do?", "Chinese Checkers", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Watch game","Play game"},"Ok");

        while(true) {
            String input = JOptionPane.showInputDialog("Ip address of server host (ip:port) :");
            if (input == null) break;

            try {
                String ip = input.split(":")[0];
                int port = Integer.parseInt(input.split(":")[1]);

                Socket socket = new Socket(InetAddress.getByName(ip), port);

                MainClient mainClient = new MainClient(socket);
                CommunicationModule communicationModule;
                if (mode == 0) {
                    mainClient.connectWindow(new SwingAppWindowWatching(mainClient));
                    communicationModule = new WatchingCommunicationModule(mainClient); }
                else {
                    mainClient.connectWindow(new SwingAppWindowPlaying(mainClient));
                    communicationModule = new GameCommunicationModule(mainClient); }
                mainClient.connectModule(communicationModule);

                System.out.println("Client connected to : "+input);
                mainClient.start();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Connection refused, try again");
            }
        }
    }

    public AppWindow getAppWindow() {
        return this.appWindow;
    }

    public ClientCommunicationService getCommunicationService() {
        return this.communicationService;
    }

}