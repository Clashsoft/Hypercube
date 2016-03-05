package com.clashsoft.hypercube.ide.grid;

import com.clashsoft.hypercube.ide.HypercubeIDE;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.state.Position;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

import java.util.HashMap;
import java.util.Map;

public class Grid
{
	public Map<Position, GridElement> gridElementMap = new HashMap<>();

	private final HypercubeIDE ide;

	public Group mainGroup = new Group();

	public Grid(HypercubeIDE ide) {this.ide = ide;}

	public GridElement getElement(Position position)
	{
		final GridElement element = this.gridElementMap.get(position);
		if (element == null)
		{
			final Box renderBox = new Box(1, 1, 1);
			renderBox.setTranslateX(position.x);
			renderBox.setTranslateY(position.y);
			renderBox.setTranslateZ(position.z);

			renderBox.setMaterial(GridElement.MATERIAL);
			renderBox.setDrawMode(DrawMode.LINE);

			this.mainGroup.getChildren().add(renderBox);

			final GridElement gridElement = new GridElement(renderBox);
			this.gridElementMap.put(position, gridElement);

			renderBox.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY)
				{
					this.ide.selectPosition(position);
				}
			});

			return gridElement;
		}

		return element;
	}

	public void setInstruction(Position position, Instruction instruction)
	{
		this.getElement(position).setInstruction(instruction);
	}
}

