package com.github.tharindusathis.jomodoro.util;

import javafx.scene.text.Font;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class Resources
{
    private static final Map<CustomFont,Font> FONTS = new EnumMap<>( CustomFont.class );

    private Resources()
    {
    }

    public static Optional<Font> getFont( CustomFont name )
    {
        return Optional.ofNullable( FONTS.get( name ) );
    }

    public static void addFont( CustomFont name, Font font )
    {
        FONTS.put( name, font );
    }

    public enum CustomFont
    {
        ROBOTO_250, ROBOTO_87
    }
}
