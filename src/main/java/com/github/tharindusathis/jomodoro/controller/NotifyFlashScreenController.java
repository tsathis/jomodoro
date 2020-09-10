package com.github.tharindusathis.jomodoro.controller;

import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class NotifyFlashScreenController extends Controller
{
    @FXML
    Rectangle notifyRect;
    FillTransition flashTransition;

    public void flash()
    {
        getStage().show();
        flashTransition.play();

    }

    @FXML
    void initialize()
    {
        flashTransition = new FillTransition();
        flashTransition.setAutoReverse( true );
        flashTransition.setDuration( Duration.millis( 250 ) );
        flashTransition.setFromValue( Color.rgb( 115, 214, 148, 0 ) );
        flashTransition.setToValue( Color.rgb( 115, 214, 148, 0.6 ) );
        flashTransition.setCycleCount( 4 );
        flashTransition.setShape( notifyRect );
        notifyRect.setHeight( 1080 );
        notifyRect.setWidth( 1920 );
        notifyRect.setMouseTransparent( true );
        notifyRect.setStyle( "-fx-fill: rgba(1,1,1,0)" );
        flashTransition.setOnFinished( e -> getStage().hide() );
    }
}
