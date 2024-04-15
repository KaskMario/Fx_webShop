package com.example.fx_shoes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginBox {

    Button cancel;
    Button log;
    Button yes;
    Button no;

    TextField id;
    TextField password;

    Label idLbl;
    Label pwLbl;
    Label label;
    Stage window;
    ProgressBar progBar;



    public void logDisplay(String title,String message) {

        window = new Stage();
        idLbl = new Label("Firstname");
        pwLbl = new Label("Password");
        cancel = new Button("Cancel");
        log = new Button("Log in");
        id = new TextField();
        password = new TextField();
        progBar = new ProgressBar();
        progBar.setPrefHeight(20);
        progBar.setPrefWidth(100);
        progBar.setProgress(0.0);


        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        label = new Label(message);
        log.setOnAction(e -> {
            cancel.setText("Close");
            log.setVisible(false);

        });
        cancel.setOnAction(e -> window.close());
        VBox layout = new VBox(60);
        layout.getChildren().addAll(label,idLbl,id,pwLbl,password,log,cancel,progBar);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(65));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
    void createAccount(){
        window = new Stage();
        idLbl = new Label("Enter firstname");
        pwLbl =new Label("Choose a password (max 10 letters)");
        id = new TextField();
        password = new TextField();
        cancel = new Button("Cancel");
        log = new Button("Sign Up");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create an account");
        window.setMinWidth(350);


        label = new Label();
        label.setText("Enter firstname and choose a password");

            log.setOnAction(e -> {
                        if(!id.getText().isEmpty() && !password.getText().isEmpty()) {
                            cancel.setText("Close");
                            label.setText("Account created!");
                            log.setVisible(false);
                            idLbl.setText("");
                            pwLbl.setText("");
                            id.setVisible(false);
                            password.setVisible(false);

                            String sql = "INSERT INTO customers(first_name, pass_word) VALUES (?, ?);";
                            String first_name = id.getText();
                            String pass_word = password.getText();
                            try
                                (PreparedStatement prepared_InsertStatement = DbConnection.createConnection().prepareStatement(sql)) {
                                prepared_InsertStatement.setString(1, first_name);
                                prepared_InsertStatement.setString(2, pass_word);
                                prepared_InsertStatement.executeUpdate();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }DbConnection.closeDb();

                        }
            });



        cancel.setOnAction(e -> window.close());

        VBox layout = new VBox(60);
        layout.getChildren().addAll(label,idLbl,id,pwLbl,password,log,cancel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(65));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
    void secondDisplay() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("CONFORMATION");
        window.setMinWidth(250);
        label =  new Label("Add item to basket?");
        no = new Button("No");
        yes = new Button("Yes");
        yes.setOnAction(e -> {
            no.setText("Close");
            label.setText("Item added");
            yes.setVisible(false);

        });


        no.setOnAction(e -> window.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yes, no);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    void correct(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Button btn = new Button("Close");
        Label lbl = new Label("Logged in!");
        btn.setOnAction(e -> window.close());
        VBox box = new VBox(15);
        box.getChildren().addAll(lbl,btn);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        Scene scene = new Scene(box);
        window.setScene(scene);
        window.showAndWait();

    }void close(){
        window.close();
    }


}//END
