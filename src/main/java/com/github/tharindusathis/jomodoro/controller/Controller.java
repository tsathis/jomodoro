package com.github.tharindusathis.jomodoro.controller;

public abstract class Controller
{
    ControllerManager controllerManager;

    public void setControllerManager( ControllerManager controllerManager )
    {
        this.controllerManager = controllerManager;
    }

    public ControllerManager getControllerManager()
    {
        return controllerManager;
    }
}
