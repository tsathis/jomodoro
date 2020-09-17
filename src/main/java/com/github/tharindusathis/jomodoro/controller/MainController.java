package com.github.tharindusathis.jomodoro.controller;

import com.github.tharindusathis.jomodoro.timer.Configs;
import com.github.tharindusathis.jomodoro.timer.CountdownTask;
import com.github.tharindusathis.jomodoro.util.Loggers;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.robot.Robot;

import java.util.Timer;
import java.util.logging.Level;

import static com.github.tharindusathis.jomodoro.controller.ControllerManager.View;
import static com.github.tharindusathis.jomodoro.controller.FullScreenController.FullscreenStageViews;

/**
 * Main controller class
 *
 * @author tharindusathis
 */
public class MainController extends Controller
{
    State currentState = State.INIT;

    Timer mainTimer;
    int remainingSeconds = Configs.getTimerDuration();

    double stageDragOffsetX;
    double stageDragOffsetY;
    boolean stageDragging = false;

    Bounds containerBoundsOnScreen;
    Robot robot = new Robot();

    @FXML
    private StackPane parentContainer;
    @FXML
    private StackPane timerStopView;
    @FXML
    private Button tagBtnTimerView;
    @FXML
    private Button tagBtnControlView;
    @FXML
    private Button btnStart;
    @FXML
    private Label timeLabelTimerView;
    @FXML
    private Label timeLabelControlView;
    @FXML
    private BorderPane timerView;
    @FXML
    private StackPane controlView;
    @FXML
    private Button controlButtonTimerView;
    @FXML
    private Button controlButtonControlView;
    @FXML
    private Button btnTimerPlayBreak;
    @FXML
    private GridPane controlAreaGridPane;

    void addTime( @SuppressWarnings( "SameParameterValue" ) int seconds )
    {
        State previousState = currentState;
        pauseTimer();
        remainingSecondsSetter( remainingSeconds + seconds );
        if( previousState.isRunning() )
        {
            startTimer();
        }
    }

    void afterInitialize()
    {
        playSound();
    }

    @FXML
    void btnAddOnAction( @SuppressWarnings( "unused" ) ActionEvent event )
    {
        addTime( 60 );
    }

    @FXML
    void btnBreakOnAction(  @SuppressWarnings( "unused" ) ActionEvent event )
    {
        setTimerToBreakTime();
        getControllerManager().flatMap( ctrlMgr -> ctrlMgr.getController( FullScreenController.class ) )
                              .ifPresent( controller -> controller.setView( FullscreenStageViews.START ) );
    }

    @FXML
    void btnCloseOnAction(  @SuppressWarnings( "unused" ) ActionEvent event )
    {
        Platform.exit();
        System.exit( 0 );
    }

    @FXML
    void btnRestartOnAction(  @SuppressWarnings( "unused" )  ActionEvent event )
    {
        resetTimer();
    }

    @FXML
    void btnSettingsOnAction( @SuppressWarnings( "unused" )  ActionEvent event )
    {
        getControllerManager().ifPresent(
                ctrlMgr -> ctrlMgr.getController( FullScreenController.class )
                                  .ifPresent( controller ->
                                  {
                                      controller.setView( FullscreenStageViews.START );
                                      ctrlMgr.showView( View.FULLSCREEN );
                                  } )
        );
    }

    @FXML
    void btnStartOnAction(  @SuppressWarnings( "unused" )  ActionEvent event )
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
    void controlAreaGridPaneOnMouseDragged( MouseEvent event )
    {
        stageDraggingOnDragged( event );
    }

    @FXML
    void controlAreaGridPaneOnMousePressed( MouseEvent event )
    {
        stageDraggingOnPressed( event );
    }

    @FXML
    void controlAreaGridPaneOnMouseReleased( @SuppressWarnings( "unused" )  MouseEvent event )
    {
        stageDraggingOnReleased();
    }

    @FXML
    void controlButtonTimerViewOnAction( @SuppressWarnings( "unused" )  ActionEvent event )
    {
        setView( MainStageViews.CONTROL );
    }

    @FXML
    void controlButtonTimerViewOnMouseEntered(  @SuppressWarnings( "unused" ) MouseEvent event )
    {
        setView( MainStageViews.CONTROL );
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
    void handleBtnHide( @SuppressWarnings( "unused" )  ActionEvent event )
    {
        containerBoundsOnScreen = parentContainer.localToScreen( parentContainer.getBoundsInLocal() );
        parentContainer.setVisible( false );
    }

    @FXML
    void handleBtnTimerPlay( @SuppressWarnings( "unused" )  ActionEvent event )
    {
        resetTimer();
        getControllerManager().ifPresent( ctrlMgr -> ctrlMgr.getController( FullScreenController.class )
                                                            .ifPresent( controller ->
                                                            {
                                                                controller.setView( FullscreenStageViews.START );
                                                                ctrlMgr.showView( View.FULLSCREEN );
                                                            } ) );
    }

    @FXML
    void handleBtnTimerPlayBreak( @SuppressWarnings( "unused" )  ActionEvent event )
    {
        setTimerToBreakTime();
        startTimer();

        getControllerManager().ifPresent( ctrlMgr -> ctrlMgr.getController( FullScreenController.class )
                                                            .ifPresent( controller ->
                                                            {
                                                                controller.setView( FullscreenStageViews.BREAK );
                                                                ctrlMgr.showView( View.FULLSCREEN );
                                                            } ) );
    }

    @FXML
    void handleOnMouseDraggedBtnCtrlCtrlView( MouseEvent event )
    {
        double newX = event.getScreenX() - timerView.getBoundsInParent().getCenterX();

        if( newX < 1440 && newX > 120 )
        {
            getStage().setWidth( newX );
        }
    }

    @FXML
    void handleOnMousePressedBtnCtrlCtrlView( MouseEvent event )
    {
        stageDraggingOnPressed( event );
    }

    @FXML
    void handleOnMouseReleasedBtnCtrlCtrlView( @SuppressWarnings( "unused" )  MouseEvent event )
    {
        stageDraggingOnReleased();
    }

    void initViewHoverListeners()
    {
        final InvalidationListener controlViewVisibilitySetter = event ->
        {
            if( stageDragging ) return;
            if( ( controlButtonControlView.isHover() || controlAreaGridPane.isHover() ) )
            {
                setView( MainStageViews.CONTROL );
            }
            else if(currentState.isRunning() || remainingSeconds > 0)
            {
                setView( MainStageViews.TIMER );
            }
            else
            {
                btnTimerPlayBreak.setVisible( currentState == State.WORK_STOP );
                setView( MainStageViews.TIMER_STOP );
            }
        };

        controlButtonControlView.hoverProperty().addListener( controlViewVisibilitySetter );
        controlAreaGridPane.hoverProperty().addListener( controlViewVisibilitySetter );

        timerView.hoverProperty().addListener( e ->
        {
            /* TODO: check whether this breaks current code || remove */
            if(!currentState.isRunning()) return;

            if( timerView.isHover() && !controlButtonTimerView.isHover() )
            {
                containerBoundsOnScreen = parentContainer.localToScreen( parentContainer.getBoundsInLocal() );
                parentContainer.setVisible( false );
            }
        } );
        containerBoundsOnScreen = parentContainer.localToScreen( parentContainer.getBoundsInLocal() );
    }


    @FXML
    void initialize()
    {
        setView( MainStageViews.TIMER_STOP );

        btnTimerPlayBreak.setVisible( false );
        parentContainer.setVisible( true );

        resetTimer();
        initViewHoverListeners();

        afterInitialize();
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

        if( mainTimer != null )
        {
            mainTimer.cancel();
            mainTimer.purge();
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
            Loggers.COMMON_LOGGER.log( Level.WARNING, e.getMessage() );
            e.printStackTrace();
        }
    }

    void remainingSecondsSetter( int secs )
    {
        if( containerBoundsOnScreen != null )
        {
            Point2D mousePoint = robot.getMousePosition();
            if( !containerBoundsOnScreen.contains( mousePoint ) )
            {
                parentContainer.setVisible( true );
            }
        }

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
        Loggers.COMMON_LOGGER.log( Level.INFO, () -> "Set time to: " + formattedTime );

        timeLabelTimerView.setText( formattedTime );

        getControllerManager().flatMap( ctrlMgr -> ctrlMgr.getController( FullScreenController.class ) )
                              .ifPresent( controller ->
                              {
                                  controller.getLblTimer().setText( formattedTime );
                                  controller.updateBreakProgressBar(
                                          ( double ) remainingSeconds / Configs.getBreakTimerDuration() );
                              } );

        timeLabelControlView.setText( formattedTime );
        if( secs == 0 )
        {
            playSound();

            getControllerManager()
                    .flatMap( ctrlMgr -> ctrlMgr.getController( NotifyFlashScreenController.class ) )
                    .ifPresent( controller -> controller.flashRepeatedly( Configs.getFlashNotifyInterval() ) )
            ;

            setView( MainStageViews.TIMER_STOP );
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
        currentState = State.INIT;
        remainingSecondsSetter( Configs.getTimerDuration() );
    }


    public void setTagLabel( String text )
    {
        if( !text.isBlank() )
        {
            text = text.trim();
            tagBtnControlView.setText( text );
            tagBtnTimerView.setText( text );
        }
    }

    void setTimerToBreakTime()
    {
        pauseTimer();
        remainingSecondsSetter( Configs.getBreakTimerDuration() );
    }

    public void setView( MainStageViews view )
    {
        if( view == MainStageViews.TIMER || view == MainStageViews.TIMER_STOP)
        {
            timerView.setVisible( true );
            controlView.setVisible( false );
            timerStopView.setVisible( view == MainStageViews.TIMER_STOP );
        }
        else if( view == MainStageViews.CONTROL )
        {
            timerView.setVisible( false );
            controlView.setVisible( true );
            timerStopView.setVisible( false );
        }
        Loggers.COMMON_LOGGER.log( Level.FINE, () -> "Set view: " + view.name() );
    }

    void stageDraggingOnDragged( MouseEvent event )
    {
        stageDragging = true;

        getStage().setX( event.getScreenX() + stageDragOffsetX );
        getStage().setY( event.getScreenY() + stageDragOffsetY );
    }

    void stageDraggingOnPressed( MouseEvent event )
    {
        stageDragging = true;
        stageDragOffsetX = getStage().getX() - event.getScreenX();
        stageDragOffsetY = getStage().getY() - event.getScreenY();
    }

    void stageDraggingOnReleased()
    {
        if( !stageDragging )
        {
            return;
        }
        else if( controlButtonControlView.isHover() || controlAreaGridPane.isHover() )
        {
            setView( MainStageViews.CONTROL );
        }
        else
        {
            if( remainingSeconds >= 1 )
            {
                setView( MainStageViews.TIMER );
            }
            else
            {
                setView( MainStageViews.TIMER_STOP );
            }
        }
        stageDragging = false;
    }

    public void startTimer()
    {
        getControllerManager()
                .flatMap( ctrlMgr -> ctrlMgr.getController( NotifyFlashScreenController.class ) )
                .ifPresent( NotifyFlashScreenController::flashReset )
        ;

        if( currentState == State.BREAK_STOP )
        {
            currentState = State.BREAK_RUNNING;
        }
        else
        {
            currentState = State.WORK_RUNNING;
        }

        mainTimer = new Timer();
        mainTimer.schedule( new CountdownTask( remainingSeconds, this::remainingSecondsSetter ), 0, 1000 );


        btnStart.getStyleClass().remove( "btn-start" );
        btnStart.getStyleClass().add( "btn-pause" );

        /* TODO: check whether this if this code needed: `setView( MainStageViews.TIMER );` */
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

    public enum MainStageViews
    {
        TIMER, CONTROL, TIMER_STOP
    }

}
