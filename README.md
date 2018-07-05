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

# SQLite

SQLite 是一种数据库，使我们的应用和与之交互的设备上创建一个本地数据库。Lite 一词是指典型数据库的轻量级版本（lightweight version）。对应地就存在着重量级数据库，例如 MySQL，它可以提供更加复杂的功能。SQLite 不需要服务器，数据存储在设备的本地文本文件里。SQLite 是免费开源的，并且是 Android 自带的数据库这就是为什么需要学习这一特定类型的数据库。

## SQL 数据值类型

|pattern|description|
|---|---|
|NULL|null|
|INTEGER|整型|
|REAL|浮点型|
|TEXT|字符型|
|BLOB|按照输入的样式存储起来，比如图片、二进制数|

## SQL 语法

在设置数据库表格前，我们都需要对需求进行分析，绘制出需要内容在 EXCEL 表格中，再来对照创建数据库表。比如
![image](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530775585720&di=754e168d92f5ea983a795a7cd40dadd5&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D4288014583%2C1299116437%26fm%3D214%26gp%3D0.jpg?raw=true)







