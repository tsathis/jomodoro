package com.github.tharindusathis.jomodoro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FullScreenController extends Controller
{
    @FXML
    private AnchorPane paneParent;
    @FXML
    private BorderPane txtTimeContainer;
    @FXML
    private Button btnSpinnerTimeDown;
    @FXML
    private Button btnSpinnerTimeUp;
    @FXML
    private TextField txtTime;
    @FXML
    private BorderPane txtBreakContainer;
    @FXML
    private Button btnSpinnerBreakDown;
    @FXML
    private Button btnSpinnerBreakUp;
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
        //todo
        getStage().close();
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
        final MainController mainController = (MainController) controllerManager.getController( ControllerManager.View.MAIN );
        mainController.setDefaultTimerDuration( Integer.parseInt( txtTime.getText() ) * 60 );
        mainController.setBreakTimerDuration( Integer.parseInt( txtBreak.getText() ) * 60 );
        mainController.resetTimer();
        mainController.startTimer();
        mainController.setTagLabel( txtLabel.getText() );

        //todo
        getStage().close();
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

    @FXML
    void initialize()
    {
        txtTime.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( !validateDuration( newValue ) && !newValue.isEmpty() )
            {
                txtTime.setText( oldValue );
            }
        } );
        txtBreak.textProperty().addListener( ( observable, oldValue, newValue ) ->
        {
            if( !validateDuration( newValue ) && !newValue.isEmpty() )
            {
                txtBreak.setText( oldValue );
            }
        } );
        txtTime.setText( "25" );
        txtBreak.setText( "5" );
    }

    @FXML
    void txtTimeInputMethodTextChanged( InputMethodEvent event )
    {
        System.out.println( event );
    }

    private boolean validateDuration( String input )
    {
        try
        {
            int value = Integer.parseInt( input );
            return value >= 1 && value <= 999;
        }
        catch( NumberFormatException e )
        {
            return false;
        }
    }
}
