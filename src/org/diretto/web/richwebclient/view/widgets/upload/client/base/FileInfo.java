package org.diretto.web.richwebclient.view.widgets.upload.client.base;

/**
 * The {@code FileInfo} class encapsulates the basic information of a file,
 * which is necessary for the uploading process. <br/><br/>
 * 
 * <i>Annotation:</i> It is an immutable class.
 * 
 * @author Tobias Schlecht
 */
public final class FileInfo
{
	private final String name;
	private final String type;
	private final long size;

	/**
	 * Creates a {@link FileInfo} object.
	 * 
	 * @param name The name of the file
	 * @param type The type of the file
	 * @param size The size of the file in {@code Bytes}
	 */
	public FileInfo(String name, String type, long size)
	{
		this.name = name;
		this.type = type;
		this.size = size;

		if(name == null || type == null)
		{
			throw new NullPointerException();
		}

		if(name.equals("") || size < 0)
		{
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns the name of the file.
	 * 
	 * @return The name of the file
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the type of the file.
	 * 
	 * @return The type of the file
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Returns the size of the file in {@code Bytes}.
	 * 
	 * @return The size of the file in {@code Bytes}
	 */
	public long getSize()
	{
		return size;
	}

	@Override
	public String toString()
	{
		return name + ";;" + type + ";;" + size;
	}

	/**
	 * This method serves as counterpart to the {@link #toString()} method.
	 * 
	 * @param value A {@code String} representation of a {@code FileInfo} object
	 * @return A {@code FileInfo} object for the given {@code String}
	 *         representation
	 */
	public static FileInfo fromString(String value)
	{
		String[] parts = value.split(";;");

		return new FileInfo(parts[0], parts[1], Long.parseLong(parts[2]));
	}

	@Override
	public boolean equals(Object object)
	{
		return toString().equals(object.toString());
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}
