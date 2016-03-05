package com.clashsoft.hypercube.ide.grid;

import com.clashsoft.hypercube.instruction.Instruction;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

public class GridElement
{
	public static final PhongMaterial MATERIAL          = new PhongMaterial(new Color(1, 1, 1, 0.25F));
	public static final PhongMaterial SELECTED_MATERIAL = new PhongMaterial(Color.rgb(0xFF, 0xA5, 0, 0.5));

	static
	{
		SELECTED_MATERIAL.setSpecularPower(200);
	}

	public final Box         renderBox;
	public       Instruction instruction;

	public GridElement(Box renderBox)
	{
		this.renderBox = renderBox;
	}

	public void setInstruction(Instruction instruction)
	{
		this.instruction = instruction;

		if (instruction == null)
		{
			this.renderBox.setMaterial(MATERIAL);
			this.renderBox.setDrawMode(DrawMode.LINE);
		}
		else
		{
			this.renderBox.setMaterial(instruction.getMaterial());
			this.renderBox.setDrawMode(DrawMode.FILL);
		}
	}
}
