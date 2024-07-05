package org.example.wallpaperchangerui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class WallpaperChangerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wallpaper Changer");

        Button btn = new Button("Change Wallpaper");
        btn.setOnAction(e -> changeWallpaper(primaryStage));

        VBox vbox = new VBox(btn);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeWallpaper(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"));
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try {
                setWallpaper(filePath);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setWallpaper(String filePath) throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("Operating System: " + os);
        System.out.println("File Path: " + filePath);

        if (os.contains("win")) {
            // Windows
            String command = "reg add \"HKEY_CURRENT_USER\\Control Panel\\Desktop\" /v Wallpaper /t REG_SZ /d \"" + filePath + "\" /f";
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.inheritIO().start().waitFor();

            ProcessBuilder updateProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "RUNDLL32.EXE user32.dll,UpdatePerUserSystemParameters");
            updateProcessBuilder.inheritIO().start().waitFor();
        } else if (os.contains("mac")) {
            // macOS
            List<String> script = List.of("osascript", "-e", "tell application \"Finder\"", "-e", "set desktop picture to POSIX file \"" + filePath + "\"", "-e", "end tell");
            ProcessBuilder processBuilder = new ProcessBuilder(script);
            processBuilder.inheritIO().start().waitFor();
        } else if (os.contains("nix") || os.contains("nux")) {
            // Linux (Gnome)
            List<String> script = List.of("gsettings", "set", "org.gnome.desktop.background", "picture-uri", "file://" + filePath);
            ProcessBuilder processBuilder = new ProcessBuilder(script);
            processBuilder.inheritIO().start().waitFor();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
