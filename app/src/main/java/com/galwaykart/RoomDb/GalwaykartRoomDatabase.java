package com.galwaykart.RoomDb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.sqlite.db.SupportSQLiteDatabase;

//import com.galwaykart.dbfiles.DataAO.ProductDataModelDao;
import com.galwaykart.dbfiles.ProductDataModel;

//@Database(entities = {ProductDataModel.class}, version = 6)
//public abstract class GalwaykartRoomDatabase  {
//
//    private static volatile  GalwaykartRoomDatabase instance;
//  //  public abstract ProductDataModelDao productDataModelDao();
//
//    public static GalwaykartRoomDatabase getDatabase(final Context context){
//
//        if(instance==null){
//            synchronized (GalwaykartRoomDatabase.class){
//                if(instance== null){
//
////                    instance= Room.databaseBuilder(context.getApplicationContext(),
////                            GalwaykartRoomDatabase.class,"db_galwaykart")
////                            .addCallback(sRoomDatabaseCallback)
////                            .fallbackToDestructiveMigration()
////                            .build();
//                }
//            }
//        }
//        return instance;
//
//    }
//
//
////    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
////
////        @Override
////        public void onOpen(@NonNull SupportSQLiteDatabase db) {
////            super.onOpen(db);
////        }
////    };
//
//
//}
