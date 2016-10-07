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

public class User_RealmProxy extends com.anubis.flickr.models.User_
    implements RealmObjectProxy, User_RealmProxyInterface {

    static final class User_ColumnInfo extends ColumnInfo
        implements Cloneable {

        public long idIndex;
        public long usernameIndex;

        User_ColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(2);
            this.idIndex = getValidColumnIndex(path, table, "User_", "id");
            indicesMap.put("id", this.idIndex);
            this.usernameIndex = getValidColumnIndex(path, table, "User_", "username");
            indicesMap.put("username", this.usernameIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final User_ColumnInfo otherInfo = (User_ColumnInfo) other;
            this.idIndex = otherInfo.idIndex;
            this.usernameIndex = otherInfo.usernameIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final User_ColumnInfo clone() {
            return (User_ColumnInfo) super.clone();
        }

    }
    private User_ColumnInfo columnInfo;
    private ProxyState proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("username");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    User_RealmProxy() {
        if (proxyState == null) {
            injectObjectContext();
        }
        proxyState.setConstructionFinished();
    }

    private void injectObjectContext() {
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (User_ColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState(com.anubis.flickr.models.User_.class, this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @SuppressWarnings("cast")
    public String realmGet$id() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idIndex);
    }

    public void realmSet$id(String value) {
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
                row.getTable().setNull(columnInfo.idIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.idIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.idIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.idIndex, value);
    }

    public com.anubis.flickr.models.Username realmGet$username() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNullLink(columnInfo.usernameIndex)) {
            return null;
        }
        return proxyState.getRealm$realm().get(com.anubis.flickr.models.Username.class, proxyState.getRow$realm().getLink(columnInfo.usernameIndex), false, Collections.<String>emptyList());
    }

    public void realmSet$username(com.anubis.flickr.models.Username value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("username")) {
                return;
            }
            if (value != null && !RealmObject.isManaged(value)) {
                value = ((Realm) proxyState.getRealm$realm()).copyToRealm(value);
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                // Table#nullifyLink() does not support default value. Just using Row.
                row.nullifyLink(columnInfo.usernameIndex);
                return;
            }
            if (!RealmObject.isValid(value)) {
                throw new IllegalArgumentException("'value' is not a valid managed object.");
            }
            if (((RealmObjectProxy) value).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
                throw new IllegalArgumentException("'value' belongs to a different Realm.");
            }
            row.getTable().setLink(columnInfo.usernameIndex, row.getIndex(), ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex(), true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().nullifyLink(columnInfo.usernameIndex);
            return;
        }
        if (!(RealmObject.isManaged(value) && RealmObject.isValid(value))) {
            throw new IllegalArgumentException("'value' is not a valid managed object.");
        }
        if (((RealmObjectProxy)value).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
            throw new IllegalArgumentException("'value' belongs to a different Realm.");
        }
        proxyState.getRow$realm().setLink(columnInfo.usernameIndex, ((RealmObjectProxy)value).realmGet$proxyState().getRow$realm().getIndex());
    }

    public static RealmObjectSchema createRealmObjectSchema(RealmSchema realmSchema) {
        if (!realmSchema.contains("User_")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("User_");
            realmObjectSchema.add(new Property("id", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            if (!realmSchema.contains("Username")) {
                UsernameRealmProxy.createRealmObjectSchema(realmSchema);
            }
            realmObjectSchema.add(new Property("username", RealmFieldType.OBJECT, realmSchema.get("Username")));
            return realmObjectSchema;
        }
        return realmSchema.get("User_");
    }

    public static Table initTable(SharedRealm sharedRealm) {
        if (!sharedRealm.hasTable("class_User_")) {
            Table table = sharedRealm.getTable("class_User_");
            table.addColumn(RealmFieldType.STRING, "id", Table.NULLABLE);
            if (!sharedRealm.hasTable("class_Username")) {
                UsernameRealmProxy.initTable(sharedRealm);
            }
            table.addColumnLink(RealmFieldType.OBJECT, "username", sharedRealm.getTable("class_Username"));
            table.setPrimaryKey("");
            return table;
        }
        return sharedRealm.getTable("class_User_");
    }

    public static User_ColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_User_")) {
            Table table = sharedRealm.getTable("class_User_");
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

            final User_ColumnInfo columnInfo = new User_ColumnInfo(sharedRealm.getPath(), table);

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("id") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'id' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.idIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'id' is required. Either set @Required to field 'id' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("username")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'username' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("username") != RealmFieldType.OBJECT) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Username' for field 'username'");
            }
            if (!sharedRealm.hasTable("class_Username")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing class 'class_Username' for field 'username'");
            }
            Table table_1 = sharedRealm.getTable("class_Username");
            if (!table.getLinkTarget(columnInfo.usernameIndex).hasSameSchema(table_1)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid RealmObject for field 'username': '" + table.getLinkTarget(columnInfo.usernameIndex).getName() + "' expected - was '" + table_1.getName() + "'");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'User_' class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_User_";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.anubis.flickr.models.User_ createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        if (json.has("username")) {
            excludeFields.add("username");
        }
        com.anubis.flickr.models.User_ obj = realm.createObjectInternal(com.anubis.flickr.models.User_.class, true, excludeFields);
        if (json.has("id")) {
            if (json.isNull("id")) {
                ((User_RealmProxyInterface) obj).realmSet$id(null);
            } else {
                ((User_RealmProxyInterface) obj).realmSet$id((String) json.getString("id"));
            }
        }
        if (json.has("username")) {
            if (json.isNull("username")) {
                ((User_RealmProxyInterface) obj).realmSet$username(null);
            } else {
                com.anubis.flickr.models.Username usernameObj = UsernameRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("username"), update);
                ((User_RealmProxyInterface) obj).realmSet$username(usernameObj);
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.anubis.flickr.models.User_ createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.anubis.flickr.models.User_ obj = new com.anubis.flickr.models.User_();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((User_RealmProxyInterface) obj).realmSet$id(null);
                } else {
                    ((User_RealmProxyInterface) obj).realmSet$id((String) reader.nextString());
                }
            } else if (name.equals("username")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((User_RealmProxyInterface) obj).realmSet$username(null);
                } else {
                    com.anubis.flickr.models.Username usernameObj = UsernameRealmProxy.createUsingJsonStream(realm, reader);
                    ((User_RealmProxyInterface) obj).realmSet$username(usernameObj);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        obj = realm.copyToRealm(obj);
        return obj;
    }

    public static com.anubis.flickr.models.User_ copyOrUpdate(Realm realm, com.anubis.flickr.models.User_ object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.User_) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.anubis.flickr.models.User_ copy(Realm realm, com.anubis.flickr.models.User_ newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.User_) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            com.anubis.flickr.models.User_ realmObject = realm.createObjectInternal(com.anubis.flickr.models.User_.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((User_RealmProxyInterface) realmObject).realmSet$id(((User_RealmProxyInterface) newObject).realmGet$id());

            com.anubis.flickr.models.Username usernameObj = ((User_RealmProxyInterface) newObject).realmGet$username();
            if (usernameObj != null) {
                com.anubis.flickr.models.Username cacheusername = (com.anubis.flickr.models.Username) cache.get(usernameObj);
                if (cacheusername != null) {
                    ((User_RealmProxyInterface) realmObject).realmSet$username(cacheusername);
                } else {
                    ((User_RealmProxyInterface) realmObject).realmSet$username(UsernameRealmProxy.copyOrUpdate(realm, usernameObj, update, cache));
                }
            } else {
                ((User_RealmProxyInterface) realmObject).realmSet$username(null);
            }
            return realmObject;
        }
    }

    public static long insert(Realm realm, com.anubis.flickr.models.User_ object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.User_.class);
        long tableNativePtr = table.getNativeTablePointer();
        User_ColumnInfo columnInfo = (User_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.User_.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((User_RealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        }

        com.anubis.flickr.models.Username usernameObj = ((User_RealmProxyInterface) object).realmGet$username();
        if (usernameObj != null) {
            Long cacheusername = cache.get(usernameObj);
            if (cacheusername == null) {
                cacheusername = UsernameRealmProxy.insert(realm, usernameObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.usernameIndex, rowIndex, cacheusername, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.User_.class);
        long tableNativePtr = table.getNativeTablePointer();
        User_ColumnInfo columnInfo = (User_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.User_.class);
        com.anubis.flickr.models.User_ object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.User_) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((User_RealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                }

                com.anubis.flickr.models.Username usernameObj = ((User_RealmProxyInterface) object).realmGet$username();
                if (usernameObj != null) {
                    Long cacheusername = cache.get(usernameObj);
                    if (cacheusername == null) {
                        cacheusername = UsernameRealmProxy.insert(realm, usernameObj, cache);
                    }
                    table.setLink(columnInfo.usernameIndex, rowIndex, cacheusername, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.anubis.flickr.models.User_ object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.User_.class);
        long tableNativePtr = table.getNativeTablePointer();
        User_ColumnInfo columnInfo = (User_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.User_.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$id = ((User_RealmProxyInterface)object).realmGet$id();
        if (realmGet$id != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
        }

        com.anubis.flickr.models.Username usernameObj = ((User_RealmProxyInterface) object).realmGet$username();
        if (usernameObj != null) {
            Long cacheusername = cache.get(usernameObj);
            if (cacheusername == null) {
                cacheusername = UsernameRealmProxy.insertOrUpdate(realm, usernameObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.usernameIndex, rowIndex, cacheusername, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.usernameIndex, rowIndex);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.User_.class);
        long tableNativePtr = table.getNativeTablePointer();
        User_ColumnInfo columnInfo = (User_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.User_.class);
        com.anubis.flickr.models.User_ object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.User_) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$id = ((User_RealmProxyInterface)object).realmGet$id();
                if (realmGet$id != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.idIndex, rowIndex, realmGet$id, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.idIndex, rowIndex, false);
                }

                com.anubis.flickr.models.Username usernameObj = ((User_RealmProxyInterface) object).realmGet$username();
                if (usernameObj != null) {
                    Long cacheusername = cache.get(usernameObj);
                    if (cacheusername == null) {
                        cacheusername = UsernameRealmProxy.insertOrUpdate(realm, usernameObj, cache);
                    }
                    Table.nativeSetLink(tableNativePtr, columnInfo.usernameIndex, rowIndex, cacheusername, false);
                } else {
                    Table.nativeNullifyLink(tableNativePtr, columnInfo.usernameIndex, rowIndex);
                }
            }
        }
    }

    public static com.anubis.flickr.models.User_ createDetachedCopy(com.anubis.flickr.models.User_ realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.anubis.flickr.models.User_ unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.anubis.flickr.models.User_)cachedObject.object;
            } else {
                unmanagedObject = (com.anubis.flickr.models.User_)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.anubis.flickr.models.User_();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((User_RealmProxyInterface) unmanagedObject).realmSet$id(((User_RealmProxyInterface) realmObject).realmGet$id());

        // Deep copy of username
        ((User_RealmProxyInterface) unmanagedObject).realmSet$username(UsernameRealmProxy.createDetachedCopy(((User_RealmProxyInterface) realmObject).realmGet$username(), currentDepth + 1, maxDepth, cache));
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("User_ = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{username:");
        stringBuilder.append(realmGet$username() != null ? "Username" : "null");
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
        User_RealmProxy aUser_ = (User_RealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aUser_.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aUser_.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aUser_.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
