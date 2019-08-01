package net.catsbilisim.canliyayin.Preferences;

import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.InstaLoginResponse;

public interface IAddInstagram {
    void Succes(boolean ok, InstaLoginResponse login);
}
