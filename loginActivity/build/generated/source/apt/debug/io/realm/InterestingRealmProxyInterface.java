package io.realm;


public interface InterestingRealmProxyInterface {
    public java.util.Date realmGet$timestamp();
    public void realmSet$timestamp(java.util.Date value);
    public RealmList<com.anubis.flickr.models.Photo> realmGet$interestingPhotos();
    public void realmSet$interestingPhotos(RealmList<com.anubis.flickr.models.Photo> value);
}
