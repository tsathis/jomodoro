package com.github.tharindusathis.jomodoro.controller;

import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ControllerManager
{
    private Map<View,Controller> controllersMap;

    private <T extends Controller> T findController( Class<T> controllerType )
    {
        if( controllersMap == null ) return null;

        for (Map.Entry<View, Controller> entry : controllersMap.entrySet()) {
            if(controllerType.isInstance( entry.getValue())){
                return (T) entry.getValue();
            }
        }
        return  null;
    }

    public <T extends Controller> Optional<T> getController( Class<T> controllerType){
        return Optional.ofNullable( findController( controllerType ) );
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
