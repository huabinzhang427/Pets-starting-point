# 概述

在 Android 中需要采取两个基本步骤来设置 SQLite 就可以和数据库互动了。

 1. Define a schema and contract-创建架构和契约类
 2. Create a database using an SQLOpenHelper-使用 SQLiteOpenHelper 创建数据库 
 
 # 创建架构和契约类
 
 创建数据库架构其实就是需要**规划数据库结构**。所以我们需要问两个问题：
 
  1. 表格的名称是什么？
  2. 表格里列的名称和数据类型是什么？
 
## 创建 Contract (契约)类

为什么要使用 Contract 类？

总结使用 Contract 类的三个理由：

 - 帮助我们定义架构，规定了去哪里查找数据库常量；
 - 在生成 SQL 指令时，可以帮助我们避免拼写错误；
 - 使我们更容易更新数据库架构。
 
 根据之前规划的数据库结构即我们定义的架构，我们在项目的主包下面创建一个包名为 `data`，然后在改包下创建一个新的 java 类名为 `PetContract`，将该类用 `final` 修饰，因为它只是用来提供常量，我们不需要扩展或为此外部类实现任何内容。使用我们定义的架构，为每个表格创建一个内部类，并为每个列标题创建常量，代码如下所示：
 
 ```java
public final class PetContract {
	private PetContract() {}
	
	public static final class PetEntry implements BaseColumns {
		
		public final static String TABLE_NAME = "pets";
		
		public final static String _ID = BaseColumn._ID;
		public final static String COLUMN_PET_NAME = "name";
		public final static String COLUMN_PET_BREED = "breed";
		public final static String COLUMN_PET_GENDER = "gender";
		public final static String COLUMN_PET_WEIGHT = "weight";

		public final static int GENDER_UNKONWN = 0;
		public final static int GENDER_MALE = 1;
		public final static int GENDER_FORMALE = 2;
	}
}
```

## 使用 SQLiteOpenHelper 创建数据库 

在 `dada` 包中创建一个新的 `PetDbHelper` 类，该类继承自 `SQLiteOpenHelper`类。我们需要重写 `onCreate()` 和 `onUpGrade()` 方法，并且为数据库名称和版本创建常量，同时别忘了创建该类的构造函数，还要为用来创建表格的 SQLite 指令创建一个字符串常量。

```java
/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;
    
    // 使用 SQL 语句来创建和初始化架构
    private static final String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
            + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
            + PetEntry.COLUMN_PET_BREED + " TEXT , "
            + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
            + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
```

# 连接数据库流程

![image](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_imgs/20180627163307878.png)

```java
PetDbHelper mDbHelper = new PetDbHelper(this);
SQLiteDatabase db = mDbHelper.getReadableDatabase();
```
请求数据库，mDbHelper 将检查是否已经存在一个数据库？

如果不存在，则 PetDbHelper 的实例将使用 `onCreate()` 方法创建一个数据库，接着创建 SQLiteDatabase 的实例对象返回给 Activity。

如果数据库已经存在，PetDbHelper 的实例将不会调用 onCreate() 方法，相反，将创建一个 SQLiteDatabase 的实例对象并关联现有的数据库，然后将该对象返回给请求数据库的 Activity。

最终时帮助我们创建 SQLiteDatabase 对象与 shelter 数据库关联的 SQLiteDatabase 对象，之后我们就可以通过该对象向 shelter 数据库传达 SQLite 指令了。 

# 数据库对象及插入数据

在 Android 文档 [Put information into a database](https://developer.android.com/training/data-storage/sqlite#WriteDbRow) 中，为我们举出了例子。

```java
// Gets the data repository in write mode
SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
ContentValues values = new ContentValues();
values.put(FeedEntry.COLUMN_NAME_TITLE, title);
values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

// Insert the new row, returning the primary key value of the new row
long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
```

注意 `insert()` 方法会返回新插入的 ID，如果出现错误它会返回 -1。

# 数据库查询方法

```java
SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
String[] projection = {
    BaseColumns._ID,
    FeedEntry.COLUMN_NAME_TITLE,
    FeedEntry.COLUMN_NAME_SUBTITLE
    };

// Filter results WHERE "title" = 'My Title'
String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
String sortOrder =
    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

Cursor cursor = db.query(
    FeedEntry.TABLE_NAME,   // The table to query
    projection,             // The array of columns to return (pass null to get all)
    selection,              // The columns for the WHERE clause
    selectionArgs,          // The values for the WHERE clause
    null,                   // don't group the rows
    null,                   // don't filter by row groups
    sortOrder               // The sort order
    );
```

观察上述代码，我们发现首先定义了一个字符串数组 `projection`，它其实是指我们想要获取的列名称，就像这样的语句：

```
SELECT name, breed FROM pets;
```
定义 `projection` **使我们能够指定想要获取的具体列**，默认则获取所有列，类似于：

```
SELECT * FROM pets;
```

`projection` **大小会影响到性能**。

调用 `query()` 方法，该方法具有大量输入参数，代表了 SELECT 语句的不同部分。第一个参数 `projection` 我们已经在上面介绍了，接下来 `selection` 和 `selectionArgs` 处理的是可选 WHERE 条件。例如根据 ID 选择单个宠物：

```
SELECT * FROM pets WHERE _id = 1;
```
使用 `selection` 和 `selectionArgs` 属性就是这样的：

```java
// Define 'where' part of query
String selection = PetEntry._ID + "?";
// Specify arguments in placeholder order
String[] selectionArgs = {"1"}; 
```
`selection` 参数是 WHERE 关键字之后的类型为 String 的，这里使用 `?` 作为占位符，然后填充为 `selectionArgs` 参数中的值，它是一个字符串数组，负责替换这里 `selection` 中的问号，这里将其设为 1。

















