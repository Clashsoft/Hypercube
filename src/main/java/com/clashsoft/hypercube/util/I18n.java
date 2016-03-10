package com.clashsoft.hypercube.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n
{
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Hypercube");

	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException ex)
		{
			return '#' + key;
		}
	}

	public static String getString(String key, Object... args)
	{
		return String.format(getString(key), args);
	}
}
