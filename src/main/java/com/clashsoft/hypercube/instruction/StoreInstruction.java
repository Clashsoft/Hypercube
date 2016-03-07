package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public class StoreInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("store");

	public final String varName;

	public StoreInstruction(String varName)
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
		return "Store '" + this.varName + '\'';
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.store(this.varName, state.pop());
	}
}
