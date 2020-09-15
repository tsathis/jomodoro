package com.github.tharindusathis.jomodoro.controller;

import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class NotifyFlashScreenController extends Controller
{
    @FXML
    Rectangle notifyRect;
    FillTransition flashTransition;
    Timer timer = new Timer();

    public void flash()
    {
        getStage().show();
        flashTransition.play();
    }

    public void flashRepeatedly( int seconds )
    {
        flashReset();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(() -> flash());
            }
        }, 0, seconds * 1000L );
    }

    public void flashReset()
    {
        if( timer != null )
        {
            timer.purge();
            timer.cancel();
        }
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

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        notifyRect.setHeight( screenBounds.getHeight() );
        notifyRect.setWidth( screenBounds.getWidth() );
        notifyRect.setMouseTransparent( true );
        notifyRect.setStyle( "-fx-fill: rgba(1,1,1,0)" );
        notifyRect.setMouseTransparent( true );
        flashTransition.setOnFinished( e -> getStage().hide() );
    }
}
