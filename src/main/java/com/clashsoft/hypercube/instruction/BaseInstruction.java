package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Consumer;

public class BaseInstruction implements Instruction
{
	private final byte                     id;
	private final String                   description;
	private final Material                 material;
	private final Consumer<ExecutionState> consumer;

	public BaseInstruction(byte id, String desc, Material material, Consumer<ExecutionState> consumer)
	{
		this.id = id;
		this.description = desc;
		this.material = material;
		this.consumer = consumer;
	}

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
