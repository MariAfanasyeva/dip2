package deserializator.user;

public class UserResponse {
    private String success;
    private UserCreds user;
    private String accessToken;
    private String refreshToken;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public UserCreds getUser() {
        return user;
    }

    public void setUser(UserCreds user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

