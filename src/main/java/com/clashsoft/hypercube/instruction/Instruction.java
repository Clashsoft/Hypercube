package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;

public interface Instruction
{
	void execute(ExecutionState state) throws ExecutionException;
}
