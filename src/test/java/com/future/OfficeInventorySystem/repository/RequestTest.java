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
        request.setSuperior(user3.getSuperior());
        request.setAdministrator(user2);

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
                requestRepository.findRequestByIdRequest(
                        Long.valueOf(111111)));
        assertNotNull(requestRepository.findRequestByIdRequest(idReq));
        assertEquals(request,
                requestRepository.findRequestByIdRequest(idReq)
        );

    }


    @Test
    public void findAllRequestByUser() {
        List<Request> listRequest = new ArrayList<>();
        listRequest = requestRepository.findAllRequestByUser(user3);

        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByUser(user2));
        assertNotNull(requestRepository.findAllRequestByUser(user3));
        assertEquals(request,listRequest.get(0));

    }

    @Test
    public void findAllRequestByStatus() {
        List<Request> listRequest = new ArrayList<>();
        listRequest = requestRepository.findAllRequestByStatus(Status.SENT);

        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatus(Status.REQUESTED));
        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatus(Status.APPROVED));
        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatus(Status.REJECTED));
        assertNotNull(requestRepository.findAllRequestByStatus(Status.SENT));
        assertEquals(request,listRequest.get(0));
    }

    @Test
    public void findAllRequestByStatusAndSuperior() {
        List<Request> listRequest = new ArrayList<>();
        listRequest = requestRepository.
                findAllRequestByStatusAndSuperior(Status.SENT, user1);

        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatusAndSuperior(
                        Status.REQUESTED,user1));
        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatusAndSuperior(
                        Status.APPROVED, user1));
        assertEquals(new ArrayList<Request>(),
                requestRepository.findAllRequestByStatusAndSuperior(
                        Status.REJECTED, user1));
        assertNotNull(
                requestRepository.findAllRequestByStatusAndSuperior(
                        Status.SENT, user1));
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