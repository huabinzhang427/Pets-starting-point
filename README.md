Android 数据库存储
-------
# Android 三种数据存储方式

|pattern|description|
|---|---|
|Files|保存大型媒体文件，例如图片、图书、歌曲或视频文件|
|SharedPreferences|保存用户偏好设置，比如聊天中消息提示类型|
|SQLite Database|数据库类型（有组织的数据集合），保存大量相关的结构化文本类数据，就像电子表格中的行和列结构|

注意，在很多应用中会结合使用上面表格中的三种存储方式，比如相机应用，它会使用文件来将照片保存到本地，同时使用 SQLite 数据库来存储元数据（信息的信息，包括照片时间，地点，说明信息等）。更多详细 Android 数据存储介绍可查看官方文档 http://developer.android.youdaxue.com/guide/topics/data/data-storage?utm_source=udacity&utm_medium=course&utm_campaign=android_basics
