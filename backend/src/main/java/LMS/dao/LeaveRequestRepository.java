package kameleon.dao;

import kameleon.model.booking.Booking;
import kameleon.model.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("Select b From Booking b join b.user user where user.username = :username")
    List<Booking> findAllOwnedByUsername(String username);

    @Query("Select b From Booking b join b.apartment apartment " +
            "where (b.status = 'BOOKED' or b.status = 'PAID' or b.status = 'TENTATIVE') " +
            "and (apartment.id = :apartment_id or (apartment.id != :apartment_id and b.dogIncluded = :dogIncluded))")
    List<Booking> findAllActiveByApartmentAndDogIncluded(Long apartment_id, boolean dogIncluded);

    @Query("Select b From Booking b join b.apartment apartment where apartment.id = :apartment_id " +
            "and (b.status = 'BOOKED' or b.status = 'PAID' or b.status = 'TENTATIVE')")
    List<Booking> findAllActiveByApartment(Long apartment_id);
}
