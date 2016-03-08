package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Instruction
{
	byte getID();

	void writeData(DataOutput dataOutput) throws IOException;

	void readData(DataInput dataInput) throws IOException;

	Material getMaterial();

	String getDescription();

	void execute(ExecutionState state) throws ExecutionException;
}
