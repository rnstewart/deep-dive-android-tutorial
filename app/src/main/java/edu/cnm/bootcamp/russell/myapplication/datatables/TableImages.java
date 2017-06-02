package edu.cnm.bootcamp.russell.myapplication.datatables;

/**
 * Created by russell on 5/28/17.
 */

public class TableImages {
    public static final String NAME = "images_table";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LINK = "link";
    public static final String COL_DATETIME = "datetime";
    public static final String COL_VIEWS = "views";
    public static final String COL_SCORE = "score";
    public static final String COL_COVER = "cover";
    public static final String COL_SECTION = "section";
    public static final String COL_IN_GALLERY = "in_gallery";

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS "
            + NAME
            + " (_id integer primary key autoincrement, "
            + COL_ID + " int, "
            + COL_TITLE + " text, "
            + COL_DESCRIPTION + " text, "
            + COL_LINK + " text, "
            + COL_DATETIME + " int, "
            + COL_VIEWS + " int, "
            + COL_SCORE + " int, "
            + COL_COVER + " text, "
            + COL_SECTION + " text,"
            + COL_IN_GALLERY + " int);";
}
