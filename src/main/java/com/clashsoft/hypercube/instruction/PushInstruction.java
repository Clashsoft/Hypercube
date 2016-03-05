package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public class PushInstruction implements Instruction
{
	private static final Material MATERIAL = NullaryInstructions.textured("push");

	private Object value;

	public PushInstruction(Object value)
	{
		this.value = value;
	}

	@Override
	public Material getMaterial()
	{
		return MATERIAL;
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.push(this.value);
	}
}
