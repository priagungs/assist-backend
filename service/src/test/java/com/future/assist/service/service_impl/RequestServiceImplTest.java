package com.future.assist.service.service_impl;

import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.User;
import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.model.request_model.request.ReqCreateRequest;
import com.future.assist.model.request_model.request.ReqItemCreateRequest;
import com.future.assist.model.request_model.request.ReqUpdateRequest;
import com.future.assist.repository.RequestRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserHasItemService;
import com.future.assist.service.service_interface.UserService;
import javafx.beans.binding.When;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestServiceImplTest {

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestServiceImpl requestService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ItemServiceImpl itemService;

    @MockBean
    private UserHasItemServiceImpl userHasItemService;

    private User user;
    private User user2;
    private Item item1;
    private Item item2;
    private Request request;
    private Request request2;
    private ReqUpdateRequest requestUpdate;
    private ReqCreateRequest requestBody;
    private ReqItemCreateRequest itemReq1;
    private UserHasItem userHasItem;

    @Before
    public void setUp() {
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
        item1.setIdItem(Long.valueOf(123456));
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


        requestBody = new ReqCreateRequest();
        requestBody.setIdUser(user.getIdUser());

        itemReq1 = new ReqItemCreateRequest();
        itemReq1.setItem(item1);
        itemReq1.setRequestQty(1);

        List<ReqItemCreateRequest> itemsReq = new ArrayList<>();
        itemsReq.add(itemReq1);

        requestBody.setItems(itemsReq);

        requestUpdate = new ReqUpdateRequest();
        requestUpdate.setIdRequest(request.getIdRequest());
        requestUpdate.setIdSuperior(user.getSuperior().getIdUser());

        userHasItem = new UserHasItem();
        userHasItem.setUser(user);
        userHasItem.setItem(item1);
    }
    @Test
    public void createRequest() {

    }

    @Test (expected = InvalidValueException.class)
    public void createRequestInvalidValueTest() {
        requestBody.getItems().get(0).setRequestQty(100);
        Mockito.when(userService.readUserByIdUser(requestBody.getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(itemReq1.getItem().getIdItem()))
                .thenReturn(item1);

        requestService.createRequest(requestBody);
    }


    @Test
    public void createRequestSuccessTest() {
        Mockito.when(userService.readUserByIdUser(requestBody.getIdUser()))
                .thenReturn(user);
        Mockito.when(itemService.readItemByIdItem(itemReq1.getItem().getIdItem()))
                .thenReturn(item1);
        Mockito.when(itemService.updateItem(item1))
                .thenReturn(item1);
        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        assertEquals(request, requestService.createRequest(requestBody).get(0));
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestNotFoundTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.empty());

        requestService.updateRequest(requestUpdate);
    }

    @Test
    public void updateRequestApprovedTest() {

        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(userService.readUserByIdUser(requestUpdate.getIdSuperior()))
                .thenReturn(user2);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        requestUpdate.setRequestStatus(RequestStatus.APPROVED);

        assertEquals(user.getSuperior().getIdUser(),requestService.updateRequest(requestUpdate).getApprovedBy());
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestApprovedSuperiorNotFoundTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        requestUpdate.setRequestStatus(RequestStatus.APPROVED);

        requestService.updateRequest(requestUpdate);
    }

    @Test
    public void updateRequestRejectedTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(userService.readUserByIdUser(requestUpdate.getIdSuperior()))
                .thenReturn(user2);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        Mockito.when(itemService.readItemByIdItem(request.getItem().getIdItem()))
                .thenReturn(item1);

        Mockito.when(itemService.updateItem(item1))
                .thenReturn(item1);

        requestUpdate.setRequestStatus(RequestStatus.REJECTED);

        assertEquals(user.getSuperior().getIdUser(),requestService.updateRequest(requestUpdate).getRejectedBy());

//        assertEquals(item1.getAvailableQty(),7);
        if(item1.getAvailableQty() != 7) {
           assertTrue(false);
        }
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestRejectedNotFoundTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        Mockito.when(itemService.readItemByIdItem(request.getItem().getIdItem()))
                .thenReturn(item1);

        Mockito.when(itemService.updateItem(item1))
                .thenReturn(item1);

        requestUpdate.setRequestStatus(RequestStatus.REJECTED);

        requestService.updateRequest(requestUpdate);
    }

    @Test
    public void updateRequestSentTest() {
        User userAdmin = new User();
        userAdmin.setUsername("ricky");
        userAdmin.setName("ricky");
        userAdmin.setIsAdmin(true);
        userAdmin.setIsActive(true);
        userAdmin.setHasItems(new ArrayList<>());


        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(userService.readUserByIdUser(requestUpdate.getIdAdmin()))
                .thenReturn(userAdmin);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        Mockito.when(userHasItemService.createUserHasItem(any()))
                .thenReturn(userHasItem);

        request.setRequestStatus(RequestStatus.APPROVED);
        requestUpdate.setRequestStatus(RequestStatus.SENT);

        assertEquals(userAdmin.getIdUser(),requestService.updateRequest(requestUpdate).getHandedOverBy());
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestSentNotFoundTest() {
        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        request.setRequestStatus(RequestStatus.APPROVED);
        requestUpdate.setRequestStatus(RequestStatus.SENT);

        requestService.updateRequest(requestUpdate);
    }

    @Test (expected = InvalidValueException.class)
    public void updateRequestInvalidValueTest() {
        User userAdmin = new User();
        userAdmin.setUsername("ricky");
        userAdmin.setName("ricky");
        userAdmin.setIsAdmin(true);
        userAdmin.setIsActive(true);
        userAdmin.setHasItems(new ArrayList<>());


        Mockito.when(requestRepository.findRequestByIdRequest(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(userService.readUserByIdUser(requestUpdate.getIdAdmin()))
                .thenReturn(userAdmin);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        Mockito.when(userHasItemService.createUserHasItem(any()))
                .thenReturn(userHasItem);

//        request.setRequestStatus(RequestStatus.APPROVED);
        requestUpdate.setRequestStatus(RequestStatus.SENT);

        assertEquals(userAdmin.getIdUser(),requestService.updateRequest(requestUpdate).getHandedOverBy());
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestByRequestObjectNotFoundTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.empty());

        requestService.updateRequestByRequestObject(request);
    }

    @Test
    public void updateRequestByRequestObjectSuccessTest() {
        Request beforeRequest = new Request();
        beforeRequest.setRequestBy(user);
        beforeRequest.setItem(item1);
        beforeRequest.setReqQty(2);
        beforeRequest.setRequestStatus(RequestStatus.REQUESTED);

        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.of(beforeRequest));

        Mockito.when(itemService.readItemByIdItem(request.getItem().getIdItem()))
                .thenReturn(item1);

        request.setRequestStatus(RequestStatus.REJECTED);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(beforeRequest);

        Request result = requestService.updateRequestByRequestObject(request);
        assertEquals(beforeRequest.getRejectedBy(),result.getRejectedBy());
        assertEquals(beforeRequest.getRequestStatus(),result.getRequestStatus());
        assertEquals(beforeRequest.getReturnedBy(),result.getReturnedBy());

        if(item1.getAvailableQty() != 7) {
            assertTrue(false);
        }
    }

    @Test (expected = NotFoundException.class)
    public void updateRequestStatusToReturnedNotFound() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.empty());

        requestService.updateRequestStatusToReturned(request);
    }

    @Test
    public void updateRequestStatusToReturnedTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Mockito.when(requestRepository.save(request))
                .thenReturn(request);

        Request result = requestService.updateRequestStatusToReturned(request);

        assertEquals(result.getRequestStatus(),RequestStatus.RETURNED);
    }


    @Test
    public void readAllRequestsByItem() {
        List<Request> expect = new ArrayList<>();
        expect.add(request);

        Mockito.when(requestRepository.findAllRequestsByItem(item1))
                .thenReturn(expect);

        List<Request> result = requestService.readAllRequestsByItem(item1);
        assertEquals(expect.size(), result.size());
        assertEquals(expect.get(0).getItem(), result.get(0).getItem());
    }

    @Test
    public void readAllRequest() {
        PageImpl requests = new PageImpl(new ArrayList());

        Mockito.when(requestRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(requests);

        assertEquals(requests, requestService.readAllRequest(PageRequest.of(0, Integer.MAX_VALUE)));
    }

    @Test
    public void readRequestByUser() {
        List<Request> returnList = new ArrayList<>();
        returnList.add(request);
        returnList.add(request2);


        PageImpl requests = new PageImpl(returnList);

        Mockito.when(requestRepository.findAllRequestsByRequestBy(user, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(new PageImpl(returnList));

        assertEquals(returnList.size(), requestService.readRequestByUser(PageRequest.of(0, Integer.MAX_VALUE), user).getContent().size());
    }

    @Test
    public void readAllRequestBySuperior() {
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userService.readAllUsersByIdSuperior(
                user2.getIdUser(),
                PageRequest.of(0,Integer.MAX_VALUE)
                ))
                .thenReturn(new PageImpl(users));

        List<Request> returnList = new ArrayList<>();
        returnList.add(request);
        returnList.add(request2);

        Mockito.when(requestRepository.findAllRequestsByRequestBy(
                user,PageRequest.of(0,Integer.MAX_VALUE))
                ).thenReturn(new PageImpl(returnList));

        assertEquals(
                returnList.size(),
                requestService.readAllRequestBySuperior(
                    PageRequest.of(0,Integer.MAX_VALUE),
                    user2).getContent().size()
                );

    }

    @Test
    public void readAllRequestByRequestStatus() {
        List<Request> returnList = new ArrayList<>();
        returnList.add(request);
        returnList.add(request2);

        Mockito.when(requestRepository.findAllRequestsByRequestStatus(
                RequestStatus.REQUESTED,PageRequest.of(0,Integer.MAX_VALUE))
        ).thenReturn(new PageImpl(returnList));

        assertEquals(returnList.size(), requestService.readAllRequestByRequestStatus(
                    PageRequest.of(0,Integer.MAX_VALUE),
                    RequestStatus.REQUESTED).getContent().size()
        );
    }

    @Test
    public void readAllRequestBySuperiorAndRequestStatus() {
        List<Request> returnList = new ArrayList<>();
        returnList.add(request);
        returnList.add(request2);

        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userService.readAllUsersByIdSuperior(
                user2.getIdUser(),
                PageRequest.of(0,Integer.MAX_VALUE)
        ))
                .thenReturn(new PageImpl(users));

        Mockito.when(requestRepository.findAllRequestsByRequestBy(
                user,PageRequest.of(0,Integer.MAX_VALUE))
        ).thenReturn(new PageImpl(returnList));

        assertEquals(returnList.size(),requestService.readAllRequestBySuperiorAndRequestStatus(
                PageRequest.of(0,Integer.MAX_VALUE),
                user2,
                RequestStatus.REQUESTED
                ).getContent().size()
        );
    }

    @Test
    public void readAllRequestByUserAndStatus() {
        List<Request> returnList = new ArrayList<>();
        returnList.add(request);
        returnList.add(request2);

        Mockito.when(requestRepository.findAllByRequestByAndRequestStatus(user,RequestStatus.REQUESTED,PageRequest.of(0,Integer.MAX_VALUE)))
                .thenReturn(new PageImpl(returnList));

        assertEquals(returnList.size(),requestService.readAllRequestByUserAndStatus(
                PageRequest.of(0,Integer.MAX_VALUE),
                user,
                RequestStatus.REQUESTED
                ).getContent().size()
        );
    }

    @Test (expected = NotFoundException.class)
    public void deleteRequestNotFoundTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.empty());

        requestService.deleteRequest(request);
    }

    @Test
    public void deleteRequestTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.of(request));
        Mockito.when(itemService.readItemByIdItem(request.getItem().getIdItem()))
                .thenReturn(item1);

        requestService.deleteRequest(request);

        if(item1.getAvailableQty() != 7) {
            assertTrue(false);
        }
    }

    @Test (expected = NotFoundException.class)
    public void readRequestByIdRequestNotFoundTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.empty());

        Request result = requestService.readRequestByIdRequest(request.getIdRequest());
    }

    @Test
    public void readRequestByIdRequestSuccessTest() {
        Mockito.when(requestRepository.findById(request.getIdRequest()))
                .thenReturn(Optional.of(request));

        Request result = requestService.readRequestByIdRequest(request.getIdRequest());

        assertEquals(request,result);
    }
}