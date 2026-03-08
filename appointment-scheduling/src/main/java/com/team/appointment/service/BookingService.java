package com.team.appointment.service;

	

	import java.util.List;
	import com.team.appointment.model.AppointmentSlot;

	public class BookingService {

	    private static final int REQUIRED_DURATION = 30;
	  

	    private final SlotService slotService;

	    public BookingService(SlotService slotService) {
	        this.slotService = slotService;
	    }

	    public boolean bookAppointment(int index, int duration, int participants) {
	        List<AppointmentSlot> availableSlots = slotService.getAvailableSlots();

	        if (index < 1 || index > availableSlots.size()) {
	            return false;
	        }

	        if (duration != REQUIRED_DURATION) {
	            return false;
	        }

	        AppointmentSlot slot = availableSlots.get(index - 1);

	        if (!slot.canAcceptParticipants(participants)) {
	            return false;
	        }

	        if (slot.isBooked()) {
	            return false;
	        }

	        slot.book(participants);
	        return true;
	    }
	    public boolean cancelBooking(int index) {
	        List<AppointmentSlot> allSlots = slotService.getAllSlots();

	        if (index < 1 || index > allSlots.size()) {
	            return false;
	        }

	        AppointmentSlot slot = allSlots.get(index - 1);

	        if (!slot.isBooked()) {
	            return false;
	        }

	        slot.cancel();
	        return true;
	    }
	}

