package org.diretto.web.richwebclient.view.base.client;

/**
 * This enum represents a specially tailored set of {@code MediaMainType}s.
 * 
 * @author Tobias Schlecht
 */
public enum MediaType
{
	/**
	 * This type represents the {@link MediaType} {@code image}.
	 */
	IMAGE("Image"),

	/**
	 * This type represents the {@link MediaType} {@code video}.
	 */
	VIDEO("Video"),

	/**
	 * This type represents the {@link MediaType} {@code audio}.
	 */
	AUDIO("Audio"),

	/**
	 * This type represents the {@link MediaType} {@code text}.
	 */
	TEXT("Text"),

	/**
	 * This type represents all <i>other</i> {@link MediaType}s.
	 */
	OTHER("Other");

	private final String name;

	/**
	 * Constructs a {@link MediaType}.
	 * 
	 * @param name The name
	 */
	MediaType(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the corresponding {@link MediaType} for the given name.
	 * 
	 * @param name The name
	 * @return The corresponding {@code MediaType}
	 */
	public static MediaType getValue(String name)
	{
		if(name.equals(IMAGE.name))
		{
			return IMAGE;
		}
		else if(name.equals(VIDEO.name))
		{
			return VIDEO;
		}
		else if(name.equals(AUDIO.name))
		{
			return AUDIO;
		}
		else if(name.equals(TEXT.name))
		{
			return TEXT;
		}
		else
		{
			return OTHER;
		}
	}
}
