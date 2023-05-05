package com.mu.store;

import com.mu.store.entity.Store;
import com.mu.store.mapper.StoreMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicaitonTest {



    @Autowired
    private StoreMapper storeMapper;
    @Test
    public void testSelectStoreCount(){
        int i = storeMapper.selectStoreCount("1", "001");
        System.out.println(i);
    }

    @Test
    public void testSelectVersion(){
        int i = storeMapper.selectVersion("1", "001");
        System.out.println(i);
    }

    @Test
    public void testUpdateStoreCountByVersion(){
        storeMapper.updateStoreCountByVersion(0,"1","001"
                ,"admin",new Date(System.currentTimeMillis()));
        Store store = storeMapper.selectByPrimaryKey("10000");
        System.out.println(store);
    }
}
