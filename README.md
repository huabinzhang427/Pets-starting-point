# Android 应用中的数据库存储

我们都知道，Android 自带的数据库是 SQLite 数据库，作为一种轻量级的数据库，可以使我们的应用和与之交互的设备上创建一个本地的数据库。

具体 SQLite 与 Android 的联系可以查看 [Android 数据库基础](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_mds/Android%20%E6%95%B0%E6%8D%AE%E5%BA%93%E5%9F%BA%E7%A1%80.md)

了解了SQLite 与 Android 的关系，我们就要根据我们的实际需求，按照步骤创建我们的数据库，实现交互。

1. Define a schema and contract-创建架构和契约类
2. Create a database using an SQLOpenHelper-使用 SQLiteOpenHelper 创建数据库

具体创建过程可查看 [Android 应用中使用数据库](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_mds/Android%20%E5%BA%94%E7%94%A8%E4%B8%AD%E4%BD%BF%E7%94%A8%E6%95%B0%E6%8D%AE%E5%BA%93.md)

为了集中对数据的访问和编辑，我们定义了 ContentProvider 来作为数据源与 UI 代码的附加层，也叫抽象层。同时我们可以在数据存储前做一些判定，防止无效数据的存储。

具体添加和使用 ContentProvider 过程可以查看 [Android 数据存储之 ContentProvider](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_mds/Android%20%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%E4%B9%8B%20ContentProvider.md)

在数据库的操作中，读取和写入数据到数据库的过程也被看作是一个耗时的操作，和为了请求一样，所以我们可以使用 CursorLoader 加载器，它可以在后台加载数据，加载完成后自动返回到前台，同时它也具备自动刷新的功能，这样节省了我们不必要的资源浪费，而这个功能最重要的就是 Uri 这个属性。

具体内容可以查看 [Android 数据存储之使用 CursorLOader 加载数据](https://github.com/huabinzhang427/Pets-starting-point/blob/master/readme_mds/Android%20%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%E4%B9%8B%E4%BD%BF%E7%94%A8%20CursorLOader%20%E5%8A%A0%E8%BD%BD%E6%95%B0%E6%8D%AE.md)

