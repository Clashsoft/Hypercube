package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StoreInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("store");

	public String varName;

	public StoreInstruction(String varName)
	{
		this.varName = varName;
	}

	public StoreInstruction()
	{
	}

	@Override
	public byte getID()
	{
		return Instructions.STORE_VAR_ID;
	}

	@Override
	public void writeData(DataOutput dataOutput) throws IOException
	{
		dataOutput.writeUTF(this.varName);
	}

	@Override
	public void readData(DataInput dataInput) throws IOException
	{
		this.varName = dataInput.readUTF();
	}

	@Override
	public Material getMaterial()
	{
		return MATERIAL;
	}

	@Override
	public String getDescription()
	{
		return "Store '" + this.varName + '\'';
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.store(this.varName, state.pop());
	}
}
