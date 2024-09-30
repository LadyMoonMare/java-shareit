package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBooker_Id(Long booker_id);

    List<Booking> findByBooker_IdAndStatus(Long booker_id, Status status);

    List<Booking> findByBooker_IdAndEndIsAfter(Long booker_id, LocalDateTime time);

    List<Booking> findByBooker_IdAndEndIsBefore(Long booker_id, LocalDateTime time);

    List<Booking> findByBooker_IdAndStartIsAfter(Long booker_id, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "inner join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1",nativeQuery = true)
    List<Booking> findByOwner_Id(Long owner_id);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.status = ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndStatus(Long owner_id, Status status);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.end > ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsAfter(Long owner_id, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.end < ?2", nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsBefore(Long owner_id, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "join items on b.item_id = items.id " +
            "join users as u on it.owner_id = u.id " +
            "where it.owner_id = ?1 " +
            "and b.start < ?2",nativeQuery = true)
    List<Booking> findByOwner_IdAndStartIsAfter(Long owner_id, LocalDateTime time);

    List<Booking> findByItem_id(Long item_id);
}
