package org.diretto.web.richwebclient.view.widgets.googlemap.client.markers;

import org.diretto.web.richwebclient.view.base.client.MediaType;
import com.google.gwt.maps.client.HasMap;

/**
 * This class represents a <i>regular</i> {@link Marker}.
 * 
 * @author Tobias Schlecht
 */
public class RegularMarker extends DocumentMarker
{
	private RegularMarkerData regularMarkerData;

	/**
	 * Constructs a {@code RegularMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 * @param regularMarkerData The corresponding {@code RegularMarkerData}
	 *        object
	 */
	public RegularMarker(HasMap map, double latitude, double longitude, MediaType mediaType, RegularMarkerData regularMarkerData)
	{
		this(map, latitude, longitude, mediaType, Action.REGULAR, regularMarkerData);
	}

	/**
	 * Constructs a {@code RegularMarker}.
	 * 
	 * @param map The corresponding map
	 * @param latitude The {@code Marker} latitude in degrees
	 * @param longitude The {@code Marker} longitude in degrees
	 * @param mediaType The {@code MediaType}
	 * @param action The {@code Action} type
	 * @param regularMarkerData The corresponding {@code RegularMarkerData}
	 *        object
	 */
	public RegularMarker(HasMap map, double latitude, double longitude, MediaType mediaType, Action action, RegularMarkerData regularMarkerData)
	{
		super(map, latitude, longitude, mediaType, Colored.COLORED, action);

		this.regularMarkerData = regularMarkerData;
	}

	/**
	 * Returns the corresponding {@link RegularMarkerData} object.
	 * 
	 * @return The corresponding {@code RegularMarkerData} object
	 */
	public RegularMarkerData getRegularMarkerData()
	{
		return regularMarkerData;
	}

	/**
	 * Sets the given {@link RegularMarkerData} object.
	 * 
	 * @param regularMarkerData A {@code RegularMarkerData} object
	 */
	public void setRegularMarkerData(RegularMarkerData regularMarkerData)
	{
		this.regularMarkerData = regularMarkerData;
	}

	/**
	 * This <i>static member</i> class encapsulates the necessary data
	 * corresponding to a {@link RegularMarker}.
	 */
	public static class RegularMarkerData
	{
		private final String documentID;
		private final String title;
		private final String publisher;
		private final long averageTime;
		private final int votesUP;
		private final int votesDOWN;
		private final String thumbnailURL;

		/**
		 * Constructs a {@link RegularMarkerData} object.
		 * 
		 * @param documentID The {@code DocumentID} ({@code String}
		 *        representation)
		 * @param title The title
		 * @param publisher The {@code UserID} of the publisher ({@code String}
		 *        representation)
		 * @param averageTime The average {@code DateTime}
		 * @param votesUP The value of the {@code UP} votes
		 * @param votesDOWN The value of the {@code DOWN} votes
		 * @param thumbnailURL The {@code URL} of the thumbnail ({@code String}
		 *        representation)
		 */
		public RegularMarkerData(String documentID, String title, String publisher, long averageTime, int votesUP, int votesDOWN, String thumbnailURL)
		{
			this.documentID = documentID;
			this.title = title;
			this.publisher = publisher;
			this.averageTime = averageTime;
			this.votesUP = votesUP;
			this.votesDOWN = votesDOWN;
			this.thumbnailURL = thumbnailURL;
		}

		/**
		 * Returns the {@code DocumentID} in {@code String} representation.
		 * 
		 * @return The {@code DocumentID} ({@code String} representation)
		 */
		public String getDocumentID()
		{
			return documentID;
		}

		/**
		 * Returns the title.
		 * 
		 * @return The title
		 */
		public String getTitle()
		{
			return title;
		}

		/**
		 * Returns the {@code UserID} of the publisher in {@code String}
		 * representation.
		 * 
		 * @return The {@code UserID} of the publisher ({@code String}
		 *         representation)
		 */
		public String getPublisher()
		{
			return publisher;
		}

		/**
		 * Returns the average {@code DateTime}.
		 * 
		 * @return The average {@code DateTime}
		 */
		public long getAverageTime()
		{
			return averageTime;
		}

		/**
		 * Returns the value of the {@code UP} votes.
		 * 
		 * @return The value of the {@code UP} votes
		 */
		public int getVotesUP()
		{
			return votesUP;
		}

		/**
		 * Returns the value of the {@code DOWN} votes.
		 * 
		 * @return The value of the {@code DOWN} votes
		 */
		public int getVotesDOWN()
		{
			return votesDOWN;
		}

		/**
		 * Returns the {@code URL} of the thumbnail in {@code String}
		 * representation.
		 * 
		 * @return The {@code URL} of the thumbnail ({@code String}
		 *         representation)
		 */
		public String getThumbnailURL()
		{
			return thumbnailURL;
		}
	}
}
