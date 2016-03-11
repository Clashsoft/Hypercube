package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.util.I18n;
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
	private static final byte MODULO_ID   = 5;
	private static final byte COMPARE_ID  = 8;

	private static final   byte POP_ID  = 12;
	private static final   byte DUP_ID  = 13;
	protected static final byte PUSH_ID = 14;

	private static final byte OUTPUT_ID       = 16;
	private static final byte INPUT_TEXT_ID   = 17;
	private static final byte INPUT_NUMBER_ID = 18;

	protected static final byte LOAD_VAR_ID   = 24;
	protected static final byte STORE_VAR_ID  = 25;
	protected static final byte DELETE_VAR_ID = 26;

	private static final byte UP_ID       = 32;
	private static final byte DOWN_ID     = 33;
	private static final byte RIGHT_ID    = 34;
	private static final byte LEFT_ID     = 35;
	private static final byte FORWARD_ID  = 36;
	private static final byte BACKWARD_ID = 37;

	public static Instruction ADD      = new NumberInstruction(ADD_ID, "instruction.add", textured("add"),
	                                                           (n1, n2) -> n1.doubleValue() + n2.doubleValue());
	public static Instruction SUBTRACT = new NumberInstruction(SUBTRACT_ID, "instruction.subtract", textured("subtract"),
	                                                           (n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction(MULTIPLY_ID, "instruction.multiply", textured("multiply"),
	                                                           (n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE   = new NumberInstruction(DIVIDE_ID, "instruction.divide", textured("divide"),
	                                                           (n1, n2) -> n1.doubleValue() / n2.doubleValue());
	public static Instruction MODULO   = new NumberInstruction(MODULO_ID, "instruction.modulo", textured("modulo"),
	                                                           (n1, n2) -> n1.doubleValue() % n2.doubleValue());

	public static Instruction OUTPUT = new BaseInstruction(OUTPUT_ID, "instruction.output", textured("print"), //
	                                                       executionState -> executionState.print(
		                                                       String.valueOf(executionState.pop())));

	public static Instruction INPUT_TEXT;

	public static Instruction INPUT_NUMBER;

	static
	{
		INPUT_TEXT = new BaseInstruction(INPUT_TEXT_ID, "instruction.input.text", textured("read_text"), executionState -> {
			final String input = executionState.readInput(I18n.getString("instruction.input.text"));
			if (input == null)
			{
				throw new ExecutionException("Text Input Cancelled");
			}

			executionState.push(input);
		});

		INPUT_NUMBER = new BaseInstruction(INPUT_NUMBER_ID, "instruction.input.number", textured("read_num"), executionState -> {
			final String input = executionState.readInput(I18n.getString("instruction.input.number"));
			if (input == null)
			{
				throw new ExecutionException("Number Input Cancelled");
			}

			try
			{
				final double inputNumber = Double.parseDouble(input);
				executionState.push(inputNumber);
			}
			catch (NumberFormatException nfe)
			{
				throw new ExecutionException("Invalid Number Input", nfe);
			}
		});
	}

	public static Instruction POP = new BaseInstruction(POP_ID, "instruction.pop", textured("pop"), ExecutionState::pop);

	public static Instruction DUP = new BaseInstruction(DUP_ID, "instruction.dup", textured("dup"), executionState -> {
		Object top = executionState.pop();
		executionState.push(top);
		executionState.push(top);
	});

	public static Instruction UP       = new DirectionInstruction(UP_ID, "instruction.up", textured("up"), Direction.UP);
	public static Instruction DOWN     = new DirectionInstruction(DOWN_ID, "instruction.down", textured("down"), Direction.DOWN);
	public static Instruction RIGHT    = new DirectionInstruction(RIGHT_ID, "instruction.east", textured("east"),
	                                                              Direction.EAST);
	public static Instruction LEFT     = new DirectionInstruction(LEFT_ID, "instruction.west", textured("west"), Direction.WEST);
	public static Instruction FORWARD  = new DirectionInstruction(FORWARD_ID, "instruction.north", textured("north"),
	                                                              Direction.NORTH);
	public static Instruction BACKWARD = new DirectionInstruction(BACKWARD_ID, "instruction.south", textured("south"),
	                                                              Direction.SOUTH);

	public static Instruction COMPARE = new BaseInstruction(COMPARE_ID, "instruction.compare", textured("cmp"), executionState -> {
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
		case MODULO_ID:
			return MODULO;
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
		case INPUT_NUMBER_ID:
			return INPUT_NUMBER;
		case INPUT_TEXT_ID:
			return INPUT_TEXT;
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
