package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PushInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("push");

	private static final byte KIND_NULL    = 0;
	private static final byte KIND_STRING  = 1;
	private static final byte KIND_NUMBER  = 2;
	private static final byte KIND_INTEGER = 3;

	private byte   kind;
	private Object value;

	public PushInstruction()
	{

	}

	public PushInstruction(String value)
	{
		this.value = value;
		this.kind = KIND_STRING;
	}

	public PushInstruction(double value)
	{
		this.value = value;
		this.kind = KIND_NUMBER;
	}

	public PushInstruction(int value)
	{
		this.value = value;
		this.kind = KIND_INTEGER;
	}

	@Override
	public byte getID()
	{
		return Instructions.PUSH_ID;
	}

	@Override
	public void writeData(DataOutput dataOutput) throws IOException
	{
		dataOutput.writeByte(this.kind);

		switch (this.kind)
		{
		case KIND_NUMBER:
			dataOutput.writeDouble((double) this.value);
			return;
		case KIND_STRING:
			dataOutput.writeUTF((String) this.value);
			return;
		case KIND_INTEGER:
			dataOutput.writeInt((int) this.value);
			return;
		}
	}

	@Override
	public void readData(DataInput dataInput) throws IOException
	{
		this.kind = dataInput.readByte();

		switch (this.kind)
		{
		case KIND_NUMBER:
			this.value = dataInput.readDouble();
			return;
		case KIND_STRING:
			this.value = dataInput.readUTF();
			return;
		case KIND_INTEGER:
			this.value = dataInput.readInt();
			return;
		}
	}

	@Override
	public String getDescription()
	{
		return "Push " + this.value;
	}

	@Override
	public Material getMaterial()
	{
		return MATERIAL;
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.push(this.value);
	}
}
