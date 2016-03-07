package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.util.function.BiFunction;

public class NumberInstruction implements Instruction
{
	private final String                             description;
	private final Material                           material;
	private final BiFunction<Number, Number, Number> mapper;

	public NumberInstruction(String desc, Material material, BiFunction<Number, Number, Number> mapper)
	{
		this.description = desc;
		this.material = material;
		this.mapper = mapper;
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
		Object left = state.pop();
		Object right = state.pop();

		if (left instanceof Number && right instanceof Number)
		{
			state.push(this.mapper.apply((Number) left, (Number) right));
			return;
		}

		throw new ExecutionException("Cannot perform numeric operation");
	}
}
