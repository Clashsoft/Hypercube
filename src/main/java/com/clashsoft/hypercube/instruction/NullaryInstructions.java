package com.clashsoft.hypercube.instruction;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.io.File;

public final class NullaryInstructions
{
	public static Instruction ADD      = new NumberInstruction(textured("add"),
	                                                           (n1, n2) -> n1.doubleValue() + n2.doubleValue());
	public static Instruction SUBTRACT = new NumberInstruction(textured("subtract"),
	                                                           (n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction(textured("multiply"),
	                                                           (n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE   = new NumberInstruction(textured("divide"),
	                                                           (n1, n2) -> n1.doubleValue() / n2.doubleValue());

	public static Material textured(String imageSource)
	{
		final PhongMaterial material = new PhongMaterial(Color.WHITE);

		try
		{
			material.setDiffuseMap(new Image("instructions" + File.separator + imageSource + ".png"));
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Cannot load image: " + imageSource, ex);
		}
		return material;
	}
}
