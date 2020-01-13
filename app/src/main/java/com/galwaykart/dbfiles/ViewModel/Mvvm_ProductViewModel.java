package com.galwaykart.dbfiles.ViewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


//import com.galwaykart.RoomDb.GalwaykartRoomDatabase;
//import com.galwaykart.dbfiles.DataAO.ProductDataModelDao;
import com.galwaykart.dbfiles.ProductDataModel;
import com.galwaykart.dbfiles.Repository.MVVM_HomePage_TopProductRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class Mvvm_ProductViewModel extends AndroidViewModel {

    private MutableLiveData<List<ProductDataModel>> products;
    private LiveData<List<ProductDataModel>> liveProducts;


    private MVVM_HomePage_TopProductRepo repo;
   // private ProductDataModelDao productDataModelDao;
    //GalwaykartRoomDatabase db;
    public Mvvm_ProductViewModel(@NonNull Application application) {
        super(application);
 }



    public void init(){
        if(products!=null){
            return;
        }
        repo= MVVM_HomePage_TopProductRepo.getInstance();

    }

    static int num_count=0;

//    private static class getProductCountAsyncTask extends AsyncTask<Void, Void, Integer> {
//      //  private ProductDataModelDao mAsyncTaskDao;
//
//     //   getProductCountAsyncTask(ProductDataModelDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Integer doInBackground(Void... voids) {
//             num_count=mAsyncTaskDao.itemCount();
//
//             return num_count;
//            //return null;
//        }
//    }


    public LiveData<List<ProductDataModel>> getLiveProduct()
    {

       // db=GalwaykartRoomDatabase.getDatabase(getApplication());
       // productDataModelDao=db.productDataModelDao();

       // try {
//            AsyncTask<Void, Void, Integer> num_of_rows=new getProductCountAsyncTask(productDataModelDao).execute();
//            int nRows=num_of_rows.get();
//
//            //Log.d("mvvmDao","Total "+ String.valueOf(nRows));

//            if(nRows>0){
//
//                liveProducts=productDataModelDao.getAllProducts();
//                liveProducts=getProduct();
//            }
//            else
//            {
                liveProducts=getProduct();
            //}
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //new getProductCountAsyncTask(productDataModelDao).execute();

       // if(liveProducts.getValue()!=null)
        //    //Log.d("mvvmDao", String.valueOf(productDataModelDao.getAllProducts().getValue().size()));
       //if(liveProducts){
        //   liveProducts= getProduct();
       // }
        //else
         //   return liveProducts;

        return liveProducts;
    }


    /**
     * Get ProductList for Homepage TopProducts
     * @return
     */
    public LiveData<List<ProductDataModel>> getProduct()
    {
        if(products==null){
            //Log.d("mvvmDao","fromapi");
            products=new MutableLiveData<List<ProductDataModel>>();
        }



       // db=GalwaykartRoomDatabase.getDatabase(getApplication());
      //  productDataModelDao=db.productDataModelDao();
        loadProducts();

        return products;

    }

    /**
     * Get Products for HomePage TopProducts
     */
    private void loadProducts(){
        products=repo.getProduct(getApplication());
    }


    /*****************************************************************/

    /**
     * Get Products for Category
     * @return
     */
    public MutableLiveData<List<ProductDataModel>> getProductOfCategory(String s_query,Boolean is_search){
        if(products==null){
            products=new MutableLiveData<List<ProductDataModel>>();



        }
        loadProductDetail(s_query,is_search);
        return  products;
    }



    public void loadProductDetail(String s_query,Boolean is_search){

        products=repo.getProductDetail(getApplication(),s_query,is_search);

    }




    /**
     * Sorting
     * 1->Name
     * 2->Price
     * 3->IP
     * @param sortBy
     * @return
     */
    public MutableLiveData<List<ProductDataModel>> sortProducts(int sortBy){

        if(products!=null){
           products=repo.sortByPrice(getApplication(),products,sortBy);
       }
       return products;
    }




}
