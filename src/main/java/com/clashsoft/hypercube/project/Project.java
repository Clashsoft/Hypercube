package com.clashsoft.hypercube.project;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.state.Position;

import java.io.*;

public class Project
{
	private final String name;

	private final HypercubeIDE ide;
	private final Grid         grid;

	private Position selectedPosition = new Position(0, 0, 0, 0);

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

	public Position getSelectedPosition()
	{
		return this.selectedPosition;
	}

	public void setSelectedPosition(Position selectedPosition)
	{
		this.selectedPosition = selectedPosition;
	}

	public boolean readFrom(File file)
	{
		try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file))))
		{
			this.grid.readFrom(dataInputStream);

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
			this.grid.writeTo(dataOutputStream);
			return true;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
