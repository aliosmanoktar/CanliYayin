package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints;

public class PeriscopeEndpointFactory {
    private static AuthorizationEndpoints authorizationEndpoint;

    private UserEndpoints userEndpoints;

    private BroadcastEndpoints broadcastEndpoints;

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    public PeriscopeEndpointFactory(String tokenType, String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public static AuthorizationEndpoints getAuthorizationEndpoint() {
        if (authorizationEndpoint == null) {
            authorizationEndpoint = new AuthorizationEndpoints();
        }
        return authorizationEndpoint;
    }


    public UserEndpoints getUserEndpoints() {
        if (userEndpoints == null) {
            userEndpoints = new UserEndpoints(tokenType, accessToken);
        }
        return userEndpoints;
    }

    public BroadcastEndpoints getBroadcastEndpoints() {
        if (broadcastEndpoints == null) {
            broadcastEndpoints = new BroadcastEndpoints(tokenType, accessToken);
        }
        return broadcastEndpoints;
    }


}
