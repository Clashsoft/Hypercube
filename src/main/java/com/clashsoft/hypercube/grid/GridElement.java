package com.clashsoft.hypercube.grid;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.state.Position;
import javafx.scene.input.MouseButton;
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

	public final    Position    position;
	protected final Box         renderBox;
	private         Instruction instruction;

	protected GridElement(Grid grid, Position position, Instruction instruction)
	{
		this.position = position;

		final Box renderBox = new Box(1, 1, 1);
		renderBox.setTranslateX(this.position.x);
		renderBox.setTranslateY(this.position.y);
		renderBox.setTranslateZ(this.position.z);

		grid.mainGroup.getChildren().add(renderBox);

		renderBox.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY)
			{
				grid.ide.selectPosition(position);
			}
		});

		this.renderBox = renderBox;

		this.setInstruction(instruction);
	}

	public Instruction getInstruction()
	{
		return this.instruction;
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
