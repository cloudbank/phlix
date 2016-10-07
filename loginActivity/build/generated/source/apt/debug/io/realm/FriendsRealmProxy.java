package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.SharedRealm;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsRealmProxy extends com.anubis.flickr.models.Friends
    implements RealmObjectProxy, FriendsRealmProxyInterface {

    static final class FriendsColumnInfo extends ColumnInfo
        implements Cloneable {

        public long timestampIndex;
        public long friendsIndex;

        FriendsColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(2);
            this.timestampIndex = getValidColumnIndex(path, table, "Friends", "timestamp");
            indicesMap.put("timestamp", this.timestampIndex);
            this.friendsIndex = getValidColumnIndex(path, table, "Friends", "friends");
            indicesMap.put("friends", this.friendsIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final FriendsColumnInfo otherInfo = (FriendsColumnInfo) other;
            this.timestampIndex = otherInfo.timestampIndex;
            this.friendsIndex = otherInfo.friendsIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final FriendsColumnInfo clone() {
            return (FriendsColumnInfo) super.clone();
        }

    }
    private FriendsColumnInfo columnInfo;
    private ProxyState proxyState;
    private RealmList<com.anubis.flickr.models.Photo> friendsRealmList;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("timestamp");
        fieldNames.add("friends");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    FriendsRealmProxy() {
        if (proxyState == null) {
            injectObjectContext();
        }
        proxyState.setConstructionFinished();
    }

    private void injectObjectContext() {
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (FriendsColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState(com.anubis.flickr.models.Friends.class, this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @SuppressWarnings("cast")
    public Date realmGet$timestamp() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.timestampIndex)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.timestampIndex);
    }

    public void realmSet$timestamp(Date value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.timestampIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setDate(columnInfo.timestampIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.timestampIndex);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.timestampIndex, value);
    }

    public RealmList<com.anubis.flickr.models.Photo> realmGet$friends() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (friendsRealmList != null) {
            return friendsRealmList;
        } else {
            LinkView linkView = proxyState.getRow$realm().getLinkList(columnInfo.friendsIndex);
            friendsRealmList = new RealmList<com.anubis.flickr.models.Photo>(com.anubis.flickr.models.Photo.class, linkView, proxyState.getRealm$realm());
            return friendsRealmList;
        }
    }

    public void realmSet$friends(RealmList<com.anubis.flickr.models.Photo> value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("friends")) {
                return;
            }
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.anubis.flickr.models.Photo> original = value;
                value = new RealmList<com.anubis.flickr.models.Photo>();
                for (com.anubis.flickr.models.Photo item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        LinkView links = proxyState.getRow$realm().getLinkList(columnInfo.friendsIndex);
        links.clear();
        if (value == null) {
            return;
        }
        for (RealmModel linkedObject : (RealmList<? extends RealmModel>) value) {
            if (!(RealmObject.isManaged(linkedObject) && RealmObject.isValid(linkedObject))) {
                throw new IllegalArgumentException("Each element of 'value' must be a valid managed object.");
            }
            if (((RealmObjectProxy)linkedObject).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
                throw new IllegalArgumentException("Each element of 'value' must belong to the same Realm.");
            }
            links.add(((RealmObjectProxy)linkedObject).realmGet$proxyState().getRow$realm().getIndex());
        }
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("Friends")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("Friends");
            realmObjectSchema.add(new Property("timestamp", RealmFieldType.DATE, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            if (!realmSchema.contains("Photo")) {
                PhotoRealmProxy.createRealmObjectSchema(realmSchema);
            }
            realmObjectSchema.add(new Property("friends", RealmFieldType.LIST, realmSchema.get("Photo")));
            return realmObjectSchema;
        }
        return realmSchema.get("Friends");
    }

    public static Table initTable(SharedRealm sharedRealm) {
        if (!sharedRealm.hasTable("class_Friends")) {
            Table table = sharedRealm.getTable("class_Friends");
            table.addColumn(RealmFieldType.DATE, "timestamp", Table.NULLABLE);
            if (!sharedRealm.hasTable("class_Photo")) {
                PhotoRealmProxy.initTable(sharedRealm);
            }
            table.addColumnLink(RealmFieldType.LIST, "friends", sharedRealm.getTable("class_Photo"));
            table.setPrimaryKey("");
            return table;
        }
        return sharedRealm.getTable("class_Friends");
    }

    public static FriendsColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_Friends")) {
            Table table = sharedRealm.getTable("class_Friends");
            final long columnCount = table.getColumnCount();
            if (columnCount != 2) {
                if (columnCount < 2) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 2 but was " + columnCount);
                }
                if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 2 but was %1$d", columnCount);
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 2 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final FriendsColumnInfo columnInfo = new FriendsColumnInfo(sharedRealm.getPath(), table);

            if (!columnTypes.containsKey("timestamp")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'timestamp' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("timestamp") != RealmFieldType.DATE) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Date' for field 'timestamp' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.timestampIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'timestamp' is required. Either set @Required to field 'timestamp' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("friends")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'friends'");
            }
            if (columnTypes.get("friends") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Photo' for field 'friends'");
            }
            if (!sharedRealm.hasTable("class_Photo")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing class 'class_Photo' for field 'friends'");
            }
            Table table_1 = sharedRealm.getTable("class_Photo");
            if (!table.getLinkTarget(columnInfo.friendsIndex).hasSameSchema(table_1)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid RealmList type for field 'friends': '" + table.getLinkTarget(columnInfo.friendsIndex).getName() + "' expected - was '" + table_1.getName() + "'");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'Friends' class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Friends";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.anubis.flickr.models.Friends createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        if (json.has("friends")) {
            excludeFields.add("friends");
        }
        com.anubis.flickr.models.Friends obj = realm.createObjectInternal(com.anubis.flickr.models.Friends.class, true, excludeFields);
        if (json.has("timestamp")) {
            if (json.isNull("timestamp")) {
                ((FriendsRealmProxyInterface) obj).realmSet$timestamp(null);
            } else {
                Object timestamp = json.get("timestamp");
                if (timestamp instanceof String) {
                    ((FriendsRealmProxyInterface) obj).realmSet$timestamp(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((FriendsRealmProxyInterface) obj).realmSet$timestamp(new Date(json.getLong("timestamp")));
                }
            }
        }
        if (json.has("friends")) {
            if (json.isNull("friends")) {
                ((FriendsRealmProxyInterface) obj).realmSet$friends(null);
            } else {
                ((FriendsRealmProxyInterface) obj).realmGet$friends().clear();
                JSONArray array = json.getJSONArray("friends");
                for (int i = 0; i < array.length(); i++) {
                    com.anubis.flickr.models.Photo item = PhotoRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    ((FriendsRealmProxyInterface) obj).realmGet$friends().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.anubis.flickr.models.Friends createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.anubis.flickr.models.Friends obj = new com.anubis.flickr.models.Friends();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("timestamp")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((FriendsRealmProxyInterface) obj).realmSet$timestamp(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((FriendsRealmProxyInterface) obj).realmSet$timestamp(new Date(timestamp));
                    }
                } else {
                    ((FriendsRealmProxyInterface) obj).realmSet$timestamp(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("friends")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((FriendsRealmProxyInterface) obj).realmSet$friends(null);
                } else {
                    ((FriendsRealmProxyInterface) obj).realmSet$friends(new RealmList<com.anubis.flickr.models.Photo>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.anubis.flickr.models.Photo item = PhotoRealmProxy.createUsingJsonStream(realm, reader);
                        ((FriendsRealmProxyInterface) obj).realmGet$friends().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static com.anubis.flickr.models.Friends copyOrUpdate(Realm realm, com.anubis.flickr.models.Friends object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.Friends) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.anubis.flickr.models.Friends copy(Realm realm, com.anubis.flickr.models.Friends newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.Friends) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            com.anubis.flickr.models.Friends realmObject = realm.createObjectInternal(com.anubis.flickr.models.Friends.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((FriendsRealmProxyInterface) realmObject).realmSet$timestamp(((FriendsRealmProxyInterface) newObject).realmGet$timestamp());

            RealmList<com.anubis.flickr.models.Photo> friendsList = ((FriendsRealmProxyInterface) newObject).realmGet$friends();
            if (friendsList != null) {
                RealmList<com.anubis.flickr.models.Photo> friendsRealmList = ((FriendsRealmProxyInterface) realmObject).realmGet$friends();
                for (int i = 0; i < friendsList.size(); i++) {
                    com.anubis.flickr.models.Photo friendsItem = friendsList.get(i);
                    com.anubis.flickr.models.Photo cachefriends = (com.anubis.flickr.models.Photo) cache.get(friendsItem);
                    if (cachefriends != null) {
                        friendsRealmList.add(cachefriends);
                    } else {
                        friendsRealmList.add(PhotoRealmProxy.copyOrUpdate(realm, friendsList.get(i), update, cache));
                    }
                }
            }

            return realmObject;
        }
    }

    public static long insert(Realm realm, com.anubis.flickr.models.Friends object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.Friends.class);
        long tableNativePtr = table.getNativeTablePointer();
        FriendsColumnInfo columnInfo = (FriendsColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Friends.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        java.util.Date realmGet$timestamp = ((FriendsRealmProxyInterface)object).realmGet$timestamp();
        if (realmGet$timestamp != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.timestampIndex, rowIndex, realmGet$timestamp.getTime(), false);
        }

        RealmList<com.anubis.flickr.models.Photo> friendsList = ((FriendsRealmProxyInterface) object).realmGet$friends();
        if (friendsList != null) {
            long friendsNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.friendsIndex, rowIndex);
            for (com.anubis.flickr.models.Photo friendsItem : friendsList) {
                Long cacheItemIndexfriends = cache.get(friendsItem);
                if (cacheItemIndexfriends == null) {
                    cacheItemIndexfriends = PhotoRealmProxy.insert(realm, friendsItem, cache);
                }
                LinkView.nativeAdd(friendsNativeLinkViewPtr, cacheItemIndexfriends);
            }
            LinkView.nativeClose(friendsNativeLinkViewPtr);
        }

        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.Friends.class);
        long tableNativePtr = table.getNativeTablePointer();
        FriendsColumnInfo columnInfo = (FriendsColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Friends.class);
        com.anubis.flickr.models.Friends object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.Friends) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                java.util.Date realmGet$timestamp = ((FriendsRealmProxyInterface)object).realmGet$timestamp();
                if (realmGet$timestamp != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.timestampIndex, rowIndex, realmGet$timestamp.getTime(), false);
                }

                RealmList<com.anubis.flickr.models.Photo> friendsList = ((FriendsRealmProxyInterface) object).realmGet$friends();
                if (friendsList != null) {
                    long friendsNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.friendsIndex, rowIndex);
                    for (com.anubis.flickr.models.Photo friendsItem : friendsList) {
                        Long cacheItemIndexfriends = cache.get(friendsItem);
                        if (cacheItemIndexfriends == null) {
                            cacheItemIndexfriends = PhotoRealmProxy.insert(realm, friendsItem, cache);
                        }
                        LinkView.nativeAdd(friendsNativeLinkViewPtr, cacheItemIndexfriends);
                    }
                    LinkView.nativeClose(friendsNativeLinkViewPtr);
                }

            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.anubis.flickr.models.Friends object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.Friends.class);
        long tableNativePtr = table.getNativeTablePointer();
        FriendsColumnInfo columnInfo = (FriendsColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Friends.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        java.util.Date realmGet$timestamp = ((FriendsRealmProxyInterface)object).realmGet$timestamp();
        if (realmGet$timestamp != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.timestampIndex, rowIndex, realmGet$timestamp.getTime(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.timestampIndex, rowIndex, false);
        }

        long friendsNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.friendsIndex, rowIndex);
        LinkView.nativeClear(friendsNativeLinkViewPtr);
        RealmList<com.anubis.flickr.models.Photo> friendsList = ((FriendsRealmProxyInterface) object).realmGet$friends();
        if (friendsList != null) {
            for (com.anubis.flickr.models.Photo friendsItem : friendsList) {
                Long cacheItemIndexfriends = cache.get(friendsItem);
                if (cacheItemIndexfriends == null) {
                    cacheItemIndexfriends = PhotoRealmProxy.insertOrUpdate(realm, friendsItem, cache);
                }
                LinkView.nativeAdd(friendsNativeLinkViewPtr, cacheItemIndexfriends);
            }
        }
        LinkView.nativeClose(friendsNativeLinkViewPtr);

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.Friends.class);
        long tableNativePtr = table.getNativeTablePointer();
        FriendsColumnInfo columnInfo = (FriendsColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Friends.class);
        com.anubis.flickr.models.Friends object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.Friends) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                java.util.Date realmGet$timestamp = ((FriendsRealmProxyInterface)object).realmGet$timestamp();
                if (realmGet$timestamp != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.timestampIndex, rowIndex, realmGet$timestamp.getTime(), false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.timestampIndex, rowIndex, false);
                }

                long friendsNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.friendsIndex, rowIndex);
                LinkView.nativeClear(friendsNativeLinkViewPtr);
                RealmList<com.anubis.flickr.models.Photo> friendsList = ((FriendsRealmProxyInterface) object).realmGet$friends();
                if (friendsList != null) {
                    for (com.anubis.flickr.models.Photo friendsItem : friendsList) {
                        Long cacheItemIndexfriends = cache.get(friendsItem);
                        if (cacheItemIndexfriends == null) {
                            cacheItemIndexfriends = PhotoRealmProxy.insertOrUpdate(realm, friendsItem, cache);
                        }
                        LinkView.nativeAdd(friendsNativeLinkViewPtr, cacheItemIndexfriends);
                    }
                }
                LinkView.nativeClose(friendsNativeLinkViewPtr);

            }
        }
    }

    public static com.anubis.flickr.models.Friends createDetachedCopy(com.anubis.flickr.models.Friends realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.anubis.flickr.models.Friends unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.anubis.flickr.models.Friends)cachedObject.object;
            } else {
                unmanagedObject = (com.anubis.flickr.models.Friends)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.anubis.flickr.models.Friends();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((FriendsRealmProxyInterface) unmanagedObject).realmSet$timestamp(((FriendsRealmProxyInterface) realmObject).realmGet$timestamp());

        // Deep copy of friends
        if (currentDepth == maxDepth) {
            ((FriendsRealmProxyInterface) unmanagedObject).realmSet$friends(null);
        } else {
            RealmList<com.anubis.flickr.models.Photo> managedfriendsList = ((FriendsRealmProxyInterface) realmObject).realmGet$friends();
            RealmList<com.anubis.flickr.models.Photo> unmanagedfriendsList = new RealmList<com.anubis.flickr.models.Photo>();
            ((FriendsRealmProxyInterface) unmanagedObject).realmSet$friends(unmanagedfriendsList);
            int nextDepth = currentDepth + 1;
            int size = managedfriendsList.size();
            for (int i = 0; i < size; i++) {
                com.anubis.flickr.models.Photo item = PhotoRealmProxy.createDetachedCopy(managedfriendsList.get(i), nextDepth, maxDepth, cache);
                unmanagedfriendsList.add(item);
            }
        }
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Friends = [");
        stringBuilder.append("{timestamp:");
        stringBuilder.append(realmGet$timestamp() != null ? realmGet$timestamp() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{friends:");
        stringBuilder.append("RealmList<Photo>[").append(realmGet$friends().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsRealmProxy aFriends = (FriendsRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aFriends.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aFriends.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aFriends.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
