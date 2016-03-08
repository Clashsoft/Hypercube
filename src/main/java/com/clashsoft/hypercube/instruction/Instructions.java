package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.util.TextureLoader;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class Instructions
{
	private static final byte NOOP_ID = 0;

	private static final byte ADD_ID      = 1;
	private static final byte SUBTRACT_ID = 2;
	private static final byte MULTIPLY_ID = 3;
	private static final byte DIVIDE_ID   = 4;
	private static final byte MOD_ID      = 5;
	private static final byte COMPARE_ID  = 8;

	private static final   byte POP_ID  = 12;
	private static final   byte DUP_ID  = 13;
	protected static final byte PUSH_ID = 14;

	private static final byte OUTPUT_ID = 16;

	protected static final byte LOAD_VAR_ID   = 24;
	protected static final byte STORE_VAR_ID  = 25;
	protected static final byte DELETE_VAR_ID = 26;

	private static final byte UP_ID       = 32;
	private static final byte DOWN_ID     = 33;
	private static final byte RIGHT_ID    = 34;
	private static final byte LEFT_ID     = 35;
	private static final byte FORWARD_ID  = 36;
	private static final byte BACKWARD_ID = 37;

	public static Instruction ADD      = new NumberInstruction(ADD_ID, "Add", textured("add"),
	                                                           (n1, n2) -> n1.doubleValue() + n2.doubleValue());
	public static Instruction SUBTRACT = new NumberInstruction(SUBTRACT_ID, "Subtract", textured("subtract"),
	                                                           (n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction(MULTIPLY_ID, "Multiply", textured("multiply"),
	                                                           (n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE   = new NumberInstruction(DIVIDE_ID, "Divide", textured("divide"),
	                                                           (n1, n2) -> n1.doubleValue() / n2.doubleValue());

	public static Instruction OUTPUT = new BaseInstruction(OUTPUT_ID, "Output", textured("print"), //
	                                                       executionState -> executionState.print(
		                                                       String.valueOf(executionState.pop())));

	public static Instruction POP = new BaseInstruction(POP_ID, "Pop", textured("pop"), ExecutionState::pop);

	public static Instruction DUP = new BaseInstruction(DUP_ID, "Duplicate", textured("dup"), executionState -> {
		Object top = executionState.pop();
		executionState.push(top);
		executionState.push(top);
	});

	public static Instruction UP       = new DirectionInstruction(UP_ID, "Up", textured("up"), Direction.UP);
	public static Instruction DOWN     = new DirectionInstruction(DOWN_ID, "Down", textured("down"), Direction.DOWN);
	public static Instruction RIGHT    = new DirectionInstruction(RIGHT_ID, "Right", textured("right"),
	                                                              Direction.RIGHT);
	public static Instruction LEFT     = new DirectionInstruction(LEFT_ID, "Left", textured("left"), Direction.LEFT);
	public static Instruction FORWARD  = new DirectionInstruction(FORWARD_ID, "Forward", textured("forward"),
	                                                              Direction.FORWARD);
	public static Instruction BACKWARD = new DirectionInstruction(BACKWARD_ID, "Backward", textured("backward"),
	                                                              Direction.BACKWARD);

	public static Instruction COMPARE = new BaseInstruction(COMPARE_ID, "Compare", textured("cmp"), executionState -> {
		double number = ((Number) executionState.pop()).doubleValue();
		if (number < 0)
		{
			executionState.setDirection(Direction.DOWN);
		}
		else if (number > 0)
		{
			executionState.setDirection(Direction.UP);
		}
		// else keep direction
	});

	protected static Material textured(String name)
	{
		return TextureLoader.material("instructions", name);
	}

	public static Instruction readFrom(DataInput dataInput) throws IOException
	{
		final Instruction instruction;
		switch (dataInput.readByte())
		{
		case NOOP_ID:
		default:
			return null;
		case ADD_ID:
			return ADD;
		case SUBTRACT_ID:
			return SUBTRACT;
		case MULTIPLY_ID:
			return MULTIPLY;
		case DIVIDE_ID:
			return DIVIDE;
		case COMPARE_ID:
			return COMPARE;
		case POP_ID:
			return POP;
		case DUP_ID:
			return DUP;
		case PUSH_ID:
			instruction = new PushInstruction();
			break;
		case OUTPUT_ID:
			return OUTPUT;
		case UP_ID:
			return UP;
		case DOWN_ID:
			return DOWN;
		case LEFT_ID:
			return LEFT;
		case RIGHT_ID:
			return RIGHT;
		case FORWARD_ID:
			return FORWARD;
		case BACKWARD_ID:
			return BACKWARD;
		case LOAD_VAR_ID:
			instruction = new LoadInstruction();
			break;
		case STORE_VAR_ID:
			instruction = new StoreInstruction();
			break;
		}

		instruction.readData(dataInput);
		return instruction;
	}

	public static void writeTo(Instruction instruction, DataOutput dataOutput) throws IOException
	{
		if (instruction == null)
		{
			dataOutput.writeByte(0);
			return;
		}

		dataOutput.writeByte(instruction.getID());
		instruction.writeData(dataOutput);
	}
}
