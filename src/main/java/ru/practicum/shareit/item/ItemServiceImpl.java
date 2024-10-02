package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public List<BookingItemDto> getItems(Long userId) {
        log.info("validation of existence of user{}", userId);
        User user = getUser(userId);
        List<Item> items = itemRepository.getByOwner_Id(userId);

        //result list
        List<BookingItemDto> dtos = new ArrayList<>();

        //getting all comments and bookings from repo
        List<Comment> allComments = commentRepository.findAll();
        List<Booking> allBookings = bookingRepository.findAll();

        for (Item i : items) {
            BookingItemDto dto = new BookingItemDto(
                    i.getId(),
                    i.getName(),
                    i.getDescription(),
                    i.getAvailable());

            //adding comments to specific item
            List<CommentDto> comments = allComments.stream()
                    .filter(c -> c.getItem().equals(i))
                    .map(CommentMapper::toDto)
                    .collect(Collectors.toList());
            dto.setComments(comments);

            //adding booking to specific item
            List<BookingDto> bookings = allBookings.stream()
                    .filter(b -> b.getItem().equals(i))
                    .map(BookingMapper::toDto)
                    .toList();
            for (BookingDto b : bookings) {
                if ((b.getStart().isBefore(LocalDateTime.now()) &&
                        b.getEnd().isAfter(LocalDateTime.now()))) {
                    dto.setLastBooking(b);
                    break;
                }
                if (b.getStart().isAfter(LocalDateTime.now())) {
                    dto.setNextBooking(b);
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional
    public RequestItemDto addNewItem(Long userId, RequestItemDto dto) {
        Item item = ItemMapper.fromRequestItemDto(dto);

        log.info("validation of owner for item {}", dto);
        User owner = getUser(userId);

        if (dto.getRequestId() != null) {
            log.info("validation of request for item {}", dto);
            Long requestId = dto.getRequestId();
            ItemRequest request = requestRepository.findById(requestId).orElseThrow(() -> {
                log.warn("request with id is not existing");
                return new InvalidDataException("invalid request");
            });
            item.setRequest(request);
        }

        item.setOwner(owner);
        log.debug("item {}", item);
        return ItemMapper.toRequestItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        log.info("validation of existence of item {}", item);
        Item updateItem = getItem(itemId);
        log.info("validation of existence of supposed owner for item {}", updateItem);
        User supposedOwner = getUser(userId);
        log.info("validation of owner for item {}", updateItem);
        validateOwner(supposedOwner, updateItem);

        //patching updating entity
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(updateItem));
    }

    @Override
    public BookingItemDto getItemById(Long itemId) {
        Item item = getItem(itemId);
        BookingItemDto dto = new BookingItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable());

        List<CommentDto> comments = commentRepository.findByItem_id(itemId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        dto.setComments(comments);

        List<Booking> bookings = bookingRepository.findByItem_id(itemId);
        for (Booking b : bookings) {
            if ((b.getStart().isBefore(LocalDateTime.now()) &&
                    b.getEnd().isAfter(LocalDateTime.now()))) {
                dto.setLastBooking(BookingMapper.toDto(b));
            }
            if (b.getStart().isAfter(LocalDateTime.now())) {
                dto.setNextBooking(BookingMapper.toDto(b));
            }
        }
        return dto;
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            log.info("empty search");
            return new ArrayList<>();
        }
        log.info("search from items to {}", text);
        List<Item> items = itemRepository.search(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        log.info("validation of existence of item {}", itemId);
        Item item = getItem(itemId);
        log.info("validation of existence of author {}", userId);
        User user = getUser(userId);

        log.info("booking validation");
        Booking booking = bookingRepository.findByBooker_idAndItem_id(userId, itemId).orElseThrow(() -> {
            log.warn("booking with id is not existing");
            return new InvalidDataException("invalid booking");
        });
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            log.warn("booking in progress");
            throw new InvalidDataException("Access forbidden, your booking in progress");
        }

        //setting params to comment
        comment.setUser(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn("item with id =  {} is not existing", itemId);
            return new NotFoundException("Item is not found");
        });
    }

    private void validateOwner(User user, Item item) {
        if (!item.getOwner().equals(user)) {
            throw new ForbiddenException("Access is forbidden for users except owner");
        }
    }
}
