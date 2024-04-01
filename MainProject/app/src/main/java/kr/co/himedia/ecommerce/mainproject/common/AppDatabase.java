package kr.co.himedia.ecommerce.mainproject.common;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kr.co.himedia.ecommerce.mainproject.buy.dao.BuyDtlDao;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;


@Database(entities = {BuyDtlDto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BuyDtlDao buyDtlDao();

    private static AppDatabase INSTANCE = null;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "cart.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
