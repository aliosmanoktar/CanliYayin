package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.annotations.SerializedName;

public class BroadcastCreateResponse {
    @SerializedName("broadcast_id")
    public long BroadcastId;
    @SerializedName("upload_url")
    public String UploadUrl;
    @SerializedName("max_time_in_seconds")
    public int MaxTimeInSeconds;
    @SerializedName("speed_test_ui_timeout")
    public int SpeedTestUiTimeout;
    @SerializedName("stream_network_speed_test_payload_chunk_size_in_bytes")
    public int StreamNnetworkSpeedTestPayloadChunkSizeInBytes;
    @SerializedName("stream_network_speed_test_payload_size_in_bytes")
    public int StreamNetworkSpeedTestPayloadSizeInBytes;
    @SerializedName("stream_network_speed_test_payload_timeout_in_seconds")
    public int StreamNetworkSpeedTestPayloadTimeoutInSeconds;
    @SerializedName("speed_test_minimum_bandwidth_threshold")
    public int SpeedTestMinimumBandwidthThreshold;
    @SerializedName("speed_test_retry_max_count")
    public int SpeedTestRetryMaxCount;
    @SerializedName("speed_test_retry_time_delay")
    public float SpeedTestRetryTimeDelay;
    @SerializedName("disable_speed_test")
    public int DisableSpeedTest;
    @SerializedName("stream_video_allow_b_frames")
    public int StreamVideoAllowBFrames;
    @SerializedName("stream_video_width")
    public int StreamVideoWidth;
    @SerializedName("stream_video_bit_rate")
    public int StreamVideoBitRate;
    @SerializedName("stream_video_fps")
    public int StreamVideoFps;
    @SerializedName("stream_audio_bit_rate")
    public int StreamAudioBitRate;
    @SerializedName("stream_audio_sample_rate")
    public int StreamAudioSampleRate;
    @SerializedName("stream_audio_channels")
    public int StreamAudioChannels;
    @SerializedName("heartbeat_interval")
    public int HeartbeatInterval;
    @SerializedName("broadcaster_update_frequency")
    public int BroadcasterUpdateFrequency;
    @SerializedName("stream_video_adaptive_bitrate_config")
    public String StreamVideoAdaptiveBitrateConfig;
    @SerializedName("stream_network_connection_retry_count")
    public int StreamNetworkConnectionRetryCount;
    @SerializedName("stream_network_connection_retry_delay_in_seconds")
    public int StreamNetworkConnectionRetryDelayInSeconds;
    @SerializedName("connect_with_1rtt")
    public int ConnectWith1rtt;
    @SerializedName("allow_resolution_change")
    public int AllowResolutionChange;
    @SerializedName("avc_rtmp_payload")
    public int AvcRtmpPayload;
    @SerializedName("pass_thru_enabled")
    public int PassThruEnabled;
    @SerializedName("live_trace_enabled")
    public int LiveTraceEnabled;
    @SerializedName("live_trace_sample_interval_in_seconds")
    public int LiveTraceSampleIntervalInSeconds;
    @SerializedName("live_trace_sampling_source")
    public int LiveTraceSamplingSource;
    @SerializedName("status")
    public String Status;
}