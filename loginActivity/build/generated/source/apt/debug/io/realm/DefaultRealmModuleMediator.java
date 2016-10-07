package io.realm;


import android.util.JsonReader;
import io.realm.RealmObjectSchema;
import io.realm.internal.ColumnInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>();
        modelClasses.add(com.anubis.flickr.models.Tag.class);
        modelClasses.add(com.anubis.flickr.models.Interesting.class);
        modelClasses.add(com.anubis.flickr.models.Hottags_.class);
        modelClasses.add(com.anubis.flickr.models.Comment.class);
        modelClasses.add(com.anubis.flickr.models.Photo.class);
        modelClasses.add(com.anubis.flickr.models.Comments_.class);
        modelClasses.add(com.anubis.flickr.models.User_.class);
        modelClasses.add(com.anubis.flickr.models.Friends.class);
        modelClasses.add(com.anubis.flickr.models.Username.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Table createTable(Class<? extends RealmModel> clazz, SharedRealm sharedRealm) {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return io.realm.TagRealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return io.realm.InterestingRealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return io.realm.Hottags_RealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return io.realm.CommentRealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return io.realm.PhotoRealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return io.realm.Comments_RealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return io.realm.User_RealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return io.realm.FriendsRealmProxy.initTable(sharedRealm);
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return io.realm.UsernameRealmProxy.initTable(sharedRealm);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public RealmObjectSchema createRealmObjectSchema(Class<? extends RealmModel> clazz, RealmSchema realmSchema) {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return io.realm.TagRealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return io.realm.InterestingRealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return io.realm.Hottags_RealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return io.realm.CommentRealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return io.realm.PhotoRealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return io.realm.Comments_RealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return io.realm.User_RealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return io.realm.FriendsRealmProxy.createRealmObjectSchema(realmSchema);
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return io.realm.UsernameRealmProxy.createRealmObjectSchema(realmSchema);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public ColumnInfo validateTable(Class<? extends RealmModel> clazz, SharedRealm sharedRealm, boolean allowExtraColumns) {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return io.realm.TagRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return io.realm.InterestingRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return io.realm.Hottags_RealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return io.realm.CommentRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return io.realm.PhotoRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return io.realm.Comments_RealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return io.realm.User_RealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return io.realm.FriendsRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return io.realm.UsernameRealmProxy.validateTable(sharedRealm, allowExtraColumns);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public List<String> getFieldNames(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return io.realm.TagRealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return io.realm.InterestingRealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return io.realm.Hottags_RealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return io.realm.CommentRealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return io.realm.PhotoRealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return io.realm.Comments_RealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return io.realm.User_RealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return io.realm.FriendsRealmProxy.getFieldNames();
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return io.realm.UsernameRealmProxy.getFieldNames();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public String getTableName(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return io.realm.TagRealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return io.realm.InterestingRealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return io.realm.Hottags_RealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return io.realm.CommentRealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return io.realm.PhotoRealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return io.realm.Comments_RealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return io.realm.User_RealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return io.realm.FriendsRealmProxy.getTableName();
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return io.realm.UsernameRealmProxy.getTableName();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
                return clazz.cast(new io.realm.TagRealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
                return clazz.cast(new io.realm.InterestingRealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
                return clazz.cast(new io.realm.Hottags_RealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
                return clazz.cast(new io.realm.CommentRealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
                return clazz.cast(new io.realm.PhotoRealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
                return clazz.cast(new io.realm.Comments_RealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
                return clazz.cast(new io.realm.User_RealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
                return clazz.cast(new io.realm.FriendsRealmProxy());
            } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
                return clazz.cast(new io.realm.UsernameRealmProxy());
            } else {
                throw getMissingProxyClassException(clazz);
            }
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return clazz.cast(io.realm.TagRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Tag) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return clazz.cast(io.realm.InterestingRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Interesting) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return clazz.cast(io.realm.Hottags_RealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Hottags_) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Comment) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return clazz.cast(io.realm.PhotoRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Photo) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return clazz.cast(io.realm.Comments_RealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Comments_) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return clazz.cast(io.realm.User_RealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.User_) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return clazz.cast(io.realm.FriendsRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Friends) obj, update, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return clazz.cast(io.realm.UsernameRealmProxy.copyOrUpdate(realm, (com.anubis.flickr.models.Username) obj, update, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            io.realm.TagRealmProxy.insert(realm, (com.anubis.flickr.models.Tag) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            io.realm.InterestingRealmProxy.insert(realm, (com.anubis.flickr.models.Interesting) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            io.realm.Hottags_RealmProxy.insert(realm, (com.anubis.flickr.models.Hottags_) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            io.realm.CommentRealmProxy.insert(realm, (com.anubis.flickr.models.Comment) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            io.realm.PhotoRealmProxy.insert(realm, (com.anubis.flickr.models.Photo) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            io.realm.Comments_RealmProxy.insert(realm, (com.anubis.flickr.models.Comments_) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            io.realm.User_RealmProxy.insert(realm, (com.anubis.flickr.models.User_) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            io.realm.FriendsRealmProxy.insert(realm, (com.anubis.flickr.models.Friends) object, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            io.realm.UsernameRealmProxy.insert(realm, (com.anubis.flickr.models.Username) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new IdentityHashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
                io.realm.TagRealmProxy.insert(realm, (com.anubis.flickr.models.Tag) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
                io.realm.InterestingRealmProxy.insert(realm, (com.anubis.flickr.models.Interesting) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
                io.realm.Hottags_RealmProxy.insert(realm, (com.anubis.flickr.models.Hottags_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
                io.realm.CommentRealmProxy.insert(realm, (com.anubis.flickr.models.Comment) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
                io.realm.PhotoRealmProxy.insert(realm, (com.anubis.flickr.models.Photo) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
                io.realm.Comments_RealmProxy.insert(realm, (com.anubis.flickr.models.Comments_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
                io.realm.User_RealmProxy.insert(realm, (com.anubis.flickr.models.User_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
                io.realm.FriendsRealmProxy.insert(realm, (com.anubis.flickr.models.Friends) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
                io.realm.UsernameRealmProxy.insert(realm, (com.anubis.flickr.models.Username) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
                    io.realm.TagRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
                    io.realm.InterestingRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
                    io.realm.Hottags_RealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
                    io.realm.CommentRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
                    io.realm.PhotoRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
                    io.realm.Comments_RealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
                    io.realm.User_RealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
                    io.realm.FriendsRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
                    io.realm.UsernameRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            io.realm.TagRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Tag) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            io.realm.InterestingRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Interesting) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            io.realm.Hottags_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Hottags_) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            io.realm.CommentRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Comment) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            io.realm.PhotoRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Photo) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            io.realm.Comments_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Comments_) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            io.realm.User_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.User_) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            io.realm.FriendsRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Friends) obj, cache);
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            io.realm.UsernameRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Username) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new IdentityHashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
                io.realm.TagRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Tag) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
                io.realm.InterestingRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Interesting) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
                io.realm.Hottags_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Hottags_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
                io.realm.CommentRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Comment) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
                io.realm.PhotoRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Photo) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
                io.realm.Comments_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Comments_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
                io.realm.User_RealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.User_) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
                io.realm.FriendsRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Friends) object, cache);
            } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
                io.realm.UsernameRealmProxy.insertOrUpdate(realm, (com.anubis.flickr.models.Username) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
                    io.realm.TagRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
                    io.realm.InterestingRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
                    io.realm.Hottags_RealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
                    io.realm.CommentRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
                    io.realm.PhotoRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
                    io.realm.Comments_RealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
                    io.realm.User_RealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
                    io.realm.FriendsRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
                    io.realm.UsernameRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return clazz.cast(io.realm.TagRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return clazz.cast(io.realm.InterestingRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return clazz.cast(io.realm.Hottags_RealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return clazz.cast(io.realm.PhotoRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return clazz.cast(io.realm.Comments_RealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return clazz.cast(io.realm.User_RealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return clazz.cast(io.realm.FriendsRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return clazz.cast(io.realm.UsernameRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return clazz.cast(io.realm.TagRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return clazz.cast(io.realm.InterestingRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return clazz.cast(io.realm.Hottags_RealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return clazz.cast(io.realm.PhotoRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return clazz.cast(io.realm.Comments_RealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return clazz.cast(io.realm.User_RealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return clazz.cast(io.realm.FriendsRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return clazz.cast(io.realm.UsernameRealmProxy.createUsingJsonStream(realm, reader));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(com.anubis.flickr.models.Tag.class)) {
            return clazz.cast(io.realm.TagRealmProxy.createDetachedCopy((com.anubis.flickr.models.Tag) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Interesting.class)) {
            return clazz.cast(io.realm.InterestingRealmProxy.createDetachedCopy((com.anubis.flickr.models.Interesting) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Hottags_.class)) {
            return clazz.cast(io.realm.Hottags_RealmProxy.createDetachedCopy((com.anubis.flickr.models.Hottags_) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Comment.class)) {
            return clazz.cast(io.realm.CommentRealmProxy.createDetachedCopy((com.anubis.flickr.models.Comment) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Photo.class)) {
            return clazz.cast(io.realm.PhotoRealmProxy.createDetachedCopy((com.anubis.flickr.models.Photo) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Comments_.class)) {
            return clazz.cast(io.realm.Comments_RealmProxy.createDetachedCopy((com.anubis.flickr.models.Comments_) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.User_.class)) {
            return clazz.cast(io.realm.User_RealmProxy.createDetachedCopy((com.anubis.flickr.models.User_) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Friends.class)) {
            return clazz.cast(io.realm.FriendsRealmProxy.createDetachedCopy((com.anubis.flickr.models.Friends) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(com.anubis.flickr.models.Username.class)) {
            return clazz.cast(io.realm.UsernameRealmProxy.createDetachedCopy((com.anubis.flickr.models.Username) realmObject, 0, maxDepth, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

}
