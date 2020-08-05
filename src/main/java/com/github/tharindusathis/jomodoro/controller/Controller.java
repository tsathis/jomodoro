package com.github.tharindusathis.jomodoro.controller;

import javafx.stage.Stage;

public abstract class Controller
{
    ControllerManager controllerManager;
    Stage stage;

    public void setControllerManager( ControllerManager controllerManager )
    {
        this.controllerManager = controllerManager;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void setStage( Stage stage )
    {
        this.stage = stage;
    }
}
