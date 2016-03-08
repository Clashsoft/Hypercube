package com.clashsoft.hypercube.project;

import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.instruction.Instructions;
import com.clashsoft.hypercube.state.Position;
import javafx.scene.Group;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Grid
{
	protected final Project project;

	private Map<Position, GridElement> gridElementMap = new HashMap<>();

	public transient Group mainGroup = new Group();

	public Grid(Project project)
	{
		this.project = project;
	}

	public GridElement getElement(Position position)
	{
		return this.gridElementMap.get(position);
	}

	public GridElement createElement(Position position)
	{
		final GridElement element = this.gridElementMap.get(position);
		if (element == null)
		{
			final GridElement gridElement = new GridElement(this, position, null);
			this.gridElementMap.put(position, gridElement);

			return gridElement;
		}

		return element;
	}

	public Collection<GridElement> getElements()
	{
		return this.gridElementMap.values();
	}

	public void reset()
	{
		this.gridElementMap.clear();
	}

	public void readFrom(DataInput dataInput) throws IOException
	{
		final int entries = dataInput.readShort();

		this.gridElementMap = new HashMap<>(entries);

		for (int i = 0; i < entries; i++)
		{
			final Position position = Position.readFrom(dataInput);
			final Instruction instruction = Instructions.readFrom(dataInput);

			final GridElement gridElement = new GridElement(this, position, instruction);
			this.gridElementMap.put(position, gridElement);
		}
	}

	public void writeTo(DataOutput dataOutput) throws IOException
	{
		dataOutput.writeShort(this.gridElementMap.size());

		for (Map.Entry<Position, GridElement> entry : this.gridElementMap.entrySet())
		{
			final Position position = entry.getKey();
			final GridElement gridElement = entry.getValue();

			position.writeTo(dataOutput);
			Instructions.writeTo(gridElement.getInstruction(), dataOutput);
		}
	}
}

