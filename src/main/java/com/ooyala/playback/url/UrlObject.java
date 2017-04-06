package com.ooyala.playback.url;

public class UrlObject {

	private String url;
    private String videoPlugins;
    private String adFirstPlay;
    private String adFrequency;
    private String streamType;
    private String pcode;
    private String embedCode;
    private String apiKey;
    private String secret;
    public String errorCode;
    private String errorDescription;
    private String channelId;
    public String provider;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdFirstPlay() {
		return adFirstPlay;
	}

	public void setAdFirstPlay(String adFirstPlay) {
		this.adFirstPlay = adFirstPlay;
	}

	public String getAdFrequency() {
		return adFrequency;
	}

	public void setAdFrequency(String adFrequency) {
		this.adFrequency = adFrequency;
	}

	public String getStreamType() {
		return streamType;
	}

	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}

	public String getPCode() {
		return pcode;
	}

	public void setPCode(String pcode) {
		this.pcode = pcode;
	}

	public String getEmbedCode() {
		return embedCode;
	}

	public void setEmbedCode(String embedCode) {
		this.embedCode = embedCode;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getErrorCode() {return errorCode;}

	public void setErrorCode(String errorCode) {this.errorCode = errorCode;}

	public String getErrorDescription() {return errorDescription;}

	public void setErrorDescription(String errorDescription) {this.errorDescription = errorDescription;}

    public void setChannelId(String channelId) {this.channelId = channelId;}

    public String getChannelId() {return channelId;}

    public String getProvider() {return provider;}

    public void setProvider(String provider) {this.provider = provider;}

	public String getVideoPlugins() {return videoPlugins;}

	public void setVideoPlugins(String plugins) {this.videoPlugins = plugins;}
}
