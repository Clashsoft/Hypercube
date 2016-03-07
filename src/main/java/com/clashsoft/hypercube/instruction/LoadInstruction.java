package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public class LoadInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("load");

	public final String varName;

	public LoadInstruction(String varName)
	{
		this.varName = varName;
	}

	@Override
	public Material getMaterial()
	{
		return MATERIAL;
	}

	@Override
	public String getDescription()
	{
		return "Load '" + this.varName + '\'';
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.push(state.load(this.varName));
	}
}
