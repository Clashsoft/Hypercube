package com.clashsoft.hypercube.util;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.io.File;

public final class TextureLoader
{
	public static Material material(String domain, String imageSource)
	{
		final PhongMaterial material = new PhongMaterial(Color.WHITE);
		material.setDiffuseMap(load(domain, imageSource));
		return material;
	}

	public static Image load(String domain, String imageSource)
	{
		try
		{
			return new Image(domain + File.separator + imageSource + ".png");
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Cannot load image: " + domain + "/" + imageSource, ex);
		}
	}
}
