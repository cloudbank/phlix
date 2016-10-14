package io.realm;


public interface FriendsRealmProxyInterface {
    public java.util.Date realmGet$timestamp();
    public void realmSet$timestamp(java.util.Date value);
    public String realmGet$user();
    public void realmSet$user(String value);
    public RealmList<com.anubis.flickr.models.Photo> realmGet$friends();
    public void realmSet$friends(RealmList<com.anubis.flickr.models.Photo> value);
}
