package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final Comparator<ItemRequest> comparator = new Comparator<ItemRequest>() {
        @Override
        public int compare(ItemRequest o1, ItemRequest o2) {
            if (o1.getCreated().isBefore(o2.getCreated())) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    @Override
    @Transactional
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
        List<ItemRequestItemDto> userRequests = requestRepository.getByRequestor_Id(userId).stream()
                .sorted(comparator)
                .map(ItemRequestMapper::toItemRequestItemDto)
                .toList();

        //getting all items from bd
        List<Item> allItems = itemRepository.findAll();

        //setting item list for specific request
        List<OwnerItemDto> requestItems;
        for(ItemRequestItemDto r : userRequests) {
            requestItems = allItems.stream()
                    .filter(i -> i.getRequest() != null)
                    .filter(i -> i.getRequest().getId().equals(r.getId()))
                    .map(ItemMapper::toOwnerItemDto)
                    .collect(Collectors.toList());
            r.setItems(requestItems);
        }
        return userRequests;
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .sorted(comparator)
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestItemDto getRequest(Long requestId) {
        log.info("validation for request {} existence",requestId);
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() -> {
            log.warn("request with id =  {} is not existing", requestId);
            return new NotFoundException("request is not found");
        });
        ItemRequestItemDto dto = ItemRequestMapper.toItemRequestItemDto(request);

        log.info("getting requests {} items", requestId);
        List<OwnerItemDto> items = itemRepository.getByRequest_Id(requestId).stream()
                .map(ItemMapper :: toOwnerItemDto)
                .toList();
        dto.setItems(items);
        return dto;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
    }
}
