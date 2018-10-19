package com.future.OfficeInventorySystem.repository;

import com.future.OfficeInventorySystem.model.Item;
import com.future.OfficeInventorySystem.model.Request;
import com.future.OfficeInventorySystem.model.User;
import com.future.OfficeInventorySystem.model.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.junit.Assert.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class RequestTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RequestRepository requestRepository;


    private Request request;
    private User user1;
    private User user2;
    private User user3;
    private Item item;

    @Before
    public void setUp(){
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPicture("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setSuperior(null);
        user1.setIsAdmin(false);

        item = new Item();
        item.setItemName("indomie");
        item.setPicture("image.jpg");
        item.setPrice(1000);
        item.setTotalQty(10);
        item.setAvailableQty(1);
        item.setDescription("micin");

        user2 = new User();
        user2.setUsername("rickykennedy25");
        user2.setPassword("hashedpassword");
        user2.setName("Ricky Kennedy");
        user2.setPicture("ini link foto");
        user2.setRole("System Analyst");
        user2.setDivision("Technology");
        user2.setSuperior(null);
        user2.setIsAdmin(true);

        user3 = new User();
        user3.setUsername("priagungs");
        user3.setPassword("hashedpassword");
        user3.setName("Priagung Satyagama");
        user3.setPicture("ini link foto");
        user3.setRole("Data Scientist");
        user3.setDivision("Technology");
        user3.setSuperior(user1);
        user3.setIsAdmin(false);

        request = new Request();
        request.setUser(user3);
        request.setItem(item);
        request.setRequestDate(new Date());
        request.setReqQty(2);
        request.setStatus(Status.SENT);


        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(item);
        entityManager.persist(request);
    }

    @Test
    public void findRequestByIdRequest() {
        Long idReq = new Long(request.getIdRequest());
        assertNull(
                requestRepository.findById(
                        Long.valueOf(111111)).get());
        assertNotNull(requestRepository.findById(idReq).get());
        assertEquals(request,
                requestRepository.findById(idReq).get()
        );

    }


    @Test
    public void findAllRequestByUser() {

        List<Request> listRequest = requestRepository
                .findAllRequestByUser(user3,
                        new PageRequest(0, 1))
                .getContent();

        assertEquals(0, requestRepository
                .findAllRequestByUser(user2, new PageRequest(0, 1))
                .getContent()
                .size());
        assertEquals(1, requestRepository
                .findAllRequestByUser(user3, new PageRequest(0, 1))
                .getContent()
                .size());
        assertEquals(request,listRequest.get(0));

    }

    @Test
    public void findAllRequestByStatus() {
        List<Request> listRequest = requestRepository
                .findAllRequestByStatus(Status.SENT, new PageRequest(0, 1))
                .getContent();

        assertEquals(new ArrayList<Request>(),
                requestRepository
                        .findAllRequestByStatus(Status.REQUESTED, new PageRequest(0, 1))
                        .getContent());
        assertEquals(new ArrayList<Request>(),
                requestRepository
                        .findAllRequestByStatus(Status.APPROVED, new PageRequest(0, 1))
                        .getContent());
        assertEquals(0,
                requestRepository
                        .findAllRequestByStatus(Status.REJECTED, new PageRequest(0, 1))
                        .getContent()
                        .size());
        assertTrue(requestRepository
                .findAllRequestByStatus(Status.SENT, new PageRequest(0, 1))
                .getContent()
                .size() > 0);
        assertEquals(request,listRequest.get(0));
    }

    @Test
    public void findAllRequestByStatusAndSuperior() {
        Pageable pageRequest = new PageRequest(0, 1);
        List<Request> listRequest = requestRepository
                .findAllRequestByStatusAndSuperior(Status.SENT, user1, pageRequest)
                .getContent();

        assertEquals(0,
                requestRepository
                        .findAllRequestByStatusAndSuperior(Status.REQUESTED, user1, pageRequest)
                        .getContent()
                        .size());
        assertEquals(new ArrayList<Request>(),
                requestRepository
                        .findAllRequestByStatusAndSuperior(Status.APPROVED, user1, pageRequest)
                        .getContent());
        assertEquals(0,
                requestRepository
                        .findAllRequestByStatusAndSuperior(Status.REJECTED, user1, pageRequest)
                        .getContent()
                        .size());
        assertTrue(
                requestRepository
                        .findAllRequestByStatusAndSuperior(Status.SENT, user1, pageRequest)
                        .getContent()
                        .size() > 0);
        assertEquals(request,listRequest.get(0));
    }

    @Test
    public void testSaveRequest() {

        requestRepository.save(request);

        List<Request> requests = requestRepository.findAll();

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(request, requests.get(0));

    }

    @Test
    public void testDeleteRequest() {

        assertNotNull(requestRepository.findAll());
        assertEquals(1, requestRepository.findAll().size());

        requestRepository.delete(request);
        assertEquals(0, requestRepository.findAll().size());

    }
}