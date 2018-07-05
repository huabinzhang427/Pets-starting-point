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

![image](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_imgs/20180625222126775.png)

### 创建表格

```java
CREATE TABLE <table_name> (<column_name_1> <data_type_1>, <column_name_2> <data_type_2>, ...);
```

注意，**SQLite 关键字都是大写**。在每个表格里都应该具有 ID 来唯一标识对象，因为对象的其它属性可能会有重复，导致混乱。

### 查询表格

`sqlite> .tables`，查询表格，系统返回表格名称

`sqlite> .schema <table_name>`，返回创建该表格的语句

`sqlite> PRAGMA TABLE_INFO(headphones);`，显示所有列名称和值类型

### 删除表格

`DROP TABLE <table_name>;`，慎重使用。


以上图为例，SQL 语句如下

```java
sqlite> CREATE TABLE pets (_id INTEGER, name TEXT, breed TEXT, gender INTEGER, weight INTEGER);
```
创建好表格后，我们就可以开始向表格中添加行，并读取和操作其中的数据。

添加：
```java
INSERT INFO <table_name> (<columns_name_1>, <columns_name_2>, ...) VALUES (<value_1>, <value_2>, ...);

INSERT INTO pets (_id, name, breed, gender, weight) VALUES (1, "Tommy", "pomeranian", 1, 4);
```
查询：
```java
sqlite> .header on
sqlite> .mode column

SELECT <columns> FROM <table_name>
// * 通配符，所有行和列
SELECT * FROM pets;
_id         name        breed       gender      weight    
----------  ----------  ----------  ----------  ----------
1           Tommy       pomeranian  1           4         
2           Garfield    Tabby       2           8  
1           Binx        Bombay      1           14
```
从输出内容可以看出，本来应该唯一的 ID，不再唯一。该怎么办？

### SQL 关键字

SQL 提供了便捷的关键字：`PRIMARY KEY`、`AUTOINCREMENT`、`NOT NULL`、`DEFAULT <value>`。

```java
sqlite> CREATE TABLE headphones (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, style INTEGER, in_stock INTEGER, description TEXT);
```
**PRIMARY KEY**：将相关列标为唯一标识的行，该属性通常与表示每个表格只能有一个主键。它可以确保唯一性。同时添加 **AUTO INCREMENT** 表示没有插入 ID 也会自动使其加1，无需指定数字。

```java
sqlite> INSERT INTO headphones (name, price, style, in_stock, description) VALUES ("DJ Bliss Red Headphones", 7198, 2, 1, "These awesome headphones will make you feel like a DJ");
sqlite> INSERT INTO headphones (name, price, style, in_stock, description) VALUES ("CityRunner Active Wireless Headphones", 17198,3, 0, "On the go city dwellers will love these wireless headphones");

sqlite> SELECT * FROM headphones;
_id         name                     price       style       in_stock    description                                          
----------  -----------------------  ----------  ----------  ----------  -----------------------------------------------------
1           DJ Bliss Red Headphones  7198        2           1           These awesome headphones will make you feel like a DJ
2           CityRunner Active Wirel  17198       3           0           On the go city dwellers will love these wireless head
```
在上面的设置下，如果再插入一条 ID 为 1 的数据，系统就会提示错误

```java
sqlite> INSERT INTO headphones (_id, name, price, style, in_stock, description) VALUES (1, "CityRunner Active Wireless Headphones", 17198,3, 0, "On the go city dwellers will love these wireless headphones");
Error: UNIQUE constraint failed: headphones._id
```
**NOT NULL**：表示向表格中插入某个值时，必须具有相关的值，不能为空。
**DEFAULT**：表示在没有给定值时会添加一个默认值。

条件查询：

```java
// 选择
SELECT FROM <table_name> WHERE 条件；
// 排序
SELECT * FROM <table_name> ORDER BY 列属性 ASC/DESC（升序/降序）
```

选择更新：

```java
// 条件使我们的指令更加具体，指定需要的
UPDATE <table_name> SET 更新内容 条件；
DELETE FROM <table_name>;
```






