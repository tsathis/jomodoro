package com.github.tharindusathis.jomodoro;

import com.github.tharindusathis.jomodoro.controller.ControllerManager;
import com.github.tharindusathis.jomodoro.controller.FullScreenController;
import com.github.tharindusathis.jomodoro.controller.MainController;
import com.github.tharindusathis.jomodoro.controller.NotifyFlashScreenController;
import com.github.tharindusathis.jomodoro.util.Loggers;
import com.github.tharindusathis.jomodoro.util.Resources;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;

/**
 * @author tharindusathis
 */
public class App extends Application
{
    private static final double INIT_UI_SCALE_FACTOR = .4;
    private Stage mainViewStage;
    private Stage fullscreenViewStage;
    private Stage notifyFlashScreenStage;

    public static void main( String[] args )
    {
        launch();
    }

    private FullScreenController createFullscreenView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource( "/com/github/tharindusathis/jomodoro/view/fullscreen-stage.fxml" ) );
        Region contentRootRegion = loader.load();

        double origW = 1920;
        double origH = 1080;

        contentRootRegion.setPrefWidth( origW );
        contentRootRegion.setPrefHeight( origH );

        Group group = new Group( contentRootRegion );
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add( group );
        rootPane.setStyle( "-fx-background-color: rgba(0,0,0,0.8)" );

        Scene scene = new Scene( rootPane, origW, origH );


        DoubleBinding width = rootPane.maxWidthProperty().divide( origW );
        group.scaleXProperty().bind( width );
        group.scaleYProperty().bind( width );
        rootPane.setMaxWidth( origW );


        fullscreenViewStage = new Stage();
        fullscreenViewStage.initOwner( getUtilityStage() );
        fullscreenViewStage.initStyle( StageStyle.TRANSPARENT );
        scene.setFill( Color.TRANSPARENT );
        fullscreenViewStage.setScene( scene );
        fullscreenViewStage.setAlwaysOnTop( true );

        rootPane.setMaxWidth( origW * INIT_UI_SCALE_FACTOR * 2 );
        return loader.getController();
    }

    private MainController createMainView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource( "/com/github/tharindusathis/jomodoro/view/main-stage.fxml" ) );
        Region contentRootRegion = loader.load();

        double origW = 720.0;
        double origH = 360.0;

        contentRootRegion.setPrefWidth( origW );
        contentRootRegion.setPrefHeight( origH );

        Group group = new Group( contentRootRegion ); // non-resizable container (Group)
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add( group );
        rootPane.setStyle( "-fx-background-color: #00000000" );
        Scene scene = new Scene( rootPane, origW, origH );

        // bind the scene's width and height to the scaling parameters on the group
        DoubleBinding width = scene.widthProperty().divide( origW );
        group.scaleXProperty().bind( width );
        group.scaleYProperty().bind( width );

        mainViewStage = new Stage();
        mainViewStage.initOwner( getUtilityStage() );
        mainViewStage.initStyle( StageStyle.TRANSPARENT );
        scene.setFill( Color.TRANSPARENT );
        mainViewStage.setScene( scene );
        mainViewStage.setAlwaysOnTop( true );

        // initial scaling
        mainViewStage.setWidth( origW * INIT_UI_SCALE_FACTOR );

        // restore when manual minimize
        mainViewStage.iconifiedProperty().addListener(
                ( ov, t, t1 ) -> mainViewStage.setIconified( false ) );

        return loader.getController();
    }

    private NotifyFlashScreenController createNotifyFlashScreenView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource( "/com/github/tharindusathis/jomodoro/view/notify-flash-screen-stage.fxml" ) );
        Region contentRootRegion = loader.load();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        contentRootRegion.setPrefWidth( screenBounds.getWidth() );
        contentRootRegion.setPrefHeight( screenBounds.getHeight() );
        contentRootRegion.setMouseTransparent( true );
        contentRootRegion.setPickOnBounds( false );

        notifyFlashScreenStage = new Stage();
        notifyFlashScreenStage.initOwner( getUtilityStage() );
        notifyFlashScreenStage.initStyle( StageStyle.TRANSPARENT );
        Scene scene = new Scene( contentRootRegion, screenBounds.getWidth(), screenBounds.getHeight() );
        contentRootRegion.setStyle( "-fx-background-color: rgba(0,0,0,0.0)" );
        scene.setFill( Color.TRANSPARENT );
        notifyFlashScreenStage.setScene( scene );
        notifyFlashScreenStage.setAlwaysOnTop( true );
        return loader.getController();
    }

    /**
     * Get a utility styled stage.
     *
     * @return Utility styled stage
     */
    private Stage getUtilityStage()
    {
        Stage utilityStage = new Stage();
        utilityStage.initStyle( StageStyle.UTILITY );
        utilityStage.setOpacity( 0 );
        utilityStage.setHeight( 0 );
        utilityStage.setWidth( 0 );
        utilityStage.show();
        return utilityStage;
    }

    @Override
    public void start( Stage stage )
    {
        try
        {
            String fontRobotoPath = getClass().getResource( "/com/github/tharindusathis/jomodoro/fonts/Roboto-Medium.ttf" )
                                              .toExternalForm();
            Resources.addFont( Resources.CustomFont.ROBOTO_250, Font.loadFont( fontRobotoPath , 250 ) );
            Resources.addFont( Resources.CustomFont.ROBOTO_87, Font.loadFont( fontRobotoPath, 87 ) );
        }
        catch( Exception e )
        {
            Loggers.COMMON_LOGGER.log( Level.SEVERE, e::getMessage );
        }

        MainController mainController = null;
        FullScreenController fullscreenController = null;
        NotifyFlashScreenController notifyFlashScreenController = null;
        try
        {
            mainController = createMainView();
            fullscreenController = createFullscreenView();
            notifyFlashScreenController = createNotifyFlashScreenView();
        }
        catch( IOException e )
        {
            Loggers.COMMON_LOGGER.log( Level.SEVERE, e::getMessage );
        }

        final ControllerManager controllerManager = new ControllerManager();
        controllerManager.registerController(
                ControllerManager.View.MAIN, mainController, mainViewStage );
        controllerManager.registerController(
                ControllerManager.View.FULLSCREEN, fullscreenController, fullscreenViewStage );
        controllerManager.registerController(
                ControllerManager.View.NOTIFY_FLASH, notifyFlashScreenController, notifyFlashScreenStage );

        mainViewStage.show();
    }


}