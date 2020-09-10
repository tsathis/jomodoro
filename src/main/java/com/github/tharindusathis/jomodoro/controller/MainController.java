package com.github.tharindusathis.jomodoro.controller;

import com.github.tharindusathis.jomodoro.service.CountdownTask;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Timer;

/**
 * Main controller class
 *
 * @author tharindusathis
 */
public class MainController extends Controller
{
    State currentState = State.INIT;
    Timer timer;
    int defaultTimerDuration = 25 * 60;
    int breakTimerDuration = 5 * 60;
    int remainingSeconds = defaultTimerDuration;
    double xOffset;
    double yOffset;
    boolean stageDragging = false;
    Bounds mainWindowBounds;
    FullScreenController fullScreenController;
    @FXML
    private Pane mainWindowInvisibleBorder;
    @FXML
    private StackPane mainWindow;
    @FXML
    private StackPane timerStopView;
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
    private Button btnHide;
    @FXML
    private Button btnTimerPlay;
    @FXML
    private Button btnTimerPlayBreak;
    @FXML
    private AnchorPane outerPath;
    @FXML
    private StackPane paneParent;
    @FXML
    private GridPane gridPaneCtrlBtnArea;

    void addMinute()
    {
        State previousState = currentState;
        pauseTimer();
        remainingSecondsSetter( remainingSeconds + 60 );
        if( previousState.isRunning() )
        {
            startTimer();
        }
    }

    private FullScreenController getFullScreenController()
    {
        if( fullScreenController == null )
        {
            if( controllerManager == null )
            {
                return null;
            }
            else
            {
                fullScreenController = ( FullScreenController ) controllerManager.getController(
                        ControllerManager.View.FULLSCREEN );
                return fullScreenController;
            }
        }
        return fullScreenController;
    }

    int getMinutes()
    {
        return remainingSeconds / 60;
    }

    int getSeconds()
    {
        return remainingSeconds % 60;
    }

    @FXML
    void handleBtnAdd( ActionEvent event )
    {
        addMinute();
    }

    @FXML
    void handleBtnBreak( ActionEvent event )
    {
        setTimerToBreakTime();
        getFullScreenController().setView( FullScreenController.FullscreenControllerView.START );
    }

    @FXML
    void handleBtnCtrlCtrlView( ActionEvent event )
    {
        setView( MainControllerViews.MAIN );
    }

    @FXML
    void handleBtnCtrlMainView( ActionEvent event )
    {
        setView( MainControllerViews.CTRL );
    }

    @FXML
    void handleBtnHide( ActionEvent event )
    {
        mainWindow.setVisible( false );
        mainWindowInvisibleBorder.setStyle( "-fx-border-color: rgba(254, 254, 254, 0.01)" );
        mainWindowBounds = mainWindow.getBoundsInParent();
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

        getFullScreenController().setView( FullScreenController.FullscreenControllerView.START );
        controllerManager.showView( ControllerManager.View.FULLSCREEN );
    }

    @FXML
    void handleBtnStart( ActionEvent event )
    {
        if( currentState == State.INIT )
        {
            startTimer();
        }
        else if( currentState.isRunning() )
        {
            pauseTimer();
        }
        else
        {
            startTimer();
        }
    }

    @FXML
    void handleBtnTimerPlay( ActionEvent event )
    {
        resetTimer();
        getFullScreenController().setView( FullScreenController.FullscreenControllerView.START );
        controllerManager.showView( ControllerManager.View.FULLSCREEN );
    }

    @FXML
    void handleBtnTimerPlayBreak( ActionEvent event )
    {
        setTimerToBreakTime();
        startTimer();
        getFullScreenController().setView( FullScreenController.FullscreenControllerView.BREAK );
        controllerManager.showView( ControllerManager.View.FULLSCREEN );
    }

    @FXML
    void handleMouseEnteredBtnCtrlMainView( MouseEvent event )
    {
        setView( MainControllerViews.CTRL );
    }

    @FXML
    void handleOnMouseDraggedBtnCtrlCtrlView( MouseEvent event )
    {
        double newX = event.getScreenX() - mainView.getBoundsInParent().getCenterX();

        if( newX < 1440 && newX > 120 )
        {
            getStage().setWidth( newX );
        }
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

    @FXML
    void initialize()
    {
        playSound();

        setView( MainControllerViews.TIMER_STOP );
        btnTimerPlayBreak.setVisible( false );

        final InvalidationListener ctrlViewVisualizer = event ->
        {
            if( stageDragging ) return;
            if( ( btnCtrlCtrlView.isHover() || gridPaneCtrlBtnArea.isHover() ) )
            {
                setView( MainControllerViews.CTRL );
            }
            else if( remainingSeconds == defaultTimerDuration )
            {
                btnTimerPlayBreak.setVisible( false );
                setView( MainControllerViews.TIMER_STOP );
            }
            else if( remainingSeconds > 0 )
            {
                setView( MainControllerViews.MAIN );
            }

            else
            {
                btnTimerPlayBreak.setVisible( true );
                setView( MainControllerViews.TIMER_STOP );
            }
        };
        btnCtrlCtrlView.hoverProperty().addListener( ctrlViewVisualizer );
        gridPaneCtrlBtnArea.hoverProperty().addListener( ctrlViewVisualizer );

        mainWindow.setVisible( true );

        mainWindowBounds = mainWindow.getBoundsInParent();
        mainWindowInvisibleBorder.setStyle( "-fx-border-color: rgba(0,0,0,0.0)" );
        mainView.hoverProperty().addListener( e ->
        {
            if( mainView.isHover() && !btnCtrlMainView.isHover() )
            {
                mainWindow.setVisible( false );
                mainWindowInvisibleBorder.setStyle( "-fx-border-color: rgba(254, 254, 254, 0.01)" );
                mainWindowBounds = mainWindow.getBoundsInParent();
            }
        } );
        final EventHandler<MouseEvent> onMouseLeaveMainView = e ->
        {
            if( mainWindowBounds == null ) return;
            if(
                    e.getX() >= mainWindowBounds.getMinX() && e.getX() <= mainWindowBounds.getMaxX() && e.getY() >= mainWindowBounds.getMinY() && e.getY() <= mainWindowBounds.getMaxY()
            )
            {
                System.out.println( "on view" );
            }
            else
            {
                mainWindow.setVisible( true );
                mainWindowInvisibleBorder.setStyle( "-fx-border-color: rgba(0,0,0,0.0)" );
            }
        };
        mainWindowInvisibleBorder.setOnMouseMoved( onMouseLeaveMainView );
        mainWindowInvisibleBorder.setOnMouseEntered( onMouseLeaveMainView );
        mainWindowInvisibleBorder.setOnMouseExited( onMouseLeaveMainView );

        getFullScreenController();
        resetTimer();
    }

    void pauseTimer()
    {
        if( currentState == State.WORK_RUNNING )
        {
            currentState = State.WORK_STOP;
        }
        else if( currentState == State.BREAK_RUNNING )
        {
            currentState = State.BREAK_STOP;
        }
        else
        {
            return;
        }

        if( timer != null )
        {
            timer.cancel();
            timer.purge();
        }
        btnStart.getStyleClass().remove( "btn-pause" );
        btnStart.getStyleClass().add( "btn-start" );
    }

    void playSound()
    {
        try
        {
            Media sound = new Media(
                    getClass().getResource( "/com/github/tharindusathis/jomodoro/sounds/microwave_oven.mp3" ).toString()
            );
            MediaPlayer mediaPlayer = new MediaPlayer( sound );
            mediaPlayer.play();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    void remainingSecondsSetter( int secs )
    {
        this.remainingSeconds = secs;
        String formattedTime;
        if( String.valueOf( getMinutes() ).length() <= 2 )
        {
            formattedTime = String.format( "%02d:%02d", getMinutes(), getSeconds() );
        }
        else if( String.valueOf( getMinutes() ).length() <= 3 )
        {
            formattedTime = String.format( "%02d", getMinutes() );
        }
        else
        {
            formattedTime = "999+";
        }
        System.out.println( "Set time to: " + formattedTime );
        lblTimer.setText( formattedTime );
        if( getFullScreenController() != null )
        {
            getFullScreenController().getLblTimer().setText( formattedTime );
            getFullScreenController().updateBreakProgressBar( ( double ) remainingSeconds / breakTimerDuration );
        }
        lblTimerSmall.setText( formattedTime );
        if( secs == 0 )
        {
            playSound();
            ( ( NotifyFlashScreenController ) controllerManager.getController(
                    ControllerManager.View.NOTIFY_FLASH ) ).flash();

            setView( MainControllerViews.TIMER_STOP );
            if( currentState == State.BREAK_RUNNING )
            {
                currentState = State.BREAK_STOP;
            }
            else
            {
                currentState = State.WORK_STOP;
            }
            btnTimerPlayBreak.setVisible( currentState == State.WORK_STOP );
        }
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

    public void setTagLabel( String text )
    {
        if( !text.isBlank() )
        {
            text = text.trim();
            tagBtnCtrlView.setText( text );
            tagBtnMainView.setText( text );
        }
    }

    void setTimerToBreakTime()
    {
        pauseTimer();
        remainingSecondsSetter( breakTimerDuration );
    }

    public void setView( MainControllerViews view )
    {
        if( view == MainControllerViews.MAIN )
        {
            mainView.setVisible( true );
            ctrlView.setVisible( false );
            timerStopView.setVisible( false );
        }
        else if( view == MainControllerViews.CTRL )
        {
            mainView.setVisible( false );
            ctrlView.setVisible( true );
            timerStopView.setVisible( false );
        }
        else if( view == MainControllerViews.TIMER_STOP )
        {
            mainView.setVisible( true );
            ctrlView.setVisible( false );
            timerStopView.setVisible( true );
        }
    }

    void stageDraggingOnDragged( MouseEvent event )
    {
        stageDragging = true;

        getStage().setX( event.getScreenX() + xOffset );
        getStage().setY( event.getScreenY() + yOffset );
    }

    void stageDraggingOnPressed( MouseEvent event )
    {
        stageDragging = true;
        xOffset = getStage().getX() - event.getScreenX();
        yOffset = getStage().getY() - event.getScreenY();
    }

    void stageDraggingOnReleased()
    {
        if( !stageDragging )
        {
            return;
        }
        else if( btnCtrlCtrlView.isHover() || gridPaneCtrlBtnArea.isHover() )
        {
            setView( MainControllerViews.CTRL );
        }
        else
        {
            if( remainingSeconds >= 1 )
            {
                setView( MainControllerViews.MAIN );
            }
            else
            {
                setView( MainControllerViews.TIMER_STOP );
            }
        }
        stageDragging = false;
    }

    public void startTimer()
    {
        if( currentState == State.BREAK_STOP )
        {
            currentState = State.BREAK_RUNNING;
        }
        else
        {
            currentState = State.WORK_RUNNING;
        }

        timer = new Timer();
        timer.schedule( new CountdownTask( remainingSeconds, this::remainingSecondsSetter ), 0, 1000 );

        setView( MainControllerViews.MAIN );

        btnStart.getStyleClass().remove( "btn-start" );
        btnStart.getStyleClass().add( "btn-pause" );
    }

    public enum State
    {
        WORK_RUNNING( true ),
        BREAK_RUNNING( true ),
        WORK_STOP( false ),
        BREAK_STOP( false ),
        INIT( false );

        private final boolean running;

        State( boolean running )
        {
            this.running = running;
        }

        public boolean isRunning()
        {
            return this.running;
        }
    }

    public enum MainControllerViews
    {
        MAIN, CTRL, TIMER_STOP
    }

}
