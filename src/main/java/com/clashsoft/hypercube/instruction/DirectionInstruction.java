package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public class DirectionInstruction implements Instruction
{
	private final String    description;
	private final Material  material;
	private final Direction direction;

	public DirectionInstruction(String desc, Material material, Direction direction)
	{
		this.description = desc;
		this.material = material;
		this.direction = direction;
	}

	@Override
	public String getDescription()
	{
		return this.description;
	}

	@Override
	public Material getMaterial()
	{
		return this.material;
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.setDirection(this.direction);
	}
}
