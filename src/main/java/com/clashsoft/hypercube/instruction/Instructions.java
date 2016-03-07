package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.util.TextureLoader;
import javafx.scene.paint.Material;

public final class Instructions
{
	public static Instruction ADD      = new NumberInstruction("Add", textured("add"),
	                                                           (n1, n2) -> n1.doubleValue() + n2.doubleValue());
	public static Instruction SUBTRACT = new NumberInstruction("Subtract", textured("subtract"),
	                                                           (n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction("Multiply", textured("multiply"),
	                                                           (n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE   = new NumberInstruction("Divide", textured("divide"),
	                                                           (n1, n2) -> n1.doubleValue() / n2.doubleValue());

	public static Instruction OUTPUT = new BaseInstruction("Output", textured("print"), //
	                                                       executionState -> executionState.print(
		                                                       String.valueOf(executionState.pop())));

	public static Instruction POP = new BaseInstruction("Pop", textured("pop"), ExecutionState::pop);

	public static Instruction DUP = new BaseInstruction("Duplicate", textured("dup"), executionState -> {
		Object top = executionState.pop();
		executionState.push(top);
		executionState.push(top);
	});

	public static Instruction UP       = new DirectionInstruction("Up", textured("up"), Direction.UP);
	public static Instruction DOWN     = new DirectionInstruction("Down", textured("down"), Direction.DOWN);
	public static Instruction RIGHT    = new DirectionInstruction("Right", textured("right"), Direction.RIGHT);
	public static Instruction LEFT     = new DirectionInstruction("Left", textured("left"), Direction.LEFT);
	public static Instruction FORWARD  = new DirectionInstruction("Forward", textured("forward"), Direction.FORWARD);
	public static Instruction BACKWARD = new DirectionInstruction("Backward", textured("backward"), Direction.BACKWARD);

	protected static Material textured(String name)
	{
		return TextureLoader.material("instructions", name);
	}
}
