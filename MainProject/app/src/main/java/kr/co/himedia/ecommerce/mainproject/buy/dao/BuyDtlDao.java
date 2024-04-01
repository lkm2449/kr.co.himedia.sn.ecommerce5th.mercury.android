package kr.co.himedia.ecommerce.mainproject.buy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;

@Dao
public interface BuyDtlDao {

    @Query("SELECT * FROM buyDtlDto")
    List<BuyDtlDto> getAll();

    @Insert
    void insert(BuyDtlDto buyDtlDto);

    @Query("SELECT count(*) FROM buydtldto WHERE seq_sle = :seq_sle")
    int selectCount(int seq_sle);

    @Query("DELETE FROM buydtldto WHERE seq_sle = :seq_sle")
    void deleteBySeqSle(int seq_sle);

    @Query("UPDATE buydtldto SET count = :count WHERE seq_sle = :seq_sle")
    void updateCount(int count, int seq_sle);

    @Query("DELETE FROM buydtldto")
    void deleteAll();

    @Query("SELECT count(*) FROM buydtldto")
    int selectCountAll();
}
