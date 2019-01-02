package com.future.assist.service.service_impl;

import com.future.assist.exception.ConflictException;
import com.future.assist.exception.InvalidValueException;
import com.future.assist.exception.NotFoundException;
import com.future.assist.model.RequestStatus;
import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.Request;
import com.future.assist.model.entity_model.UserHasItem;
import com.future.assist.repository.ItemRepository;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.RequestService;
import com.future.assist.service.service_interface.UserHasItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserHasItemService userHasItemService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private LoggedinUserInfo loggedinUserInfo;

    @Transactional
    public Item createItem(Item item) {
        if (itemRepository.findByItemNameIgnoreCaseAndIsActive(item.getItemName(), true).isPresent()) {
            throw new ConflictException(item.getItemName() + " is exist");
        }

        if (item.getTotalQty() < 0 || item.getPrice() < 0) {
            throw new InvalidValueException("Invalid value");
        }

        item.setAvailableQty(item.getTotalQty());
        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Item item) {
        Item itemBefore = itemRepository
                .findByIdItemAndIsActive(item.getIdItem(), true)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getTotalQty() < itemBefore.getTotalQty() - itemBefore.getAvailableQty() || item.getPrice() < 0) {
            throw new InvalidValueException("Invalid value");
        }

        if (itemRepository.findByItemNameIgnoreCaseAndIsActive(item.getItemName(), true).isPresent() &&
                !itemBefore.getItemName().equals(item.getItemName())) {
            throw new ConflictException("Item already present");
        }

        itemBefore.setItemName(item.getItemName());
        itemBefore.setPictureURL(item.getPictureURL());
        itemBefore.setPrice(item.getPrice());
        itemBefore.setDescription(item.getDescription());
        if (item.getAvailableQty() == null) {
            itemBefore.setAvailableQty(itemBefore.getAvailableQty() + item.getTotalQty() - itemBefore.getTotalQty());
        } else {
            if (item.getAvailableQty() < 0) {
                throw new InvalidValueException("available quantity must be positive");
            }
            itemBefore.setAvailableQty(item.getAvailableQty());
        }
        itemBefore.setTotalQty(item.getTotalQty());

        return itemRepository.save(itemBefore);
    }

    public Page<Item> readAllItems(Pageable pageable) {
        return itemRepository.findAllByIsActive(true, pageable);
    }

    public Item readItemByIdItem(Long id) {
        return itemRepository.findByIdItemAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    public Page<Item> readItemsByAvailableGreaterThan(Integer min, Pageable pageable) {
        if (min < 0) {
            throw new InvalidValueException("Invalid value");
        }
        return itemRepository.findAllByAvailableQtyGreaterThanAndIsActive(min, true, pageable);
    }

    public Page<Item> readAllItemsContaining(String keyword, Pageable pageable) {
        return itemRepository.findByItemNameIgnoreCaseContainingAndIsActive(keyword, true, pageable);
    }

    public Page<Item> readAllItemsByKeywordAndAvailableGreaterThan(String keyword, Integer min, Pageable pageable) {
        return itemRepository.findByItemNameIgnoreCaseContainingAndAvailableQtyGreaterThanAndIsActive(
                keyword, min, true, pageable);
    }

    public Item readItemByItemName(String name) {
        return itemRepository.findByItemNameIgnoreCaseAndIsActive(name, true)
                .orElseThrow(() -> new NotFoundException("not found"));
    }

    @Transactional
    public ResponseEntity deleteItem(Long id) {
        Item item = itemRepository.findByIdItemAndIsActive(id, true)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        List<Request> requests = requestService.readAllRequestsByItem(item);

        for (Request request : requests) {
            if (request.getRequestStatus() == RequestStatus.APPROVED ||
                    request.getRequestStatus() == RequestStatus.REQUESTED) {
                request.setRequestStatus(RequestStatus.REJECTED);
                request.setRejectedBy(loggedinUserInfo.getUser().getIdUser());
                request.setRejectedDate(new Date());
                requestService.updateRequestByRequestObject(request);
            } else if (request.getRequestStatus() == RequestStatus.SENT) {
                request.setRequestStatus(RequestStatus.RETURNED);
                request.setReturnedBy(loggedinUserInfo.getUser().getIdUser());
                request.setReturnedDate(new Date());
                requestService.updateRequestByRequestObject(request);
            }
        }

        List<UserHasItem> userHasItems = userHasItemService.readAllUserHasItemsByIdItem(item.getIdItem(),
                PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        for (UserHasItem userHasItem : userHasItems) {
            userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
        }

        item.setIsActive(false);
        itemRepository.save(item);

        return ResponseEntity.ok().build();
    }
}
