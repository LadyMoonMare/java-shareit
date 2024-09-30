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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Comparator<Booking> comparator= new Comparator<Booking>() {
        @Override
        public int compare(Booking o1, Booking o2) {
            if (o1.getStart().isAfter(o2.getStart())) return 1;
            else return 0;
        }
    };

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

    @Override
    public List<BookingDto> getBookingsByBooker(Long userId, String text) {
        log.info("validation by userId {}", userId);
        User user = getUser(userId);

        //getting bookings by params
        List<Booking> bookings = new ArrayList<>();
        try {
            State state = State.valueOf(text);
            if (state.equals(State.ALL)) {
                log.info("search for all user`s bookings");
                bookings = bookingRepository.findByBooker_Id(userId);
            } else if (state.equals(State.WAITING)) {
                log.info("search for waiting user`s bookings");
                bookings = bookingRepository.findByBooker_IdAndStatus(userId,
                        Status.WAITING);
            } else if (state.equals(State.REJECTED)) {
                log.info("search for rejected user`s bookings");
                bookings = bookingRepository.findByBooker_IdAndStatus(userId,
                        Status.REJECTED);
            } else if (state.equals(State.CURRENT)) {
                log.info("search for current user`s bookings");
                bookings = bookingRepository.findByBooker_IdAndEndIsAfter(userId, LocalDateTime.now());
            } else if (state.equals(State.PAST)) {
                log.info("search for past user`s bookings");
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now());
            } else if (state.equals(State.FUTURE)) {
                log.info("search for future user`s bookings");
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now());
            }
        } catch (IllegalArgumentException e) {
            log.warn("illegal state {}", text);
            throw  new InvalidDataException("invalid state");
        }
        return sortAndMapBookings(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, String text) {
        log.info("validation by userId {}", userId);
        User user = getUser(userId);

        log.info("validation to user`s {} items", userId);
        List<Item> items = itemRepository.getByOwner_Id(userId);
        if(items.isEmpty()) {
            log.warn("user {} has no items", userId);
            throw new NotFoundException("You have not got any items");
        }

        //getting bookings by params
        List<Booking> bookings = new ArrayList<>();
        try {
            State state = State.valueOf(text);
            if (state.equals(State.ALL)) {
                log.info("search for all owner`s bookings");
                bookings = bookingRepository.findByOwner_Id(userId);
            } else if (state.equals(State.WAITING)) {
                log.info("search for waiting owner`s bookings");
                bookings = bookingRepository.findByOwner_IdAndStatus(userId,
                        Status.WAITING);
            } else if (state.equals(State.REJECTED)) {
                log.info("search for rejected owner`s bookings");
                bookings = bookingRepository.findByOwner_IdAndStatus(userId,
                        Status.REJECTED);
            } else if (state.equals(State.CURRENT)) {
                log.info("search for current owner`s bookings");
                bookings = bookingRepository.findByOwner_IdAndEndIsAfter(userId, LocalDateTime.now());
            } else if (state.equals(State.PAST)) {
                log.info("search for past owner`s bookings");
                bookings = bookingRepository.findByOwner_IdAndEndIsBefore(userId, LocalDateTime.now());
            } else if (state.equals(State.FUTURE)) {
                log.info("search for future owner`s bookings");
                bookings = bookingRepository.findByOwner_IdAndStartIsAfter(userId, LocalDateTime.now());
            }
        } catch (IllegalArgumentException e) {
            log.warn("illegal state {}", text);
            throw  new InvalidDataException("invalid state");
        }
        return sortAndMapBookings(bookings);
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

    private List<BookingDto> sortAndMapBookings(List<Booking> bookings) {
        log.info("sorting bookings from new to old");
        //sort bookings
        return bookings.stream()
                .sorted(comparator)
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

}
