package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;

public final class NullaryInstructions
{
	public static Instruction ADD = state -> {
		Object object1 = state.pop();
		Object object2 = state.pop();
		Object result;

		if (object1 instanceof Number && object2 instanceof Number)
		{
			result = ((Number) object1).doubleValue() + ((Number) object2).doubleValue();
		}
		else if (object1 instanceof String || object2 instanceof String)
		{
			result = object1 + "" + object2;
		}
		else
		{
			throw new ExecutionException("Can't add '" + object1 + "' and '" + object2 + "'");
		}

		state.push(result);
	};

	public static Instruction SUBTRACT = new NumberInstruction((n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction((n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE = new NumberInstruction((n1, n2) -> n1.doubleValue() / n2.doubleValue());
}
