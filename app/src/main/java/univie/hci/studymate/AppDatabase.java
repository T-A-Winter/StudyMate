package univie.hci.studymate;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EventEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();

}

