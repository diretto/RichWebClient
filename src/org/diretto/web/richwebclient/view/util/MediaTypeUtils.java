package org.diretto.web.richwebclient.view.util;

import org.diretto.api.client.base.data.MediaMainType;
import org.diretto.api.client.base.data.MediaTypeFactory;
import org.diretto.web.richwebclient.view.base.client.MediaType;

/**
 * {@code MediaTypeUtils} is a noninstantiable utility class and is responsible
 * for all {@link MediaType} related aspects.
 * 
 * @author Tobias Schlecht
 */
public final class MediaTypeUtils
{
	/**
	 * The constructor is {@code private} to suppress the default constructor
	 * for noninstantiability.
	 */
	private MediaTypeUtils()
	{
		throw new AssertionError();
	}

	/**
	 * Returns the corresponding {@link MediaMainType} for the given
	 * {@link MediaType} or {@code null} if there is no {@code MediaMainType}
	 * for the given {@code MediaType}.
	 * 
	 * @param mediaType A {@code MediaType}
	 * @return The corresponding {@code MediaMainType}
	 */
	public static MediaMainType getMediaMainType(MediaType mediaType)
	{
		if(mediaType == MediaType.IMAGE)
		{
			return MediaTypeFactory.getMediaMainType("image");
		}
		else if(mediaType == MediaType.VIDEO)
		{
			return MediaTypeFactory.getMediaMainType("video");
		}
		else if(mediaType == MediaType.AUDIO)
		{
			return MediaTypeFactory.getMediaMainType("audio");
		}
		else if(mediaType == MediaType.TEXT)
		{
			return MediaTypeFactory.getMediaMainType("text");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the corresponding {@link MediaType} for the given
	 * {@link MediaMainType}.
	 * 
	 * @param mediaMainType A {@code MediaMainType}
	 * @return The corresponding {@code MediaType}
	 */
	public static MediaType getMediaType(MediaMainType mediaMainType)
	{
		if(mediaMainType.TYPE.equals("image"))
		{
			return MediaType.IMAGE;
		}
		else if(mediaMainType.TYPE.equals("video"))
		{
			return MediaType.VIDEO;
		}
		else if(mediaMainType.TYPE.equals("audio"))
		{
			return MediaType.AUDIO;
		}
		else if(mediaMainType.TYPE.equals("text"))
		{
			return MediaType.TEXT;
		}
		else
		{
			return MediaType.OTHER;
		}
	}
}
