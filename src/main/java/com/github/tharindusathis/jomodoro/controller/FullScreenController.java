package com.github.tharindusathis.jomodoro.controller;

import com.github.tharindusathis.jomodoro.timer.Configs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class FullScreenController extends Controller
{
    @FXML
    private BorderPane breakView;
    @FXML
    private BorderPane timerView;
    @FXML
    private TextField timeTextBox;
    @FXML
    private Label lblTimer;
    @FXML
    private ProgressBar breakProgressBar;
    @FXML
    private TextField breakTextBox;
    @FXML
    private TextField tagTextBox;

    @FXML
    void btnCloseOnAction( ActionEvent event )
    {
        getControllerManager().ifPresent(
                controllerManager -> controllerManager.showView( ControllerManager.View.MAIN ) );
    }

    @FXML
    void btnSpinnerBreakDownOnAction( ActionEvent event )
    {
        changeDurationValue( breakTextBox, -1 );
    }

    @FXML
    void btnSpinnerBreakUpOnAction( ActionEvent event )
    {
        changeDurationValue( breakTextBox, 1 );
    }

    @FXML
    void btnSpinnerTimeDownOnAction( ActionEvent event )
    {
        changeDurationValue( timeTextBox, -1 );
    }

    @FXML
    void btnSpinnerTimeUpOnAction( ActionEvent event )
    {
        changeDurationValue( timeTextBox, 1 );
    }

    @FXML
    void btnStartOnAction( ActionEvent event )
    {
        startTimer();
    }

    private void changeDurationValue( TextField textField, int i )
    {
        String rawText = textField.getText();
        try
        {
            int value = Integer.parseInt( rawText ) + i;
            if( value < 1 || value > 999 )
            {
                return;
            }
            textField.setText( String.valueOf( value ) );
        }
        catch( NumberFormatException e )
        {
            e.printStackTrace();
        }
    }

    public Label getLblTimer()
    {
        return lblTimer;
    }

    @FXML
    void handleBtnFinishBreak( ActionEvent event )
    {
        setView( FullscreenStageViews.START );
        getControllerManager()
                .flatMap( controllerManager -> controllerManager.getController( MainController.class ) )
                .ifPresent( MainController::resetTimer );
    }

    @FXML
    void initialize()
    {
        timeTextBox.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( isInvalidDuration( newValue ) && !newValue.isEmpty() )
            {
                timeTextBox.setText( oldValue );
            }
        } );
        breakTextBox.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( isInvalidDuration( newValue ) && !newValue.isEmpty() )
            {
                breakTextBox.setText( oldValue );
            }
        } );
        tagTextBox.setOnKeyReleased( event ->
        {
            if( event.getCode() == KeyCode.ENTER )
            {
                startTimer();
            }
        } );
        timeTextBox.setText( String.format("%d",Configs.getTimerDuration() / 60));
        breakTextBox.setText( String.format("%d",Configs.getBreakTimerDuration() / 60));
    }

    private boolean isInvalidDuration( String input )
    {
        try
        {
            int value = Integer.parseInt( input );
            return value < 1 || value > 999;
        }
        catch( NumberFormatException e )
        {
            return true;
        }
    }

    public void setView( FullscreenStageViews view )
    {
        if( view == FullscreenStageViews.START )
        {
            timerView.setVisible( true );
            breakView.setVisible( false );
        }
        else if( view == FullscreenStageViews.BREAK )
        {
            timerView.setVisible( false );
            breakView.setVisible( true );
        }
    }

    private void startTimer()
    {
        getControllerManager()
                .flatMap( controllerManager -> controllerManager.getController( MainController.class ) )
                .ifPresent( mainController ->
                {
                    Configs.setTimerDuration( Integer.parseInt( timeTextBox.getText() ) * 60 );
                    Configs.setBreakTimerDuration( Integer.parseInt( breakTextBox.getText() ) * 60 );
                    mainController.resetTimer();
                    mainController.startTimer();
                    mainController.setTagLabel( tagTextBox.getText() );
                    mainController.setView( MainController.MainStageViews.TIMER );
                } );
        getControllerManager().ifPresent(
                controllerManager -> controllerManager.showView( ControllerManager.View.MAIN ) );
    }

    public void updateBreakProgressBar( double value )
    {
        breakProgressBar.setProgress( value );
    }

    enum FullscreenStageViews
    {
        START, BREAK
    }
}
