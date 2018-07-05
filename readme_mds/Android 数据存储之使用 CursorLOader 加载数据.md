# CursorLoader 简介

> Loads data on a **background thread** because reading and writing to a database can be an expensive operation

* 在**后台线程加载数据**因为读取和写入数据库可能是一个很耗时的操作。

> Works well with ContentProvider because a Loader is **tied to a URI**

* 与 ContentProvider 很好的配合使用，因为加载器被**绑定在 URI 上**

> When the underlying data changes, we can **automatically refresh the loader** to query the data source again with the same URI.

* 当底层数据发生更改时，我们可以**自动刷新加载器**，用相同的 URI 再次查询数据源。

# 使用 CursorLoader

了解 CursorLoader 详细内容，可以参考链接内容

http://developer.android.youdaxue.com/guide/components/loaders?utm_source=udacity&utm_medium=course&utm_campaign=android_basics

http://developer.android.youdaxue.com/guide/topics/ui/layout/listview?utm_source=udacity&utm_medium=course&utm_campaign=android_basics

```java
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // 加载器常量
    private static final int PET_LOADER = 0;

    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor
        mCursorAdapter = new PetCursorAdapter(this, null);

        // Attach the adapter to the ListView
        petListView.setAdapter(mCursorAdapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        // kick off the loader
        getSupportLoaderManager().initLoader(PET_LOADER, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                eleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void eleteAllPets() {
        int rowDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowDeleted + " rows deleted from pet database");
    }

    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        Uri petUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
        if (petUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // 控制 projection 大小，提高应用性能
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };
        // 这个加载器将在后台线程上执行 ContentProvider 的 query() 方法
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update with this new cursor containing updated pet data-更新
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted-删除
        mCursorAdapter.swapCursor(null);
    }
}
```

# CursorLoader 的自动加载

为了避免无用的加载浪费资源，仅在 SQL 数据库中的某些数据发生变化时更新 Cursor。CursorLoader 主要解决的问题之一就是：我们想追踪数据是否已加载，如果已加载，则避免重复加载，除非有必要。

只有当我们在**插入、更新或删除数据时触发 Cursor 进行更新**。而插入、更新或删除数据的具体操作是在 ContentProvider 中执行的，所以我们需要**在 ContentProvider 中进行设置**，当数据发生变化时，告诉 CursorLoader 需要重新加载数据的地方。

在 Android 中存在一个机制能告诉 CursorLoader 与其关联的数据需要重新加载。因为**每个加载器与特定数据的 URI 关联**。URI 的作用是说明我们想要加载的数据是什么。Cursor 有一个属性称为 `Notification URI`，它指代某个特定数据点就像所有其它 URI 一样。一旦 **Cursor 设置为在 URI 发生变化时接收通知**。我们可以通过**对 URI 调用 notifyChange 方法发出 URI 数据发生了变化的信号**。

在 Provider 代码中的设置：

```java
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
...
// Set notification URI on the Cursor
// so we konw what content URI the Cursor was created for.
// If the data at this URI changes, then we konw we need to update the Cursor.
// 在 Cursor 上设置通知 URI，当 URI 的数据发生变化，我们就知道需要更新 Cursor
cursor.setNotificationUri(getContext().getContentResolver(), uri);
return cursor;
}

// 在 insert()、update() 和 delete() 方法中设置代码以使它们在 URI 发生变化时正确通知所有监听器

insert(){
...
// Notify all listeners that the data has changed for the pet content URI
getContext().getContentResolver().notifyChange(uri, null);
...
}

update(){
...
// Returns the number of database rows affected by the update statement
        updateCount = database.update(PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        // notify all listeners of changes
        if (updateCount > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
}

delete(){
...
deleteCount = database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
if (deleteCOunt > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
}
```

![image](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_imgs/20180704160242155.png)

分析过程：

首先 CatalogActivity 实现一个 `LoaderManager.LoaderCallbacks<Cursor>` 接口，重写默认的回调方法。在 onCreate() 方法中使用 `initLoader()` 方法初始化查询。后台框架将调用 `onCreateLoader()` 回调方法以初始化我们的 `CursorLoader` 对象，过程中使用 `ContentResolver`，找到适合的 `Provider`，在 PetProvider 的 `query()` 方法中通过 `UriMatcher` 找到数据库中正确的数据并获得正确的 `Cursor`。接下来使用 `setNotificationUri()` 方法，告诉我们需要监视的 URI，当数据源发生变化时它就会知道需要更新 Cursor，并将 Cursor 返回至 CursorLoader。比如表中新插入了宠物，Uri 会的通知，并转而告诉 Cursor 发生了变化通知更新，更新后的 Cursor 会被回传到 Provider 再到 ContentResolver 最后到 CursorLoader 中重新加载新信息，重新加载后将使用新的 Cursor 调用 onLoadFInishes，且 UI 得到更新。而 onLoaderReset() 用来删除旧的 Cursor，换入新的数据。

setNotificationUri()：http://developer.android.youdaxue.com/reference/android/database/Cursor#setNotificationUri%28android.content.ContentResolver,%20android.net.Uri%29

notifyChange()：https://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/
















