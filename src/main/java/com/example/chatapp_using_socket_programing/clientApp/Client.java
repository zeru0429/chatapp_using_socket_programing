package com.example.clientApp;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private  BufferedImage bufferedImageSend;
    private  BufferedImage bufferedImageReceived;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;



    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creating client");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessageToServer(String messageToServer) {

        try{
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to client");
            closeEverything(socket, bufferedReader,bufferedWriter);
        }

    }

    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try{
                        String messageFromServer = bufferedReader.readLine();
                        HelloController.addLabel(messageFromServer, vBox);
                    } catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message from client");
                        closeEverything(socket, bufferedReader,bufferedWriter);
                        break;
                    }
                }}}).start();
    }
 public void receiveImageFromServer(VBox vbox_image){
     new Thread(new Runnable() {
         @Override
         public void run() {
             while(socket.isConnected()) {
                 try{

                     BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                     byte[] buffer = new byte[ 4096 ];
                     int bytesRead;
                     while ( (bytesRead = bufferedInputStream.read(buffer)) != -1 ) {
                        bufferedOutputStream.write( buffer, 0, bytesRead );
                     }
                     bufferedInputStream.close();
                     bufferedOutputStream.flush();
                     bufferedOutputStream.close();

                        ImageIO.write(bufferedImage, "jpg", new File("MyFile.jpg"));
                     File file = new File("C:/JavaAssignment/MyFile.jpg");

                     ImageView imageView = new ImageView(new javafx.scene.image.Image("MyFile.jpg"));
                     HelloController.addLabel(imageView, vbox_image);
                 } catch (IOException e){
                     e.printStackTrace();
                     System.out.println("Error receiving message from client");
                     closeEverything(socket, bufferedReader,bufferedWriter);
                     break;
                 }
             }}}).start();
 }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){       // this method is used to close buffer and socket once in all rather than reaptedly close
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
