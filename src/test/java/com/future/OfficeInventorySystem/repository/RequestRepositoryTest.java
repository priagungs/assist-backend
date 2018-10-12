package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class RequestRepositoryTest {

    @Test
    public void findRequestByIdRequest() {
        Request request = new Request();

    }

    @Test
    public void findAllRequestByUser() {
    }

    @Test
    public void findAllRequestByStatus() {
    }

    @Test
    public void findAllRequestByStatusAndSuperior() {
    }
}