package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LoadInstruction implements Instruction
{
	private static final Material MATERIAL = Instructions.textured("load");

	public String varName;

	public LoadInstruction(String varName)
	{
		this.varName = varName;
	}

	public LoadInstruction()
	{
	}

	@Override
	public byte getID()
	{
		return Instructions.LOAD_VAR_ID;
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
		return "Load '" + this.varName + '\'';
	}

	@Override
	public void execute(ExecutionState state) throws ExecutionException
	{
		state.push(state.load(this.varName));
	}
}
