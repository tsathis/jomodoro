package com.github.tharindusathis.jomodoro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class FullScreenController extends Controller
{
    @FXML
    private AnchorPane paneParent;
    @FXML
    private BorderPane txtTimeContainer;
    @FXML
    private BorderPane breakViewPane;
    @FXML
    private BorderPane startViewPane;
    @FXML
    private Button btnSpinnerTimeDown;
    @FXML
    private Button btnSpinnerTimeUp;
    @FXML
    private TextField txtTime;
    @FXML
    private Label lblTimer;
    @FXML
    private ProgressBar breakProgressBar;
    @FXML
    private BorderPane txtBreakContainer;
    @FXML
    private Button btnSpinnerBreakDown;
    @FXML
    private Button btnSpinnerBreakUp;
    @FXML
    private Button btnFinishBreak;
    @FXML
    private TextField txtBreak;
    @FXML
    private BorderPane txtLabelContainer;
    @FXML
    private TextField txtLabel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnStart;

    @FXML
    void btnCloseOnAction( ActionEvent event )
    {
        getControllerManager().ifPresent( controllerManager -> controllerManager.showView( ControllerManager.View.MAIN ) );
    }

    @FXML
    void btnSpinnerBreakDownOnAction( ActionEvent event )
    {
        System.out.println( event );
        changeDurationValue( txtBreak, -1 );
    }

    @FXML
    void btnSpinnerBreakUpOnAction( ActionEvent event )
    {
        changeDurationValue( txtBreak, 1 );
    }

    @FXML
    void btnSpinnerTimeDownOnAction( ActionEvent event )
    {
        changeDurationValue( txtTime, -1 );
    }

    @FXML
    void btnSpinnerTimeUpOnAction( ActionEvent event )
    {
        changeDurationValue( txtTime, 1 );
    }

    @FXML
    void btnStartOnAction( ActionEvent event )
    {
        startTimer();
    }

    private void startTimer()
    {
        getControllerManager()
                .flatMap( controllerManager -> controllerManager.getController( MainController.class ) )
                .ifPresent( mainController ->
                {
                    mainController.setDefaultTimerDuration( Integer.parseInt( txtTime.getText() ) * 60 );
                    mainController.setBreakTimerDuration( Integer.parseInt( txtBreak.getText() ) * 60 );
                    mainController.resetTimer();
                    mainController.startTimer();
                    mainController.setTagLabel( txtLabel.getText() );
                    mainController.setView( MainController.MainControllerViews.MAIN );
                } );
        getControllerManager().ifPresent( controllerManager -> controllerManager.showView( ControllerManager.View.MAIN ));
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
        setView( FullscreenControllerView.START );
        // TODO:
        // ( ( MainController ) controllerManager.getController( ControllerManager.View.MAIN ) ).setCurrentState( MainController.State.BREAK_STOP );
        // ( ( MainController ) controllerManager.getController( ControllerManager.View.MAIN ) ).resetTimer();
    }

    @FXML
    void initialize()
    {
        txtTime.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( isInvalidDuration( newValue ) && !newValue.isEmpty() )
            {
                txtTime.setText( oldValue );
            }
        } );
        txtBreak.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( isInvalidDuration( newValue ) && !newValue.isEmpty() )
            {
                txtBreak.setText( oldValue );
            }
        } );
        txtLabel.setOnKeyReleased( event -> {
            if(event.getCode() == KeyCode.ENTER){
                startTimer();
            }
        } );
        txtTime.setText( "25" );
        txtBreak.setText( "5" );
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

    public void setView( FullscreenControllerView view )
    {
        if( view == FullscreenControllerView.START )
        {
            startViewPane.setVisible( true );
            breakViewPane.setVisible( false );
        }
        else if( view == FullscreenControllerView.BREAK )
        {
            startViewPane.setVisible( false );
            breakViewPane.setVisible( true );
        }
    }

    @FXML
    void txtTimeInputMethodTextChanged( InputMethodEvent event )
    {
        System.out.println( event );
    }

    public void updateBreakProgressBar( double value )
    {
        breakProgressBar.setProgress( value );
    }

    enum FullscreenControllerView
    {
        START, BREAK
    }
}
