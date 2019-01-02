package com.future.assist;

import com.future.assist.exception.UnauthorizedException;
import com.future.assist.mapper.UserHasItemMapper;
import com.future.assist.model.request_model.user.UserHasItemModelRequest;
import com.future.assist.model.response_model.PageResponse;
import com.future.assist.model.response_model.UserHasItemResponse;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.UserHasItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserHasItemController {

    @Autowired
    private UserHasItemService userHasItemService;

    @Autowired
    private LoggedinUserInfo loggedinUserInfo;

    @Autowired
    private UserHasItemMapper mapper;

    @GetMapping("/user-items")
    public PageResponse<UserHasItemResponse> readUserHasItems(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                                       @RequestParam(value = "idUser", required = false) Long idUser,
                                                       @RequestParam(value = "idItem", required = false) Long idItem,
                                                       @RequestParam("sort") String sort) {
        if (idUser != null) {
            return mapper.pageToPageResponse(userHasItemService.readAllUserHasItemsByIdUser(idUser,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        } else if (idItem != null) {
            return mapper.pageToPageResponse(userHasItemService.readAllUserHasItemsByIdItem(idItem,
                    PageRequest.of(page, limit, Sort.Direction.ASC, sort)));
        } else {
            return mapper.pageToPageResponse(userHasItemService.readAllUserHasItems(PageRequest.
                    of(page, limit, Sort.Direction.ASC, sort)));
        }
    }

    @GetMapping("/user-items/{id}")
    public UserHasItemResponse readUserHasItem(@PathVariable("id") Long id) {
        return mapper.entityToResponse(userHasItemService.readUserHasItemById(id));
    }

    @DeleteMapping("/user-items")
    public ResponseEntity deleteUserHasItem(@RequestBody UserHasItemModelRequest userHasItem) {
        Long idUser = userHasItemService.readUserHasItemById(userHasItem.getIdUserHasItem()).getUser().getIdUser();
        if (!loggedinUserInfo.getUser().getIsAdmin() && loggedinUserInfo.getUser().getIdUser() != idUser) {
            throw new UnauthorizedException("you are not permitted to create userhasitem");
        }
        return userHasItemService.deleteUserHasItem(userHasItem.getIdUserHasItem());
    }
}
