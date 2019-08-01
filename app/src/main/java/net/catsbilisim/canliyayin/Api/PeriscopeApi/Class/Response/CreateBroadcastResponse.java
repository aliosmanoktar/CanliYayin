package net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response;

import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Type.Broadcast;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Type.Encoder;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Type.VideoAccess;


public class CreateBroadcastResponse {

	/**
	 * Broadcast object
	 */
	public Broadcast broadcast;

	/**
	 * Information for streaming the video
	 */
	public VideoAccess video_access;

	/**
	 * Public URL for watching this broadcast
	 */
	public String share_url;

	/**
	 * Encoder object representing the streaming configuration for this device / user
	 */
	public Encoder encoder;

}
