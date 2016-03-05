package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import javafx.scene.paint.Material;

public interface Instruction
{
	Material getMaterial();

	void execute(ExecutionState state) throws ExecutionException;
}
