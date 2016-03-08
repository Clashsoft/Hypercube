package com.clashsoft.hypercube.project;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.state.Position;

import java.io.*;

public class Project
{
	public static final byte FILE_VERSION = 1;

	private final String name;

	private final HypercubeIDE ide;
	private final Grid         grid;

	private Position selectedPosition = new Position(0, 0, 0, 0);
	private Position executionStartPosition = new Position(0, 0, 0, 0);

	public Project(HypercubeIDE ide, String name)
	{
		this.ide = ide;
		this.name = name;

		this.grid = new Grid(this);
		this.grid.createElement(this.selectedPosition);
	}

	public String getName()
	{
		return this.name;
	}

	public Grid getGrid()
	{
		return this.grid;
	}

	public HypercubeIDE getIDE()
	{
		return this.ide;
	}

	public Position getSelectedPosition()
	{
		return this.selectedPosition;
	}

	public void setSelectedPosition(Position selectedPosition)
	{
		this.selectedPosition = selectedPosition;
	}

	public Position getExecutionStartPosition()
	{
		return this.executionStartPosition;
	}

	public void setExecutionStartPosition(Position executionStartPosition)
	{
		this.executionStartPosition = executionStartPosition;
	}

	public boolean readFrom(File file)
	{
		try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file))))
		{
			this.readFrom(dataInputStream);

			this.ide.loadProject(this);
			return true;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public boolean writeTo(File file)
	{
		try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file))))
		{
			this.writeTo(dataOutputStream);
			return true;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	private void readFrom(DataInput dataInput) throws IOException
	{
		dataInput.readShort(); // File Version

		this.grid.readFrom(dataInput);

		this.selectedPosition = Position.readFrom(dataInput);
		this.executionStartPosition = Position.readFrom(dataInput);
	}

	private void writeTo(DataOutput dataOutput) throws IOException
	{
		dataOutput.writeShort(FILE_VERSION);

		this.grid.writeTo(dataOutput);

		this.selectedPosition.writeTo(dataOutput);
		this.executionStartPosition.writeTo(dataOutput);
	}
}
