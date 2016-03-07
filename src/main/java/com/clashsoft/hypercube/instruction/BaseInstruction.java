package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.util.function.Consumer;

public class BaseInstruction implements Instruction
{
	private final String                             description;
	private final Material                           material;
	private final Consumer<ExecutionState> consumer;

	public BaseInstruction(String desc, Material material, Consumer<ExecutionState> consumer)
	{
		this.description = desc;
		this.material = material;
		this.consumer = consumer;
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
		this.consumer.accept(state);
	}
}
