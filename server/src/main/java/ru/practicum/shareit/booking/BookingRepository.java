package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBooker_Id(Long bookerId);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, Status status);

    List<Booking> findByBooker_IdAndEndIsAfter(Long bookerId, LocalDateTime time);

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime time);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "inner join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1",nativeQuery = true)
    List<Booking> findByOwner_Id(Long ownerId);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.status = ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndStatus(Long ownerId, Status status);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.end > ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsAfter(Long ownerId, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.end < ?2", nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsBefore(Long ownerId, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.start < ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndStartIsAfter(Long ownerId, LocalDateTime time);

    List<Booking> findByItem_id(Long itemId);

    Optional<Booking> findByBooker_idAndItem_id(Long bookerId, Long itemId);
}
