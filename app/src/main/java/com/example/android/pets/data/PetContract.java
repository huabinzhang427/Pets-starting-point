package com.example.android.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * author: zhanghuabin
 * date: 2018/6/29 13:51
 * github: https://github.com/zhanghuabin-sh
 * email: 2908882095@qq.com
 *
 * 1、一个外部类
 * 2、数据库中的每个表格还需要一个内部类，那个内部类实现 BaseColumns 类
 * 3、Contract 中应该包含表格名称和标题或列名称的字符串常量
 * 4、针对具体案例情形，创建需要的常量。
 */
public final class PetContract {

    public PetContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PETS = "pets";

    public static final class PetEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        // 表格名称
        public final static String TABLE_NAME = "pets";

        // 唯一的 ID
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PET_NAME = "name";

        public final static String COLUMN_PET_BREED = "breed";

        public final static String COLUMN_PET_GENDER = "gender";

        public final static String COLUMN_PET_WEIGHT = "weight";


        // Possible values for the gender of the pet
        public final static int GENDER_UNKNOWN = 0;

        public final static int GENDER_MALE = 1;

        public final static int GENDER_FEMALE = 2;

        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         * */
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_FEMALE || gender == GENDER_MALE) {
                return true;
            }
            return false;
        }

    }




}
