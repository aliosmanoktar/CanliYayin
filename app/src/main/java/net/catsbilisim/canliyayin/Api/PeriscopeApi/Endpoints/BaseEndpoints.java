package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints;

public class BaseEndpoints {



    private String tokenType;
    private String accessToken;

    public BaseEndpoints() {

    }

    public BaseEndpoints(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    protected String getAccessToken() {
        return accessToken;
    }

    protected String getTokenType() {
        return tokenType;
    }
}
