package io.realm;


public interface UserModelRealmProxyInterface {
    public String realmGet$userId();
    public void realmSet$userId(String value);
    public String realmGet$name();
    public void realmSet$name(String value);
    public java.util.Date realmGet$timestamp();
    public void realmSet$timestamp(java.util.Date value);
    public RealmList<com.anubis.flickr.models.Photo> realmGet$friendsList();
    public void realmSet$friendsList(RealmList<com.anubis.flickr.models.Photo> value);
    public RealmList<com.anubis.flickr.models.Tag> realmGet$tagsList();
    public void realmSet$tagsList(RealmList<com.anubis.flickr.models.Tag> value);
}
