module org.example.wallpaperchangerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.wallpaperchangerui to javafx.fxml;
    exports org.example.wallpaperchangerui;
}