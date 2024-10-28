package Networking;


import model.CourseInfo;

import java.io.*;
import java.net.Socket;
import java.util.List;

import control.ANSIColor;

public class ApplicationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Request for latest courses
            output.writeObject("GET_LATEST_COURSES");
            output.flush();

            // Read and print the response from the server
            List<CourseInfo> latestCourses = (List<CourseInfo>) input.readObject();
            for (CourseInfo course : latestCourses) {
                System.out.println(ANSIColor.CYAN+course.getTitle() +ANSIColor.WHITE+ " - " + course.getDescription());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
