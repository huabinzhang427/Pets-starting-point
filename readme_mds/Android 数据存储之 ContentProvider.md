# 为什么要使用 ContentProvider

**集中化数据的访问和编辑**。ContentProvider 作为一个**数据验证层**（data validation），会在我们错误输入无效的数据时进行验证，如果数据存在错误，就会在这一步被捕捉到。它作为数据源和 UI 代码之间的附加层。它通常被称为**抽象层**（abstraction layer），因为 ContentProvider 会抽象化数据存储的方式或隐藏数据存储的详情，所以 UI 代码在进行任何数据访问时，只需和 ContentProvider 进行通信，ContentProvider 会以 UI 看不到的方式对底层数据进行暗箱处理。因为 UI 不关心数据是存储在数据库中还是存储在单个文本文件中，所以如果在应用的更新版本中我们想将数据库换做不同的存储类型 UI 代码将保持不变，并继续与现有的 ContentProvider 交互。

Content Provider 三大优势：

 - 作为数据源与 UI 代码之间的抽象层，可以帮助我们很好地进行数据验证；
 - 帮助我们与其它框架类完美协助；
 - 帮助我们轻松与其它应用分享数据

# ContentProvider 工作原理

![image](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_imgs/2018063015341162.png)

CatalogActivity 调用 CRUD （查询/插入/修改/删除）方法的任意一个，并传入一个 URI 指定其想与之交互的特定数据集，这个特定数据可以是数据库里某行中的特定名称，然后给定 URI 中的信息，ContentResolver 会将该消息发送到适当的 ContentProvider，而 ContentProvider 会和 PetDbHelper 进行交互，database 从而获取适当的数据并将它返回至调用者，返回的数据会回传至 ContentResolver，并最终返回至 CatalogActivity 以在 UI 中显示。


# 添加 ContentProvider 代码

在 data 包名下，创建 PetProvider 类继承自 ContentProvider，重写所有的方法。

```java
public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
```

在 AndroidManifest 文件中，添加代码 说明我们使用的是带这个 provider 标签的 ContentProvider。

```xml
<!--authorities:唯一指定设备上的 Content Provider，通常直接使用应用的包名即可-->
<!--exported:决定我们的 Content Provider 是否对其它应用可见-->
<provider
  android:name=".data.PetProvider"
  android:authorities="com.example.android.pets"
  android:exported="false" />
```

# 向 Contract 添加 URI

如何向 `PetContract.java` 代码添加 URI ？

上文中说到的 URI 的 3 个部分：**Scheme**，**Content Authority**，**Type of data**。其中某些成分是可重复使用的，不会发生变化，我们可以将它们作为常数。那么存储这些常数的最佳地方是哪里？

我们之前将与数据相关的所有常数存储子 Contract 类中，所以我们也可以将 URI 常数信息存储其中。

 **CONTENT_AUTHORITY**
 
 存储我们之前在 AndroidManifest 文件中 provider 标签下的 `android:authorities` 的内容。

```xml
 <provider
      android:name=”.data.PetProvider”
      android:authorities=”com.example.android.pets”
      android:exported=”false” />
```
在 `PetContract.java` 中设置

```java
public static final String CONTENT_AUTHORITY = "com.example.android.pets";
```

**BASE_CONTENT_URI**

将 CONTENT_AUTHORITY 常数与 Scheme 的 `content://` 连接起来，它将由**与 PetsProvider 关联的每个 URI 共用**：

```java
"content://" + CONTENT_AUTHORITY
---
public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
``` 

**PATH_TableName**

该常数存储位置将添加到基本内容 URI 的每个表的路径：

```java
 public static final String PATH_PETS = "pets";
```

**完成 CONTENT_URI**

最后，在 contract 中的每个 Entry 类中，我们为类创建一个完整的 URI 作为常数，称为 CONTENT_URI。

```java
 public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);
```

# 向 ContentProvider 添加 UriMatcher

要使用 UriMatcher，我们需要在 ContentProvider 中实现两个步骤：

 1. 用 ContentProvider 可接受的 URI 模式设置 UriMatcher，并向每个模式分配一个整数代码；
 2. 调用 UriMatcher.match(Uri) 方法，传入一个 URI，返回对应的整数代码。前提是它与某个有效模式匹配，如果不匹配它也会给我们提示。

ContentProvider  的 `query()` 方法

```java
/** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, "pets", PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, "pets/#", PET_ID);
    }
  
  /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null ,sortOrder);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetContract.PetEntry._ID + "=?";
                // ContentUris.parseId(uri)：路径最后一段换成数字
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw  new IllegalArgumentException("Cannot query unkonwn URI " + uri);
        }
        return cursor;
    }
    
```

ContentProvider 的 `insert()` 方法

```java
/**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues contentValues) {
        // TODO: Insert a new pet into the pets database table with the given ContentValues
        // Get readable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long petId = database.insert(PetEntry.TABLE_NAME, null, contentValues);

        // Once we know the ID of the new row in the table,

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (petId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, petId);
    }
```
ContentProvider 的 `update()` 方法

虽然数据要求与 insert() 方法一样，但是仍然有一个关键差别。对于 insert() 方法，由于要插入的是全新宠物，所有属性（品种除外）都应提供。但对于 update() 方法，ContentValues 对象中不需要全部四个属性，可能只需更新一个或多个属性。在这种情况下，我们在检查值是否合理之前，使用 `ContentValues.containsKey()` 方法来检查键/值是否存在。

```java
/**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match  = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // If there are no values to update, then don't try to update the database
        if (contentValues.size() == 0) {
            return 0;
        }

        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (contentValues.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // // check that the gender value is valid.
        if (contentValues.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw  new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (contentValues.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Returns the number of database rows affected by the update statement
        return database.update(PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }
```    






# 向 ContentProvider 添加数据验证

```java
/**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues contentValues) {
        // Check that the name is not null
        String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is valid
        Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        // No need to check the breed, any value is valid (including null).

        // Otherwise, get writeable database to insert the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long petId = database.insert(PetEntry.TABLE_NAME, null, contentValues);

        // Once we know the ID of the new row in the table,

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (petId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, petId);
    }
```
**ContentProvider 可以视为警察，它负责允许或拒绝进入数据库的数据**。

ContentProvider 的 `delete()` 方法

```java
/**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not support for " + uri);
        }
    }
```
ContentProvider 的 `getType()` 方法

实现此 [方法] 来处理给定 URI 的 MIME 数据类型请求。返回的 MIME 类型对于单个记录应以“`vnd.android.cursor.item`” 开头，多个项应以“`vnd.android.cursor.dir/`”开头。

添加步骤：

步骤一：在 PetContract 中声明 MIME 类型常数

```java
 /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         *
         * */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         *
         * */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PETS;

```
步骤二：实现 ContentProvider getType() 方法

```java
/**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unkonwn URI " + uri + " with match " + match);

        }
    }
```




