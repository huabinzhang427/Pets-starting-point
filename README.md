Android 数据库存储
-------
# 计算机内存和硬盘存储的区别

计算机有两种存储 - 临时和永久存储。计算机的内存用于临时存储，而计算机的硬盘用于永久存储。

**存储空间大小不同**。简单类比，内存就像桌面的工作空间，硬盘就像一个填充柜。您的文件柜通常足够大，可以存储数百个文件夹和数千张纸。您的桌面工作空间不足以容纳所有这些文件夹和文件。

**存储空间成本不同**。内存，RAM，比硬盘存储空间贵得多。这是一个粗略的比较。1 GB的RAM成本约为8美元，而1 GB的硬盘存储空间成本约为10美分。换句话说，RAM的成本大约是硬盘存储空间的8000！！！

**存储速度不同**。这个价格差异解释了为什么我们可以拥有比内存（RAM）存储空间更多的硬盘存储空间，但为什么我们需要两者？为什么计算机以这种方式设计，不断地来回复制数据到内存然后再回到硬盘？主要原因是RAM比硬盘存储空间快得多。我没有确切的数字，但RAM数千，可能比硬盘快几十或几十万倍。

更多相关内容可以查看链接信息 https://soundsupport.biz/2012/05/06/whats-the-difference-between-computer-memory-ram-and-hard-drive-storage/

# Android 三种数据存储方式

|pattern|description|
|---|---|
|Files|保存大型媒体文件，例如图片、图书、歌曲或视频文件|
|SharedPreferences|保存用户偏好设置，比如聊天中消息提示类型|
|SQLite Database|数据库类型（有组织的数据集合），保存大量相关的结构化文本类数据，就像电子表格中的行和列结构|

注意，在很多应用中会结合使用上面表格中的三种存储方式，比如相机应用，它会使用文件来将照片保存到本地，同时使用 SQLite 数据库来存储元数据（信息的信息，包括照片时间，地点，说明信息等）。更多详细 Android 数据存储介绍可查看官方文档 http://developer.android.youdaxue.com/guide/topics/data/data-storage?utm_source=udacity&utm_medium=course&utm_campaign=android_basics


