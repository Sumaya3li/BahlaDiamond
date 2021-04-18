package com.databoat.barcodescanner.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FormDao {

    @Insert
    void insert(Form form);

    @Query("SELECT * from data WHERE date_do = :date GROUP BY idst,date_do ORDER BY id")
    LiveData<List<Form>> getDataByDate(String date);

    @Query("SELECT * FROM data ORDER BY id DESC LIMIT 1")
    LiveData<Form> readPrevious();

    @Query("SELECT * FROM data WHERE idst = :id AND date_do = :date")
    LiveData<Form> getPrevious(String id, String date);

    @Update
    void update(Form form);

    @Query("SELECT * FROM data WHERE idst = :idst ORDER BY id DESC LIMIT 1")
    LiveData<Form> getLastReading(String idst);

    @Query("SELECT * FROM data WHERE date_do = :date")
    LiveData<List<Form>> getListPrevious(String date);

    //@Query("UPDATE data SET perusal_current=:perusalCurrent, perusal_previous=:perusal_previous, note = :note WHERE idst LIKE :idst AND date_do LIKE :date")
    //LiveData<Form> updateEntry(String idst, String perusal_previous, String perusalCurrent, String note, String date);

   // @Query("UPDATE data SET  idst=:idst,name_id=:name_id,perusal_previous=:perusal_previous,perusal_current=:perusal_current,idst_type=:idst_type,consumption=:consumption,note=:note,consumption=:consumption")
    //String updateItem ( String idst, String name_id, String perusal_previous, String perusal_current, String idst_type, String consumption, String note, String date_do );

}
