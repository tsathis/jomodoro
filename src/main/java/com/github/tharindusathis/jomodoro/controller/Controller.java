package com.github.tharindusathis.jomodoro.controller;

import javafx.stage.Stage;

import java.util.Optional;

public abstract class Controller
{
    public Optional<ControllerManager> getControllerManager()
    {
        return Optional.ofNullable(controllerManager);
    }

    private ControllerManager controllerManager;
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
