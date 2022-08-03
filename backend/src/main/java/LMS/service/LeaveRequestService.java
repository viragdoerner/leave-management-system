package kameleon.service;

import exception.CustomMessageException;
import kameleon.dao.BookingRepository;
import kameleon.dao.StatusTransitionRepository;
import kameleon.dto.*;
import kameleon.model.apartman.Apartment;
import kameleon.model.auth.User;
import kameleon.model.booking.Booking;
import kameleon.model.booking.BookingStatus;
import kameleon.model.booking.StatusTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private UserService userService;
    private ApartmentService apartmentService;
    private StatusTransitionRepository transitionRepository;

    @Autowired
    BookingService(BookingRepository br, StatusTransitionRepository tr, UserService us, ApartmentService as){
        this.bookingRepository = br;
        this.transitionRepository = tr;
        this.userService = us;
        this.apartmentService = as;
    }

    public BookingListsDTO getAllBooking() {
        BookingListsDTO dto = new BookingListsDTO();
        List<Booking> bookings = bookingRepository.findAll();
        for(Booking b : bookings){
            if(b.getStatus() == BookingStatus.DELETED || b.getStatus() == BookingStatus.OUTDATED){
                dto.addInactive(convertBookingToDTO(b));
            }else{
                dto.addActive(convertBookingToDTO(b));
            }
        }
        return dto;
    }

    @Transactional
    public BookingDTO bookApartment(BookingRequest bookingRequest) {
        //TODO check if the date is free in that apartment, and if dog also the other apartment is without a dog
        Date today = new Date();
        if(bookingRequest.getArrival().compareTo(today) == -1){
            throw new CustomMessageException("A foglalás dátuma nem lehet korábbi a mai napnál");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.YEAR, 2);
        Date dateInTwoYear = c.getTime();
        if(bookingRequest.getDeparture().compareTo(dateInTwoYear) == 1){
            throw new CustomMessageException("A foglalás dátuma nem lehet távolabb, mint 2 év");
        }

        //tentative típusú foglalás létrehozása
        Booking booking = createBookingFromRequest(bookingRequest);
        bookingRepository.save(booking);

        //TODO email admin
        //TODO email user
        return convertBookingToDTO(booking);
    }

    private Booking createBookingFromRequest(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setDogIncluded(bookingRequest.getDogIncluded());
        booking.setArrival(bookingRequest.getArrival());
        booking.setDeparture(bookingRequest.getDeparture());
        booking.setStatus(BookingStatus.TENTATIVE);

        StatusTransition transition = new StatusTransition();
        transition.setComment(bookingRequest.getComment());
        transition.setCreated(new Date());
        transition.setNewStatus(BookingStatus.TENTATIVE);
        transition.setEditor(userService.getCurrentFullUser());
        transition.setBooking(booking);
        booking.addTransition(transition);

        Apartment a = getApartment(bookingRequest.getApartmentId());
        booking.setApartment(a);
        User u = userService.getFullUserByEmail(userService.getCurrentUsername());
        booking.setUser(u);

        return booking;
    }

    private Apartment getApartment(Long apartmentId) {
        Apartment a = apartmentService.getApartmentById(apartmentId);
        if(a == null ){
            throw new CustomMessageException("Nincs ilyen apartman");
        }
        return a;
    }

    boolean hasActiveBooking(){
        List<Booking> activeBookings = getActiveBookingFromUser();
        return activeBookings.size()>0;
    }



    public List<Booking> getAllBookingFromUser() {
        return bookingRepository.findAllOwnedByUsername(userService.getCurrentUsername());
    }

    public List<Booking> getActiveBookingFromUser() {
        List<Booking> bookings = getAllBookingFromUser();
        List<Booking> active = new ArrayList<Booking>();
        for(Booking b : bookings){
            if(b.getStatus() != BookingStatus.DELETED && b.getStatus() != BookingStatus.OUTDATED){
                active.add(b);
            }
        }
        return active;
    }

    @Transactional
    public BookingDTO changeBookingStatus(Long booking_id, BookingStatusChangeRequest request) {
        //státusz módosíása
        //statustransition létrehozása
        Booking b = bookingRepository.findById(booking_id).orElseThrow(() ->new CustomMessageException("Nem létezik foglalás ezzel az ID-val"));
        if(b.getStatus() == request.getNewStatus()){
            return convertBookingToDTO(b);
        }
        b.setStatus(request.getNewStatus());
        StatusTransition transition = new StatusTransition(request, b, userService.getCurrentFullUser());
        //b.addTransition(transition);
        bookingRepository.save(b);
        transitionRepository.save(transition);
        //TODO emailek küldése
        return convertBookingToDTO(b);
    }

    @Transactional
    public BookingDTO cancelBooking(BookingStatusChangeRequest request, Long booking_id) {

        List<Booking> bookings = getActiveBookingFromUser();
        Booking b =null;
        for(Booking booking : bookings){
            if(booking.getId() == booking_id) b = booking;
        }
        if(b == null ){
            throw new CustomMessageException("Nincs aktív foglalás");
        }
        if( b.getStatus() == request.getNewStatus()){
            return convertBookingToDTO(b);
        }
        request.setNewStatus(BookingStatus.DELETED);
        b.setStatus(BookingStatus.DELETED);
        StatusTransition transition = new StatusTransition(request, b, userService.getCurrentFullUser());
        //b.addTransition(transition);
        bookingRepository.save(b);
        transitionRepository.save(transition);
        //TODO emailek küldése
        return convertBookingToDTO(b);
    }

    @Transactional
    public void deleteBooking(Long booking_id) {
        Booking b = bookingRepository.findById(booking_id).orElseThrow(() ->new CustomMessageException("Nem létezik foglalás ezzel az ID-val"));
        bookingRepository.deleteById(booking_id);
        //TODO email küldése??
    }

    public List<String> getDisabledDates(Long apartment_id, Boolean dogIncluded) {
        Apartment a = getApartment(apartment_id);
        List<Booking> bookings;
        if(dogIncluded) {
            // össze olyan foglalás az adott apartmanra, és minden kutyás foglalás a másik apartmanra
            bookings = bookingRepository.findAllActiveByApartmentAndDogIncluded(apartment_id, true);
        } else{
            bookings = bookingRepository.findAllActiveByApartment(apartment_id);
        }
        Set<LocalDate> disabledDates = new HashSet<LocalDate>();
        for(Booking b : bookings){
            disabledDates.addAll(getDatesBetweenUsingJava9(b.getArrival(), incrementDateByOneDay(b.getDeparture())));
        }
        List<String> disabledDateStrings = disabledDates.stream()
                .map(d -> d.toString())
                .collect(Collectors.toList());
        return disabledDateStrings;
    }

    private Date incrementDateByOneDay(Date d) {
        Date dayAfter = new Date(d.getTime() + TimeUnit.DAYS.toMillis( 1 ));
        return dayAfter;
    }

    public static List<LocalDate> getDatesBetweenUsingJava9(
            Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }


    public BookingListsDTO getBookingListsFromUser() {
        BookingListsDTO dto = new BookingListsDTO();
        List<Booking> bookings = getAllBookingFromUser();
        for(Booking b : bookings){
            if(b.getStatus() == BookingStatus.DELETED || b.getStatus() == BookingStatus.OUTDATED){
                dto.addInactive(convertBookingToDTO(b));
            }else{
                dto.addActive(convertBookingToDTO(b));
            }
        }
        return dto;
    }
    public BookingDTO convertBookingToDTO(Booking b) {
        BookingDTO dto = new BookingDTO(b);
        return dto;
    }
}
