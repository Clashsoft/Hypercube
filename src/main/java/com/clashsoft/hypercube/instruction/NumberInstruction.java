package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;

import java.util.function.BiFunction;

public class NumberInstruction implements Instruction
{
	private final BiFunction<Number, Number, Number> mapper;

	public NumberInstruction(BiFunction<Number, Number, Number> mapper)
	{
		this.mapper = mapper;
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
