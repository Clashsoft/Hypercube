package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public class OutputInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("print");

	@Override
	public String getDescription()
	{
		return "Output";
	}

	@Override
	public Material getMaterial()
	{
		return MATERIAL;
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.print(String.valueOf(state.pop()));
	}
}
