package com.example.yogyakarta_app.Data.Db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.yogyakarta_app.Data.Db.Entity.Article;
import com.example.yogyakarta_app.Data.Db.Entity.Info;
import com.example.yogyakarta_app.Data.Db.Entity.Tour;
import com.example.yogyakarta_app.Features.Article.Model.ArticleDao;
import com.example.yogyakarta_app.Features.Info.Model.InfoDao;
import com.example.yogyakarta_app.Features.Tour.Model.TourDao;

@Database(entities = {Article.class,Tour.class, Info.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;

    public abstract ArticleDao articleDao();

    public abstract TourDao tourDao();

    public abstract InfoDao infoDao();

    public static synchronized LocalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, "app_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback =new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private ArticleDao articleDao;
        private TourDao tourDao;
        private InfoDao infoDao;

        public PopulateDbAsyncTask(LocalDatabase db) {
            articleDao = db.articleDao();
            tourDao = db.tourDao();
            infoDao = db.infoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            articleDao.insert(new Article("Maulana Ibrahim","Passion is number one","bagaimana cara mencari passion ? paling gampang yaitu ketika kita mengerjakan sesuatu kita merasa tidak terbebani dan malah suka, that is passion"));
            tourDao.insert(new Tour("Candi Prambanan","Jl. Raya Solo - Yogyakarta No.16, Kranggan, Bokoharjo, Kec. Prambanan, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55571","http://bob.kemenpar.go.id/wp-content/uploads/2019/10/candi-prambanan-1.jpg",-7.7520206,110.4892787));
            tourDao.insert(new Tour("Pasar Malioboro","Jl. Ahmad Yani, Ngupasan, Kec. Gondomanan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55122","https://cdn2.tstatic.net/jogja/foto/bank/images/sepanjang-jalan-malioboro.jpg",-7.8074743,110.3442387));
            tourDao.insert(new Tour("Taman Pintar Yogyakarta","Jl. Panembahan Senopati No.1-3, Ngupasan, Kec. Gondomanan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55122","https://static.palingseru.com/2018/01/a-1-1024x685.jpg",-7.8006807,110.3655265));
            infoDao.insert(new Info("telah hilang stnk atas nama maulana ibrahim di sekitaran alun-alun bagi yang menemukan bisa wa 0895328892402","2020-01-20 11:21","Maulana Ibrahim"));
            return null;
        }
    }
}
