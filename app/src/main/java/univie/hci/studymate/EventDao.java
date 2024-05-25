package univie.hci.studymate;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(EventEntity event);
    @Query("SELECT * FROM events WHERE date = :selectedDate")
     List<EventEntity> getEventsByDate(String selectedDate);

}

