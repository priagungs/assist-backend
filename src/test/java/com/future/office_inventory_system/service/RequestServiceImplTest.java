package com.future.office_inventory_system.service;

import com.future.office_inventory_system.exception.InvalidValueException;
import com.future.office_inventory_system.exception.NotFoundException;
import com.future.office_inventory_system.model.*;
import com.future.office_inventory_system.repository.RequestRepository;
import com.future.office_inventory_system.value_object.RequestBodyRequestCreate;
import com.future.office_inventory_system.value_object.RequestUpdate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RequestServiceImplTest {

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserHasItemService userHasItemService;

    private User user;
    private User user2;
    private Item item1;
    private Item item2;
    private Request request;
    private Request request2;
    private RequestUpdate requestUpdate;
    private RequestBodyRequestCreate requestBody;

    @Before
    public void before(){
        user = new User();
        user.setUsername("priagung");
        user.setName("Priagung Satyagama");
        user.setIsAdmin(true);
        user.setIsActive(true);
        user.setHasItems(new ArrayList<>());

        user2 = new User();

        user2.setUsername("bambang");
        user2.setName("Bambang Nugroho");
        user2.setIsAdmin(false);
        user2.setIsActive(true);
        user2.setPictureURL("picture.img");
        user2.setPassword("asdfasdf");
        user2.setDivision("Engineering");
        user2.setRole("Software Engineer");

        List<User> subordinates = new ArrayList<>();
        user.setSuperior(user2);
        subordinates.add(user);
        user2.setSubordinates(subordinates);


        item1 = new Item();
        item1.setItemName("Buku");
        item1.setTotalQty(10);
        item1.setAvailableQty(5);

        item2 = new Item();
        item2.setItemName("Buku");
        item2.setTotalQty(10);
        item2.setAvailableQty(5);

        request = new Request();
        request.setRequestBy(user);
        request.setItem(item1);
        request.setReqQty(2);
        request.setRequestStatus(RequestStatus.REQUESTED);

        request2 = new Request();
        request2.setRequestBy(user);
        request2.setItem(item2);
        request2.setReqQty(2);
        request2.setRequestStatus(RequestStatus.REQUESTED);


        requestBody = new RequestBodyRequestCreate();
        requestBody.setIdUser(user.getIdUser());
        requestBody.setItem(item1);
        requestBody.setRequestQty(2);
        requestBody.setRequestDate(new Date());

        requestUpdate = new RequestUpdate();
        requestUpdate.setIdRequest(request.getIdRequest());
        requestUpdate.setIdSuperior(user.getSuperior().getIdUser());
    }

    @Test
    public void createRequestSuccessTest() {
        Mockito.when(itemService.readItemByIdItem(requestBody.getItem().getIdItem())).thenReturn(item1);
        Mockito.when(userService.readUserByIdUser(requestBody.getIdUser())).thenReturn(user);
        request.setRequestDate(requestBody.getRequestDate());
        Mockito.when(requestRepository.save(request)).thenReturn(request);
        Assert.assertEquals(request, requestService.createRequest(requestBody));
    }

    @Test (expected = InvalidValueException.class)
    public void createRequestInvalidValueExceptionTest() {
        request.setReqQty(item1.getAvailableQty()+1);
        request.setRequestDate(requestBody.getRequestDate());
        Mockito.when(requestRepository.save(request)).thenReturn(request);
        Mockito.when(itemService.readItemByIdItem(requestBody.getItem().getIdItem())).thenReturn(item1);

        requestBody.setRequestQty(item1.getAvailableQty()+1);

        requestService.createRequest(requestBody);
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestNotFoundTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest())).thenReturn(Optional.empty());

        requestService.updateRequest(requestUpdate);
    }


    @Test
    public void updateRequestApprovedTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest())).thenReturn(Optional.of(request));

        requestUpdate.setRequestStatus(RequestStatus.APPROVED);
        Assert.assertEquals(user2.getIdUser(),requestService.updateRequest(requestUpdate).getApprovedBy());
    }

    @Test
    public void updateRequestRejectedTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest())).thenReturn(Optional.of(request));
        Mockito.when(itemService.readItemByIdItem(request.getIdRequest())).thenReturn(item1);
        requestUpdate.setRequestStatus(RequestStatus.REJECTED);
        Assert.assertEquals(user2.getIdUser(),requestService.updateRequest(requestUpdate).getRejectedBy());

        if(item1.getAvailableQty() != 7) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateRequestSentTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest())).thenReturn(Optional.of(request));

        requestUpdate.setRequestStatus(RequestStatus.SENT);
        Assert.assertEquals(user2.getIdUser(),requestService.updateRequest(requestUpdate).getHandedOverBy());
    }



    @Test
    public void readAllRequestTest() {
        PageImpl requests = new PageImpl(new ArrayList());

        Mockito.when(requestRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(requests);

        Assert.assertEquals(requests, requestService.readAllRequest(PageRequest.of(0, Integer.MAX_VALUE)));

    }

    @Test
    public void readRequestByUserTest() {
        PageImpl requests = new PageImpl(new ArrayList());

        Mockito.when(requestRepository.findAllRequestsByRequestBy(user, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(requests);

        Assert.assertEquals(requests, requestService.readRequestByUser(PageRequest.of(0, Integer.MAX_VALUE), user));

    }

    @Test
    public void readAllRequestBySuperiorTest() {
        List<User> users = new ArrayList<>();
        users.add(user);

        PageImpl<User> pageUsers = new PageImpl<>(users,PageRequest.of(0, Integer.MAX_VALUE),users.size());

        Mockito.when(userService.readAllUsersByIdSuperior(user2.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))).thenReturn(pageUsers);

        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);

        PageImpl<Request> pageRequests = new PageImpl<>(requests,PageRequest.of(0, Integer.MAX_VALUE),requests.size());
        Mockito.when(requestRepository.findAllRequestsByRequestBy(user,PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(pageRequests);

        Assert.assertEquals(
                pageRequests,
                requestService.readAllRequestBySuperior(
                    PageRequest.of(0, Integer.MAX_VALUE),user2
                )
        );

    }

    @Test
    public void readAllRequestByRequestStatusTest() {
        PageImpl requests = new PageImpl(new ArrayList());
        RequestStatus requestStatus = RequestStatus.REQUESTED;

        Mockito.when(requestRepository.findAllRequestsByRequestStatus(
                requestStatus,
                PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(requests);

        Assert.assertEquals(
                requests,
                requestService.readAllRequestByRequestStatus(
                        PageRequest.of(0, Integer.MAX_VALUE), requestStatus
                        )
                );
    }

    @Test
    public void readAllRequestBySuperiorAndRequestStatusTest() {
        List<User> users = new ArrayList<>();
        users.add(user);

        PageImpl<User> pageUsers = new PageImpl<>(users,PageRequest.of(0, Integer.MAX_VALUE),users.size());

        Mockito.when(userService.readAllUsersByIdSuperior(user2.getIdUser(),
                PageRequest.of(0, Integer.MAX_VALUE))).thenReturn(pageUsers);

        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);

        PageImpl<Request> pageRequests = new PageImpl<>(requests,PageRequest.of(0, Integer.MAX_VALUE),requests.size());

        Mockito.when(requestRepository.findAllRequestsByRequestBy(
                request.getRequestBy(),
                PageRequest.of(0, Integer.MAX_VALUE))).thenReturn(pageRequests);

        Assert.assertEquals(pageRequests, requestService.readAllRequestBySuperiorAndRequestStatus(
                PageRequest.of(0, Integer.MAX_VALUE),
                user2,RequestStatus.REQUESTED));
    }

    @Test
    public void deleteRequestRequestedTest() {
        Mockito.when(itemService.readItemByIdItem(request.getIdRequest())).thenReturn(item1);

        Assert.assertEquals(ResponseEntity.ok().build(), requestService.deleteRequest(request));

        if(item1.getAvailableQty() != 7) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void deleteRequestApprovedTest() {
        request.setRequestStatus(RequestStatus.APPROVED);
        Mockito.when(itemService.readItemByIdItem(request.getIdRequest())).thenReturn(item1);

        Assert.assertEquals(ResponseEntity.ok().build(), requestService.deleteRequest(request));

        if(item1.getAvailableQty() != 5) {
            Assert.assertTrue(false);
        }
    }
}
