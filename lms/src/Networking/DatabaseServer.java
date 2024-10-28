package Networking;

import model.CourseInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import control.CourseOps;

public class DatabaseServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle client requests
    private static class ClientHandler extends Thread {
    	CourseOps CourseOperations;
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String request = (String) input.readObject();

                switch (request) {
                    case "GET_LATEST_COURSES":
                        List<CourseInfo> latestCourses = CourseOperations.getLatestCourses();
                        output.writeObject(latestCourses);
                        break;

                    case "GET_COURSES_BY_CATEGORY":
                        String category = (String) input.readObject();
                        List<CourseInfo> coursesByCategory = CourseOperations.getCoursesByCategoryName(category);
                        output.writeObject(coursesByCategory);
                        break;

                    case "GET_ALL_COURSES":
                        List<CourseInfo> allCourses = CourseOperations.getCourses();
                        output.writeObject(allCourses);
                        break;

                    case "GET_INSTRUCTOR_COURSES":
                        List<CourseInfo> instructorCourses = CourseOperations.getCoursesInstructor();
                        output.writeObject(instructorCourses);
                        break;

                    default:
                        output.writeObject("INVALID_REQUEST");
                        break;
                }

                output.flush();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
