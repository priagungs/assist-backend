package com.future.assist.repository;

import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    private User user1;
    private Item item;
    private User user2;
    private User user3;
    private Request request;
    private Request request1;


    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user1.setUsername("shintaayuck");
        user1.setPassword("hashedpassword");
        user1.setName("Shinta Ayu C K");
        user1.setPictureURL("ini link foto");
        user1.setRole("UX Researcher");
        user1.setDivision("Technology");
        user1.setSuperior(null);
        user1.setIsAdmin(false);

        item = new Item();
        item.setItemName("indomie");
        item.setPictureURL("image.jpg");
        item.setPrice(1000L);
        item.setTotalQty(10);
        item.setAvailableQty(10);
        item.setDescription("micin");

        user2 = new User();
        user2.setUsername("rickykennedy25");
        user2.setPassword("hashedpassword");
        user2.setName("Ricky Kennedy");
        user2.setPictureURL("ini link foto");
        user2.setRole("System Analyst");
        user2.setDivision("Technology");
        user2.setSuperior(null);
        user2.setIsAdmin(true);

        user3 = new User();
        user3.setUsername("priagungs");
        user3.setPassword("hashedpassword");
        user3.setName("Priagung Satyagama");
        user3.setPictureURL("ini link foto");
        user3.setRole("Data Scientist");
        user3.setDivision("Technology");
        user3.setSuperior(user1);
        user3.setIsAdmin(false);

        request = new Request();
        request.setRequestBy(user3);
        request.setItem(item);
        request.setRequestDate(new Date());
        request.setReqQty(2);
        request.setRequestStatus(RequestStatus.SENT);

        request1 = new Request();
        request1.setRequestBy(user3);
        request1.setItem(item);
        request1.setRequestDate(new Date());
        request1.setReqQty(4);
        request1.setRequestStatus(RequestStatus.REQUESTED);


        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(item);
        entityManager.persist(request);
        entityManager.persist(request1);
        entityManager.flush();
    }


    @Test
    public void findRequestByIdRequest() {
        Long idReq = new Long(request.getIdRequest());

        Optional<Request> req =requestRepository.findById(Long.valueOf(111111));
        assertFalse(req.isPresent());

        assertTrue(requestRepository.findById(idReq).isPresent());
        assertNotNull(requestRepository.findById(idReq).get());
        assertEquals(request, requestRepository.findById(idReq).get());
    }

    @Test
    public void findAllRequestsByRequestBy() {

        List<Request> listRequest = requestRepository
            .findAllRequestsByRequestBy(user1, PageRequest.of(0, 3)).getContent();
        assertEquals(0, listRequest.size());

        listRequest = requestRepository
            .findAllRequestsByRequestBy(user3, PageRequest.of(0, 3)).getContent();
        assertEquals(2, listRequest.size());

    }

    @Test
    public void findAllRequestsByRequestStatus() {
        List<Request> listRequest = requestRepository
            .findAllRequestsByRequestStatus(RequestStatus.REQUESTED,PageRequest.of(0,2))
            .getContent();

        assertEquals(1, listRequest.size());
        assertEquals(RequestStatus.REQUESTED, listRequest.get(0).getRequestStatus());

        listRequest = requestRepository
            .findAllRequestsByRequestStatus(RequestStatus.APPROVED,PageRequest.of(0,2))
            .getContent();

        assertEquals(0,listRequest.size());

    }

    @Test
    public void findAllByRequestByAndRequestStatus() {
        List<Request> listRequest = requestRepository
            .findAllByRequestByAndRequestStatus(user3,RequestStatus.SENT,PageRequest.of(0,2))
            .getContent();

        assertEquals(1,listRequest.size());

        listRequest = requestRepository
                .findAllByRequestByAndRequestStatus(user3,RequestStatus.REQUESTED,PageRequest.of(0,2))
                .getContent();

        assertEquals(1,listRequest.size());

        listRequest = requestRepository
                .findAllByRequestByAndRequestStatus(user1,RequestStatus.SENT,PageRequest.of(0,2))
                .getContent();

        assertEquals(0,listRequest.size());

    }

    @Test
    public void findAllRequestsByItem() {
        List<Request> listRequest = requestRepository.findAllRequestsByItem(item);

        assertEquals(listRequest.size(),2);

    }

    @Test
    public void testSaveRequest() {

        requestRepository.save(request);
        requestRepository.save(request1);


        List<Request> requests = requestRepository.findAll();

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(request, requests.get(0));

    }

    @Test
    public void testDeleteRequest() {

        assertNotNull(requestRepository.findAll());
        assertEquals(2, requestRepository.findAll().size());

        requestRepository.delete(request);
        assertEquals(1, requestRepository.findAll().size());

    }
}