package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto save(Long userId, Long itemId, RequestBookingDto bookingDto) {
        Booking booking = BookingMapper.fromItemBookingDto(bookingDto);

        log.info("validation by userId {}", userId);
        User user = getUser(userId);
        booking.setBooker(user);

        log.info("validation by itemId {}", itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn("item with id =  {} is not existing", itemId);
            return new NotFoundException("Item is not found");
        });
        if (!item.getAvailable()) {
            log.warn("item {} is not available", itemId);
            throw new InvalidDataException("Item is not available");
        }
        booking.setItem(item);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, boolean approved) {
        log.info("validation by bookingId {}", bookingId);
        Booking booking = getBooking(bookingId);

        log.info("validation userId {} to be ownerId", userId);
        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Access is forbidden for users except owner");
        }

        //patching of booking status
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking patchedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(patchedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        log.info("validation by bookingId {}", bookingId);
        Booking booking = getBooking(bookingId);

        log.info("validation userId {} to be owner or booker", userId);
        Item item = booking.getItem();
        if (item.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return BookingMapper.toDto(booking);
        } else {
            throw new ForbiddenException("Access is forbidden for users except owner or booker");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} does not exist", userId);
            return new NotFoundException("User is not found");
        });
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.warn("user with id =  {} does not exist", bookingId);
            return new NotFoundException("Booking is not found");
        });
    }

}
