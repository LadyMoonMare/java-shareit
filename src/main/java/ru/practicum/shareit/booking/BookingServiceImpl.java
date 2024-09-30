package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
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
    public BookingDto save(Long userId, Long itemId, ItemBookingDto bookingDto) {
        Booking booking = BookingMapper.fromItemBookingDto(bookingDto);

        log.info("validation by userId {}", userId);
        User user =userRepository.findById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} does not exist", userId);
            return new NotFoundException("User is not found");
        });
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
}
