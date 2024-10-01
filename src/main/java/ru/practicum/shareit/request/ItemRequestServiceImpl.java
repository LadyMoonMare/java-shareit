package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequest request){
        log.info("validation for user {} existence",userId);
        User user = getUser(userId);

        request.setCreated(LocalDateTime.now());
        request.setRequestor(user);
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestItemDto> getUserItems(Long userId) {
        log.info("validation for user {} existence",userId);
        User user = getUser(userId);

        log.info("getting user`s {} requests", userId);
        List<ItemRequestItemDto> userRequests = requestRepository.findByRequestor_Id(userId).stream()
                .map(ItemRequestMapper::toItemRequestItemDto)
                .sorted(new Comparator<ItemRequestItemDto>() {
                    @Override
                    public int compare(ItemRequestItemDto o1, ItemRequestItemDto o2) {
                        if (o1.getCreated().isBefore(o2.getCreated())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                })
                .toList();

        //getting all items from bd
        List<Item> allItems = itemRepository.findAll();

        //setting item list for specific request
        List<OwnerItemDto> requestItems;
        for(ItemRequestItemDto r : userRequests) {
            requestItems = allItems.stream()
                    .filter(i -> i.getRequest().getId().equals(r.getId()))
                    .map(ItemMapper::toOwnerItemDto)
                    .collect(Collectors.toList());
            r.setItems(requestItems);
        }
        return userRequests;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
    }
}
