package com.github.tharindusathis.jomodoro.controller;

import com.github.tharindusathis.jomodoro.service.CountdownTask;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Timer;

/**
 * Main controller class
 *
 * @author tharindusathis
 */
public class MainController extends Controller
{
    boolean running = false;
    Timer timer;
    int defaultTimerDuration = 25 * 60;
    int breakTimerDuration = 5 * 60;
    int remainingSeconds = defaultTimerDuration;
    double xOffset;
    double yOffset;
    boolean stageDragging = false;
    Stage fullScreenStage;
    Stage mainStage;
    @FXML
    private Button tagBtnMainView;
    @FXML
    private Button tagBtnCtrlView;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnRestart;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnQuit;
    @FXML
    private Button btnBreak;
    @FXML
    private Button btnStart;
    @FXML
    private Label lblTimer;
    @FXML
    private Label lblTimerSmall;
    @FXML
    private BorderPane mainView;
    @FXML
    private StackPane ctrlView;
    @FXML
    private Button btnCtrlMainView;
    @FXML
    private Button btnCtrlCtrlView;
    @FXML
    private AnchorPane outerPath;
    @FXML
    private StackPane paneParent;
    @FXML
    private GridPane gridPaneCtrlBtnArea;

    void addTime( int seconds )
    {
        boolean state = running;
        System.out.println( "Adding" + seconds / 60 + ":" + seconds % 60 );
        pauseTimer();
        remainingSecondsSetter( remainingSeconds + seconds );
        if( state )
            startTimer();

    }

    void breakTimer()
    {
        pauseTimer();
        remainingSecondsSetter( breakTimerDuration );
    }

    int getMins()
    {
        return remainingSeconds / 60;
    }

    int getSecs()
    {
        return remainingSeconds % 60;
    }

    @FXML
    void handleBtnAdd( ActionEvent event )
    {
        addTime( 60 );
    }

    @FXML
    void handleBtnBreak( ActionEvent event )
    {
        breakTimer();
    }

    @FXML
    void handleBtnCtrlCtrlView( ActionEvent event )
    {
        setViewAsMainView();
    }

    @FXML
    void handleBtnCtrlMainView( ActionEvent event )
    {
        setViewAsCtrlView();
    }

    @FXML
    void handleBtnQuit( ActionEvent event )
    {
        Platform.exit();
        System.exit( 0 );
    }

    @FXML
    void handleBtnReset( ActionEvent event )
    {
        resetTimer();
    }

    @FXML
    void handleBtnSettings( ActionEvent event )
    {
        //todo:
        showFullScreenView();
    }

    @FXML
    void handleBtnStart( ActionEvent event )
    {
        if( running ) pauseTimer();
        else startTimer();
    }

    @FXML
    void handleMouseEnteredBtnCtrlCtrlView( MouseEvent event )
    {

//        if( fullScreenStage != null )
//        {
//            fullScreenStage.show();
//        }
    }

    @FXML
    void handleMouseEnteredBtnCtrlMainView( MouseEvent event )
    {
        if( mainView.isVisible() )
        {
            ctrlView.setVisible( true );
            mainView.setVisible( false );
            if( fullScreenStage != null )
            {
                fullScreenStage.hide();
            }
            if( mainStage != null )
            {
                mainStage.show();
            }
        }
    }

    @FXML
    void handleMouseEnteredMainView( MouseEvent event )
    {
        System.out.println( "Entered To Main" );
//        setInvisible();
        System.out.println( paneParent.getBoundsInParent().toString() );
//        event.get
    }

    @FXML
    void handleMouseExitedBtnCtrlCtrlView( MouseEvent event )
    {

    }

    @FXML
    void handleMouseExitedMainView( MouseEvent event )
    {
        System.out.println( "Exitted From Main" );
//        parentPane.setOpacity( 1 );

    }

    private void handleOnFullScreenViewHiding()
    {
        paneParent.setVisible( true );
    }

    private void handleOnFullScreenViewShowing()
    {
        paneParent.setVisible( false );
    }

    @FXML
    void handleOnMouseDraggedBtnCtrlCtrlView( MouseEvent event )
    {
//        stageDraggingOnDragged( event );
//        stageDragging = true;
        Stage mainStage = ( Stage ) paneParent.getScene().getWindow();
////        stage.setX( event.getScreenX() + xOffset );
////        stage.setY( event.getScreenY() + yOffset );
//        stage.setWidth( stage.getWidth() + xOffset );
        double newX = event.getScreenX() - mainStage.getX();// + 13;
        double newY = event.getScreenY() - mainStage.getY();// + 10;
//        if (newX % 5 == 0 || newY % 5 == 0) {
        if( newX < 720 && newX > 100 )
            mainStage.setWidth( newX );
//            } else {
//                stage.setWidth(550);
//            }
//
//            if (newY > 200) {
//                stage.setHeight(newY);
//            } else {
//                stage.setHeight(200);
//            }
//        }
    }

    @FXML
    void handleOnMouseDraggedBtnCtrlMainView( MouseEvent event )
    {
    }

    @FXML
    void handleOnMouseDraggedCtrlBtnArea( MouseEvent event )
    {
        stageDraggingOnDragged( event );
    }

    @FXML
    void handleOnMousePressedBtnCtrlCtrlView( MouseEvent event )
    {
        stageDraggingOnPressed( event );
    }

    @FXML
    void handleOnMousePressedBtnCtrlMainView( MouseEvent event )
    {

    }

    @FXML
    void handleOnMousePressedCtrlBtnArea( MouseEvent event )
    {
        stageDraggingOnPressed( event );
    }

    @FXML
    void handleOnMouseReleasedBtnCtrlCtrlView( MouseEvent event )
    {
        stageDraggingOnReleased();
    }

    @FXML
    void handleOnMouseReleasedCtrlBtnArea( MouseEvent event )
    {
        stageDraggingOnReleased();
    }

    private void initFullScreenView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource( "/com/github/tharindusathis/jomodoro/view/full-screen-view.fxml" ) );

        Region contentRootRegion = loader.load();

        double origW = 1920;
        double origH = 1080;

        contentRootRegion.setPrefWidth( origW );
        contentRootRegion.setPrefHeight( origH );

        Group group = new Group( contentRootRegion );
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add( group );

        rootPane.setStyle( "-fx-background-color: #00000000" );

        Scene scene = new Scene( rootPane, origW, origH );

        DoubleBinding width = scene.widthProperty().divide( origW );
        group.scaleXProperty().bind( width );
        group.scaleYProperty().bind( width );
        fullScreenStage = new Stage();
        fullScreenStage.initStyle( StageStyle.TRANSPARENT );
        scene.setFill( Color.TRANSPARENT );
        fullScreenStage.setScene( scene );
        fullScreenStage.setAlwaysOnTop( true );
        fullScreenStage.setWidth( fullScreenStage.getWidth() * 0.5 );
        fullScreenStage.setOnHiding( event ->
        {
            handleOnFullScreenViewHiding();
        } );
        fullScreenStage.setOnShowing( event ->
        {
            handleOnFullScreenViewShowing();
        } );

        FullScreenController fullScreenController = loader.getController();
//        fullScreenController.setMainController( this );

        fullScreenStage.show();
//        fullScreenStage.setMaximized( true );
//        group.scaleXProperty().unbind();
//        group.scaleYProperty().unbind();
//        fullScreenStage.setWidth( 1920 );
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        running = false;
        resetTimer();

        setViewAsMainView();

        final InvalidationListener ctrlViewVisualizer = event ->
        {
            if( stageDragging ) return;
            if( btnCtrlCtrlView.isHover() || gridPaneCtrlBtnArea.isHover() )
            {
                ctrlView.setVisible( true );
                mainView.setVisible( false );
            }
            else
            {
                ctrlView.setVisible( false );
                mainView.setVisible( true );
            }
        };
        btnCtrlCtrlView.hoverProperty().addListener( ctrlViewVisualizer );
        gridPaneCtrlBtnArea.hoverProperty().addListener( ctrlViewVisualizer );

        //init variables

    }

    void pauseTimer()
    {
        running = false;
        if( timer != null )
        {
            timer.cancel();
            timer.purge();
        }
        btnStart.getStyleClass().remove( "btn-pause" );
        btnStart.getStyleClass().add( "btn-start" );
    }

    void remainingSecondsSetter( int secs )
    {
        this.remainingSeconds = secs;
        String formattedTime;
        if( String.valueOf( getMins() ).length() <= 2 )
        {
            formattedTime = String.format( "%02d:%02d", getMins(), getSecs() );
        }
        else if( String.valueOf( getMins() ).length() <= 3 )
        {
            formattedTime = String.format( "%02d", getMins() );
        }
        else
        {
            formattedTime = "999+";
        }
        System.out.println( "Set time to: " + formattedTime );
        lblTimer.setText( formattedTime );
        lblTimerSmall.setText( formattedTime );
    }

    public void resetTimer()
    {
        pauseTimer();
        remainingSecondsSetter( defaultTimerDuration );
    }

    public void setBreakTimerDuration( int breakTimerDuration )
    {
        this.breakTimerDuration = breakTimerDuration;
    }

    public void setDefaultTimerDuration( int defaultTimerDuration )
    {
        this.defaultTimerDuration = defaultTimerDuration;
    }

    private void setInvisible()
    {
        paneParent.setOpacity( 0.01 );
        ctrlView.setVisible( false );
    }

    public void setTagLabel( String text )
    {
        if( text.isBlank() )
        {
            text = "Pomodoro";
        }
        else
        {
            text = text.trim();
        }
        tagBtnCtrlView.setText( text );
        tagBtnMainView.setText( text );
    }

    private void setViewAsCtrlView()
    {
        mainView.setVisible( false );
        ctrlView.setVisible( true );
    }

    private void setViewAsMainView()
    {
        mainView.setVisible( true );
        ctrlView.setVisible( false );
    }

    private void showFullScreenView()
    {
        if( fullScreenStage == null )
        {
            try
            {
                initFullScreenView();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
        fullScreenStage.show();
    }

    void stageDraggingOnDragged( MouseEvent event )
    {
        stageDragging = true;
        if( mainStage == null )
        {
            mainStage = ( Stage ) paneParent.getScene().getWindow();
        }
        mainStage.setX( event.getScreenX() + xOffset );
        mainStage.setY( event.getScreenY() + yOffset );
    }

    void stageDraggingOnPressed( MouseEvent event )
    {
        stageDragging = true;
        if( mainStage == null )
        {
            mainStage = ( Stage ) paneParent.getScene().getWindow();
        }
        xOffset = mainStage.getX() - event.getScreenX();
        yOffset = mainStage.getY() - event.getScreenY();
    }

    void stageDraggingOnReleased()
    {
        if( !stageDragging )
        {
            return;
        }
        else if( btnCtrlCtrlView.isHover() || gridPaneCtrlBtnArea.isHover() )
        {
            ctrlView.setVisible( true );
            mainView.setVisible( false );
        }
        else
        {
            ctrlView.setVisible( false );
            mainView.setVisible( true );
        }
        stageDragging = false;
    }

    public void startTimer()
    {
        running = true;
        timer = new Timer();
        timer.schedule( new CountdownTask( remainingSeconds, this::remainingSecondsSetter ), 0, 1000 );
        btnStart.getStyleClass().remove( "btn-start" );
        btnStart.getStyleClass().add( "btn-pause" );
    }

}
