package com.github.tharindusathis.jomodoro.controller;

public class ControllerManager
{
    private final MainController mainController;
    private final FullScreenController fullScreenController;
    private final TrayMenuController trayMenuController;

    public ControllerManager(
            MainController mainController,
            FullScreenController fullScreenController,
            TrayMenuController trayMenuController )
    {
        this.mainController = mainController;
        this.fullScreenController = fullScreenController;
        this.trayMenuController = trayMenuController;
    }

    public FullScreenController getFullScreenController()
    {
        return fullScreenController;
    }

    public MainController getMainController()
    {
        return mainController;
    }

    public TrayMenuController getTrayMenuController()
    {
        return trayMenuController;
    }
}
