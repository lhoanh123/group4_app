package com.example.group4_app;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MySQLConnector {
    private static final String URL = "https://schizoid-quarters.000webhostapp.com/"; // Update to correct URL
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public MySQLConnector() {}

    public void addUser(User user) {
        executorService.execute(() -> {
            RequestBody body = new FormBody.Builder()
                    .add("user_name", user.getName())
                    .add("user_email", user.getEmail())
                    .add("user_password", user.getPassword())
                    .build();
            Request request = new Request.Builder()
                    .url(URL + "add_user.php")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String result = response.body().string();
                System.out.println("Add User Response: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Future<Boolean> checkUserSignUp(String email) {
        Callable<Boolean> task = () -> {
            RequestBody body = new FormBody.Builder()
                    .add("user_email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(URL + "check_user_signup.php")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String result = response.body().string();
                return result.equals("true");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        };

        return executorService.submit(task);
    }

    public Future<Boolean> checkUserSignIn(String username, String password) {
        Callable<Boolean> task = () -> {
            RequestBody body = new FormBody.Builder()
                    .add("user_name", username)
                    .add("user_password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(URL + "check_user_signin.php")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String result = response.body().string();
                return result.equals("true");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        };

        return executorService.submit(task);
    }
}
