package com.clashsoft.hypercube.grid;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.instruction.Instruction;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

public class GridElement
{
	public static final PhongMaterial MATERIAL = new PhongMaterial(new Color(1, 1, 1, 0.25F));

	static
	{
		HypercubeIDE.SELECTED_MATERIAL.setSpecularPower(200);
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
