package com.github.tharindusathis.jomodoro.controller;

import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

public class ControllerManager
{
    private Map<View,Controller> controllersMap;

    public Controller getController( View view )
    {
        if( controllersMap == null ) return null;
        if( !controllersMap.containsKey( view )) return null;
        return controllersMap.get( view );
    }

    public void registerController( View view, Controller controller, Stage stage )
    {
        if( controllersMap == null )
        {
            controllersMap = new EnumMap<>( View.class );
        }
        if( controllersMap.containsKey( view ) ) return;
        controller.setStage( stage );
        controller.setControllerManager( this );
        controllersMap.put( view, controller );
    }

    public void showView( View view )
    {
        controllersMap.forEach( ( v, controller ) ->
        {
            if( v == view )
            {
                controller.getStage().show();
            }
            else
            {
                controller.getStage().hide();
            }
        } );
    }

    public enum View
    {
        MAIN,
        FULLSCREEN,
        TRAY_MENU,
        NOTIFY_FLASH
    }
}
