package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.util.I18n;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.BiFunction;

public final class NumberInstruction implements Instruction
{
	private final byte                               id;
	private final String                             description;
	private final Material                           material;
	private final BiFunction<Number, Number, Number> mapper;

	public NumberInstruction(byte id, String desc, Material material, BiFunction<Number, Number, Number> mapper)
	{
		this.id = id;
		this.description = desc;
		this.material = material;
		this.mapper = mapper;
	}

	@Override
	public byte getID()
	{
		return this.id;
	}

	@Override
	public void writeData(DataOutput dataOutput) throws IOException
	{
	}

	@Override
	public void readData(DataInput dataInput) throws IOException
	{
	}

	@Override
	public String getDescription()
	{
		return I18n.getString(this.description);
	}

	@Override
	public Material getMaterial()
	{
		return this.material;
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		final Object right = state.pop();
		final Object left = state.pop();

		if (left instanceof Number && right instanceof Number)
		{
			state.push(this.mapper.apply((Number) left, (Number) right));
			return;
		}

		throw new ExecutionException("Cannot perform numeric operation");
	}
}
