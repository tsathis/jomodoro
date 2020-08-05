package com.github.tharindusathis.jomodoro;

import com.github.tharindusathis.jomodoro.controller.ControllerManager;
import com.github.tharindusathis.jomodoro.controller.FullScreenController;
import com.github.tharindusathis.jomodoro.controller.MainController;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author tharindusathis
 */
public class App extends Application
{
    private Stage mainViewStage;
    private Stage fullScreenViewStage;
    private ControllerManager controllerManager;

    public static void main( String[] args )
    {
        launch();
    }

    private FullScreenController createFullscreenView() throws IOException
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

        fullScreenViewStage = new Stage();
        fullScreenViewStage.initStyle( StageStyle.TRANSPARENT );
        scene.setFill( Color.TRANSPARENT );
        fullScreenViewStage.setScene( scene );
        fullScreenViewStage.setAlwaysOnTop( true );

        fullScreenViewStage.setWidth( fullScreenViewStage.getWidth() * 0.5 );

        //todo: move this logic to ControllerManager
        fullScreenViewStage.setOnHiding( event ->
        {
            // handleOnFullScreenViewHiding();
        } );
        fullScreenViewStage.setOnShowing( event ->
        {
            // handleOnFullScreenViewShowing();
        } );

        return loader.getController();
    }

    private MainController createMainView() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource( "/com/github/tharindusathis/jomodoro/view/main-view.fxml" ) );
        Region contentRootRegion = loader.load();

        double origW = 720;
        double origH = 360;

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
        mainViewStage.initStyle( StageStyle.TRANSPARENT );
        scene.setFill( Color.TRANSPARENT );
        mainViewStage.setScene( scene );
        mainViewStage.setAlwaysOnTop( true );

        mainViewStage.setWidth( mainViewStage.getWidth() * 0.5 ); // initial scaling
        return loader.getController();
    }

    @Override
    public void start( Stage stage )
    {
        MainController mainController = null;
        FullScreenController fullscreenController = null;
        try
        {
            mainController = createMainView();
            fullscreenController = createFullscreenView();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        controllerManager = new ControllerManager();
        controllerManager.registerController(
                ControllerManager.View.MAIN, mainController, mainViewStage );
        controllerManager.registerController(
                ControllerManager.View.FULLSCREEN, fullscreenController, fullScreenViewStage );

        mainViewStage.show();
    }
}