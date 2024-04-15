package com.example.fx_shoes;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class HelloController implements Initializable {


    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, xsBtn, sBtn, lBtn, mBtn, xlBtn;

    @FXML
    private Label lbl1, lbl2, lbl3, lbl4, xsLbl, sLbl, mLbl, lLbl, xlLbl, enterLbl, nameLbl;
    @FXML
    private TextField text_field;


    @FXML
    private ImageView view1;

    ResultSet resultSet;
    ResultSet resultSet2;
    Button temp;
    boolean isClicked = false;
    boolean sizeChosen = false;
    boolean loggedIn = false;
    LoginBox loggingIn;
    String username;
    String id;

    CallableStatement myStmt = null;



    @FXML
    void first_item(ActionEvent event) {
        try {
            resultSet.first();
            resultSet2.first();
            isClicked = true;
           // btn1.setDisable(true);
            applyRotateTransition();
            applyScaleTransition();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        display();

    }

    @FXML
    void last_item(ActionEvent event) {
        if (isClicked) {
            try {

                resultSet.last();
                resultSet2.last();
                applyRotateTransition();
                applyScaleTransition();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            display();
        }
    }

    @FXML
    void next_item(ActionEvent event) {
        if (isClicked) {
            try {

                resultSet.next();
                resultSet2.next();
                applyRotateTransition();
                applyScaleTransition();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            display();
        }
    }

    @FXML
    void previous_item(ActionEvent event) {
        if (isClicked) {
            try {

                resultSet.previous();
                resultSet2.previous();
                applyRotateTransition();
                applyScaleTransition();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            display();
        }
    }

    @FXML
    void signUp(ActionEvent event) {
        LoginBox createAc = new LoginBox();
        createAc.createAccount();

    }

    @FXML
    void logIn(ActionEvent event) {

        loggingIn = new LoginBox();

        loggingIn.logDisplay("LOG IN", "Enter firstname and password");
        loggingIn.log.setOnAction(e -> {
            try {

                username = loggingIn.id.getText();
                String password = loggingIn.password.getText();


                myStmt = DbConnection.createConnection().prepareCall("{call user_auth(?,?,?)}");
                myStmt.setString(1, username);
                myStmt.setString(2, password);
                myStmt.registerOutParameter(3, Types.VARCHAR);
                myStmt.execute();

                id = myStmt.getString(3);
                if (id != null && id.equals(username)) {
                   valid();

                } else {
                  inValid();

                }
            } catch (Exception y) {
                y.printStackTrace();
            }DbConnection.closeDb();
        });


    }

    @FXML
    void addToBasket(ActionEvent event) {
        if (isClicked && sizeChosen && loggedIn) {
            LoginBox confirm = new LoginBox();
            confirm.secondDisplay();


            try {
                if (confirm.no.getText().equals("No")) {
                    enterLbl.setText("No item added!");
                    text_field.setText("");

                } else {
                    text_field.setText(resultSet.getString(5) + "$");
                    enterLbl.setText("Added to basket!");
                    String sql = "Insert into orders(size, time_, model, customer_id) values (?,?,?,?);";
                    Timestamp time_ = new Timestamp(System.currentTimeMillis());

                    String size = lbl4.getText();
                    String model = lbl2.getText();
                    int customer_id = getLoggedInCustomerId();

                    PreparedStatement prepared_InsertStatement = null;
                    try {
                        prepared_InsertStatement = DbConnection.createConnection().prepareStatement(sql);
                        prepared_InsertStatement.setString(1, size);
                        prepared_InsertStatement.setTimestamp(2, time_);
                        prepared_InsertStatement.setString(3, model);
                        prepared_InsertStatement.setInt(4, customer_id);
                        prepared_InsertStatement.executeUpdate();

                    } catch (SQLException e) {
                        System.out.println(customer_id);
                        e.printStackTrace();
                    }DbConnection.closeDb();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            nameLbl.setText("Log in or create an account!");
        }

    }


    @FXML
    void clicked(ActionEvent event) {

        temp = (Button) event.getSource();

        if (isClicked) {
            if (temp.getText().equals("XS") && xsLbl.getText() != null) {
                enterLbl.setText("1 pair selected!");
                lbl4.setText("XS");
                btn6.setVisible(true);
            } else if (temp.getText().equals("S") && sLbl.getText() != null) {
                enterLbl.setText("1 pair selected!");
                lbl4.setText("S");
                btn6.setVisible(true);
            } else if (temp.getText().equals("M") && mLbl.getText() != null) {
                enterLbl.setText("1 pair selected!");
                lbl4.setText("M");
                btn6.setVisible(true);
            } else if (temp.getText().equals("L") && lLbl.getText() != null) {
                enterLbl.setText("1 pair selected!");
                lbl4.setText("L");
                btn6.setVisible(true);
            } else if (temp.getText().equals("XL") && xlLbl.getText() != null) {
                enterLbl.setText("1 pair selected!");
                lbl4.setText("XL");
                btn6.setVisible(true);
            } else {
                enterLbl.setText("Choose your size!");
                lbl4.setText("");
                btn6.setVisible(false);

            }
        }
        sizeChosen = true;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Statement statement = DbConnection.createConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement statement2 = DbConnection.createConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT * FROM shoes;";
            String sql2 = "SELECT * FROM size;";

            resultSet = statement.executeQuery(sql);
            resultSet2 = statement2.executeQuery(sql2);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void display() {

        try {
            if (resultSet.isBeforeFirst() && resultSet2.isBeforeFirst()) {
                resultSet.first();
                resultSet2.first();
            } else if (resultSet.isAfterLast() && resultSet2.isAfterLast()) {
                resultSet.last();
                resultSet2.last();
            }

            lbl1.setText(resultSet.getString(1));
            lbl2.setText(resultSet.getString(2));
            lbl3.setText(resultSet.getString(5) + "$");
            xsLbl.setText(resultSet2.getString(2));
            sLbl.setText(resultSet2.getString(3));
            mLbl.setText(resultSet2.getString(4));
            lLbl.setText(resultSet2.getString(5));
            xlLbl.setText(resultSet2.getString(6));

            if (resultSet.getString(2).equals("dunk")) {
                view1.setImage(Pictures.img1);
            } else if (resultSet.getString(2).equals("terra")) {
                view1.setImage(Pictures.img3);
            } else if (resultSet.getString(2).equals("air force")) {
                view1.setImage(Pictures.img2);
            } else if (resultSet.getString(2).equals("grand court")) {
                view1.setImage(Pictures.img4);
            } else if (resultSet.getString(2).equals("air max")) {
                view1.setImage(Pictures.img5);
            } else if (resultSet.getString(2).equals("free")) {
                view1.setImage(Pictures.img6);
            } else if (resultSet.getString(2).equals("supernova")) {
                view1.setImage(Pictures.img7);
            } else if (resultSet.getString(2).equals("hoops")) {
                view1.setImage(Pictures.img8);
            } else if (resultSet.getString(2).equals("terrax")) {
                view1.setImage(Pictures.img9);
            } else if (resultSet.getString(2).equals("air")) {
                view1.setImage(Pictures.img10);
            } else {
                view1.setImage(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getLoggedInCustomerId() {
        try {
            String username = nameLbl.getText();

            String sql = "SELECT customer_id FROM customers WHERE first_name = ?";
            PreparedStatement statement = DbConnection.createConnection().prepareStatement(sql);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }DbConnection.closeDb();

        return 0;
    }
    void inValid(){
        String notValid = "Invalid login credentials";
        new Thread(() -> {
            double count = 0.0;
            for (int i = 0; i < 8; i++) {
                loggingIn.progBar.setProgress(count);

                count += 0.1;
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                System.out.println(notValid);
                loggingIn.label.setText(notValid);
                loggingIn.id.setText("");
                loggingIn.password.setText("");
                loggingIn.progBar.setProgress(0.0);
            });
        }).start();
    }
    void valid(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                double count = 0.0;
                for (int i = 0; i < 10; i++) {
                    loggingIn.progBar.setProgress(count);
                    count += 0.1;
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("Login successful. Welcome, " + id + "!");
                        nameLbl.setText("Welcome " + username.toUpperCase());
                        FadeTransition ft = new FadeTransition(Duration.seconds(4),
                                nameLbl);
                        ft.setInterpolator(Interpolator.EASE_OUT);
                        ft.setDelay(Duration.seconds(2));
                        ft.setFromValue(1);
                        ft.setToValue(0);
                        ft.play();
                        nameLbl.setText(username);
                        loggedIn = true;
                        loggingIn.close();
                        loggingIn.correct();

                    }
                });
            }
        }).start();

    }
    private void applyRotateTransition() {
        RotateTransition rotate = new RotateTransition();
        view1.setRotate(0);
        rotate.setNode(view1);
        rotate.setDuration(Duration.millis(4000));
        rotate.setCycleCount(1);//TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.setByAngle(360);
        rotate.setAutoReverse(true);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.play();


    }
    private void applyScaleTransition() {
        ScaleTransition scale = new ScaleTransition();
        view1.setRotate(0);
        view1.setScaleX(1.0);
        view1.setScaleY(1.0);
        scale.setNode(view1);
        scale.setDuration(Duration.millis(2000));
        scale.setCycleCount(2);//TranslateTransition.INDEFINITE);
        scale.setInterpolator(Interpolator.LINEAR);
        scale.setByX(1.5);
        scale.setByY(1.5);
        scale.setAutoReverse(true);
        scale.playFromStart();
    }



}//End