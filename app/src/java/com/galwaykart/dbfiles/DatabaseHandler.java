package com.galwaykart.dbfiles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.galwaykart.Cart.CartItemList;
import com.galwaykart.dbfiles.DbBeanClass.CartItem;
import com.galwaykart.dbfiles.DbBeanClass.CartProductImage;
import com.galwaykart.dbfiles.DbBeanClass.CategoryList;
import com.galwaykart.dbfiles.DbBeanClass.CategoryProductBeanClass;
import com.galwaykart.dbfiles.DbBeanClass.LoginBeanClass;
import com.galwaykart.dbfiles.DbBeanClass.ProductBeanClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumitsaini on 9/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION =11;

    // Database Name
    private static final String DATABASE_NAME = "dbGalwayKart";

//    // Login table name
    private static final String TABLE_LOGIN = "tbl_user_login";

    // Product table name
    private static final String TABLE_PRODUCT = "tbl_product";

    // Category Product table name
    private static final String TABLE_CAT_PRODUCT = "tbl_category_product";


    /******************************************/
    // Category List table
    private static final String TABLE_category_list= "tbl_category_list";

    /**
     * Cart Item list
     */
    private static final String TABLE_cart="tbl_cart";

    //cart added product image path
    private static final String TABLE_CART_PRODUCT = "tbl_cart_product";

    /**
     *  Category Main List
     *  Ankesh Kumar
     */
    private static final String TAG_cat_name="catname";
    private static final String TAG_cat_sub_name="subcatname";
    private static final String TAG_cat_sub_id="subcatid";

    /**
     * In cart
     * Ankesh Kumar
     */
    private static final String TAG_cart_sku="sku";
    private static final String TAG_cart_name="pname";
    private static final String TAG_cart_image="image";
    private static final String TAG_cart_qty="qty";
    private static final String TAG_cart_price="price";
//    private static final String TAG_cart_dp_price="dp_price";
//    private static final String TAG_cart_ip="product_ip";


    /**
     * Cart added Item
     */
    private static final String TAG_psku= "sku";
    private static final String TAG_image= "image";



    // Login Table Columns names
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_LNAME = "lName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NO = "phone_no";

    // Product Table Columns names
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_SKU = "sku";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMAGE= "image";
    private static final String KEY_QTY = "qty";

    // Product Table Columns names
    private static final String KEY_CAT_ID = "cat_id";
    private static final String KEY_CAT_NAME = "cat_name";
    private static final String KEY_CAT_IMAGE = "cat_image_path";


    /**
     *
     *
     *
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

 //********************************************* CREATE TABLES *******************************************************************

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_CUSTOMER_ID + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_FNAME + " TEXT,"
                + KEY_LNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE_NO + " TEXT)";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_ITEM_ID + " TEXT,"
                + KEY_SKU + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_QTY + " TEXT)";

        String CREATE_CAT_PRODUCT_TABLE = "CREATE TABLE " + TABLE_CAT_PRODUCT + "("
                + KEY_CAT_ID + " TEXT,"
                + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_IMAGE + " TEXT)";


        String CREATE_Cart_item_TABLE = "CREATE TABLE " + TABLE_cart+ "("
                + TAG_cart_sku + " TEXT,"
                + TAG_cart_name + " TEXT,"
                + TAG_cart_image + " TEXT,"
                + TAG_cart_qty + " TEXT,"
                + TAG_cart_price+ " TEXT)";
//                + TAG_cart_dp_price+ " TEXT,"
//                + TAG_cart_ip+ " TEXT)";


        String CREATE_Category_List_TABLE = "CREATE TABLE " + TABLE_category_list+ "("
                + TAG_cat_name + " TEXT,"
                + TAG_cat_sub_id+ " TEXT)";


        db.execSQL(CREATE_Cart_item_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_CAT_PRODUCT_TABLE);
//        db.execSQL(CREATE_Category_List_TABLE);


        /**
         * Incentive
         */
        String CREATE_Cart_TABLE = "CREATE TABLE " + TABLE_CART_PRODUCT + "("
                + TAG_psku + " TEXT," +  TAG_image+ " TEXT"
                + ")";
        db.execSQL(CREATE_Cart_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_cart);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_category_list);

        // Create tables again
        onCreate(db);

    }

    /**
     * Add Category List
     */
    public void addCategoryList(CategoryList catList){
        String TAG_cat_name="catname";
        String TAG_cat_sub_name="subcatname";
        String TAG_cat_sub_id="subcatid";

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TAG_cat_name, catList.getTAG_cat_name());
        values.put(TAG_cat_sub_name, catList.getTAG_cat_sub_name());
        values.put(TAG_cat_sub_id, catList.getTAG_cat_sub_id());

        db.insert(TABLE_cart, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Add Cart Product
     */
    public void addCartProduct(CartItem cartItem){
        String TAG_cart_sku="sku";
        String TAG_cart_name="pname";
        String TAG_cart_image="image";
        String TAG_cart_qty="qty";
        String TAG_cart_price="price";
//        String TAG_cart_dp_price="dp_price";
//        String TAG_cart_ip="product_ip";

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TAG_cart_sku, cartItem.getTAG_cart_sku());
        values.put(TAG_cart_name, cartItem.getTAG_cart_name());
        values.put(TAG_cart_image, cartItem.getTAG_cart_image());
        values.put(TAG_cart_qty, cartItem.getTAG_cart_qty());
        values.put(TAG_cart_price, cartItem.getTAG_cart_price());
//        values.put(TAG_cart_dp_price, cartItem.getTAG_cart_dp_price());
//        values.put(TAG_cart_ip, cartItem.getTAG_cart_ip());

        db.insert(TABLE_cart, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Add New Cart Product Image
     */
    public void addCartProductImage(CartProductImage productImage){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TAG_psku, productImage.get_sku());
        values.put(TAG_image, productImage.get_image());

        db.insert(TABLE_CART_PRODUCT, null, values);
        db.close(); // Closing database connection
    }



    /**
     * Fetch all cart product
     * @param
     * @return
     */
    public List<CategoryList> getAllCategoryList()
    {

        List<CategoryList> contactList=new ArrayList<CategoryList>();
        String selectQuery="Select * from "+ TABLE_category_list;

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

/**
 *  looping through all rows and adding to list
 */
        if (cursor.moveToFirst()) {
            do {
                CategoryList contact = new CategoryList();

                contact.setTAG_cat_name(cursor.getString(0));
                contact.setTAG_cat_sub_name(cursor.getString(1));
                contact.setTAG_cat_sub_id(cursor.getString(2));
                contactList.add(contact);

            }while(cursor.moveToNext());
        }

        return contactList;
    }

    /**
     * Fetch all cart product
     * @param
     * @return
     */
    public List<CartItem> getallCartItem()
    {

        List<CartItem> contactList=new ArrayList<CartItem>();
        String selectQuery="Select * from "+ TABLE_cart;

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

/**
 *  looping through all rows and adding to list
 */
        if (cursor.moveToFirst()) {
            do {
                CartItem contact = new CartItem();

                contact.setTAG_cart_sku(cursor.getString(0));
                contact.setTAG_cart_name(cursor.getString(1));
                contact.setTAG_cart_image(cursor.getString(2));
                contact.setTAG_cart_qty(cursor.getString(3));
                contact.setTAG_cart_price(cursor.getString(4));
//                contact.setTAG_cart_dp_price(cursor.getString(5));
//                contact.setTAG_cart_ip(cursor.getString(6));
                contactList.add(contact);

            }while(cursor.moveToNext());
        }

        return contactList;
    }


    /**
     * Fetch all cart product image
     * @param
     * @return
     */
    public List<CartProductImage> getallCartProductImage()
    {

        List<CartProductImage> contactList=new ArrayList<CartProductImage>();
        String selectQuery="Select * from "+ TABLE_CART_PRODUCT;

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

/**
 *  looping through all rows and adding to list
 */
        if (cursor.moveToFirst()) {
            do {
                CartProductImage contact = new CartProductImage();

                contact.set_sku(cursor.getString(0));
                contact.set_image(cursor.getString(1));

                contactList.add(contact);
            }while(cursor.moveToNext());
        }

        return contactList;
    }



    /**
     * Get  image from passing id
     * @param
     * @return
     */

    /**
     *  Getting All Products with id
     * @param pid
     * @return
     */
    public List<CartProductImage> getCartProductImage(String pid) {
        List<CartProductImage> contactList = new ArrayList<CartProductImage>();
        /**
         *  Select All Query
         */
        String selectQuery = "SELECT  * FROM " + TABLE_CART_PRODUCT +" where sku='"+pid+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        /**
         *  looping through all rows and adding to list
         */
        if (cursor.moveToFirst()) {
            do {
                CartProductImage contact = new CartProductImage();
                contact.set_sku(cursor.getString(0));
                contact.set_image(cursor.getString(1));
                /**
                 *  Adding product to list
                 */
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        /**
         *  return product list
         */
        return contactList;
    }

    /**
     * Delete
     * @param
     * @return
     */
    /**
     * Delete all data KRA
     */
    public void deleteCartProductImage(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CART_PRODUCT);
        db.close();
    }

    /**
     * Delete sel data KRA
     */
    public void deleteCategoryList(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_category_list);
        db.close();
    }

    /**
     * Delete sel data KRA
     */
    public void deleteCartItem(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_cart );
        db.close();
    }

    /**
     * Delete
     * @param
     * @return
     */
    /**
     * Delete sel data KRA
     */
    public void deleteCartProductImageSKu(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CART_PRODUCT+" where sku='"+id);
        db.close();
    }

    /**
     * Get Category item count
     */
    public int getCategoryListCount(){

        String countQuery = "SELECT  * FROM " + TABLE_category_list;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int crcount=cursor.getCount();
        cursor.close();

        // return count
        return crcount;
    }

    /**
     * Count
     * @param
     * @return
     */
    /**
     * Get Cart item count
     */
    public int getCartItemCount(){

        String countQuery = "SELECT  * FROM " + TABLE_cart;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int crcount=cursor.getCount();
        cursor.close();

        // return count
        return crcount;
    }


    public int getProductCountByCatId(String catid){

        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT+" where item_id='"+catid+"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int crcount=cursor.getCount();
        cursor.close();

        // return count
        return crcount;
    }


    public int getProductCount(){

        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int crcount=cursor.getCount();
        cursor.close();

        // return count
        return crcount;
    }

    /**
     * Count
     * @param
     * @return
     */
    /**
     * Get Attendance
     */
    public int getCartProductImageSKuCount(){

        String countQuery = "SELECT  * FROM " + TABLE_CART_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int crcount=cursor.getCount();
        cursor.close();

        // return count
        return crcount;
    }


    public boolean insertLoginDetails(LoginBeanClass loginBeanClass){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_CUSTOMER_ID,loginBeanClass.getCustomer_id());
        cv.put(KEY_EMAIL,loginBeanClass.getEmail_id());
        cv.put(KEY_FNAME,loginBeanClass.getfName());
        cv.put(KEY_LNAME,loginBeanClass.getlName());
        cv.put(KEY_PASSWORD,loginBeanClass.getPassword());
        cv.put(KEY_PHONE_NO,loginBeanClass.getPhone_no());

        database.insert(TABLE_LOGIN,null,cv);
//        database.close();
        return true;

    }



    public boolean insertProductDetails(ProductBeanClass productBeanClass){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_ITEM_ID,productBeanClass.getItem_id());
        cv.put(KEY_SKU,productBeanClass.getSku());
        cv.put(KEY_NAME,productBeanClass.getName());
        cv.put(KEY_IMAGE,productBeanClass.getImage());
        cv.put(KEY_PRICE,productBeanClass.getPrice());
        cv.put(KEY_QTY,productBeanClass.getQty());


        database.insert(TABLE_PRODUCT,null,cv);
//        database.close();
        return true;

    }

    public boolean insertCatProductDetails(CategoryProductBeanClass categoryProductBeanClass){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_CAT_ID,categoryProductBeanClass.getCat_id());
        cv.put(KEY_CAT_NAME,categoryProductBeanClass.getCat_name());
        cv.put(KEY_CAT_IMAGE,categoryProductBeanClass.getCat_image_path());



        database.insert(TABLE_CAT_PRODUCT,null,cv);
//        database.close();
        return true;

    }
//************************************* FATCH SINGLE DATA *************************************************************************

    // Getting single user
    public void getSingleUser(String customer_id){

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_LOGIN,
                                        new String[]{KEY_CUSTOMER_ID,KEY_EMAIL,KEY_FNAME,KEY_LNAME,KEY_PASSWORD,KEY_PHONE_NO},
                                        KEY_CUSTOMER_ID + "=?",
                                        new String[] {customer_id},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        LoginBeanClass loginBeanClass = new LoginBeanClass();

        String c_id = cursor.getString(0);
        String email = cursor.getString(1);
        String fname = cursor.getString(2);
        String lName = cursor.getString(3);
        String pass = cursor.getString(4);
        String ph = cursor.getString(5);

        loginBeanClass.setCustomer_id(cursor.getString(0));
        loginBeanClass.setEmail_id(cursor.getString(1));
        loginBeanClass.setfName(cursor.getString(2));
        loginBeanClass.setlName(cursor.getString(3));
        loginBeanClass.setPassword(cursor.getString(4));
        loginBeanClass.setPhone_no(cursor.getString(5));


    }

    // Getting Single Product
    public ProductBeanClass getSingleProduct(String item_id){

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_PRODUCT,
                new String[]{KEY_ITEM_ID,KEY_SKU,KEY_NAME,KEY_IMAGE,KEY_PRICE,KEY_QTY},
                KEY_ITEM_ID + "=?",
                new String[] {item_id},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        ProductBeanClass productBeanClass = new ProductBeanClass(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        return productBeanClass;
    }

    // Getting Single Product
    public CategoryProductBeanClass getSingleCategory(String cat_id){

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_PRODUCT,
                new String[]{KEY_CAT_ID,KEY_CAT_NAME,KEY_CAT_IMAGE},
                KEY_CAT_ID + "=?",
                new String[] {cat_id},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        CategoryProductBeanClass categoryProductBeanClass= new CategoryProductBeanClass(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2));

        return categoryProductBeanClass;
    }

//*************************************************FATCH ALL DATA****************************************************************************

    // Getting All Users
    public List<LoginBeanClass> getAllUser() {

        List<LoginBeanClass> users_list = new ArrayList<LoginBeanClass>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LoginBeanClass loginBeanClass = new LoginBeanClass();

                loginBeanClass.setCustomer_id(cursor.getString(0));
                loginBeanClass.setEmail_id(cursor.getString(1));
                loginBeanClass.setfName(cursor.getString(2));
                loginBeanClass.setlName(cursor.getString(3));
                loginBeanClass.setPassword(cursor.getString(4));
                loginBeanClass.setPhone_no(cursor.getString(5));
                // Adding contact to list
                users_list.add(loginBeanClass);
            } while (cursor.moveToNext());
        }
        return users_list;
    }



    // Getting All PRODUCT
    public List<ProductBeanClass> getAllProduct() {

        List<ProductBeanClass> product_list = new ArrayList<ProductBeanClass>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductBeanClass productBeanClass= new ProductBeanClass();

                productBeanClass.setItem_id(cursor.getString(0));
                productBeanClass.setSku(cursor.getString(1));
                productBeanClass.setName(cursor.getString(2));
                productBeanClass.setImage(cursor.getString(3));
                productBeanClass.setPrice(cursor.getString(4));
                productBeanClass.setQty(cursor.getString(5));

                // Adding contact to list
                product_list.add(productBeanClass);
            } while (cursor.moveToNext());
        }
        return product_list;
    }


    // Getting All PRODUCT
    public List<ProductBeanClass> getAllProductByCatId(String catid) {

        List<ProductBeanClass> product_list = new ArrayList<ProductBeanClass>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT+" where item_id='"+catid+"' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductBeanClass productBeanClass= new ProductBeanClass();

                productBeanClass.setItem_id(cursor.getString(0));
                productBeanClass.setSku(cursor.getString(1));
                productBeanClass.setName(cursor.getString(2));
                productBeanClass.setImage(cursor.getString(3));
                productBeanClass.setPrice(cursor.getString(4));
                productBeanClass.setQty(cursor.getString(5));

                // Adding contact to list
                product_list.add(productBeanClass);
            } while (cursor.moveToNext());
        }
        return product_list;
    }


    // Getting All CATEGORY
    public List<CategoryProductBeanClass> getAllCategory() {

        List<CategoryProductBeanClass> category_list = new ArrayList<CategoryProductBeanClass>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CAT_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CategoryProductBeanClass categoryProductBeanClass= new CategoryProductBeanClass();

                categoryProductBeanClass.setCat_id(cursor.getString(0));
                categoryProductBeanClass.setCat_name(cursor.getString(1));
                categoryProductBeanClass.setCat_image_path(cursor.getString(2));

                // Adding contact to list
                category_list.add(categoryProductBeanClass);
            } while (cursor.moveToNext());
        }
        return category_list;
    }

//***************************************************DELETE SINGLE DATA****************************************************************************


    // Deleting single User
    public void deleteSingleUser(String cat_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOGIN, KEY_CUSTOMER_ID + " = ?",
                new String[] {cat_id});

        db.close();
    }

    // Deleting single Product
    public void deleteAllProduct(ProductBeanClass productBeanClass) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_PRODUCT);
        db.close();
    }


    // Deleting single Product
    public void deleteSingleProduct(ProductBeanClass productBeanClass) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PRODUCT, KEY_ITEM_ID + " = ?",
                new String[] {productBeanClass.getItem_id() });

        db.close();
    }

    // Deleting single Product
    public void deleteSingleCategory(CategoryProductBeanClass categoryProductBeanClass) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CAT_PRODUCT, KEY_CAT_ID + " = ?",
                new String[] {categoryProductBeanClass.getCat_id() });

        db.close();
    }


//***************************************************DELETE ALL DATA****************************************************************************



  public void deleteAllData(){

      SQLiteDatabase db = this.getWritableDatabase();

      //db.execSQL("delete from "+ TABLE_LOGIN);
      db.execSQL("delete from "+ TABLE_PRODUCT);
      db.execSQL("delete from "+TABLE_CART_PRODUCT);
      db.execSQL("delete from "+TABLE_cart);

      //db.execSQL("delete from "+ TABLE_CAT_PRODUCT);
  }


}
