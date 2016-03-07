package com.clashsoft.hypercube.instruction;

import com.clashsoft.hypercube.util.TextureLoader;
import javafx.scene.paint.Material;

public final class Instructions
{
	public static Instruction ADD      = new NumberInstruction("Add", textured("add"),
	                                                           (n1, n2) -> n1.doubleValue() + n2.doubleValue());
	public static Instruction SUBTRACT = new NumberInstruction("Subtract", textured("subtract"),
	                                                           (n1, n2) -> n1.doubleValue() - n2.doubleValue());
	public static Instruction MULTIPLY = new NumberInstruction("Multiply", textured("multiply"),
	                                                           (n1, n2) -> n1.doubleValue() * n2.doubleValue());
	public static Instruction DIVIDE   = new NumberInstruction("Divide", textured("divide"),
	                                                           (n1, n2) -> n1.doubleValue() / n2.doubleValue());

	public static Instruction OUTPUT = new OutputInstruction();

	protected static Material textured(String name)
	{
		return TextureLoader.material("instructions", name);
	}
}
