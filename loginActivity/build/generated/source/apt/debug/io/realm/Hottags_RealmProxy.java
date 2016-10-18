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

public class Hottags_RealmProxy extends com.anubis.flickr.models.Hottags_
    implements RealmObjectProxy, Hottags_RealmProxyInterface {

    static final class Hottags_ColumnInfo extends ColumnInfo
        implements Cloneable {

        public long periodIndex;
        public long countIndex;
        public long tagsListIndex;

        Hottags_ColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(3);
            this.periodIndex = getValidColumnIndex(path, table, "Hottags_", "period");
            indicesMap.put("period", this.periodIndex);
            this.countIndex = getValidColumnIndex(path, table, "Hottags_", "count");
            indicesMap.put("count", this.countIndex);
            this.tagsListIndex = getValidColumnIndex(path, table, "Hottags_", "tagsList");
            indicesMap.put("tagsList", this.tagsListIndex);

            setIndicesMap(indicesMap);
        }

        @Override
        public final void copyColumnInfoFrom(ColumnInfo other) {
            final Hottags_ColumnInfo otherInfo = (Hottags_ColumnInfo) other;
            this.periodIndex = otherInfo.periodIndex;
            this.countIndex = otherInfo.countIndex;
            this.tagsListIndex = otherInfo.tagsListIndex;

            setIndicesMap(otherInfo.getIndicesMap());
        }

        @Override
        public final Hottags_ColumnInfo clone() {
            return (Hottags_ColumnInfo) super.clone();
        }

    }
    private Hottags_ColumnInfo columnInfo;
    private ProxyState proxyState;
    private RealmList<com.anubis.flickr.models.Tag> tagsListRealmList;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("period");
        fieldNames.add("count");
        fieldNames.add("tagsList");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    Hottags_RealmProxy() {
        if (proxyState == null) {
            injectObjectContext();
        }
        proxyState.setConstructionFinished();
    }

    private void injectObjectContext() {
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (Hottags_ColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState(com.anubis.flickr.models.Hottags_.class, this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @SuppressWarnings("cast")
    public String realmGet$period() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.periodIndex);
    }

    public void realmSet$period(String value) {
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
                row.getTable().setNull(columnInfo.periodIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.periodIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.periodIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.periodIndex, value);
    }

    @SuppressWarnings("cast")
    public Integer realmGet$count() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.countIndex)) {
            return null;
        }
        return (int) proxyState.getRow$realm().getLong(columnInfo.countIndex);
    }

    public void realmSet$count(Integer value) {
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
                row.getTable().setNull(columnInfo.countIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setLong(columnInfo.countIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.countIndex);
            return;
        }
        proxyState.getRow$realm().setLong(columnInfo.countIndex, value);
    }

    public RealmList<com.anubis.flickr.models.Tag> realmGet$tagsList() {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (tagsListRealmList != null) {
            return tagsListRealmList;
        } else {
            LinkView linkView = proxyState.getRow$realm().getLinkList(columnInfo.tagsListIndex);
            tagsListRealmList = new RealmList<com.anubis.flickr.models.Tag>(com.anubis.flickr.models.Tag.class, linkView, proxyState.getRealm$realm());
            return tagsListRealmList;
        }
    }

    public void realmSet$tagsList(RealmList<com.anubis.flickr.models.Tag> value) {
        if (proxyState == null) {
            // Called from model's constructor. Inject context.
            injectObjectContext();
        }

        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("tagsList")) {
                return;
            }
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.anubis.flickr.models.Tag> original = value;
                value = new RealmList<com.anubis.flickr.models.Tag>();
                for (com.anubis.flickr.models.Tag item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        LinkView links = proxyState.getRow$realm().getLinkList(columnInfo.tagsListIndex);
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
        if (!realmSchema.contains("Hottags_")) {
            RealmObjectSchema realmObjectSchema = realmSchema.create("Hottags_");
            realmObjectSchema.add(new Property("period", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            realmObjectSchema.add(new Property("count", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED));
            if (!realmSchema.contains("Tag")) {
                TagRealmProxy.createRealmObjectSchema(realmSchema);
            }
            realmObjectSchema.add(new Property("tagsList", RealmFieldType.LIST, realmSchema.get("Tag")));
            return realmObjectSchema;
        }
        return realmSchema.get("Hottags_");
    }

    public static Table initTable(SharedRealm sharedRealm) {
        if (!sharedRealm.hasTable("class_Hottags_")) {
            Table table = sharedRealm.getTable("class_Hottags_");
            table.addColumn(RealmFieldType.STRING, "period", Table.NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "count", Table.NULLABLE);
            if (!sharedRealm.hasTable("class_Tag")) {
                TagRealmProxy.initTable(sharedRealm);
            }
            table.addColumnLink(RealmFieldType.LIST, "tagsList", sharedRealm.getTable("class_Tag"));
            table.setPrimaryKey("");
            return table;
        }
        return sharedRealm.getTable("class_Hottags_");
    }

    public static Hottags_ColumnInfo validateTable(SharedRealm sharedRealm, boolean allowExtraColumns) {
        if (sharedRealm.hasTable("class_Hottags_")) {
            Table table = sharedRealm.getTable("class_Hottags_");
            final long columnCount = table.getColumnCount();
            if (columnCount != 3) {
                if (columnCount < 3) {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is less than expected - expected 3 but was " + columnCount);
                }
                if (allowExtraColumns) {
                    RealmLog.debug("Field count is more than expected - expected 3 but was %1$d", columnCount);
                } else {
                    throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field count is more than expected - expected 3 but was " + columnCount);
                }
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 3; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final Hottags_ColumnInfo columnInfo = new Hottags_ColumnInfo(sharedRealm.getPath(), table);

            if (!columnTypes.containsKey("period")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'period' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("period") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'String' for field 'period' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.periodIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Field 'period' is required. Either set @Required to field 'period' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("count")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'count' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("count") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Integer' for field 'count' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.countIndex)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(),"Field 'count' does not support null values in the existing Realm file. Either set @Required, use the primitive type for field 'count' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("tagsList")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing field 'tagsList'");
            }
            if (columnTypes.get("tagsList") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid type 'Tag' for field 'tagsList'");
            }
            if (!sharedRealm.hasTable("class_Tag")) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Missing class 'class_Tag' for field 'tagsList'");
            }
            Table table_2 = sharedRealm.getTable("class_Tag");
            if (!table.getLinkTarget(columnInfo.tagsListIndex).hasSameSchema(table_2)) {
                throw new RealmMigrationNeededException(sharedRealm.getPath(), "Invalid RealmList type for field 'tagsList': '" + table.getLinkTarget(columnInfo.tagsListIndex).getName() + "' expected - was '" + table_2.getName() + "'");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(sharedRealm.getPath(), "The 'Hottags_' class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Hottags_";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.anubis.flickr.models.Hottags_ createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        if (json.has("tagsList")) {
            excludeFields.add("tagsList");
        }
        com.anubis.flickr.models.Hottags_ obj = realm.createObjectInternal(com.anubis.flickr.models.Hottags_.class, true, excludeFields);
        if (json.has("period")) {
            if (json.isNull("period")) {
                ((Hottags_RealmProxyInterface) obj).realmSet$period(null);
            } else {
                ((Hottags_RealmProxyInterface) obj).realmSet$period((String) json.getString("period"));
            }
        }
        if (json.has("count")) {
            if (json.isNull("count")) {
                ((Hottags_RealmProxyInterface) obj).realmSet$count(null);
            } else {
                ((Hottags_RealmProxyInterface) obj).realmSet$count((int) json.getInt("count"));
            }
        }
        if (json.has("tagsList")) {
            if (json.isNull("tagsList")) {
                ((Hottags_RealmProxyInterface) obj).realmSet$tagsList(null);
            } else {
                ((Hottags_RealmProxyInterface) obj).realmGet$tagsList().clear();
                JSONArray array = json.getJSONArray("tagsList");
                for (int i = 0; i < array.length(); i++) {
                    com.anubis.flickr.models.Tag item = TagRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    ((Hottags_RealmProxyInterface) obj).realmGet$tagsList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.anubis.flickr.models.Hottags_ createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.anubis.flickr.models.Hottags_ obj = new com.anubis.flickr.models.Hottags_();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("period")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((Hottags_RealmProxyInterface) obj).realmSet$period(null);
                } else {
                    ((Hottags_RealmProxyInterface) obj).realmSet$period((String) reader.nextString());
                }
            } else if (name.equals("count")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((Hottags_RealmProxyInterface) obj).realmSet$count(null);
                } else {
                    ((Hottags_RealmProxyInterface) obj).realmSet$count((int) reader.nextInt());
                }
            } else if (name.equals("tagsList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((Hottags_RealmProxyInterface) obj).realmSet$tagsList(null);
                } else {
                    ((Hottags_RealmProxyInterface) obj).realmSet$tagsList(new RealmList<com.anubis.flickr.models.Tag>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.anubis.flickr.models.Tag item = TagRealmProxy.createUsingJsonStream(realm, reader);
                        ((Hottags_RealmProxyInterface) obj).realmGet$tagsList().add(item);
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

    public static com.anubis.flickr.models.Hottags_ copyOrUpdate(Realm realm, com.anubis.flickr.models.Hottags_ object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.Hottags_) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.anubis.flickr.models.Hottags_ copy(Realm realm, com.anubis.flickr.models.Hottags_ newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.anubis.flickr.models.Hottags_) cachedRealmObject;
        } else {
            // rejecting default values to avoid creating unexpected objects from RealmModel/RealmList fields.
            com.anubis.flickr.models.Hottags_ realmObject = realm.createObjectInternal(com.anubis.flickr.models.Hottags_.class, false, Collections.<String>emptyList());
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((Hottags_RealmProxyInterface) realmObject).realmSet$period(((Hottags_RealmProxyInterface) newObject).realmGet$period());
            ((Hottags_RealmProxyInterface) realmObject).realmSet$count(((Hottags_RealmProxyInterface) newObject).realmGet$count());

            RealmList<com.anubis.flickr.models.Tag> tagsListList = ((Hottags_RealmProxyInterface) newObject).realmGet$tagsList();
            if (tagsListList != null) {
                RealmList<com.anubis.flickr.models.Tag> tagsListRealmList = ((Hottags_RealmProxyInterface) realmObject).realmGet$tagsList();
                for (int i = 0; i < tagsListList.size(); i++) {
                    com.anubis.flickr.models.Tag tagsListItem = tagsListList.get(i);
                    com.anubis.flickr.models.Tag cachetagsList = (com.anubis.flickr.models.Tag) cache.get(tagsListItem);
                    if (cachetagsList != null) {
                        tagsListRealmList.add(cachetagsList);
                    } else {
                        tagsListRealmList.add(TagRealmProxy.copyOrUpdate(realm, tagsListList.get(i), update, cache));
                    }
                }
            }

            return realmObject;
        }
    }

    public static long insert(Realm realm, com.anubis.flickr.models.Hottags_ object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.Hottags_.class);
        long tableNativePtr = table.getNativeTablePointer();
        Hottags_ColumnInfo columnInfo = (Hottags_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Hottags_.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$period = ((Hottags_RealmProxyInterface)object).realmGet$period();
        if (realmGet$period != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.periodIndex, rowIndex, realmGet$period, false);
        }
        Number realmGet$count = ((Hottags_RealmProxyInterface)object).realmGet$count();
        if (realmGet$count != null) {
            Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, realmGet$count.longValue(), false);
        }

        RealmList<com.anubis.flickr.models.Tag> tagsListList = ((Hottags_RealmProxyInterface) object).realmGet$tagsList();
        if (tagsListList != null) {
            long tagsListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.tagsListIndex, rowIndex);
            for (com.anubis.flickr.models.Tag tagsListItem : tagsListList) {
                Long cacheItemIndextagsList = cache.get(tagsListItem);
                if (cacheItemIndextagsList == null) {
                    cacheItemIndextagsList = TagRealmProxy.insert(realm, tagsListItem, cache);
                }
                LinkView.nativeAdd(tagsListNativeLinkViewPtr, cacheItemIndextagsList);
            }
            LinkView.nativeClose(tagsListNativeLinkViewPtr);
        }

        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.Hottags_.class);
        long tableNativePtr = table.getNativeTablePointer();
        Hottags_ColumnInfo columnInfo = (Hottags_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Hottags_.class);
        com.anubis.flickr.models.Hottags_ object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.Hottags_) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$period = ((Hottags_RealmProxyInterface)object).realmGet$period();
                if (realmGet$period != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.periodIndex, rowIndex, realmGet$period, false);
                }
                Number realmGet$count = ((Hottags_RealmProxyInterface)object).realmGet$count();
                if (realmGet$count != null) {
                    Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, realmGet$count.longValue(), false);
                }

                RealmList<com.anubis.flickr.models.Tag> tagsListList = ((Hottags_RealmProxyInterface) object).realmGet$tagsList();
                if (tagsListList != null) {
                    long tagsListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.tagsListIndex, rowIndex);
                    for (com.anubis.flickr.models.Tag tagsListItem : tagsListList) {
                        Long cacheItemIndextagsList = cache.get(tagsListItem);
                        if (cacheItemIndextagsList == null) {
                            cacheItemIndextagsList = TagRealmProxy.insert(realm, tagsListItem, cache);
                        }
                        LinkView.nativeAdd(tagsListNativeLinkViewPtr, cacheItemIndextagsList);
                    }
                    LinkView.nativeClose(tagsListNativeLinkViewPtr);
                }

            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.anubis.flickr.models.Hottags_ object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.anubis.flickr.models.Hottags_.class);
        long tableNativePtr = table.getNativeTablePointer();
        Hottags_ColumnInfo columnInfo = (Hottags_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Hottags_.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        String realmGet$period = ((Hottags_RealmProxyInterface)object).realmGet$period();
        if (realmGet$period != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.periodIndex, rowIndex, realmGet$period, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.periodIndex, rowIndex, false);
        }
        Number realmGet$count = ((Hottags_RealmProxyInterface)object).realmGet$count();
        if (realmGet$count != null) {
            Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, realmGet$count.longValue(), false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.countIndex, rowIndex, false);
        }

        long tagsListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.tagsListIndex, rowIndex);
        LinkView.nativeClear(tagsListNativeLinkViewPtr);
        RealmList<com.anubis.flickr.models.Tag> tagsListList = ((Hottags_RealmProxyInterface) object).realmGet$tagsList();
        if (tagsListList != null) {
            for (com.anubis.flickr.models.Tag tagsListItem : tagsListList) {
                Long cacheItemIndextagsList = cache.get(tagsListItem);
                if (cacheItemIndextagsList == null) {
                    cacheItemIndextagsList = TagRealmProxy.insertOrUpdate(realm, tagsListItem, cache);
                }
                LinkView.nativeAdd(tagsListNativeLinkViewPtr, cacheItemIndextagsList);
            }
        }
        LinkView.nativeClose(tagsListNativeLinkViewPtr);

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.anubis.flickr.models.Hottags_.class);
        long tableNativePtr = table.getNativeTablePointer();
        Hottags_ColumnInfo columnInfo = (Hottags_ColumnInfo) realm.schema.getColumnInfo(com.anubis.flickr.models.Hottags_.class);
        com.anubis.flickr.models.Hottags_ object = null;
        while (objects.hasNext()) {
            object = (com.anubis.flickr.models.Hottags_) objects.next();
            if(!cache.containsKey(object)) {
                if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                    cache.put(object, ((RealmObjectProxy)object).realmGet$proxyState().getRow$realm().getIndex());
                    continue;
                }
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                String realmGet$period = ((Hottags_RealmProxyInterface)object).realmGet$period();
                if (realmGet$period != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.periodIndex, rowIndex, realmGet$period, false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.periodIndex, rowIndex, false);
                }
                Number realmGet$count = ((Hottags_RealmProxyInterface)object).realmGet$count();
                if (realmGet$count != null) {
                    Table.nativeSetLong(tableNativePtr, columnInfo.countIndex, rowIndex, realmGet$count.longValue(), false);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.countIndex, rowIndex, false);
                }

                long tagsListNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.tagsListIndex, rowIndex);
                LinkView.nativeClear(tagsListNativeLinkViewPtr);
                RealmList<com.anubis.flickr.models.Tag> tagsListList = ((Hottags_RealmProxyInterface) object).realmGet$tagsList();
                if (tagsListList != null) {
                    for (com.anubis.flickr.models.Tag tagsListItem : tagsListList) {
                        Long cacheItemIndextagsList = cache.get(tagsListItem);
                        if (cacheItemIndextagsList == null) {
                            cacheItemIndextagsList = TagRealmProxy.insertOrUpdate(realm, tagsListItem, cache);
                        }
                        LinkView.nativeAdd(tagsListNativeLinkViewPtr, cacheItemIndextagsList);
                    }
                }
                LinkView.nativeClose(tagsListNativeLinkViewPtr);

            }
        }
    }

    public static com.anubis.flickr.models.Hottags_ createDetachedCopy(com.anubis.flickr.models.Hottags_ realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.anubis.flickr.models.Hottags_ unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.anubis.flickr.models.Hottags_)cachedObject.object;
            } else {
                unmanagedObject = (com.anubis.flickr.models.Hottags_)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.anubis.flickr.models.Hottags_();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((Hottags_RealmProxyInterface) unmanagedObject).realmSet$period(((Hottags_RealmProxyInterface) realmObject).realmGet$period());
        ((Hottags_RealmProxyInterface) unmanagedObject).realmSet$count(((Hottags_RealmProxyInterface) realmObject).realmGet$count());

        // Deep copy of tagsList
        if (currentDepth == maxDepth) {
            ((Hottags_RealmProxyInterface) unmanagedObject).realmSet$tagsList(null);
        } else {
            RealmList<com.anubis.flickr.models.Tag> managedtagsListList = ((Hottags_RealmProxyInterface) realmObject).realmGet$tagsList();
            RealmList<com.anubis.flickr.models.Tag> unmanagedtagsListList = new RealmList<com.anubis.flickr.models.Tag>();
            ((Hottags_RealmProxyInterface) unmanagedObject).realmSet$tagsList(unmanagedtagsListList);
            int nextDepth = currentDepth + 1;
            int size = managedtagsListList.size();
            for (int i = 0; i < size; i++) {
                com.anubis.flickr.models.Tag item = TagRealmProxy.createDetachedCopy(managedtagsListList.get(i), nextDepth, maxDepth, cache);
                unmanagedtagsListList.add(item);
            }
        }
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Hottags_ = [");
        stringBuilder.append("{period:");
        stringBuilder.append(realmGet$period() != null ? realmGet$period() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{count:");
        stringBuilder.append(realmGet$count() != null ? realmGet$count() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tagsList:");
        stringBuilder.append("RealmList<Tag>[").append(realmGet$tagsList().size()).append("]");
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
        Hottags_RealmProxy aHottags_ = (Hottags_RealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aHottags_.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aHottags_.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aHottags_.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
